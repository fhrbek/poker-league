package cz.fhsoft.poker.league.shared.services;

import java.io.Serializable;

public interface Rankable extends Serializable {

	int getRank();
	
	void setRank(int rank);

	boolean isSplit();
	
	void setSplit(boolean split);
}