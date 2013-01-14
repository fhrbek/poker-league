package cz.fhsoft.poker.league.client.presenter;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.AppControllerGame;
import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.CurrentTournamentView;
import cz.fhsoft.poker.league.client.view.CurrentTournamentViewImpl;
import cz.fhsoft.poker.league.client.view.CurrentTournamentsView;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;

public class CurrentTournamentsPresenter extends PresenterWithVersionedData implements CurrentTournamentsView.Presenter {
	
	private CurrentTournamentsView view;
	
	private HasWidgets container;
	
	public CurrentTournamentsPresenter(Presenter parentPresenter, CurrentTournamentsView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		refresh();
	}

	@Override
	protected void refresh() {
		if(container != null) {
			container.clear();
			container.add(view.asWidget());
			
			view.setNoTournament(false);

			AppControllerGame.INSTANCE.getGameService().getCurrentTournaments(new AsyncCallback<List<Tournament>>() {

				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}

				@Override
				public void onSuccess(List<Tournament> tournaments) {
					for(Tournament tournament : tournaments) {
						ClientEntityManager.getInstance().registerEntity(tournament);
						CurrentTournamentView currentTournamentView = new CurrentTournamentViewImpl();
						CurrentTournamentPresenter currentTournamentPresenter = new CurrentTournamentPresenter(CurrentTournamentsPresenter.this, tournament, currentTournamentView);
						currentTournamentPresenter.go(view.getCurrentTournamentsContainer());
					}
					
					if(tournaments.size() == 0)
						view.setNoTournament(true);
				}
				
			});
		}
	}

}
