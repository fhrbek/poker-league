package cz.fhsoft.poker.league.shared.model.v1;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * A representation of the model object '<em><b>DescribedEntity</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
@Entity(name = "pl_DescribedEntity")
public abstract class DescribedEntity extends IdentifiableEntity {
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
	private String name = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Basic()
	private String description = null;

	/**
	 * Returns the value of '<em><b>name</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>name</b></em>' feature
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the '{@link DescribedEntity#getName() <em>name</em>}' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newName
	 *            the new value of the '{@link DescribedEntity#getName() name}'
	 *            feature.
	 * @generated
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Returns the value of '<em><b>description</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of '<em><b>description</b></em>' feature
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the '{@link DescribedEntity#getDescription() <em>description</em>}'
	 * feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param newDescription
	 *            the new value of the '{@link DescribedEntity#getDescription()
	 *            description}' feature.
	 * @generated
	 */
	public void setDescription(String newDescription) {
		description = newDescription;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		return "DescribedEntity " + " [name: " + getName() + "]"
				+ " [description: " + getDescription() + "]";
	}
}
