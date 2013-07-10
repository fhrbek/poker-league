package cz.fhsoft.poker.league.shared.model.v1;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A representation of the model object '<em><b>PrizeMoneyFormula</b></em>'.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_PrizeMoneyFormula")
public class PrizeMoneyFormula extends IdentifiableEntity {
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
	private PrizeMoneyRule prizeMoneyRule = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int rank = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int relativePrizeMoney = 0;

	/**
	 * Returns the value of '<em><b>prizeMoneyRule</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>prizeMoneyRule</b></em>' feature
	 * @generated
	 */
	public PrizeMoneyRule getPrizeMoneyRule() {
		return prizeMoneyRule;
	}

	/**
	 * Sets the '{@link PrizeMoneyFormula#getPrizeMoneyRule()
	 * <em>prizeMoneyRule</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newPrizeMoneyRule
	 *            the new value of the '
	 *            {@link PrizeMoneyFormula#getPrizeMoneyRule() prizeMoneyRule}'
	 *            feature.
	 * @generated
	 */
	public void setPrizeMoneyRule(PrizeMoneyRule newPrizeMoneyRule) {
		prizeMoneyRule = newPrizeMoneyRule;
	}

	/**
	 * Returns the value of '<em><b>rank</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>rank</b></em>' feature
	 * @generated
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the '{@link PrizeMoneyFormula#getRank() <em>rank</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newRank
	 *            the new value of the '{@link PrizeMoneyFormula#getRank() rank}
	 *            ' feature.
	 * @generated
	 */
	public void setRank(int newRank) {
		rank = newRank;
	}

	/**
	 * Returns the value of '<em><b>relativePrizeMoney</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>relativePrizeMoney</b></em>' feature
	 * @generated
	 */
	public int getRelativePrizeMoney() {
		return relativePrizeMoney;
	}

	/**
	 * Sets the '{@link PrizeMoneyFormula#getRelativePrizeMoney()
	 * <em>relativePrizeMoney</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newRelativePrizeMoney
	 *            the new value of the '
	 *            {@link PrizeMoneyFormula#getRelativePrizeMoney()
	 *            relativePrizeMoney}' feature.
	 * @generated
	 */
	public void setRelativePrizeMoney(int newRelativePrizeMoney) {
		relativePrizeMoney = newRelativePrizeMoney;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "PrizeMoneyFormula " + " [rank: " + getRank() + "]"
				+ " [relativePrizeMoney: " + getRelativePrizeMoney() + "]";
	}
}
