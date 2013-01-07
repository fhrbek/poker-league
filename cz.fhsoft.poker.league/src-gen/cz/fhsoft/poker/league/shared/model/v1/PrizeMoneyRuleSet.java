package cz.fhsoft.poker.league.shared.model.v1;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * A representation of the model object '<em><b>PrizeMoneyRuleSet</b></em>'.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_PrizeMoneyRuleSet")
public class PrizeMoneyRuleSet extends DescribedEntity {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "prizeMoneyRuleSet", orphanRemoval = true)
	private Set<PrizeMoneyRule> prizeMoneyRules = new HashSet<PrizeMoneyRule>();

	/**
	 * Returns the value of '<em><b>prizeMoneyRules</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>prizeMoneyRules</b></em>' feature
	 * @generated
	 */
	public Set<PrizeMoneyRule> getPrizeMoneyRules() {
		return prizeMoneyRules;
	}

	/**
	 * Adds to the <em>prizeMoneyRules</em> feature.
	 * 
	 * @generated
	 */
	public void addToPrizeMoneyRules(PrizeMoneyRule prizeMoneyRulesValue) {
		if (!prizeMoneyRules.contains(prizeMoneyRulesValue)) {
			prizeMoneyRules.add(prizeMoneyRulesValue);
		}
	}

	/**
	 * Removes from the <em>prizeMoneyRules</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromPrizeMoneyRules(PrizeMoneyRule prizeMoneyRulesValue) {
		if (prizeMoneyRules.contains(prizeMoneyRulesValue)) {
			prizeMoneyRules.remove(prizeMoneyRulesValue);
		}
	}

	/**
	 * Clears the <em>prizeMoneyRules</em> feature.
	 * 
	 * @generated
	 */
	public void clearPrizeMoneyRules() {
		while (!prizeMoneyRules.isEmpty()) {
			removeFromPrizeMoneyRules(prizeMoneyRules.iterator().next());
		}
	}

	/**
	 * Sets the '{@link PrizeMoneyRuleSet#getPrizeMoneyRules()
	 * <em>prizeMoneyRules</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '
	 *            {@link PrizeMoneyRuleSet#getPrizeMoneyRules() prizeMoneyRules}
	 *            ' feature.
	 * @generated
	 */
	public void setPrizeMoneyRules(Set<PrizeMoneyRule> newPrizeMoneyRules) {
		prizeMoneyRules = newPrizeMoneyRules;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "PrizeMoneyRuleSet ";
	}
}
