package cz.fhsoft.poker.league.shared.model.v1;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * A representation of the model object '<em><b>PrizeMoneyRule</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_PrizeMoneyRule")
public class PrizeMoneyRule extends IdentifiableEntity {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private PrizeMoneyRuleSet prizeMoneyRuleSet = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int numberOfPlayers = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "prizeMoneyRule", orphanRemoval = true)
	private Set<PrizeMoneyFormula> prizeMoneyFormulas = new HashSet<PrizeMoneyFormula>();

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
	 * Sets the '{@link PrizeMoneyRule#getPrizeMoneyRuleSet()
	 * <em>prizeMoneyRuleSet</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newPrizeMoneyRuleSet
	 *            the new value of the '
	 *            {@link PrizeMoneyRule#getPrizeMoneyRuleSet()
	 *            prizeMoneyRuleSet}' feature.
	 * @generated
	 */
	public void setPrizeMoneyRuleSet(PrizeMoneyRuleSet newPrizeMoneyRuleSet) {
		prizeMoneyRuleSet = newPrizeMoneyRuleSet;
	}

	/**
	 * Returns the value of '<em><b>numberOfPlayers</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>numberOfPlayers</b></em>' feature
	 * @generated
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * Sets the '{@link PrizeMoneyRule#getNumberOfPlayers()
	 * <em>numberOfPlayers</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newNumberOfPlayers
	 *            the new value of the '
	 *            {@link PrizeMoneyRule#getNumberOfPlayers() numberOfPlayers}'
	 *            feature.
	 * @generated
	 */
	public void setNumberOfPlayers(int newNumberOfPlayers) {
		numberOfPlayers = newNumberOfPlayers;
	}

	/**
	 * Returns the value of '<em><b>prizeMoneyFormulas</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>prizeMoneyFormulas</b></em>' feature
	 * @generated
	 */
	public Set<PrizeMoneyFormula> getPrizeMoneyFormulas() {
		return prizeMoneyFormulas;
	}

	/**
	 * Adds to the <em>prizeMoneyFormulas</em> feature.
	 * 
	 * @generated
	 */
	public void addToPrizeMoneyFormulas(
			PrizeMoneyFormula prizeMoneyFormulasValue) {
		if (!prizeMoneyFormulas.contains(prizeMoneyFormulasValue)) {
			prizeMoneyFormulas.add(prizeMoneyFormulasValue);
		}
	}

	/**
	 * Removes from the <em>prizeMoneyFormulas</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromPrizeMoneyFormulas(
			PrizeMoneyFormula prizeMoneyFormulasValue) {
		if (prizeMoneyFormulas.contains(prizeMoneyFormulasValue)) {
			prizeMoneyFormulas.remove(prizeMoneyFormulasValue);
		}
	}

	/**
	 * Clears the <em>prizeMoneyFormulas</em> feature.
	 * 
	 * @generated
	 */
	public void clearPrizeMoneyFormulas() {
		while (!prizeMoneyFormulas.isEmpty()) {
			removeFromPrizeMoneyFormulas(prizeMoneyFormulas.iterator().next());
		}
	}

	/**
	 * Sets the '{@link PrizeMoneyRule#getPrizeMoneyFormulas()
	 * <em>prizeMoneyFormulas</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newPrizeMoneyFormulas
	 *            the new value of the '
	 *            {@link PrizeMoneyRule#getPrizeMoneyFormulas()
	 *            prizeMoneyFormulas}' feature.
	 * @generated
	 */
	public void setPrizeMoneyFormulas(
			Set<PrizeMoneyFormula> newPrizeMoneyFormulas) {
		prizeMoneyFormulas = newPrizeMoneyFormulas;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "PrizeMoneyRule " + " [numberOfPlayers: " + getNumberOfPlayers()
				+ "]";
	}
}
