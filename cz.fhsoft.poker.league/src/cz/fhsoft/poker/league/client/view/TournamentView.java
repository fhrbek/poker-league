package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.shared.model.v1.Tournament;


public interface TournamentView extends RankableView<Tournament, TournamentView.Presenter>{
	
	public interface Presenter extends RankableView.Presenter<Tournament> {

		void onToggleShowInvitations();
		
		void onToggleShowGames();

	}

	HasWidgets getInvitationsContainer();

	HasWidgets getGamesContainer();

	void setName(String name);
}
