package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
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
import cz.fhsoft.poker.league.client.view.GameViewImpl;
import cz.fhsoft.poker.league.client.view.GamesView;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationReply;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class GamesPresenter extends PresenterWithVersionedData implements GamesView.Presenter {
	
	private GamesView view;
	
	private Map<Integer, GamePresenter> gamePresenterMap = new HashMap<Integer, GamePresenter>();
	
	public GamesPresenter(Presenter parentPresenter, GamesView view) {
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
		ClientEntityManager.getInstance().resolveEntity(getTournament(), new AsyncCallback<Tournament>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Tournament resolvedTournament) {
				Util.resolve(resolvedTournament != null
						? resolvedTournament.getGames()
						: Collections.<Game> emptySet(), new AsyncCallback<Set<Game>>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Set<Game> gamesSet) {
						List<Game> games = new ArrayList<Game>(gamesSet);
						Collections.sort(games, Collections.reverseOrder(Comparators.GAMES_COMPARATOR));

						Set<Integer> usedIds = new HashSet<Integer>();

						for(Game game : games) {
							GamePresenter gamePresenter = gamePresenterMap.get(game.getId());
							if(gamePresenter == null) {
								gamePresenter = new GamePresenter(GamesPresenter.this, new GameViewImpl());
								gamePresenter.setEntity(game);
								gamePresenter.go(view.getGamesContainer());
								gamePresenterMap.put(game.getId(), gamePresenter);
							}

							gamePresenter.moveViewToTop();
							usedIds.add(game.getId());
						}

						Iterator<Map.Entry<Integer, GamePresenter>> iterator = gamePresenterMap.entrySet().iterator();

						while(iterator.hasNext()) {
							Map.Entry<Integer, GamePresenter> entry = iterator.next();
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
	public void onAddGame() {
		ClientEntityManager.getInstance().resolveEntity(getTournament(), new AsyncCallback<Tournament>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(final Tournament resolvedTournament) {
				final Game newGame = new Game();
				newGame.setTournament(resolvedTournament);
				newGame.setBuyIn(resolvedTournament.getDefaultBuyIn());
				newGame.setPrizeMoneyRuleSet(resolvedTournament.getDefaultPrizeMoneyRuleSet());
				
				Util.resolve(resolvedTournament.getGames(), new AsyncCallback<Set<Game>>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Set<Game> games) {
						
						int maxOrdinal = 0;
						
						for(Game game : games)
							if(game.getOrdinal() > maxOrdinal)
								maxOrdinal = game.getOrdinal();
						
						newGame.setOrdinal(maxOrdinal+1);

						Util.resolve(resolvedTournament.getInvitations(), new AsyncCallback<Set<Invitation>>() {

							@Override
							public void onFailure(Throwable caught) {
								ErrorReporter.error(caught);
							}

							@Override
							public void onSuccess(Set<Invitation> invitations) {

								Set<PlayerInGame> playersInGame = new HashSet<PlayerInGame>(invitations.size());
								
								int newId = -1;

								for(Invitation invitation : invitations) {
									if(invitation.getReply() != InvitationReply.ACCEPTED)
										continue;
									
									PlayerInGame playerInGame = new PlayerInGame();
									playerInGame.setId(newId--);
									playerInGame.setPlayer(invitation.getPlayer());
									playerInGame.setRank(0); // 0 means still playing
									playerInGame.setGame(newGame);
									
									playersInGame.add(playerInGame);
								}
								
								newGame.setPlayersInGame(playersInGame);

								GamePresenter.gameEditor.setEntity(newGame, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										ErrorReporter.error(caught);
									}

									@Override
									public void onSuccess(Void result) {
										GamePresenter.gameEditor.showAsPopupPanel();
									}
									
								});
							}
							
						});

					}
					
				});

			}
			
		});
	}

	private Tournament getTournament() {
		return ((TournamentPresenter) getParentPresenter()).getEntity();
	}
}
