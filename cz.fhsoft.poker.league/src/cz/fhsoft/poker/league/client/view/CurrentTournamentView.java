package cz.fhsoft.poker.league.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;


public interface CurrentTournamentView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {
		
		void onNewGame(List<Integer> playerIds);
		
		void onSeatOpen(List<Integer> playerInGameIds);
		
		void onUndoSeatOpen(List<Integer> playerInGameIds);
		
		void moveViewToTop();
		
		void removeView();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
	
	void setTournamentName(String name);
	
	void setNewGameVisible(boolean visible);
	
	void setCurrentGameVisible(boolean visible);
	
	void setGameName(String name);
	
	void setPlayerCandidatessForGame(List<Player> players);
	
	void setPlayersInGame(List<PlayerInGame> playersInGame);

}
