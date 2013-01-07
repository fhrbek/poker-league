package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;


public interface WorkbenchView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {
		public enum Tab { COMPETITIONS, PLAYERS, PRIZE_MONEY_RULE_SET };
		
		void onTabChanged(Tab tab);
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	HasWidgets getCompetitionsContainer();

	HasWidgets getPlayersContainer();

	HasWidgets getPrizeMoneyRuleSetContainer();

}
