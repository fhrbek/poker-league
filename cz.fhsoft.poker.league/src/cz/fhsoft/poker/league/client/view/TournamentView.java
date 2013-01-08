package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.Tournament;


public interface TournamentView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.OrderedPresenter {

		void onEdit();
		
		void onRemove();
		
		void onToggleShowInvitations();
		
		void onToggleShowGames();
		
		void setTournament(Tournament tournament);
		
		Tournament getTournament();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
	
	HasWidgets getInvitationsContainer();

	HasWidgets getGamesContainer();

	void setName(String name);
}
