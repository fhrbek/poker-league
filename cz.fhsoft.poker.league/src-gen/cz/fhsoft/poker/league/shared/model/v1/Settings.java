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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String smtpHost = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String smtpPort = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String smtpUser = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String smtpPassword = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String smtpFrom = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String urlBase = null;

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
	 * @param newId
	 *            the new value of the '{@link Settings#getId() id}' feature.
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
	 * @param newAdminPassword
	 *            the new value of the '{@link Settings#getAdminPassword()
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
	 * @param newDefaultTimeZone
	 *            the new value of the '{@link Settings#getDefaultTimeZone()
	 *            defaultTimeZone}' feature.
	 * @generated
	 */
	public void setDefaultTimeZone(String newDefaultTimeZone) {
		defaultTimeZone = newDefaultTimeZone;
	}

	/**
	 * Returns the value of '<em><b>smtpHost</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>smtpHost</b></em>' feature
	 * @generated
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * Sets the '{@link Settings#getSmtpHost() <em>smtpHost</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newSmtpHost
	 *            the new value of the '{@link Settings#getSmtpHost() smtpHost}'
	 *            feature.
	 * @generated
	 */
	public void setSmtpHost(String newSmtpHost) {
		smtpHost = newSmtpHost;
	}

	/**
	 * Returns the value of '<em><b>smtpPort</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>smtpPort</b></em>' feature
	 * @generated
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * Sets the '{@link Settings#getSmtpPort() <em>smtpPort</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newSmtpPort
	 *            the new value of the '{@link Settings#getSmtpPort() smtpPort}'
	 *            feature.
	 * @generated
	 */
	public void setSmtpPort(String newSmtpPort) {
		smtpPort = newSmtpPort;
	}

	/**
	 * Returns the value of '<em><b>smtpUser</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>smtpUser</b></em>' feature
	 * @generated
	 */
	public String getSmtpUser() {
		return smtpUser;
	}

	/**
	 * Sets the '{@link Settings#getSmtpUser() <em>smtpUser</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newSmtpUser
	 *            the new value of the '{@link Settings#getSmtpUser() smtpUser}'
	 *            feature.
	 * @generated
	 */
	public void setSmtpUser(String newSmtpUser) {
		smtpUser = newSmtpUser;
	}

	/**
	 * Returns the value of '<em><b>smtpPassword</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>smtpPassword</b></em>' feature
	 * @generated
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * Sets the '{@link Settings#getSmtpPassword() <em>smtpPassword</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newSmtpPassword
	 *            the new value of the '{@link Settings#getSmtpPassword()
	 *            smtpPassword}' feature.
	 * @generated
	 */
	public void setSmtpPassword(String newSmtpPassword) {
		smtpPassword = newSmtpPassword;
	}

	/**
	 * Returns the value of '<em><b>smtpFrom</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>smtpFrom</b></em>' feature
	 * @generated
	 */
	public String getSmtpFrom() {
		return smtpFrom;
	}

	/**
	 * Sets the '{@link Settings#getSmtpFrom() <em>smtpFrom</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newSmtpFrom
	 *            the new value of the '{@link Settings#getSmtpFrom() smtpFrom}'
	 *            feature.
	 * @generated
	 */
	public void setSmtpFrom(String newSmtpFrom) {
		smtpFrom = newSmtpFrom;
	}

	/**
	 * Returns the value of '<em><b>urlBase</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>urlBase</b></em>' feature
	 * @generated
	 */
	public String getUrlBase() {
		return urlBase;
	}

	/**
	 * Sets the '{@link Settings#getUrlBase() <em>urlBase</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newUrlBase
	 *            the new value of the '{@link Settings#getUrlBase() urlBase}'
	 *            feature.
	 * @generated
	 */
	public void setUrlBase(String newUrlBase) {
		urlBase = newUrlBase;
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
				+ getDefaultTimeZone() + "]" + " [smtpHost: " + getSmtpHost()
				+ "]" + " [smtpPort: " + getSmtpPort() + "]" + " [smtpUser: "
				+ getSmtpUser() + "]" + " [smtpPassword: " + getSmtpPassword()
				+ "]" + " [smtpFrom: " + getSmtpFrom() + "]" + " [urlBase: "
				+ getUrlBase() + "]";
	}
}
