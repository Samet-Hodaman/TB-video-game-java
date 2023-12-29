package tBGame.entities.concretes.characters;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.entities.abstracts.Human;
import tBGame.entities.abstracts.Weapon;
import tBGame.entities.concretes.weapons.Bow;
import tBGame.entities.concretes.weapons.Spear;
import tBGame.entities.concretes.weapons.Sword;
import tBGame.exceptions.SpecialAlreadyUsedException;

public class Squire extends Human<Weapon>{

	public Squire(String name) {
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

	/**can attack for 0.5 × attack in the current turn and increase his stamina to 10.*/
	@Override
	public void specialAction(TurnOrder turnOrder, Turn turn) throws SpecialAlreadyUsedException{
		if(isSpecialUsed)
			throw new SpecialAlreadyUsedException("\n"+getColoredName()+" already used the special action.\n");
		else {
			isSpecialUsed = true;
			Turn tempTurn = new Turn(this, true);
			tempTurn.setAttackModifier(0.5);
			turnOrder.repeatTurn(tempTurn);
			refillStamina();
			System.out.println("\n"+getColoredName()+"'s stamina is refilled.");
			System.out.println("Now you can attack with 0.5 attack value.\n");			
		}
	}

}
