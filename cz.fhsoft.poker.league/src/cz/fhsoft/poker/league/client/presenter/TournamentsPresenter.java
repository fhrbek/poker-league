package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.TournamentViewImpl;
import cz.fhsoft.poker.league.client.view.TournamentsView;
import cz.fhsoft.poker.league.shared.model.v1.Competition;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationReply;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.DescribedEntityComparator;

public class TournamentsPresenter extends PresenterWithVersionedData implements TournamentsView.Presenter {
	
	protected static final Comparator<Tournament> TOURNAMENTS_COMPARATOR = new DescribedEntityComparator<Tournament>() {

		@Override
		public int compare(Tournament t1, Tournament t2) {
			int result = - t1.getTournamentStart().compareTo(t2.getTournamentStart());
			
			if(result == 0)
				return super.compare(t1, t2);

			return result;
		}
		
	};

	private TournamentsView view;
	
	private Map<Integer, TournamentPresenter> tournamentPresenterMap = new HashMap<Integer, TournamentPresenter>();
	
	public TournamentsPresenter(Presenter parentPresenter, TournamentsView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
		setVisible(false);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if(visible)
			view.asWidget().getElement().getStyle().clearDisplay();
		else
			view.asWidget().getElement().getStyle().setDisplay(Display.NONE);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		refresh();
	}

	@Override
	protected void refresh() {
		ClientEntityManager.getInstance().resolveEntity(getCompetition(), new AsyncCallback<Competition>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Competition resolvedCompetition) {
				Util.resolve(resolvedCompetition.getTournaments(), new AsyncCallback<Set<Tournament>>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Set<Tournament> tournamentsSet) {
						List<Tournament> tournaments = new ArrayList<Tournament>(tournamentsSet);
						Collections.sort(tournaments, Collections.reverseOrder(TOURNAMENTS_COMPARATOR));

						Set<Integer> usedIds = new HashSet<Integer>();

						for(Tournament tournament : tournaments) {
							TournamentPresenter tournamentPresenter = tournamentPresenterMap.get(tournament.getId());
							if(tournamentPresenter == null) {
								tournamentPresenter = new TournamentPresenter(TournamentsPresenter.this, new TournamentViewImpl());
								tournamentPresenter.setTournament(tournament);
								tournamentPresenter.go(view.getTournamentsContainer());
								tournamentPresenterMap.put(tournament.getId(), tournamentPresenter);
							}

							tournamentPresenter.moveViewToTop();
							usedIds.add(tournament.getId());
						}

						Iterator<Map.Entry<Integer, TournamentPresenter>> iterator = tournamentPresenterMap.entrySet().iterator();

						while(iterator.hasNext()) {
							Map.Entry<Integer, TournamentPresenter> entry = iterator.next();
							if(!usedIds.contains(entry.getKey())) {
								entry.getValue().removeView();
								iterator.remove();
							}
						}
					}
					
				});
			}
			
		});
	}

	@Override
	public void onAddTournament() {
		ClientEntityManager.getInstance().resolveEntity(getCompetition(), new AsyncCallback<Competition>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Competition resolvedCompetition) {
				final Tournament newTournament = new Tournament();
				Date currentDate = new Date();
				newTournament.setTournamentStart(new Date(currentDate.getTime() + 86400 * 20)); //TODO Hard-coded start at 20:00

				newTournament.setCompetition(resolvedCompetition);
				newTournament.setMinPlayers(resolvedCompetition.getDefaultMinPlayers());
				newTournament.setMaxPlayers(resolvedCompetition.getDefaultMaxPlayers());
				newTournament.setDefaultBuyIn(resolvedCompetition.getDefaultBuyIn());
				newTournament.setDeadline(resolvedCompetition.getDefaultTournamentDeadline());
				newTournament.setDefaultPrizeMoneyRuleSet(resolvedCompetition.getDefaultPrizeMoneyRuleSet());
				
				Util.resolve(resolvedCompetition.getPlayers(), new AsyncCallback<Set<Player>>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Set<Player> players) {

						Set<Invitation> invitations = new HashSet<Invitation>(players.size());
						
						for(Player player : players) {
							if(!player.isActive())
								continue;
							
							Invitation invitation = new Invitation();
							invitation.setOrdinal(0); // initially all players get 0
							invitation.setPlayer(player);
							invitation.setReply(InvitationReply.NO_REPLY);
							invitation.setTournament(newTournament);
							
							invitations.add(invitation);
						}
						
						newTournament.setInvitations(invitations);

						TournamentPresenter.tournamentEditor.setEntity(newTournament, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								ErrorReporter.error(caught);
							}

							@Override
							public void onSuccess(Void result) {
								TournamentPresenter.tournamentEditor.showAsPopupPanel();
							}
							
						});
					}
					
				});
			}
			
		});
	}

	private Competition getCompetition() {
		return ((CompetitionPresenter) getParentPresenter()).getCompetition();
	}
}