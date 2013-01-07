package cz.fhsoft.poker.league.shared.model.v1;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A representation of the model object '<em><b>PlayerInGame</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "pl_PlayerInGame")
public class PlayerInGame extends IdentifiableEntity {
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
	private Player player = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Game game = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private int rank = 0;

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
	 * Sets the '{@link PlayerInGame#getPlayer() <em>player</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link PlayerInGame#getPlayer() player}'
	 *            feature.
	 * @generated
	 */
	public void setPlayer(Player newPlayer) {
		player = newPlayer;
	}

	/**
	 * Returns the value of '<em><b>game</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>game</b></em>' feature
	 * @generated
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Sets the '{@link PlayerInGame#getGame() <em>game</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link PlayerInGame#getGame() game}'
	 *            feature.
	 * @generated
	 */
	public void setGame(Game newGame) {
		game = newGame;
	}

	/**
	 * Returns the value of '<em><b>rank</b></em>' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>rank</b></em>' feature
	 * @generated
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the '{@link PlayerInGame#getRank() <em>rank</em>}' feature.
	 * 
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param the
	 *            new value of the '{@link PlayerInGame#getRank() rank}'
	 *            feature.
	 * @generated
	 */
	public void setRank(int newRank) {
		rank = newRank;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "PlayerInGame " + " [rank: " + getRank() + "]";
	}
}
