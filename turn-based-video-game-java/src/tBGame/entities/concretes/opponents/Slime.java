package tBGame.entities.concretes.opponents;

import java.util.ArrayList;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.core.TBGame;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Slime extends Opponent{
	@Override
	public <W extends Weapon> void special(Character<W> character, Turn turn, TurnOrder turnOrder, ArrayList<Opponent> opponents) {
		int damage = absorb(character, turn);
		System.out.println(getColoredName()+" uses Absorb on "+character.getColoredName()+". Deals "+TBGame.RED_BRIGHT+damage+" damage."+TBGame.RESET);
		System.out.println(character.toString());
		System.out.println(toString()+"\n");
	}
	
	public <W extends Weapon> int absorb(Character<W> character, Turn turn) {
		int attackDamage = attack(character, turn);
		int points = getPoints(); // Current point
		setPoints(points + attackDamage); // Add attack damage value to points value.
		return attackDamage;
	}
}
