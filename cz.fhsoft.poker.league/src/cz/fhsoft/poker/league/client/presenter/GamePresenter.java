package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.DigestProviders;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.GameView;
import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor.DataProvider;
import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor.LabeledColumn;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.AbstractTemporaryEntityEditor;
import cz.fhsoft.poker.league.client.widget.AbstractTemporaryEntityListEditor;
import cz.fhsoft.poker.league.client.widget.EntitySelector;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class GamePresenter extends RankablePresenter<Game, GameView.Presenter, GameView> implements GameView.Presenter {
	
	public static final AbstractPersistentEntityEditor<Game> gameEditor = new AbstractPersistentEntityEditor<Game>() {
		
		private Entry<IntegerBox> ordinalEntry = new Entry<IntegerBox>("Pořadové číslo", new IntegerBox()) {

			@Override
			public void setUpWidget(Game entity) {
				getWidget().setValue(entity.getOrdinal());
			}

			@Override
			public void updateEntity(Game entity) {
				entity.setOrdinal(getWidget().getValue());
			}
			
		};

		private Entry<IntegerBox> buyInEntry = new Entry<IntegerBox>("Buy-in", new IntegerBox()) {

			@Override
			public void setUpWidget(Game entity) {
				getWidget().setValue(entity.getBuyIn());
			}

			@Override
			public void updateEntity(Game entity) {
				entity.setBuyIn(getWidget().getValue());
			}
			
		};
		
		private Entry<EntitySelector<PrizeMoneyRuleSet>> prizeMoneyRuleSetEntry = new Entry<EntitySelector<PrizeMoneyRuleSet>>("Pravidla dělění výhry",
				new EntitySelector<PrizeMoneyRuleSet>(PrizeMoneyRuleSet.class, Comparators.PRIZE_MONEY_RULE_SET_COMPARATOR, DigestProviders.DESCRIBED_ENTITY_PROVIDER)) {

			@Override
			public void setUpWidget(Game entity) {
				getWidget().setValue(entity.getPrizeMoneyRuleSet());
			}

			@Override
			public void updateEntity(Game entity) {
				entity.setPrizeMoneyRuleSet(getWidget().getValue());
			}
			
		};
		
		private Entry<AbstractTemporaryEntityListEditor<PlayerInGame>> playersInGameEntry = new Entry<AbstractTemporaryEntityListEditor<PlayerInGame>>(
				"Hráči", new AbstractTemporaryEntityListEditor<PlayerInGame>(
						"Nový hráč ve hře",
						new AbstractTemporaryEntityEditor<PlayerInGame>() {
							
							private Entry<IntegerBox> rankEntry = new Entry<IntegerBox>("Pořadí ve hře", new IntegerBox()) {

								@Override
								public void setUpWidget(PlayerInGame entity) {
									getWidget().setValue(entity.getRank());
								}

								@Override
								public void updateEntity(PlayerInGame entity) {
									entity.setRank(getWidget().getValue());
								}
								
							};
							
							private Entry<EntitySelector<Player>> playerEntry = new Entry<EntitySelector<Player>>("Hráč", new EntitySelector<Player>(Player.class, Comparators.PLAYERS_COMPARATOR, DigestProviders.PLAYER_DIGEST_PROVIDER)) {

								@Override
								public void setUpWidget(PlayerInGame entity) {
									getWidget().setValue(entity.getPlayer());
								}

								@Override
								public void updateEntity(PlayerInGame entity) {
									entity.setPlayer(getWidget().getValue());
								}
								
							};
							
							{
								addEntry(rankEntry);
								addEntry(playerEntry);
							}

							@Override
							protected void copyAttributes(PlayerInGame source, PlayerInGame target) {
								target.setPlayer(source.getPlayer());
								target.setRank(source.getRank());
							}

							@Override
							protected SafeHtml getLabel(PlayerInGame entity) {
								return SafeHtmlUtils
										.fromString(entity.getId() > 0 ? "Hráč ve hře"
												: "Nový hráč ve hře");
							}

							@Override
							protected PlayerInGame createTemporaryEntity() {
								return new PlayerInGame();
							}
							
							@Override
							protected void validate(PlayerInGame entity,
									AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
								Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
								if (entity.getRank() < 0)
									errorMap.put(rankEntry,
											"Pořadí ve hře musí být 0 (stále ve hře), nebo kladné číslo (konečné pořadí)");

								if (entity.getPlayer() == null)
									errorMap.put(playerEntry,
											"Nebyl zvolen žádný hráč");

								callback.onSuccess(errorMap);
							}

						}, null,
						new ArrayList<LabeledColumn<PlayerInGame>>() {
							private static final long serialVersionUID = 1L;

							{
								add(new LabeledColumn<PlayerInGame>(
										new Column<PlayerInGame, String>(new TextCell()) {

											@Override
											public String getValue(PlayerInGame playerInGame) {
												int num = playerInGame.getRank();
												switch (num) {
												case 0:
													return "<ve hře>";
												default:
													return num + ".";
												}
											}

										}, "Pořadí"));

								add(new LabeledColumn<PlayerInGame>(
										new Column<PlayerInGame, String>(new TextCell()) {

											@Override
											public String getValue(PlayerInGame playerInGame) {
												return DigestProviders.PLAYER_DIGEST_PROVIDER.getDigest(playerInGame.getPlayer());
											}

										}, "Hráč"));
							}
						}, Comparators.PLAYERS_IN_GAME_COMPARATOR) {

					@Override
					protected PlayerInGame getNewEntity() {
						return new PlayerInGame();
					}
					
				}) {

			@Override
			public void setUpWidget(final Game game) {
				getWidget().setDataProvider(new DataProvider<PlayerInGame>() {

					@Override
					protected void getData(final AsyncCallback<List<PlayerInGame>> callback) {
						Util.resolve(game.getPlayersInGame(), new AsyncCallback<Set<PlayerInGame>>() {

							@Override
							public void onFailure(Throwable caught) {
								ErrorReporter.error(caught);
							}

							@Override
							public void onSuccess(Set<PlayerInGame> resolvedPlayersInGame) {
								List<PlayerInGame> playersInGame = new ArrayList<PlayerInGame>(resolvedPlayersInGame);
								Collections.sort(playersInGame, Comparators.PLAYERS_IN_GAME_COMPARATOR);
								callback.onSuccess(playersInGame);
							}
							
						});
					}
					
				});
				
			}

			@Override
			public void updateEntity(Game entity) {
				List<PlayerInGame> playersInGame = getWidget().getEntities();

				// set/restore the reverse relationship
				for(PlayerInGame playerInGame : playersInGame)
					playerInGame.setGame(entity);

				entity.setPlayersInGame(Util.asSet(playersInGame));
			}
			
		};
		
		{
			addEntry(ordinalEntry);
			addEntry(buyInEntry);
			addEntry(prizeMoneyRuleSetEntry);
			addEntry(playersInGameEntry);
		}

		@Override
		protected SafeHtml getLabel(Game entity) {
			return SafeHtmlUtils.fromString(entity.getId() > 0
					? "Hra č. " + entity.getOrdinal()
					: "Nová hra");
		}

		@Override
		protected Game createTemporaryEntity() {
			return new Game();
		}
		
		@Override
		protected Game stripEntity(Game game) {
			game.setPrizeMoneyRuleSet(Util.proxify(game.getPrizeMoneyRuleSet(), new PrizeMoneyRuleSet()));
			if(!Util.proxify(game.getPlayersInGame()))
				for(PlayerInGame playerInGame : game.getPlayersInGame())
					playerInGame.setPlayer(Util.proxify(playerInGame.getPlayer(), new Player()));
			
			return game;
		}

		@Override
		protected void validate(Game entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(entity.getOrdinal() <= 0)
				errorMap.put(ordinalEntry, "Pořadové číslo hry musí být větší než 0");
			//TODO Validate game
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public GamePresenter(Presenter parentPresenter, GameView view) {
		super(parentPresenter, view);
	}

	@Override
	protected AbstractPersistentEntityEditor<Game> getEditor() {
		return gameEditor;
	}

	@Override
	protected void refresh() {
		if(entity != null) {
			ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<Game>() {

				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}

				@Override
				public void onSuccess(Game resolvedEntity) {
					entity = resolvedEntity;
					view.setOrdinal(entity.getOrdinal());
					//TODO Refresh all displayed values
				}
				
			});
		}
	}

}
