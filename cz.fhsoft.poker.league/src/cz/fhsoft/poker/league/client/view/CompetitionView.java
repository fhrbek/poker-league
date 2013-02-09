package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.shared.model.v1.Competition;


public interface CompetitionView extends RankableView<Competition, CompetitionView.Presenter>, ViewWithMode {
	
	public interface Presenter extends RankableView.Presenter<Competition> {

		void onToggleShowTournaments();

	}

	HasWidgets getTournamentsContainer();

	void setName(String name);
}
