package tBGame.entities.abstracts;

import tBGame.business.Turn;

public abstract class Weapon {
	private int additionalAttack;
	
	public Weapon() {
		additionalAttack = (int)(Math.random() * 11) + 10;
	}
		
	public int getAdditionalAttack() {
		return additionalAttack;
	}
	
	public abstract String[] getAttackTypes();
	public abstract <W extends Weapon> int weaponAttack(Character<W> character, Opponent enemy, Turn turn, int attackType);
	
	public String toString() {
		return getClass().getSimpleName()+" with +"+additionalAttack+" attack";
	}
}
