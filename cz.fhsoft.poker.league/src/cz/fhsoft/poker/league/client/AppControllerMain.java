package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.event.ModeChangeEvent;
import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.PresenterWithVersionedData;
import cz.fhsoft.poker.league.client.presenter.WorkbenchPresenter;
import cz.fhsoft.poker.league.client.services.StatisticsService;
import cz.fhsoft.poker.league.client.services.StatisticsServiceAsync;
import cz.fhsoft.poker.league.client.util.StyleUtil;
import cz.fhsoft.poker.league.client.view.WorkbenchView;
import cz.fhsoft.poker.league.client.view.WorkbenchViewImpl;

public class AppControllerMain extends PresenterWithVersionedData {

	public static AppControllerMain INSTANCE = null;
	
	private static StatisticsServiceAsync statisticsService = GWT.create(StatisticsService.class);
	
	private WorkbenchView workbenchView;

	private WorkbenchPresenter workbenchPresenter;
	
	private boolean adminMode;
	
	public AppControllerMain(Presenter parentPresenter, boolean adminMode) {
		super(parentPresenter);

		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerMain already exists");

		INSTANCE = this;
		
		this.adminMode = adminMode;
	}
	
	@Override
	public void go(HasWidgets container) {
		initWorkbench(container);
	}

	private void initWorkbench(HasWidgets container) {
		if(workbenchView == null) {
			workbenchView = new WorkbenchViewImpl();
			StyleUtil.makeAbsoluteFull(workbenchView.asWidget());
		}
		
		workbenchPresenter = new WorkbenchPresenter(this, workbenchView, adminMode);
		
		workbenchPresenter.go(container);
	}

	public StatisticsServiceAsync getStatisticsService() {
		return statisticsService;
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if(workbenchView != null) {
			Style style = workbenchView.asWidget().getElement().getStyle();
			
			if(visible)
				style.clearDisplay();
			else
				style.setDisplay(Display.NONE);
		}
	}

	@Override
	protected void refresh() {
		// nothing to do here, inner presenters will do their job
	}

	@Override
	public void updateForMode() {
		// nothing to do here
		
	}
	
	public void setMode(boolean adminMode) {
		if(this.adminMode != adminMode) {
			workbenchPresenter.setMode(this.adminMode = adminMode);
			Bootstrap.INSTANCE.getEventBus().fireEvent(new ModeChangeEvent());
		}
	}
}
