package cz.fhsoft.poker.league.client.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.util.TransferrableException;

@RemoteServiceRelativePath("gameService")
public interface GameService extends RemoteService {
	public static class WhiteList implements Serializable {
		private static final long serialVersionUID = -1356352194652396619L;

		Tournament tournament;
	}

	WhiteList whiteListDummy(WhiteList whiteList);
	
	List<Tournament> getCurrentTournaments() throws TransferrableException;
	
	long startNewGame(Integer tournamentId, List<Integer> playerIds) throws TransferrableException;
	
	long seatOpen(List<Integer> playerIds) throws TransferrableException;
	
	long undoSeatOpen(List<Integer> playerIds) throws TransferrableException;
}
