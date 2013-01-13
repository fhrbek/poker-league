package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.PresenterWithVersionedData;
import cz.fhsoft.poker.league.client.presenter.WorkbenchPresenter;
import cz.fhsoft.poker.league.client.services.StatisticsService;
import cz.fhsoft.poker.league.client.services.StatisticsServiceAsync;
import cz.fhsoft.poker.league.client.view.WorkbenchView;
import cz.fhsoft.poker.league.client.view.WorkbenchViewImpl;

public class AppControllerMain extends PresenterWithVersionedData {

	public static AppControllerMain INSTANCE = null;
	
	private static StatisticsServiceAsync statisticsService = GWT.create(StatisticsService.class);
	
	private WorkbenchView workbenchView;

	private WorkbenchPresenter workbenchPresenter;
	
	public AppControllerMain(Presenter parentPresenter) {
		super(parentPresenter);

		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerMain already exists");

		INSTANCE = this;
	}
	
	@Override
	public void go(HasWidgets container) {
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

	public StatisticsServiceAsync getStatisticsService() {
		return statisticsService;
	}

	public void setVisible(boolean visible) {
		super.setVisible(false);
		
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
}
