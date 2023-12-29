package tBGame.entities.concretes.weapons;

import tBGame.business.Turn;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Spear extends Weapon{
	private String[] attackTypes;
	
	public Spear() {
		attackTypes = new String[]{"stap","thrown"};
	}
	
	public <W extends Weapon> int stap(Character<W> character, Opponent enemy, Turn turn) {
		character.modifyStamina(-2); // If stamina is insufficient, don't do anything.
		int attackDamage = (character.getAttackValue() + getAdditionalAttack());
		attackDamage = (int)(attackDamage * 1.1 * turn.getAndModifyAttackModifier());
		return enemy.getDamage(attackDamage);
	}
	
	public <W extends Weapon> int thrown(Character<W> character, Opponent enemy, Turn turn) {
		character.modifyStamina(-2); // If stamina is insufficient, don't do anything.
		int attackDamage = (character.getAttackValue() + getAdditionalAttack());
		attackDamage = (int)(attackDamage * 2 * turn.getAndModifyAttackModifier()); // Double it
		turn.skipNextTurn(); // Skipping next Turn
		return enemy.getDamage(attackDamage);
	}

	@Override
	public String[] getAttackTypes() {
		return attackTypes;
	}

	@Override
	public <W extends Weapon> int weaponAttack(Character<W> character, Opponent enemy, Turn turn, int attackType) {
		return switch(attackType) {
		case 1 -> stap(character, enemy, turn);
		case 2-> thrown(character, enemy, turn);
		default -> throw new IllegalStateException("Undefined attack type: "+attackType);
		};
	}	
}