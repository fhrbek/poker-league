package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.AppControllerGame;
import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.CurrentTournamentView;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationReply;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class CurrentTournamentPresenter extends PresenterWithVersionedData implements CurrentTournamentView.Presenter {
	
	private Tournament tournament;
	
	private CurrentTournamentView view;
	
	private HasWidgets container;

	public CurrentTournamentPresenter(Presenter parentPresenter, Tournament tournament, CurrentTournamentView view) {
		super(parentPresenter);
		this.tournament = tournament;
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		refresh();
	}

	@Override
	public void moveViewToTop() {
		if(view.asWidget().getParent() instanceof FlowPanel) {
			FlowPanel parent = (FlowPanel) view.asWidget().getParent();
			parent.remove(view.asWidget());
			parent.insert(view.asWidget(), 0);
		}
	}

	@Override
	public void removeView() {
		view.asWidget().removeFromParent();
	}
	
	@Override
	protected void refresh() {
		if(!isDataChanged())
			return;

		if(container != null) {
			container.add(view.asWidget());
			view.setPlayerCandidatessForGame(Collections.<Player> emptyList());
			view.setNewGameVisible(false);
			view.setCurrentGameVisible(false);

			ClientEntityManager.getInstance().resolveEntity(tournament, new AsyncCallback<Tournament>() {

				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}

				@Override
				public void onSuccess(Tournament resolvedTournament) {
					if(resolvedTournament == null)
						return;

					tournament = resolvedTournament;
					view.setTournamentName(resolvedTournament.getCompetition().getName() + " / " + resolvedTournament.getName());
					
					Util.asLazySet(tournament.getGames()).resolve(new AsyncCallback<Set<Game>>() {

						@Override
						public void onFailure(Throwable caught) {
							ErrorReporter.error(caught);
						}

						@Override
						public void onSuccess(Set<Game> games) {
							final List<Game> sortedGames = new ArrayList<Game>(games);
							Collections.sort(sortedGames, Comparators.GAMES_COMPARATOR);
							
							final Game lastGame =  sortedGames.size() > 0
									? sortedGames.get(sortedGames.size() - 1)
									: null;
							
							if(lastGame != null) {
								Util.asLazySet(lastGame.getPlayersInGame()).resolve(new AsyncCallback<Set<PlayerInGame>>() {

									@Override
									public void onFailure(Throwable caught) {
										ErrorReporter.error(caught);
									}

									@Override
									public void onSuccess(Set<PlayerInGame> playersInGame) {
										boolean active = false;
										for(PlayerInGame playerInGame : playersInGame) {
											if(playerInGame.getRank() == 0) {
												active = true;
												break;
											}
										}

										if(!active) {
											view.setNewGameVisible(true);
											setNewPlayerCandidates();
										}

										view.setCurrentGameVisible(true);
										view.setGameName("Hra č. " + lastGame.getOrdinal());

										List<PlayerInGame> sortedPlayersInGame = new ArrayList<PlayerInGame>(playersInGame);
										Collections.sort(sortedPlayersInGame, Comparators.PLAYERS_IN_GAME_COMPARATOR);

										view.setPlayersInGame(sortedPlayersInGame);
									}
									
								});
							}
							else {
								setNewPlayerCandidates();
								view.setNewGameVisible(true);
								view.setCurrentGameVisible(false);
							}
							
						}
						
					});
				}
				
			});
		}
	}
	
	private void setNewPlayerCandidates() {
		ClientEntityManager.getInstance().resolveEntity(tournament, new AsyncCallback<Tournament>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Tournament resolvedTournament) {
				tournament = resolvedTournament;
				Util.asLazySet(tournament.getInvitations()).resolve(new AsyncCallback<Set<Invitation>>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Set<Invitation> invitations) {
						List<Player> acceptedInvitationPlayers = new ArrayList<Player>(invitations.size());
						
						for(Invitation invitation : invitations)
							if(invitation.getReply() == InvitationReply.ACCEPTED)
								acceptedInvitationPlayers.add(invitation.getPlayer());
						
						view.setPlayerCandidatessForGame(acceptedInvitationPlayers);
					}
					
				});
			}
			
		});
	}

	@Override
	public void onNewGame(List<Integer> playerIds) {
		AppControllerGame.INSTANCE.getGameService().startNewGame(tournament.getId(), playerIds, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Long newDataVersion) {
				ClientEntityManager.getInstance().checkDataVersion(newDataVersion);
			}
			
		});
	}

	@Override
	public void onSeatOpen(List<Integer> playerInGameIds) {
		AppControllerGame.INSTANCE.getGameService().seatOpen(playerInGameIds, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Long newDataVersion) {
				ClientEntityManager.getInstance().checkDataVersion(newDataVersion);
			}
			
		});
	}

	@Override
	public void onUndoSeatOpen(List<Integer> playerInGameIds) {
		AppControllerGame.INSTANCE.getGameService().undoSeatOpen(playerInGameIds, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Long newDataVersion) {
				ClientEntityManager.getInstance().checkDataVersion(newDataVersion);
			}
			
		});
	}

	@Override
	public void updateForMode() {
		// nothing to do here
	}

}
