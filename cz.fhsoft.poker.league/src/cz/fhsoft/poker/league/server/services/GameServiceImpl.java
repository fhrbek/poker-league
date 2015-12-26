package cz.fhsoft.poker.league.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Query;

import cz.fhsoft.poker.league.client.services.GameService;
import cz.fhsoft.poker.league.server.AbstractServiceImpl;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl.DataAction;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.util.TransferrableException;

@SuppressWarnings("serial")
public class GameServiceImpl extends AbstractServiceImpl implements GameService {
	
	private static final Query currentTournaments = ServletInitializer.getEntityManager().createQuery(
			"SELECT t FROM cz.fhsoft.poker.league.shared.model.v1.Tournament t WHERE t.tournamentStart <= :startLimit AND t.tournamentEnd >= :endLimit");

	@Override
	public List<Tournament> getCurrentTournaments() throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<List<Tournament>>() {

			@Override
			public List<Tournament> run() throws TransferrableException {
				Date currentTime = new Date();
				Date startLimit = new Date(currentTime.getTime() + 3600000);
				Date endLimit = new Date(currentTime.getTime() - 3600000);

				List<Tournament> resultList = new ArrayList<Tournament>();

				currentTournaments.setParameter("startLimit", startLimit);
				currentTournaments.setParameter("endLimit", endLimit);

				for(Object obj : currentTournaments.getResultList()) {
					Tournament tournament = (Tournament) obj;
					resultList.add(EntityServiceImpl.makeTransferable(tournament));
				}

				return resultList;
			}
			
		});
	}

	@Override
	public WhiteList whiteListDummy(WhiteList whiteList) {
		return null;
	}

	@Override
	public long startNewGame(final Integer tournamentId, final List<Integer> playerIds) throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<Long>() {

			@Override
			public Long run() throws TransferrableException {
				Tournament tournament = ServletInitializer.getEntityManager().find(Tournament.class, tournamentId);
				if(tournament == null)
					throw new IllegalArgumentException("Nebyl nalezen turnaj s ID=" + tournamentId);
				
				int maxGameOrdinal = 0;
				
				for(Game game : tournament.getGames())
					if(game.getOrdinal() > maxGameOrdinal)
						maxGameOrdinal = game.getOrdinal();
				
				Game game = new Game();
				game.setTournament(tournament);
				game.setOrdinal(maxGameOrdinal+1);
				game.setBuyIn(tournament.getDefaultBuyIn());
				game.setPrizeMoneyRuleSet(tournament.getDefaultPrizeMoneyRuleSet());
			
				for(Integer id : playerIds) {
					Player player = ServletInitializer.getEntityManager().find(Player.class, id);
					if(player == null)
						throw new IllegalArgumentException("Nebyl nalezen hráč s ID=" + id);
					PlayerInGame playerInGame = new PlayerInGame();
					playerInGame.setGame(game);
					playerInGame.setPlayer(player);
					playerInGame.setRank(0);
					game.addToPlayersInGame(playerInGame);
				}
				
				ServletInitializer.getEntityManager().merge(game);
				long dataVersion = EntityServiceImpl.updateDataVersion();

				return dataVersion;
			}
			
		});
	}

	@Override
	public long seatOpen(final List<Integer> playerInGameIds) throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<Long>() {

			@Override
			public Long run() throws TransferrableException {
				Game game = null;
				
				List<PlayerInGame> playersInGameForSeatOpen = new ArrayList<PlayerInGame>();

				for(Integer playerInGameId : playerInGameIds) {
					PlayerInGame playerInGame = ServletInitializer.getEntityManager().find(PlayerInGame.class, playerInGameId);
					if(playerInGame == null)
						throw new IllegalArgumentException("Hráči k vyřazení s ID=" + playerInGameId + " nebyl nalezen");
					
					if(game == null)
						game = playerInGame.getGame();
					else if(game != playerInGame.getGame())
						throw new IllegalArgumentException("Hráči k vyřazení nehrají stejnou hru");

					if(playerInGame.getRank() == 0)
						playersInGameForSeatOpen.add(playerInGame);
				}

				if(game == null)
					return EntityServiceImpl.getDataVersionStatic();

				int minRank = game.getPlayersInGame().size() + 1;
				for(PlayerInGame rankedPlayerInGame : game.getPlayersInGame())
					if(rankedPlayerInGame.getRank() > 0 && rankedPlayerInGame.getRank() < minRank)
						minRank = rankedPlayerInGame.getRank();
				
				if(minRank <= 1)
					throw new IllegalArgumentException("Alespoň jeden hráč byl již označen jako vítěz, nelze vyřadit další hráče");
				
				int finalRank = minRank - playersInGameForSeatOpen.size();

				if(finalRank == 2) // there's just one player left - let's mark him as a winner right now
					for(PlayerInGame playerInGame : game.getPlayersInGame())
						if(!playersInGameForSeatOpen.contains(playerInGame) && playerInGame.getRank() == 0) {
							playerInGame.setRank(1);
							break;
						}

				for(PlayerInGame playerInGameForSeatOpen : playersInGameForSeatOpen)
					playerInGameForSeatOpen.setRank(finalRank);
				
				ServletInitializer.getEntityManager().merge(game);
				long dataVersion = EntityServiceImpl.updateDataVersion();

				return dataVersion;
			}
			
		});
	}

	@Override
	public long undoSeatOpen(final List<Integer> playerInGameIds) throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<Long>() {

			@Override
			public Long run() throws TransferrableException {
				Game game = null;
				
				List<PlayerInGame> playersInGameForUndoSeatOpen = new ArrayList<PlayerInGame>();

				for(Integer playerInGameId : playerInGameIds) {
					PlayerInGame playerInGame = ServletInitializer.getEntityManager().find(PlayerInGame.class, playerInGameId);
					if(playerInGame == null)
						throw new IllegalArgumentException("Hráči k zařazení zpět do hry s ID=" + playerInGameId + " nebyl nalezen");

					if(game == null)
						game = playerInGame.getGame();
					else if(game != playerInGame.getGame())
						throw new IllegalArgumentException("Hráči k zařazení zpět do hry nehrají stejnou hru");

					if(playerInGame.getRank() > 0)
						playersInGameForUndoSeatOpen.add(playerInGame);
				}

				if(game == null)
					return EntityServiceImpl.getDataVersionStatic();

				int reRank = 1;
				
				TreeMap<Integer, PlayerInGame> ranks = new TreeMap<Integer, PlayerInGame>();
				for(PlayerInGame rankedPlayerInGame : game.getPlayersInGame())
					if(rankedPlayerInGame.getRank() > 0 && !playersInGameForUndoSeatOpen.contains(rankedPlayerInGame))
						ranks.put(rankedPlayerInGame.getRank(), rankedPlayerInGame);
					else
						reRank++;

				for(PlayerInGame playerInGameForUndoSeatOpen : playersInGameForUndoSeatOpen)
					playerInGameForUndoSeatOpen.setRank(0);

				for(PlayerInGame rerankedPlayerInGame : ranks.values())
					rerankedPlayerInGame.setRank(reRank++);
				
				ServletInitializer.getEntityManager().merge(game);
				long dataVersion = EntityServiceImpl.updateDataVersion();

				return dataVersion;
			}
			
		});
	}
}
