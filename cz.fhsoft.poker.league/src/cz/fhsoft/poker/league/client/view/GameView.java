package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.Game;


public interface GameView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.OrderedPresenter {

		void onEdit();
		
		void onRemove();
		
		void setGame(Game game);
		
		Game getGame();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
	
	HasWidgets getPlayersInGameContainer();
	
	void setOrdinal(int ordinal);
}
