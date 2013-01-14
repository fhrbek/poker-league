package cz.fhsoft.poker.league.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.services.GameService.WhiteList;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;

public interface GameServiceAsync {

	void getCurrentTournaments(AsyncCallback<List<Tournament>> callback);

	void whiteListDummy(WhiteList whiteList, AsyncCallback<WhiteList> callback);

	void startNewGame(Integer tournamentId, List<Integer> playerIds,
			AsyncCallback<Long> callback);

	void seatOpen(List<Integer> playerIds,
			AsyncCallback<Long> callback);

	void undoSeatOpen(List<Integer> playerIds,
			AsyncCallback<Long> callback);

}
