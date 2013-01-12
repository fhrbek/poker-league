package cz.fhsoft.poker.league.client.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.fhsoft.poker.league.shared.services.RankingRecord;

@RemoteServiceRelativePath("statisticsService")
public interface StatisticsService extends RemoteService {
	public static class WhiteList implements Serializable {
		private static final long serialVersionUID = -1356352194652396619L;

		Integer integer;
		Long longInt;
		BigDecimal bigDecimal;
		RankingRecord rankable;
	}

	WhiteList whiteListDummy(WhiteList whiteList);
	
	List<RankingRecord> getRanking(String entityClass, Number id);
}
