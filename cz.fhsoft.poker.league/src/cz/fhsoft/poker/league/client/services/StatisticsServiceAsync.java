package cz.fhsoft.poker.league.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.services.StatisticsService.WhiteList;
import cz.fhsoft.poker.league.shared.services.RankingRecord;

public interface StatisticsServiceAsync {

	void getRanking(String entityClass, Number id, AsyncCallback<List<RankingRecord>> callback);

	void whiteListDummy(WhiteList whiteList, AsyncCallback<WhiteList> callback);

}
