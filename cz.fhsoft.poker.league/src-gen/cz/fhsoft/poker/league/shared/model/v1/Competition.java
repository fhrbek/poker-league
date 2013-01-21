package cz.fhsoft.poker.league.shared.model.v1;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A representation of the model object '<em><b>Competition</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_Competition")
public class Competition extends DescribedEntity {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Temporal(TemporalType.DATE)
	private Date startDate = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Temporal(TemporalType.DATE)
	private Date endDate = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int defaultBuyIn = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private PrizeMoneyRuleSet defaultPrizeMoneyRuleSet = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int defaultMinPlayers = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int defaultMaxPlayers = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int defaultTournamentAnnouncementLead = 48;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "competition", orphanRemoval = true)
	private Set<Tournament> tournaments = new HashSet<Tournament>();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Set<Player> players = new HashSet<Player>();

	/**
	 * Returns the value of '<em><b>startDate</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>startDate</b></em>' feature
	 * @generated
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the '{@link Competition#getStartDate() <em>startDate</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getStartDate() startDate}
	 *            ' feature.
	 * @generated
	 */
	public void setStartDate(Date newStartDate) {
		startDate = newStartDate;
	}

	/**
	 * Returns the value of '<em><b>endDate</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>endDate</b></em>' feature
	 * @generated
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the '{@link Competition#getEndDate() <em>endDate</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getEndDate() endDate}'
	 *            feature.
	 * @generated
	 */
	public void setEndDate(Date newEndDate) {
		endDate = newEndDate;
	}

	/**
	 * Returns the value of '<em><b>defaultBuyIn</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>defaultBuyIn</b></em>' feature
	 * @generated
	 */
	public int getDefaultBuyIn() {
		return defaultBuyIn;
	}

	/**
	 * Sets the '{@link Competition#getDefaultBuyIn() <em>defaultBuyIn</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getDefaultBuyIn()
	 *            defaultBuyIn}' feature.
	 * @generated
	 */
	public void setDefaultBuyIn(int newDefaultBuyIn) {
		defaultBuyIn = newDefaultBuyIn;
	}

	/**
	 * Returns the value of '<em><b>defaultPrizeMoneyRuleSet</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>defaultPrizeMoneyRuleSet</b></em>' feature
	 * @generated
	 */
	public PrizeMoneyRuleSet getDefaultPrizeMoneyRuleSet() {
		return defaultPrizeMoneyRuleSet;
	}

	/**
	 * Sets the '{@link Competition#getDefaultPrizeMoneyRuleSet()
	 * <em>defaultPrizeMoneyRuleSet</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '
	 *            {@link Competition#getDefaultPrizeMoneyRuleSet()
	 *            defaultPrizeMoneyRuleSet}' feature.
	 * @generated
	 */
	public void setDefaultPrizeMoneyRuleSet(
			PrizeMoneyRuleSet newDefaultPrizeMoneyRuleSet) {
		defaultPrizeMoneyRuleSet = newDefaultPrizeMoneyRuleSet;
	}

	/**
	 * Returns the value of '<em><b>defaultMinPlayers</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>defaultMinPlayers</b></em>' feature
	 * @generated
	 */
	public int getDefaultMinPlayers() {
		return defaultMinPlayers;
	}

	/**
	 * Sets the '{@link Competition#getDefaultMinPlayers()
	 * <em>defaultMinPlayers</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getDefaultMinPlayers()
	 *            defaultMinPlayers}' feature.
	 * @generated
	 */
	public void setDefaultMinPlayers(int newDefaultMinPlayers) {
		defaultMinPlayers = newDefaultMinPlayers;
	}

	/**
	 * Returns the value of '<em><b>defaultMaxPlayers</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>defaultMaxPlayers</b></em>' feature
	 * @generated
	 */
	public int getDefaultMaxPlayers() {
		return defaultMaxPlayers;
	}

	/**
	 * Sets the '{@link Competition#getDefaultMaxPlayers()
	 * <em>defaultMaxPlayers</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getDefaultMaxPlayers()
	 *            defaultMaxPlayers}' feature.
	 * @generated
	 */
	public void setDefaultMaxPlayers(int newDefaultMaxPlayers) {
		defaultMaxPlayers = newDefaultMaxPlayers;
	}

	/**
	 * Returns the value of '<em><b>defaultTournamentAnnouncementLead</b></em>'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>defaultTournamentAnnouncementLead</b></em>'
	 *         feature
	 * @generated
	 */
	public int getDefaultTournamentAnnouncementLead() {
		return defaultTournamentAnnouncementLead;
	}

	/**
	 * Sets the '{@link Competition#getDefaultTournamentAnnouncementLead()
	 * <em>defaultTournamentAnnouncementLead</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '
	 *            {@link Competition#getDefaultTournamentAnnouncementLead()
	 *            defaultTournamentAnnouncementLead}' feature.
	 * @generated
	 */
	public void setDefaultTournamentAnnouncementLead(
			int newDefaultTournamentAnnouncementLead) {
		defaultTournamentAnnouncementLead = newDefaultTournamentAnnouncementLead;
	}

	/**
	 * Returns the value of '<em><b>tournaments</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>tournaments</b></em>' feature
	 * @generated
	 */
	public Set<Tournament> getTournaments() {
		return tournaments;
	}

	/**
	 * Adds to the <em>tournaments</em> feature.
	 * 
	 * @generated
	 */
	public void addToTournaments(Tournament tournamentsValue) {
		if (!tournaments.contains(tournamentsValue)) {
			tournaments.add(tournamentsValue);
		}
	}

	/**
	 * Removes from the <em>tournaments</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromTournaments(Tournament tournamentsValue) {
		if (tournaments.contains(tournamentsValue)) {
			tournaments.remove(tournamentsValue);
		}
	}

	/**
	 * Clears the <em>tournaments</em> feature.
	 * 
	 * @generated
	 */
	public void clearTournaments() {
		while (!tournaments.isEmpty()) {
			removeFromTournaments(tournaments.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Competition#getTournaments() <em>tournaments</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getTournaments()
	 *            tournaments}' feature.
	 * @generated
	 */
	public void setTournaments(Set<Tournament> newTournaments) {
		tournaments = newTournaments;
	}

	/**
	 * Returns the value of '<em><b>players</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>players</b></em>' feature
	 * @generated
	 */
	public Set<Player> getPlayers() {
		return players;
	}

	/**
	 * Adds to the <em>players</em> feature.
	 * 
	 * @generated
	 */
	public void addToPlayers(Player playersValue) {
		if (!players.contains(playersValue)) {
			players.add(playersValue);
		}
	}

	/**
	 * Removes from the <em>players</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromPlayers(Player playersValue) {
		if (players.contains(playersValue)) {
			players.remove(playersValue);
		}
	}

	/**
	 * Clears the <em>players</em> feature.
	 * 
	 * @generated
	 */
	public void clearPlayers() {
		while (!players.isEmpty()) {
			removeFromPlayers(players.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Competition#getPlayers() <em>players</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Competition#getPlayers() players}'
	 *            feature.
	 * @generated
	 */
	public void setPlayers(Set<Player> newPlayers) {
		players = newPlayers;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Competition " + " [startDate: " + getStartDate() + "]"
				+ " [endDate: " + getEndDate() + "]" + " [defaultBuyIn: "
				+ getDefaultBuyIn() + "]" + " [defaultMinPlayers: "
				+ getDefaultMinPlayers() + "]" + " [defaultMaxPlayers: "
				+ getDefaultMaxPlayers() + "]"
				+ " [defaultTournamentAnnouncementLead: "
				+ getDefaultTournamentAnnouncementLead() + "]";
	}
}
