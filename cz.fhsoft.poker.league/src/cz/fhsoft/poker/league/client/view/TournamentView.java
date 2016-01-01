package cz.fhsoft.poker.league.client.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.shared.model.v1.Tournament;


public interface TournamentView extends RankableView<Tournament, TournamentView.Presenter>, ViewWithMode {
	
	public interface Presenter extends RankableView.Presenter<Tournament> {

		void onToggleShowRankingCumulative();

		void onToggleShowInvitations();
		
		void onToggleShowGames();

	}

	HasWidgets getRankingContainerCumulative();

	HasWidgets getInvitationsContainer();

	HasWidgets getGamesContainer();

	void setName(String name);

	void setDescription(SafeHtml description);
}
