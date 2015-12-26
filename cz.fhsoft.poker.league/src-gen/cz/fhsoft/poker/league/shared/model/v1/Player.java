package cz.fhsoft.poker.league.shared.model.v1;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * A representation of the model object '<em><b>Player</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
@Entity(name = "pl_Player")
public class Player extends IdentifiableEntity {
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
	private boolean active = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Basic()
	private String nick = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Basic()
	private String firstName = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Basic()
	private String lastName = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Basic()
	private String emailAddress = null;

	/**
	 * Returns the value of '<em><b>active</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>active</b></em>' feature
	 * @generated
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the '{@link Player#isActive() <em>active</em>}' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newActive
	 *            the new value of the '{@link Player#isActive() active}'
	 *            feature.
	 * @generated
	 */
	public void setActive(boolean newActive) {
		active = newActive;
	}

	/**
	 * Returns the value of '<em><b>nick</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>nick</b></em>' feature
	 * @generated
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the '{@link Player#getNick() <em>nick</em>}' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newNick
	 *            the new value of the '{@link Player#getNick() nick}' feature.
	 * @generated
	 */
	public void setNick(String newNick) {
		nick = newNick;
	}

	/**
	 * Returns the value of '<em><b>firstName</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>firstName</b></em>' feature
	 * @generated
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the '{@link Player#getFirstName() <em>firstName</em>}' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newFirstName
	 *            the new value of the '{@link Player#getFirstName() firstName}'
	 *            feature.
	 * @generated
	 */
	public void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}

	/**
	 * Returns the value of '<em><b>lastName</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>lastName</b></em>' feature
	 * @generated
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the '{@link Player#getLastName() <em>lastName</em>}' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newLastName
	 *            the new value of the '{@link Player#getLastName() lastName}'
	 *            feature.
	 * @generated
	 */
	public void setLastName(String newLastName) {
		lastName = newLastName;
	}

	/**
	 * Returns the value of '<em><b>emailAddress</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>emailAddress</b></em>' feature
	 * @generated
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the '{@link Player#getEmailAddress() <em>emailAddress</em>}'
	 * feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newEmailAddress
	 *            the new value of the '{@link Player#getEmailAddress()
	 *            emailAddress}' feature.
	 * @generated
	 */
	public void setEmailAddress(String newEmailAddress) {
		emailAddress = newEmailAddress;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		return "Player " + " [active: " + isActive() + "]" + " [nick: "
				+ getNick() + "]" + " [firstName: " + getFirstName() + "]"
				+ " [lastName: " + getLastName() + "]" + " [emailAddress: "
				+ getEmailAddress() + "]";
	}
}
