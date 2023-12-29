package tBGame.entities.concretes.opponents;

import java.util.ArrayList;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.core.TBGame;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Goblin extends Opponent{

	
	@Override
	public <W extends Weapon> void special(Character<W> character, Turn turn, TurnOrder turnOrder, ArrayList<Opponent> opponents) {
		turn.setAttackModifier(0.7);
		int damage = rushingAttack(character, turn);
		Turn tempTurn = new Turn(this, true);
		tempTurn.setAttackModifier(0.7);
		turnOrder.addFirst(tempTurn);
		System.out.println(getColoredName()+" uses Rushing Attack on "+character.getName()+". Deals "+TBGame.RED_BRIGHT+damage+" damage."+TBGame.RESET);
		System.out.println(character);
		System.out.println(toString()+"\n");
	}
	
	public <W extends Weapon> int rushingAttack(Character<W> character, Turn turn) {
		int attackValue = (int)(getAttackValue() * turn.getAndModifyAttackModifier());
		return character.getDamage(attackValue);
		
		
	}
	
}
