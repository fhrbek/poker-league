package cz.fhsoft.poker.league.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.DataChangeEvent;
import cz.fhsoft.poker.league.client.persistence.DataChangeEventHandler;
import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.WorkbenchPresenter;
import cz.fhsoft.poker.league.client.services.StatisticsService;
import cz.fhsoft.poker.league.client.services.StatisticsServiceAsync;
import cz.fhsoft.poker.league.client.view.WorkbenchView;
import cz.fhsoft.poker.league.client.view.WorkbenchViewImpl;

public class AppController implements Presenter {

	public static AppController INSTANCE = null;
	
	private static StatisticsServiceAsync statisticsService = GWT.create(StatisticsService.class);
	
	private EventBus eventBus;

	private WorkbenchView workbenchView;

	private WorkbenchPresenter workbenchPresenter;
	
	private long latestUserInteraction = new Date().getTime();
	
	private static final long MAX_USER_INACTIVE_TIME = 15000; // 15s for now, should be longer in production
	
	private static final int DATA_VERSION_CHECK_INTERVAL = 5000; // check data version every 5 seconds
	
	private Timer dataVersionChecker = new Timer() {

		@Override
		public void run() {
			ClientEntityManager.getInstance().checkDataVersion(new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					maybeScheduleDataVersionChecker();
					
				}

				@Override
				public void onSuccess(Void result) {
					maybeScheduleDataVersionChecker();
				}

			});
		}
		
	};
	
	public AppController() {
		if(INSTANCE != null)
			throw new IllegalArgumentException("AppController already exists");

		INSTANCE = this;
		
		eventBus = new SimpleEventBus();
		
		ClientEntityManager.getInstance().setEventBus(eventBus);
		
		maybeScheduleDataVersionChecker();

		Event.addNativePreviewHandler(new NativePreviewHandler() {
			
			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if(event.getTypeInt() == Event.ONCLICK || event.getTypeInt() == Event.ONKEYUP)
					latestUserInteraction = new Date().getTime();
			}
		});
	}
	
	private void maybeScheduleDataVersionChecker() {
		if((new Date().getTime()) - latestUserInteraction > MAX_USER_INACTIVE_TIME)
			suspendApplication();
		else
			dataVersionChecker.schedule(DATA_VERSION_CHECK_INTERVAL);
	}
	
	private void suspendApplication() {
		final HandlerRegistration[] resizeHandlerRef = new HandlerRegistration[] { null };
		final PopupPanel blocker = new PopupPanel(true, true) {
			@Override
			public void hide(boolean auto) {
				super.hide(auto);
				latestUserInteraction = new Date().getTime();
				dataVersionChecker.run();
				if(resizeHandlerRef[0] != null)
					resizeHandlerRef[0].removeHandler();
			}
			
			@Override
			public void onAttach() {
				super.onAttach();
				//confirm current size to prevent the panel from wrong rendering on resize
				getElement().getStyle().setWidth(getOffsetWidth(), Unit.PX);
				getElement().getStyle().setHeight(getOffsetHeight(), Unit.PX);
			}
		};

		resizeHandlerRef[0] = Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				blocker.center();
			}
			
		});

		blocker.setGlassEnabled(true);
		Label label = new Label("Aplikace byla pozastavena z důvodu dlouhé nečinnosti uživatele. Klepněte kdekoli pro opětovnou aktivaci aplikace.");
		label.getElement().getStyle().setPadding(30.0, Unit.PX);
		label.getElement().getStyle().setCursor(Cursor.POINTER);
		blocker.add(label);
		
		blocker.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				blocker.hide();
			}
			
		}, ClickEvent.getType());

		blocker.center();
	}

	private void bind() {
		eventBus.addHandler(DataChangeEvent.TYPE, new DataChangeEventHandler() {

			@Override
			public void onDataChange(DataChangeEvent event) {
				// Do something if needed but in most cases presenters should take care of themselves
			}
			
		});
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		initWorkbench(container);
	}

	private void initWorkbench(HasWidgets container) {
		if(workbenchView == null)
			workbenchView = new WorkbenchViewImpl();
		
		workbenchPresenter = new WorkbenchPresenter(this, workbenchView);
		
		workbenchPresenter.go(container);
	}

	@Override
	public Presenter getParentPresenter() {
		return null;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		throw new IllegalArgumentException("AppController visibility cannot be changed");
	}

	public StatisticsServiceAsync getStatisticsService() {
		return statisticsService;
	}
}
