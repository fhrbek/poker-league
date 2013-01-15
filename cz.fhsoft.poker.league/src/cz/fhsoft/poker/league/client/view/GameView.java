package cz.fhsoft.poker.league.client.view;

import cz.fhsoft.poker.league.shared.model.v1.Game;


public interface GameView extends RankableView<Game, GameView.Presenter> {
	
	public interface Presenter extends RankableView.Presenter<Game> {

	}

	void setOrdinal(int ordinal);
}
