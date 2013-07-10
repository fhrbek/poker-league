package cz.fhsoft.poker.league.shared.model.v1;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * A representation of the model object '<em><b>IdentifiableEntity</b></em>'.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_IdentifiableEntity")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class IdentifiableEntity implements Serializable {
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id = 0;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private boolean obsolete = false;

	@Transient
	private boolean proxy = false;

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
	 * Sets the '{@link IdentifiableEntity#getId() <em>id</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newId
	 *            the new value of the '{@link IdentifiableEntity#getId() id}'
	 *            feature.
	 * @generated
	 */
	public void setId(int newId) {
		id = newId;
	}

	/**
	 * Returns the value of '<em><b>obsolete</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>obsolete</b></em>' feature
	 * @generated
	 */
	public boolean isObsolete() {
		return obsolete;
	}

	/**
	 * Sets the '{@link IdentifiableEntity#isObsolete() <em>obsolete</em>}'
	 * feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param newObsolete
	 *            the new value of the '{@link IdentifiableEntity#isObsolete()
	 *            obsolete}' feature.
	 * @generated
	 */
	public void setObsolete(boolean newObsolete) {
		obsolete = newObsolete;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "IdentifiableEntity " + " [id: " + getId() + "]" + " [proxy: "
				+ isProxy() + "]" + " [obsolete: " + isObsolete() + "]";
	}

	public boolean isProxy() {
		return proxy;
	}

	public void setProxy(boolean isProxy) {
		proxy = isProxy;
	}
}
