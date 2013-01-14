package cz.fhsoft.poker.league.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cz.fhsoft.poker.league.client.services.GameService;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl;
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
}
