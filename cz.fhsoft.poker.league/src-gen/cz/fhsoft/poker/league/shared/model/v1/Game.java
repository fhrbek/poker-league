package cz.fhsoft.poker.league.shared.model.v1;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * A representation of the model object '<em><b>Game</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_Game")
public class Game extends IdentifiableEntity {
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
	private int ordinal = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Tournament tournament = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int buyIn = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private PrizeMoneyRuleSet prizeMoneyRuleSet = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "game", orphanRemoval = true)
	private Set<PlayerInGame> playersInGame = new HashSet<PlayerInGame>();

	/**
	 * Returns the value of '<em><b>ordinal</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>ordinal</b></em>' feature
	 * @generated
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * Sets the '{@link Game#getOrdinal() <em>ordinal</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newOrdinal
	 *            the new value of the '{@link Game#getOrdinal() ordinal}'
	 *            feature.
	 * @generated
	 */
	public void setOrdinal(int newOrdinal) {
		ordinal = newOrdinal;
	}

	/**
	 * Returns the value of '<em><b>tournament</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>tournament</b></em>' feature
	 * @generated
	 */
	public Tournament getTournament() {
		return tournament;
	}

	/**
	 * Sets the '{@link Game#getTournament() <em>tournament</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newTournament
	 *            the new value of the '{@link Game#getTournament() tournament}'
	 *            feature.
	 * @generated
	 */
	public void setTournament(Tournament newTournament) {
		tournament = newTournament;
	}

	/**
	 * Returns the value of '<em><b>buyIn</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>buyIn</b></em>' feature
	 * @generated
	 */
	public int getBuyIn() {
		return buyIn;
	}

	/**
	 * Sets the '{@link Game#getBuyIn() <em>buyIn</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newBuyIn
	 *            the new value of the '{@link Game#getBuyIn() buyIn}' feature.
	 * @generated
	 */
	public void setBuyIn(int newBuyIn) {
		buyIn = newBuyIn;
	}

	/**
	 * Returns the value of '<em><b>prizeMoneyRuleSet</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>prizeMoneyRuleSet</b></em>' feature
	 * @generated
	 */
	public PrizeMoneyRuleSet getPrizeMoneyRuleSet() {
		return prizeMoneyRuleSet;
	}

	/**
	 * Sets the '{@link Game#getPrizeMoneyRuleSet() <em>prizeMoneyRuleSet</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newPrizeMoneyRuleSet
	 *            the new value of the '{@link Game#getPrizeMoneyRuleSet()
	 *            prizeMoneyRuleSet}' feature.
	 * @generated
	 */
	public void setPrizeMoneyRuleSet(PrizeMoneyRuleSet newPrizeMoneyRuleSet) {
		prizeMoneyRuleSet = newPrizeMoneyRuleSet;
	}

	/**
	 * Returns the value of '<em><b>playersInGame</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of '<em><b>playersInGame</b></em>' feature
	 * @generated
	 */
	public Set<PlayerInGame> getPlayersInGame() {
		return playersInGame;
	}

	/**
	 * Adds to the <em>playersInGame</em> feature.
	 * 
	 * @generated
	 */
	public boolean addToPlayersInGame(PlayerInGame playersInGameValue) {
		if (!playersInGame.contains(playersInGameValue)) {
			boolean result = playersInGame.add(playersInGameValue);
			return result;
		}
		return false;
	}

	/**
	 * Removes from the <em>playersInGame</em> feature.
	 * 
	 * @generated
	 */
	public boolean removeFromPlayersInGame(PlayerInGame playersInGameValue) {
		if (playersInGame.contains(playersInGameValue)) {
			boolean result = playersInGame.remove(playersInGameValue);
			return result;
		}
		return false;
	}

	/**
	 * Clears the <em>playersInGame</em> feature.
	 * 
	 * @generated
	 */
	public void clearPlayersInGame() {
		while (!playersInGame.isEmpty()) {
			removeFromPlayersInGame(playersInGame.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Game#getPlayersInGame() <em>playersInGame</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Game#getPlayersInGame()
	 *            playersInGame}' feature.
	 * @generated
	 */
	public void setPlayersInGame(Set<PlayerInGame> newPlayersInGame) {
		playersInGame = newPlayersInGame;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Game " + " [ordinal: " + getOrdinal() + "]" + " [buyIn: "
				+ getBuyIn() + "]";
	}
}
