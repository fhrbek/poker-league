package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.CurrentTournamentsPresenter;
import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.PresenterWithVersionedData;
import cz.fhsoft.poker.league.client.services.GameService;
import cz.fhsoft.poker.league.client.services.GameServiceAsync;
import cz.fhsoft.poker.league.client.view.CurrentTournamentsView;
import cz.fhsoft.poker.league.client.view.CurrentTournamentsViewImpl;

public class AppControllerGame extends PresenterWithVersionedData {

	public static AppControllerGame INSTANCE = null;
	
	private CurrentTournamentsPresenter currentTournamentsPresenter;
	
	private CurrentTournamentsView currentTournamentsView;
	
	private GameServiceAsync gameService = GWT.create(GameService.class);
	
	public AppControllerGame(Presenter parentPresenter) {
		super(parentPresenter);

		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerMain already exists");

		INSTANCE = this;
	}
	
	@Override
	public void go(HasWidgets container) {
		if(currentTournamentsView == null)
			currentTournamentsView = new CurrentTournamentsViewImpl();
		if(currentTournamentsPresenter == null)
			currentTournamentsPresenter =  new CurrentTournamentsPresenter(this, currentTournamentsView);
		
		currentTournamentsPresenter.go(container);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(false);

		if(currentTournamentsView != null) {
			Style style = currentTournamentsView.asWidget().getElement().getStyle();
			
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
	
	public GameServiceAsync getGameService() {
		return gameService;
	}
}
