package cz.fhsoft.poker.league.client;

import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.PresenterWithVersionedData;

public class AppControllerGame extends PresenterWithVersionedData {

	public static AppControllerGame INSTANCE = null;
	
	private HasWidgets container;
	
//	private WorkbenchView workbenchView;
//
//	private WorkbenchPresenter workbenchPresenter;
	
	public AppControllerGame(Presenter parentPresenter) {
		super(parentPresenter);

		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerMain already exists");

		INSTANCE = this;
	}
	
	@Override
	public void go(HasWidgets container) {
		this.container = container;
//		initWorkbench(container);
	}

//	private void initWorkbench(HasWidgets container) {
//		if(workbenchView == null)
//			workbenchView = new WorkbenchViewImpl();
//		
//		workbenchPresenter = new WorkbenchPresenter(this, workbenchView);
//		
//		workbenchPresenter.go(container);
//	}

	@Override
	public Presenter getParentPresenter() {
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(false);
		//TODO hide the main element
	}

	@Override
	protected void refresh() {
		// nothing to do here, inner presenters will do their job
	}
}
