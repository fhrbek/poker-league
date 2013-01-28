package cz.fhsoft.poker.league.client.presenter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.AppControllerGame;
import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.CurrentTournamentViewImpl;
import cz.fhsoft.poker.league.client.view.CurrentTournamentsView;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;

public class CurrentTournamentsPresenter extends PresenterWithVersionedData implements CurrentTournamentsView.Presenter {
	
	private CurrentTournamentsView view;
	
	private HasWidgets container;
	
	private Map<Integer, CurrentTournamentPresenter> tournamentPresenterMap = new HashMap<Integer, CurrentTournamentPresenter>();
	
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
		if(!isDataChanged())
			return;

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

					Set<Integer> usedIds = new HashSet<Integer>();

					for(Tournament tournament : tournaments) {
						CurrentTournamentPresenter currentTournamentPresenter = tournamentPresenterMap.get(tournament.getId());
						
						if(currentTournamentPresenter == null) {
							currentTournamentPresenter = new CurrentTournamentPresenter(CurrentTournamentsPresenter.this, tournament, new CurrentTournamentViewImpl());
							currentTournamentPresenter.go(view.getCurrentTournamentsContainer());
							tournamentPresenterMap.put(tournament.getId(), currentTournamentPresenter);
						}

						ClientEntityManager.getInstance().registerEntity(tournament);

						currentTournamentPresenter.moveViewToTop();
						usedIds.add(tournament.getId());
					}
					
					Iterator<Map.Entry<Integer, CurrentTournamentPresenter>> iterator = tournamentPresenterMap.entrySet().iterator();

					while(iterator.hasNext()) {
						Map.Entry<Integer, CurrentTournamentPresenter> entry = iterator.next();
						if(!usedIds.contains(entry.getKey())) {
							entry.getValue().removeView();
							iterator.remove();
						}
					}

					if(tournaments.size() == 0)
						view.setNoTournament(true);
				}
				
			});
		}
	}

}
