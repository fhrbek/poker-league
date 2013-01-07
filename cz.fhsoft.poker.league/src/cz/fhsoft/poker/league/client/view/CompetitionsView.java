package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;


public interface CompetitionsView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {

		void onAddCompetition();
		
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
	
	HasWidgets getCompetitionsContainer();
}
