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

	public int getPlayerId() {
		return (Integer) row.get(1);
	}

	public String getPlayerNick() {
		return (String) row.get(2);
	}

	public long getTotalGames() {
		return (Long) row.get(3);
	}

	public long getGamesPlayed() {
		return (Long) row.get(4);
	}

	public long getBuyIns() {
		return ((BigDecimal) row.get(5)).longValue();
	}

	public double getPrizeMoney() {
		return ((BigDecimal) row.get(6)).doubleValue();
	}

	public double getPoints() {
		return ((BigDecimal) row.get(7)).doubleValue();
	}

	public double getRelativePrizeMoney() {
		return ((BigDecimal) row.get(8)).doubleValue();
	}

	public double getRelativePoints() {
		return ((BigDecimal) row.get(9)).doubleValue();
	}

}