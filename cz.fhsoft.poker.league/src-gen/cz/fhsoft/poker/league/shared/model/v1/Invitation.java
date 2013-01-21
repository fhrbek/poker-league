package cz.fhsoft.poker.league.shared.model.v1;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * A representation of the model object '<em><b>Invitation</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_Invitation")
public class Invitation extends IdentifiableEntity {
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
	private Tournament tournament = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Player player = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	@Enumerated(EnumType.STRING)
	private InvitationReply reply = null;

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
	@Basic()
	private String uuid = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "invitation", orphanRemoval = true)
	private Set<InvitationEvent> events = new HashSet<InvitationEvent>();

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
	 * Sets the '{@link Invitation#getTournament() <em>tournament</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Invitation#getTournament()
	 *            tournament}' feature.
	 * @generated
	 */
	public void setTournament(Tournament newTournament) {
		tournament = newTournament;
	}

	/**
	 * Returns the value of '<em><b>player</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>player</b></em>' feature
	 * @generated
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the '{@link Invitation#getPlayer() <em>player</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Invitation#getPlayer() player}'
	 *            feature.
	 * @generated
	 */
	public void setPlayer(Player newPlayer) {
		player = newPlayer;
	}

	/**
	 * Returns the value of '<em><b>reply</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>reply</b></em>' feature
	 * @generated
	 */
	public InvitationReply getReply() {
		return reply;
	}

	/**
	 * Sets the '{@link Invitation#getReply() <em>reply</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Invitation#getReply() reply}'
	 *            feature.
	 * @generated
	 */
	public void setReply(InvitationReply newReply) {
		reply = newReply;
	}

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
	 * Sets the '{@link Invitation#getOrdinal() <em>ordinal</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Invitation#getOrdinal() ordinal}'
	 *            feature.
	 * @generated
	 */
	public void setOrdinal(int newOrdinal) {
		ordinal = newOrdinal;
	}

	/**
	 * Returns the value of '<em><b>uuid</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>uuid</b></em>' feature
	 * @generated
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the '{@link Invitation#getUuid() <em>uuid</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Invitation#getUuid() uuid}' feature.
	 * @generated
	 */
	public void setUuid(String newUuid) {
		uuid = newUuid;
	}

	/**
	 * Returns the value of '<em><b>events</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>events</b></em>' feature
	 * @generated
	 */
	public Set<InvitationEvent> getEvents() {
		return events;
	}

	/**
	 * Adds to the <em>events</em> feature.
	 * 
	 * @generated
	 */
	public void addToEvents(InvitationEvent eventsValue) {
		if (!events.contains(eventsValue)) {
			events.add(eventsValue);
		}
	}

	/**
	 * Removes from the <em>events</em> feature.
	 * 
	 * @generated
	 */
	public void removeFromEvents(InvitationEvent eventsValue) {
		if (events.contains(eventsValue)) {
			events.remove(eventsValue);
		}
	}

	/**
	 * Clears the <em>events</em> feature.
	 * 
	 * @generated
	 */
	public void clearEvents() {
		while (!events.isEmpty()) {
			removeFromEvents(events.iterator().next());
		}
	}

	/**
	 * Sets the '{@link Invitation#getEvents() <em>events</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link Invitation#getEvents() events}'
	 *            feature.
	 * @generated
	 */
	public void setEvents(Set<InvitationEvent> newEvents) {
		events = newEvents;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "Invitation " + " [reply: " + getReply() + "]" + " [ordinal: "
				+ getOrdinal() + "]" + " [uuid: " + getUuid() + "]";
	}
}
