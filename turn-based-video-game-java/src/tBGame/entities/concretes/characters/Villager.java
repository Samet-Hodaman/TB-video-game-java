package tBGame.entities.concretes.characters;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.entities.abstracts.Human;
import tBGame.entities.abstracts.Weapon;
import tBGame.entities.concretes.weapons.Bow;
import tBGame.entities.concretes.weapons.Spear;
import tBGame.entities.concretes.weapons.Sword;

public class Villager extends Human<Weapon>{

	public Villager(String name) {
		super(name);
		determineWeapon();
	}
	
	
	/** has no special action.*/
	@Override
	public void specialAction(TurnOrder turnOrder, Turn turn) {
		System.out.println("\nVillager "+getColoredName()+" has no special.\n");
		Turn tempTurn = new Turn(this, true);
		turnOrder.repeatTurn(tempTurn);
	}
	
	private void determineWeapon() {
		switch((int)(Math.random() * 3)) {
		case 0 -> equipWeapon(new Spear());
		case 1 -> equipWeapon(new Sword());
		case 2 -> equipWeapon(new Bow());
		default -> throw new IllegalStateException();
		}
	}

}
