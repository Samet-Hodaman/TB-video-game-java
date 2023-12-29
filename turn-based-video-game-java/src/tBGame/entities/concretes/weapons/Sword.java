package tBGame.entities.concretes.weapons;

import tBGame.business.Turn;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Sword extends Weapon{
	private String[] attackTypes;
	
	public Sword() {
		attackTypes = new String[]{"Slash","Stab"};
	}
	
	public <W extends Weapon> int slash(Character<W> character,Opponent enemy, Turn turn) {
		character.modifyStamina(-2); // If stamina is insufficient, don't do anything.
		int attackDamage = character.getAttackValue() + getAdditionalAttack();
		attackDamage = (int)(attackDamage * turn.getAndModifyAttackModifier());
		return enemy.getDamage(attackDamage);
	}
	
	public <W extends Weapon> int stab(Character<W> character, Opponent enemy, Turn turn) {
		character.modifyStamina(-2); // If stamina is insufficient, don't do anything.
		switch((int)(Math.random() * 4)) { // [0, 1, 2, 3]
		// %75 success.
		case 0,1,2:
			int attackDamage = character.getAttackValue() + getAdditionalAttack();
			attackDamage = (int)(attackDamage * 2 * turn.getAndModifyAttackModifier());
			return enemy.getDamage(attackDamage);
		}
		System.out.println("Stabbing is failed.");
		return 0;
	}
	
	@Override
	public String[] getAttackTypes() {
		return attackTypes;
	}

	@Override
	public <W extends Weapon> int weaponAttack(Character<W> character, Opponent enemy, Turn turn, int attackType) {
		return switch(attackType) {
		case 1 -> slash(character, enemy, turn);
		case 2 -> stab(character, enemy, turn);
		default -> throw new IllegalStateException("Undefined attack type: "+attackType);
		};
	}
}
