package cz.fhsoft.poker.league.shared.model.v1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A representation of the literals of the enumeration '
 * <em><b>InvitationReply</b></em>'. <!-- begin-user-doc --> <!-- end-user-doc
 * -->
 * 
 * @generated
 */
public enum InvitationReply {

	/**
	 * The enum: NO_REPLY <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	NO_REPLY(0, "NO_REPLY", "NO_REPLY") {

		/**
		 * @return always true for this instance
		 * @generated
		 */
		@Override
		public boolean isNO_REPLY() {
			return true;
		}
	},
	/**
	 * The enum: ACCEPTED <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	ACCEPTED(1, "ACCEPTED", "ACCEPTED") {

		/**
		 * @return always true for this instance
		 * @generated
		 */
		@Override
		public boolean isACCEPTED() {
			return true;
		}
	},
	/**
	 * The enum: REJECTED <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	REJECTED(2, "REJECTED", "REJECTED") {

		/**
		 * @return always true for this instance
		 * @generated
		 */
		@Override
		public boolean isREJECTED() {
			return true;
		}
	};

	/**
	 * An array of all the '<em><b>InvitationReply</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final InvitationReply[] VALUES_ARRAY = new InvitationReply[] {
			NO_REPLY, ACCEPTED, REJECTED };

	/**
	 * A public read-only list of all the '<em><b>InvitationReply</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<InvitationReply> VALUES = Collections
			.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>InvitationReply</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            the literal to use to get the enum instance
	 * @return the InvitationReply, the literal enum class
	 * @generated
	 */
	public static InvitationReply get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InvitationReply result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>InvitationReply</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param name
	 *            the name to use to get the enum instance
	 * @return the InvitationReply, the literal enum class
	 * @generated
	 */
	public static InvitationReply getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InvitationReply result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>InvitationReply</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the value to use to get the enum instance
	 * @return the InvitationReply, the literal enum
	 * @generated
	 */
	public static InvitationReply get(int value) {
		for (InvitationReply enumInstance : VALUES_ARRAY) {
			if (enumInstance.getValue() == value) {
				return enumInstance;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private InvitationReply(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return false, is overridden by actual enum types.
	 * @generated
	 */
	public boolean isNO_REPLY() {
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return false, is overridden by actual enum types.
	 * @generated
	 */
	public boolean isACCEPTED() {
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return false, is overridden by actual enum types.
	 * @generated
	 */
	public boolean isREJECTED() {
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the name
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the literal of this enum instance
	 * @generated
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the literal value of the enumerator, which is its string
	 *         representation.
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
}
