package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;


public interface GamesView extends ViewWithMode {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {

		void onAddGame();
		
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
	
	HasWidgets getGamesContainer();
}
