package cz.fhsoft.poker.league.shared.model.v1;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A representation of the model object '<em><b>Tournament</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_Tournament")
public class Tournament extends DescribedEntity {
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
	private int tournamentAnnouncementLead = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Temporal(TemporalType.TIMESTAMP)
	private Date tournamentStart = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Temporal(TemporalType.TIMESTAMP)
	private Date tournamentEnd = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int minPlayers = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int maxPlayers = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "tournament", orphanRemoval = true)
	private Set<Invitation> invitations = new HashSet<Invitation>();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Competition competition = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "tournament", orphanRemoval = true)
	private Set<Game> games = new HashSet<Game>();

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
	 * Sets the '{@link Tournament#getDefaultBuyIn() <em>defaultBuyIn</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getDefaultBuyIn()
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
	 * Sets the '{@link Tournament#getDefaultPrizeMoneyRuleSet()
	 * <em>defaultPrizeMoneyRuleSet</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '
	 *            {@link Tournament#getDefaultPrizeMoneyRuleSet()
	 *            defaultPrizeMoneyRuleSet}' feature.
	 * @generated
	 */
	public void setDefaultPrizeMoneyRuleSet(
			PrizeMoneyRuleSet newDefaultPrizeMoneyRuleSet) {
		defaultPrizeMoneyRuleSet = newDefaultPrizeMoneyRuleSet;
	}

	/**
	 * Returns the value of '<em><b>tournamentAnnouncementLead</b></em>'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>tournamentAnnouncementLead</b></em>' feature
	 * @generated
	 */
	public int getTournamentAnnouncementLead() {
		return tournamentAnnouncementLead;
	}

	/**
	 * Sets the '{@link Tournament#getTournamentAnnouncementLead()
	 * <em>tournamentAnnouncementLead</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '
	 *            {@link Tournament#getTournamentAnnouncementLead()
	 *            tournamentAnnouncementLead}' feature.
	 * @generated
	 */
	public void setTournamentAnnouncementLead(int newTournamentAnnouncementLead) {
		tournamentAnnouncementLead = newTournamentAnnouncementLead;
	}

	/**
	 * Returns the value of '<em><b>tournamentStart</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>tournamentStart</b></em>' feature
	 * @generated
	 */
	public Date getTournamentStart() {
		return tournamentStart;
	}

	/**
	 * Sets the '{@link Tournament#getTournamentStart()
	 * <em>tournamentStart</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getTournamentStart()
	 *            tournamentStart}' feature.
	 * @generated
	 */
	public void setTournamentStart(Date newTournamentStart) {
		tournamentStart = newTournamentStart;
	}

	/**
	 * Returns the value of '<em><b>tournamentEnd</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>tournamentEnd</b></em>' feature
	 * @generated
	 */
	public Date getTournamentEnd() {
		return tournamentEnd;
	}

	/**
	 * Sets the '{@link Tournament#getTournamentEnd() <em>tournamentEnd</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getTournamentEnd()
	 *            tournamentEnd}' feature.
	 * @generated
	 */
	public void setTournamentEnd(Date newTournamentEnd) {
		tournamentEnd = newTournamentEnd;
	}

	/**
	 * Returns the value of '<em><b>minPlayers</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>minPlayers</b></em>' feature
	 * @generated
	 */
	public int getMinPlayers() {
		return minPlayers;
	}

	/**
	 * Sets the '{@link Tournament#getMinPlayers() <em>minPlayers</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getMinPlayers()
	 *            minPlayers}' feature.
	 * @generated
	 */
	public void setMinPlayers(int newMinPlayers) {
		minPlayers = newMinPlayers;
	}

	/**
	 * Returns the value of '<em><b>maxPlayers</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>maxPlayers</b></em>' feature
	 * @generated
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * Sets the '{@link Tournament#getMaxPlayers() <em>maxPlayers</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getMaxPlayers()
	 *            maxPlayers}' feature.
	 * @generated
	 */
	public void setMaxPlayers(int newMaxPlayers) {
		maxPlayers = newMaxPlayers;
	}

	/**
	 * Returns the value of '<em><b>invitations</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>invitations</b></em>' feature
	 * @generated
	 */
	public Set<Invitation> getInvitations() {
		return invitations;
	}

	/**
	 * Adds to the <em>invitations</em> feature.
	 * 
	 * @generated
	 */
	public void addToInvitations(Invitation invitationsValue) {
		if (!invitations.contains(invitationsValue)) {
			invitations.add(invitationsValue);
		}
	}

	/**
	 * Removes from the <em>invitations</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromInvitations(Invitation invitationsValue) {
		if (invitations.contains(invitationsValue)) {
			invitations.remove(invitationsValue);
		}
	}

	/**
	 * Clears the <em>invitations</em> feature.
	 * 
	 * @generated
	 */
	public void clearInvitations() {
		while (!invitations.isEmpty()) {
			removeFromInvitations(invitations.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Tournament#getInvitations() <em>invitations</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getInvitations()
	 *            invitations}' feature.
	 * @generated
	 */
	public void setInvitations(Set<Invitation> newInvitations) {
		invitations = newInvitations;
	}

	/**
	 * Returns the value of '<em><b>competition</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>competition</b></em>' feature
	 * @generated
	 */
	public Competition getCompetition() {
		return competition;
	}

	/**
	 * Sets the '{@link Tournament#getCompetition() <em>competition</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getCompetition()
	 *            competition}' feature.
	 * @generated
	 */
	public void setCompetition(Competition newCompetition) {
		competition = newCompetition;
	}

	/**
	 * Returns the value of '<em><b>games</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>games</b></em>' feature
	 * @generated
	 */
	public Set<Game> getGames() {
		return games;
	}

	/**
	 * Adds to the <em>games</em> feature.
	 * 
	 * @generated
	 */
	public void addToGames(Game gamesValue) {
		if (!games.contains(gamesValue)) {
			games.add(gamesValue);
		}
	}

	/**
	 * Removes from the <em>games</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromGames(Game gamesValue) {
		if (games.contains(gamesValue)) {
			games.remove(gamesValue);
		}
	}

	/**
	 * Clears the <em>games</em> feature.
	 * 
	 * @generated
	 */
	public void clearGames() {
		while (!games.isEmpty()) {
			removeFromGames(games.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Tournament#getGames() <em>games</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Tournament#getGames() games}'
	 *            feature.
	 * @generated
	 */
	public void setGames(Set<Game> newGames) {
		games = newGames;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Tournament " + " [defaultBuyIn: " + getDefaultBuyIn() + "]"
				+ " [tournamentAnnouncementLead: "
				+ getTournamentAnnouncementLead() + "]" + " [tournamentStart: "
				+ getTournamentStart() + "]" + " [tournamentEnd: "
				+ getTournamentEnd() + "]" + " [minPlayers: " + getMinPlayers()
				+ "]" + " [maxPlayers: " + getMaxPlayers() + "]";
	}
}
