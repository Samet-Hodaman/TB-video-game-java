package tBGame.entities.concretes.characters;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.entities.abstracts.Human;
import tBGame.entities.abstracts.Weapon;
import tBGame.entities.concretes.weapons.Bow;
import tBGame.entities.concretes.weapons.Spear;
import tBGame.entities.concretes.weapons.Sword;
import tBGame.exceptions.SpecialAlreadyUsedException;

public class Knight extends Human<Weapon>{
	
	public Knight(String name) {
		super(name);
		determineWeapon();
	}
	
	private void determineWeapon() {
		switch((int)(Math.random() * 3)) {
		case 0 -> equipWeapon(new Spear());
		case 1 -> equipWeapon(new Sword());
		case 2 -> equipWeapon(new Bow());
		default -> throw new IllegalStateException();
		}
	}

	
	/**can skip the current turn and deals 3 × attack on his next turn.*/
	@Override
	public void specialAction(TurnOrder turnOrder, Turn turn) throws SpecialAlreadyUsedException{
		if(isSpecialUsed)
			throw new SpecialAlreadyUsedException("\n"+getColoredName()+" already used the special action.\n");
		else {
			isSpecialUsed = true;
			turn.setAttackModifier(3);
			System.out.println("\n"+getName()+" increases the attack by 3 times for the next turn.\n");			
		}
	}
}
