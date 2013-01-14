package cz.fhsoft.poker.league.shared.model.v1;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A representation of the model object '<em><b>DataStructureVersion</b></em>'.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_DataStructureVersion")
public class DataStructureVersion implements Serializable {
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
	private String currentVersion = null;

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
	 * Sets the '{@link DataStructureVersion#getId() <em>id</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link DataStructureVersion#getId() id}'
	 *            feature.
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
	public String getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Sets the '{@link DataStructureVersion#getCurrentVersion()
	 * <em>currentVersion</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '
	 *            {@link DataStructureVersion#getCurrentVersion()
	 *            currentVersion}' feature.
	 * @generated
	 */
	public void setCurrentVersion(String newCurrentVersion) {
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
		return "DataStructureVersion " + " [id: " + getId() + "]"
				+ " [currentVersion: " + getCurrentVersion() + "]";
	}
}
