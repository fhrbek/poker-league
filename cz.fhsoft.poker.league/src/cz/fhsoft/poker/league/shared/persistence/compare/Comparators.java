package cz.fhsoft.poker.league.shared.persistence.compare;

import java.util.Comparator;

import cz.fhsoft.poker.league.shared.model.v1.Competition;
import cz.fhsoft.poker.league.shared.model.v1.DescribedEntity;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyFormula;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRule;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class Comparators {

	public static final IdentifiableEntityComparator<IdentifiableEntity> IDENTIFIABLE_ENTITY_COMPARATOR = new IdentifiableEntityComparator<IdentifiableEntity>(); 

	public static final DescribedEntityComparator<DescribedEntity> DESCRIBED_ENTITY_COMPARATOR = new DescribedEntityComparator<DescribedEntity>(); 

	public static final Comparator<Competition> COMPETITIONS_COMPARATOR = new DescribedEntityComparator<Competition>() {

		@Override
		public int compare(Competition c1, Competition c2) {
			int result = - c1.getStartDate().compareTo(c2.getStartDate());
			if(result == 0)
				result = c1.getEndDate().compareTo(c2.getEndDate());
		
			if(result == 0)
				return super.compare(c1, c2);

			return result;
		}
		
	};

	public static final Comparator<Tournament> TOURNAMENTS_COMPARATOR = new DescribedEntityComparator<Tournament>() {

		@Override
		public int compare(Tournament t1, Tournament t2) {
			int result = - t1.getTournamentStart().compareTo(t2.getTournamentStart());
			
			if(result == 0)
				return super.compare(t1, t2);
			
			return result;
		}
		
	};

	public static final Comparator<Player> PLAYERS_COMPARATOR = new IdentifiableEntityComparator<Player>() {

		@Override
		public int compare(Player p1, Player p2) {
			int result = StringUtil.nonNullString(p1.getNick()).compareTo(StringUtil.nonNullString(p2.getNick()));
			if(result == 0)
				result = StringUtil.nonNullString(p1.getLastName()).compareTo(StringUtil.nonNullString(p2.getLastName()));
			if(result == 0)
				result = StringUtil.nonNullString(p1.getFirstName()).compareTo(StringUtil.nonNullString(p2.getFirstName()));
			if(result == 0)
				result = StringUtil.nonNullString(p1.getEmailAddress()).compareTo(StringUtil.nonNullString(p2.getEmailAddress()));

			if(result == 0)
				return super.compare(p1, p2);

			return result;
		}
		
	};

	public static final Comparator<PrizeMoneyRuleSet> PRIZE_MONEY_RULE_SET_COMPARATOR = new Comparator<PrizeMoneyRuleSet>() {

		@Override
		public int compare(PrizeMoneyRuleSet s1, PrizeMoneyRuleSet s2) {
			return s1.getName().compareTo(s2.getName());
		}
		
	};

	public static final Comparator<PrizeMoneyRule> PRIZE_MONEY_RULE_COMPARATOR = new Comparator<PrizeMoneyRule>() {

		@Override
		public int compare(PrizeMoneyRule r1, PrizeMoneyRule r2) {
			int n1 = r1.getNumberOfPlayers();
			int n2 = r2.getNumberOfPlayers();
			
			return n1 > n2
					? 1
					: (n1 < n2
							? -1
							: 0);
		}
		
	};

	public static final Comparator<PrizeMoneyFormula> PRIZE_MONEY_FORMULA_COMPARATOR = new Comparator<PrizeMoneyFormula>() {

		@Override
		public int compare(PrizeMoneyFormula f1, PrizeMoneyFormula f2) {
			int n1 = f1.getRank();
			int n2 = f2.getRank();
			
			return n1 > n2
					? 1
					: (n1 < n2
							? -1
							: 0);
		}
		
	};


}
