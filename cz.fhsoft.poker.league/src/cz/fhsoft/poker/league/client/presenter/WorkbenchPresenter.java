package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.view.CompetitionsView;
import cz.fhsoft.poker.league.client.view.CompetitionsViewImpl;
import cz.fhsoft.poker.league.client.view.PlayersView;
import cz.fhsoft.poker.league.client.view.PlayersViewImpl;
import cz.fhsoft.poker.league.client.view.PrizeMoneyRuleSetView;
import cz.fhsoft.poker.league.client.view.PrizeMoneyRuleSetViewImpl;
import cz.fhsoft.poker.league.client.view.WorkbenchView;

public class WorkbenchPresenter extends PresenterWithVersionedData implements WorkbenchView.Presenter {
	
	private WorkbenchView view;
	
	private CompetitionsView competitionsView;
	
	private CompetitionsView.Presenter competitionsPresenter;
	
	private PlayersView playersView;
	
	private PlayersView.Presenter playersPresenter;
	
	private PrizeMoneyRuleSetView prizeMoneyRuleSetView;
	
	private PrizeMoneyRuleSetView.Presenter prizeMoneyRuleSetPresenter;
	
	private List<Presenter> exclusivePresenters = new ArrayList<Presenter>();
	
	public WorkbenchPresenter(Presenter parentPresenter, WorkbenchView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		onTabChanged(Tab.COMPETITIONS);
	}

	@Override
	public void onTabChanged(Tab tab) {
		switch(tab) {
			case COMPETITIONS:
				updateVisibility(competitionsPresenter);

				if(competitionsView == null)
					competitionsView = new CompetitionsViewImpl();
				if(competitionsPresenter == null) {
					exclusivePresenters.add(competitionsPresenter = new CompetitionsPresenter(this, competitionsView));
					competitionsPresenter.go(view.getCompetitionsContainer());
				}
				else
					competitionsPresenter.setVisible(true);
				break;
			case PLAYERS:
				updateVisibility(playersPresenter);

				if(playersView == null)
					playersView = new PlayersViewImpl();
				if(playersPresenter == null) {
					exclusivePresenters.add(playersPresenter = new PlayersPresenter(this, playersView));
					playersPresenter.go(view.getPlayersContainer());
				}
				else
					playersPresenter.setVisible(true);
				break;
			case PRIZE_MONEY_RULE_SET:
				updateVisibility(prizeMoneyRuleSetPresenter);

				if(prizeMoneyRuleSetView == null)
					prizeMoneyRuleSetView = new PrizeMoneyRuleSetViewImpl();
				if(prizeMoneyRuleSetPresenter == null) {
					exclusivePresenters.add(prizeMoneyRuleSetPresenter = new PrizeMoneyRuleSetPresenter(this, prizeMoneyRuleSetView));
					prizeMoneyRuleSetPresenter.go(view.getPrizeMoneyRuleSetContainer());
				}
				else
					prizeMoneyRuleSetPresenter.setVisible(true);
				break;
		}
	}
	
	private void updateVisibility(Presenter visiblePresenter) {
		for(Presenter presenter : exclusivePresenters) {
			if(presenter == visiblePresenter) {
				if(!presenter.isVisible())
					presenter.setVisible(true);
			}
			else {
				if(presenter.isVisible())
					presenter.setVisible(false);
			}
		}
	}

	@Override
	protected void refresh() {
		// Nothing to do here (all data is inside child presenters)
	}

}
