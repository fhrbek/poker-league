package cz.fhsoft.poker.league.shared.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class RankingRecord implements Rankable, Serializable {
	
	private static final long serialVersionUID = 2913994578287389874L;

	private int rank = 0;
	
	private boolean split = false;
	
	private List<Object> row;
	
	protected RankingRecord() {
		
	}
	
	public RankingRecord(List<Object> row) {
		this.row = row;
	}

	@Override
	public int getRank() {
		return rank;
	}
	
	@Override
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Override
	public boolean isSplit() {
		return split;
	}

	@Override
	public void setSplit(boolean split) {
		this.split = split;
	}
	
	public int getCompetitionId() {
		return (Integer) row.get(0);
	}

	public int getMinimalAttendance() {
		return (Integer) row.get(1);
	}

	public int getPlayerId() {
		return (Integer) row.get(2);
	}

	public String getPlayerNick() {
		return (String) row.get(3);
	}

	public int getInGameFlag() {
		return (Integer) row.get(4);
	}

	public long getTotalGames() {
		return (Long) row.get(5);
	}

	public long getGamesPlayed() {
		return (Long) row.get(6);
	}

	public long getBuyIns() {
		return (Long) row.get(7);
	}

	public double getPrizeMoney() {
		return ((BigDecimal) row.get(8)).doubleValue();
	}

	public double getPoints() {
		return ((BigDecimal) row.get(9)).doubleValue();
	}

	public double getRelativePrizeMoney() {
		return ((BigDecimal) row.get(10)).doubleValue();
	}

	public double getRelativePoints() {
		return ((BigDecimal) row.get(11)).doubleValue();
	}

	public int[] getRankCount(int rank) {
		int[] result = new int[] {0, 0};
		if(rank >= 1 && rank <= 4) {
			result[0] = ((Long) row.get(11+rank)).intValue();
			result[1] = ((Long) row.get(15+rank)).intValue();
		}
		
		return result;
	}

	public long getBubbles() {
		return (Long) row.get(20);
	}
}