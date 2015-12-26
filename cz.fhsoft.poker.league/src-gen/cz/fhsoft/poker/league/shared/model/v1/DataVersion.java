package cz.fhsoft.poker.league.shared.model.v1;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A representation of the model object '<em><b>DataVersion</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
@Entity(name = "pl_DataVersion")
public class DataVersion implements Serializable {
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date currentVersion = null;

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
	 * Sets the '{@link DataVersion#getId() <em>id</em>}' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newId
	 *            the new value of the '{@link DataVersion#getId() id}' feature.
	 * @generated
	 */
	public void setId(int newId) {
		id = newId;
	}

	/**
	 * Returns the value of '<em><b>currentVersion</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>currentVersion</b></em>' feature
	 * @generated
	 */
	public Date getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Sets the '{@link DataVersion#getCurrentVersion() <em>currentVersion</em>}
	 * ' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newCurrentVersion
	 *            the new value of the '{@link DataVersion#getCurrentVersion()
	 *            currentVersion}' feature.
	 * @generated
	 */
	public void setCurrentVersion(Date newCurrentVersion) {
		currentVersion = newCurrentVersion;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		return "DataVersion " + " [id: " + getId() + "]" + " [currentVersion: "
				+ getCurrentVersion() + "]";
	}
}
