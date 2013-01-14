package cz.fhsoft.poker.league.client.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.fhsoft.poker.league.shared.model.v1.Tournament;

@RemoteServiceRelativePath("gameService")
public interface GameService extends RemoteService {
	public static class WhiteList implements Serializable {
		private static final long serialVersionUID = -1356352194652396619L;

		Tournament tournament;
	}

	WhiteList whiteListDummy(WhiteList whiteList);
	
	List<Tournament> getCurrentTournaments();
}
