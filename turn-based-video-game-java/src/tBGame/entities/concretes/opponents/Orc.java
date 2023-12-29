package tBGame.entities.concretes.opponents;

import java.util.ArrayList;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.core.TBGame;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Orc extends Opponent{

	@Override
	public <W extends Weapon> void special(Character<W> character, Turn turn, TurnOrder turnOrder, ArrayList<Opponent> opponents) {
		int damage = heavyHit(character, turn);
		
		System.out.println(getColoredName()+" Hits Heavy to "+character.getColoredName()+". Deals "+TBGame.RED_BRIGHT+damage+" damage."+TBGame.RESET);
		System.out.println(character.toString());
		System.out.println(toString()+"\n");
	}
	public <W extends Weapon> int heavyHit(Character<W> character, Turn turn) {
		int attackValue = getAttackValue();
		attackValue = (int)(attackValue * 2 * turn.getAndModifyAttackModifier());
		turn.skipNextTurn();
		return character.getDamage(attackValue);
	}
}
