package cz.fhsoft.poker.league.client;

import java.util.Date;

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
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;

public class Bootstrap {
	
	public static final Bootstrap INSTANCE = new Bootstrap();

	private EventBus eventBus;

	private long latestUserInteraction = new Date().getTime();
	
	private static final long MAX_USER_INACTIVE_TIME = 3600000; // 1 hour
	
	private static final int DATA_VERSION_CHECK_INTERVAL = 10000; // check data version every 10 seconds
	
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
	
	public Bootstrap() {
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

	public EventBus getEventBus() {
		return eventBus;
	}
}
