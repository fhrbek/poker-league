package cz.fhsoft.poker.league.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.services.GameService.WhiteList;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;

public interface GameServiceAsync {

	void getCurrentTournaments(AsyncCallback<List<Tournament>> callback);

	void whiteListDummy(WhiteList whiteList, AsyncCallback<WhiteList> callback);

}
