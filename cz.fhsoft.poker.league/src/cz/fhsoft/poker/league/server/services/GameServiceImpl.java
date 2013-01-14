package cz.fhsoft.poker.league.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cz.fhsoft.poker.league.client.services.GameService;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;

@SuppressWarnings("serial")
public class GameServiceImpl extends RemoteServiceServlet implements GameService {
	
	private static final Query currentTournaments = ServletInitializer.getEntityManager().createQuery(
			"SELECT t FROM cz.fhsoft.poker.league.shared.model.v1.Tournament t WHERE t.tournamentStart <= :startLimit AND t.tournamentEnd >= :endLimit");

	@Override
	public List<Tournament> getCurrentTournaments() {
		Date currentTime = new Date();
		Date startLimit = new Date(currentTime.getTime() - 3600000);
		Date endLimit = new Date(currentTime.getTime() + 3600000);
		currentTournaments.setParameter("startLimit", startLimit);
		currentTournaments.setParameter("endLimit", endLimit);

		List<Tournament> resultList = new ArrayList<Tournament>();

		synchronized(EntityServiceImpl.LOCK) {
			for(Object obj : currentTournaments.getResultList()) {
				Tournament tournament = (Tournament) obj;
				resultList.add(EntityServiceImpl.makeTransferable(tournament));
			}
		}

		return resultList;
	}

	@Override
	public WhiteList whiteListDummy(WhiteList whiteList) {
		return null;
	}

	@Override
	public long startNewGame(Integer tournamentId, List<Integer> playerIds) {
		synchronized (EntityServiceImpl.LOCK) {
			try {
				ServletInitializer.getEntityManager().getTransaction().begin();

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
				ServletInitializer.getEntityManager().getTransaction().commit();
				return dataVersion;
			}
			finally {
				if(ServletInitializer.getEntityManager().getTransaction().isActive())
					ServletInitializer.getEntityManager().getTransaction().rollback();
			}
		}
	}

	@Override
	public long seatOpen(List<Integer> playerInGameIds) {
		synchronized (EntityServiceImpl.LOCK) {
			try {
				ServletInitializer.getEntityManager().getTransaction().begin();
				
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

					playersInGameForSeatOpen.add(playerInGame);
				}
				
				int minRank = game.getPlayersInGame().size() + 1;
				for(PlayerInGame rankedPlayerInGame : game.getPlayersInGame())
					if(rankedPlayerInGame.getRank() > 0 && rankedPlayerInGame.getRank() < minRank)
						minRank = rankedPlayerInGame.getRank();
				
				if(minRank <= 1)
					throw new IllegalArgumentException("Alespoň jeden hráč byl již označen jako vítěz, nelze vyřadit další hráče");
				
				for(PlayerInGame playerInGameForSeatOpen : playersInGameForSeatOpen)
					playerInGameForSeatOpen.setRank(minRank-playersInGameForSeatOpen.size());

				ServletInitializer.getEntityManager().merge(game);
				long dataVersion = EntityServiceImpl.updateDataVersion();
				ServletInitializer.getEntityManager().getTransaction().commit();
				return dataVersion;
			}
			finally {
				if(ServletInitializer.getEntityManager().getTransaction().isActive())
					ServletInitializer.getEntityManager().getTransaction().rollback();
			}
		}
	}

	@Override
	public long undoSeatOpen(List<Integer> playerInGameIds) {
		synchronized (EntityServiceImpl.LOCK) {
			try {
				ServletInitializer.getEntityManager().getTransaction().begin();
				
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

					playersInGameForUndoSeatOpen.add(playerInGame);
				}
				
				for(PlayerInGame dummy : game.getPlayersInGame()) {
					// do nothing, just force loading
					@SuppressWarnings("unused")
					int dummyInt = dummy.getId();
				}
					
				
				TreeMap<Integer, PlayerInGame> ranks = new TreeMap<Integer, PlayerInGame>();
				for(PlayerInGame rankedPlayerInGame : game.getPlayersInGame())
					if(rankedPlayerInGame.getRank() > 0 && !playersInGameForUndoSeatOpen.contains(rankedPlayerInGame))
						ranks.put(rankedPlayerInGame.getRank(), rankedPlayerInGame);

				for(PlayerInGame playerInGameForUndoSeatOpen : playersInGameForUndoSeatOpen)
					playerInGameForUndoSeatOpen.setRank(0);

				int reRank = 1;
				
				for(PlayerInGame rerankedPlayerInGame : ranks.values())
					rerankedPlayerInGame.setRank(reRank++);
				
				ServletInitializer.getEntityManager().merge(game);
				long dataVersion = EntityServiceImpl.updateDataVersion();
				ServletInitializer.getEntityManager().getTransaction().commit();
				return dataVersion;
			}
			finally {
				if(ServletInitializer.getEntityManager().getTransaction().isActive())
					ServletInitializer.getEntityManager().getTransaction().rollback();
			}
		}
	}
}
