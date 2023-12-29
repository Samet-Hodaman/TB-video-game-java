package tBGame.entities.concretes.characters;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.entities.abstracts.Human;
import tBGame.entities.abstracts.Weapon;
import tBGame.entities.concretes.weapons.Bow;
import tBGame.entities.concretes.weapons.Spear;
import tBGame.entities.concretes.weapons.Sword;
import tBGame.exceptions.SpecialAlreadyUsedException;

public class Hunter extends Human<Weapon>{
	
	public Hunter(String name) {
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
	
	/**Can attack for 0.5 × attack in the current turn and have two turns back to back for his next turn. 
	 * Note that this does not mean he gets a turn immediately after the current one.*/
	@Override
	public void specialAction(TurnOrder turnOrder, Turn turn) throws SpecialAlreadyUsedException{
		if(isSpecialUsed)
			throw new SpecialAlreadyUsedException("\n"+getColoredName()+" already used the special action.\n");
		else {
			isSpecialUsed = true;
			System.out.println("\n"+getColoredName()+" gets 2 move for the next tur. This turn you can attack with 0.5X\n");
			Turn tempTurn = new Turn(this, true);
			tempTurn.setAttackModifier(0.5);
			turnOrder.repeatTurn(tempTurn);
			
			Turn thisTempTurn = new Turn(this,true);
			turnOrder.addTemp(thisTempTurn);
		}
	}
}