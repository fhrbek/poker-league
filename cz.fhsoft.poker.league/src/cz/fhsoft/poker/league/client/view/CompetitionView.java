package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.Competition;


public interface CompetitionView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.OrderedPresenter {

		void onEdit();
		
		void onRemove();
		
		void onToggleShowTournaments();
		
		void setCompetition(Competition competition);
		
		Competition getCompetition();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
	
	HasWidgets getTournamentsContainer();

	void setName(String name);
}
