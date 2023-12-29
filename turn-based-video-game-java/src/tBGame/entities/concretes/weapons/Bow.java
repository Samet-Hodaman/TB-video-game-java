package tBGame.entities.concretes.weapons;

import tBGame.business.Turn;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Bow extends Weapon{
	private String[] attackTypes;

	public Bow() {
		attackTypes = new String[]{"SingleArrow","TwoArrow"};
	}
	
	public <W extends Weapon> int singleArrow(Character<W> character, Opponent enemy, Turn turn) {
		character.modifyStamina(-1);
		int attackDamage = character.getAttackValue() + getAdditionalAttack();
		attackDamage = (int)(attackDamage * 0.8 * turn.getAndModifyAttackModifier());
		return enemy.getDamage(attackDamage);
	}
	
	public <W extends Weapon> int twoArrow(Character<W> character, Opponent enemy, Turn turn) {
		character.modifyStamina(-3);
		int attackDamage = character.getAttackValue() + getAdditionalAttack();
		attackDamage = (int)(attackDamage * 2.5 * turn.getAndModifyAttackModifier());
		return enemy.getDamage(attackDamage);
	}
	
	@Override
	public String[] getAttackTypes() {
		return attackTypes;
	}
	
	@Override
	public <W extends Weapon> int weaponAttack(Character<W> character, Opponent enemy, Turn turn, int attackType) {
		return switch(attackType) {
		case 1 -> singleArrow(character, enemy, turn);
		case 2 -> twoArrow(character, enemy, turn);
		default -> throw new IllegalStateException("Undefined attack type: "+attackType);
		};
	}

}
