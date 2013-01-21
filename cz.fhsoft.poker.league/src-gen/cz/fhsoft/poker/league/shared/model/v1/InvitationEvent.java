package cz.fhsoft.poker.league.shared.model.v1;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A representation of the model object '<em><b>InvitationEvent</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_InvitationEvent")
public class InvitationEvent extends IdentifiableEntity {
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
	private Invitation invitation = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTime = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Enumerated(EnumType.STRING)
	private InvitationEventType eventType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private boolean sent = false;

	/**
	 * Returns the value of '<em><b>invitation</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>invitation</b></em>' feature
	 * @generated
	 */
	public Invitation getInvitation() {
		return invitation;
	}

	/**
	 * Sets the '{@link InvitationEvent#getInvitation() <em>invitation</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link InvitationEvent#getInvitation()
	 *            invitation}' feature.
	 * @generated
	 */
	public void setInvitation(Invitation newInvitation) {
		invitation = newInvitation;
	}

	/**
	 * Returns the value of '<em><b>eventTime</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>eventTime</b></em>' feature
	 * @generated
	 */
	public Date getEventTime() {
		return eventTime;
	}

	/**
	 * Sets the '{@link InvitationEvent#getEventTime() <em>eventTime</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link InvitationEvent#getEventTime()
	 *            eventTime}' feature.
	 * @generated
	 */
	public void setEventTime(Date newEventTime) {
		eventTime = newEventTime;
	}

	/**
	 * Returns the value of '<em><b>eventType</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>eventType</b></em>' feature
	 * @generated
	 */
	public InvitationEventType getEventType() {
		return eventType;
	}

	/**
	 * Sets the '{@link InvitationEvent#getEventType() <em>eventType</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link InvitationEvent#getEventType()
	 *            eventType}' feature.
	 * @generated
	 */
	public void setEventType(InvitationEventType newEventType) {
		eventType = newEventType;
	}

	/**
	 * Returns the value of '<em><b>sent</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>sent</b></em>' feature
	 * @generated
	 */
	public boolean isSent() {
		return sent;
	}

	/**
	 * Sets the '{@link InvitationEvent#isSent() <em>sent</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link InvitationEvent#isSent() sent}'
	 *            feature.
	 * @generated
	 */
	public void setSent(boolean newSent) {
		sent = newSent;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "InvitationEvent " + " [eventTime: " + getEventTime() + "]"
				+ " [eventType: " + getEventType() + "]" + " [sent: "
				+ isSent() + "]";
	}
}
