package cz.fhsoft.poker.league.server.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import cz.fhsoft.poker.league.client.services.StatisticsService;
import cz.fhsoft.poker.league.server.AbstractServiceImpl;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.shared.model.v1.Competition;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.services.RankingRecord;

@SuppressWarnings("serial")
public class StatisticsServiceImpl extends AbstractServiceImpl implements StatisticsService {

	private static final String ALIAS_PLACEHOLDER = "@@CONDITION@@";
	
	private static final String SQL_TEMPLATE =
		//@fmtOff
		"SELECT prize_and_points.COMPETITION_ID," +
		"       p.ID AS PLAYER_ID," +
		"       p.NICK AS PLAYER_NICK," +
		"       (SELECT COUNT(*)" +
		"          FROM PL_COMPETITION c JOIN PL_TOURNAMENT t ON t.COMPETITION_ID = c.ID" +
		"                                JOIN PL_GAME g ON g.TOURNAMENT_ID = t.ID" +
		"          WHERE " + ALIAS_PLACEHOLDER + ".ID = ?) AS TOTAL_GAMES," +
		"       COUNT(DISTINCT prize_and_points.GAME_ID) AS GAMES_PLAYED," +
		"       SUM(g.BUYIN) AS BUY_INS," +
		"       SUM(prize_and_points.PRIZE_MONEY) AS PRIZE_MONEY," +
		"       SUM(prize_and_points.POINTS) AS POINTS," +
		"       SUM(prize_and_points.PRIZE_MONEY)/COUNT(DISTINCT prize_and_points.GAME_ID) AS RELATIVE_PRIZE_MONEY," +
		"       SUM(prize_and_points.POINTS)/COUNT(DISTINCT prize_and_points.GAME_ID) AS RELATIVE_POINTS" +
		"  FROM (" +
		"    SELECT" +
		"      prize.COMPETITION_ID," +
		"      prize.TOURNAMENT_ID," +
		"      prize.GAME_ID," +
		"      prize.PLAYER_ID," +
		"      prize.RANK," +
		"      prize.PRIZE_MONEY," +
		"      (2*(SUM(CASE WHEN pig_worse.RANK >= prize.RANK THEN 1 ELSE 0 END)-1) -" +
		"         (SUM(CASE WHEN pig_worse.RANK = prize.RANK THEN 1 ELSE 0 END)-1)) / 2 AS POINTS" +
		"      FROM (" +
		"        SELECT" +
		"          c.ID as COMPETITION_ID," +
		"          t.ID as TOURNAMENT_ID," +
		"          g.ID as GAME_ID," +
		"          pig.PLAYER_ID as PLAYER_ID," +
		"          pig.RANK as RANK," +
		"          AVG(CASE WHEN pmf.RELATIVEPRIZEMONEY IS NULL THEN 0 ELSE pmf.RELATIVEPRIZEMONEY * g.BUYIN END) / 100 AS PRIZE_MONEY" +
		"          FROM PL_COMPETITION c" +
		"            JOIN PL_TOURNAMENT t ON t.COMPETITION_ID = c.ID" +
		"            JOIN PL_GAME g ON g.TOURNAMENT_ID = t.ID" +
		"            JOIN PL_PLAYERINGAME pig ON pig.GAME_ID = g.ID" +
		"            JOIN PL_PRIZEMONEYRULESET pmrs ON pmrs.ID = g.PRIZEMONEYRULESET_ID" +
		"            LEFT OUTER JOIN PL_PRIZEMONEYRULE pmr ON pmr.PRIZEMONEYRULESET_ID = pmrs.ID AND" +
		"                                                     pmr.NUMBEROFPLAYERS = (SELECT COUNT(*)" +
		"                                                                             FROM PL_PLAYERINGAME pmrpig" +
		"                                                                             WHERE pmrpig.GAME_ID = g.ID)" +
		"            LEFT OUTER JOIN PL_PRIZEMONEYFORMULA pmf ON pmf.PRIZEMONEYRULE_ID = pmr.ID AND" +
		"                                                        pmf.RANK BETWEEN pig.RANK AND" +
		"                                                                 (pig.RANK + (SELECT COUNT(*)" +
		"                                                                                FROM PL_PLAYERINGAME pig_split" +
		"                                                                                WHERE pig_split.GAME_ID = g.ID" +
		"                                                                                  AND pig_split.RANK = pig.RANK) - 1)" +
		"          WHERE " + ALIAS_PLACEHOLDER + ".ID = ?" +
		"          GROUP BY c.ID, t.ID, g.ID, pig.ID, pig.RANK) prize" +
		"        LEFT OUTER JOIN PL_PLAYERINGAME pig_worse ON pig_worse.GAME_ID = prize.GAME_ID AND" +
		"                                                     pig_worse.RANK >= prize.RANK" +
		"        GROUP BY prize.COMPETITION_ID, prize.TOURNAMENT_ID, prize.GAME_ID, prize.PLAYER_ID, prize.RANK, prize.PRIZE_MONEY) prize_and_points" +
		"    JOIN PL_PLAYER p ON p.ID = prize_and_points.PLAYER_ID" +
		"    JOIN PL_GAME g ON g.ID = prize_and_points.GAME_ID" +
		"  GROUP BY prize_and_points.COMPETITION_ID, p.ID, p.NICK";
		//@fmtOn
	
	private final static String COMPETITION_SQL = SQL_TEMPLATE.replace(ALIAS_PLACEHOLDER, "c");

	private final static String TOURNAMENT_SQL = SQL_TEMPLATE.replace(ALIAS_PLACEHOLDER, "t");

	private final static String GAME_SQL = SQL_TEMPLATE.replace(ALIAS_PLACEHOLDER, "g");
	
	private final static Map<String, String> SQL_MAP = new HashMap<String, String>();
	
	static {
		SQL_MAP.put(Competition.class.getName(), COMPETITION_SQL);
		SQL_MAP.put(Tournament.class.getName(), TOURNAMENT_SQL);
		SQL_MAP.put(Game.class.getName(), GAME_SQL);
	}

	@Override
	public List<RankingRecord> getRanking(String entityClass, Number id) {
		List<List<Object>> results = new ArrayList<List<Object>>();
		String sql = SQL_MAP.get(entityClass);
		if(sql == null)
			throw new IllegalArgumentException("Unsupported entity type: " + entityClass);

		Query nativeQuery = ServletInitializer.getEntityManager().createNativeQuery(sql);
		nativeQuery.setParameter(1, id);
		nativeQuery.setParameter(2, id);
		
		for(Object row : nativeQuery.getResultList()) {
			//We cannot use Arrays.asList() since the result wouldn't be serializable by GWT
			List<Object> rowList = new ArrayList<Object>();
			for(Object obj : (Object[]) row)
				rowList.add(obj);
			results.add(rowList);
		}

		List<RankingRecord> rankingRecords = new ArrayList<RankingRecord>(results.size());
		for(List<Object> row : results)
			rankingRecords.add(createRankingRecord(row));
		
		return rankingRecords;
	}

	private RankingRecord createRankingRecord(List<Object> row) {
		return new RankingRecord(row);
	}

	@Override
	public WhiteList whiteListDummy(WhiteList whiteList) {
		return null;
	}
}
