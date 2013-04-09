package cz.fhsoft.poker.league.shared.model.v1;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A representation of the model object '<em><b>Settings</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_Settings")
public class Settings implements Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Id()
	private int id = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String adminPassword = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String defaultTimeZone = "GMT";

	/**
	 * Returns the value of '<em><b>id</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>id</b></em>' feature
	 * @generated
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the '{@link Settings#getId() <em>id</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Settings#getId() id}' feature.
	 * @generated
	 */
	public void setId(int newId) {
		id = newId;
	}

	/**
	 * Returns the value of '<em><b>adminPassword</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>adminPassword</b></em>' feature
	 * @generated
	 */
	public String getAdminPassword() {
		return adminPassword;
	}

	/**
	 * Sets the '{@link Settings#getAdminPassword() <em>adminPassword</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Settings#getAdminPassword()
	 *            adminPassword}' feature.
	 * @generated
	 */
	public void setAdminPassword(String newAdminPassword) {
		adminPassword = newAdminPassword;
	}

	/**
	 * Returns the value of '<em><b>defaultTimeZone</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>defaultTimeZone</b></em>' feature
	 * @generated
	 */
	public String getDefaultTimeZone() {
		return defaultTimeZone;
	}

	/**
	 * Sets the '{@link Settings#getDefaultTimeZone() <em>defaultTimeZone</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Settings#getDefaultTimeZone()
	 *            defaultTimeZone}' feature.
	 * @generated
	 */
	public void setDefaultTimeZone(String newDefaultTimeZone) {
		defaultTimeZone = newDefaultTimeZone;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Settings " + " [id: " + getId() + "]" + " [adminPassword: "
				+ getAdminPassword() + "]" + " [defaultTimeZone: "
				+ getDefaultTimeZone() + "]";
	}
}
