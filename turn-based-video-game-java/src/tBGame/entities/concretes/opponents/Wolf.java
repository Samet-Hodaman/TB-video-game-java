package tBGame.entities.concretes.opponents;

import java.util.ArrayList;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;

public class Wolf extends Opponent{

	public Wolf() {
		super();
	}
	public Wolf(Wolf wolf) { // Copy constructor
		super(wolf);
	}
	
	@Override
	public <W extends Weapon> void special(Character<W> character, Turn turn, TurnOrder turnOrder, ArrayList<Opponent> opponents) {
		Wolf friendWolf = callFriend();
		if (friendWolf != null) {
			turnOrder.addFirst(new Turn(friendWolf));
			opponents.add(friendWolf);
			System.out.println(getColoredName()+" called a friend successfully. Wolf sounds are getting louder.\n");
		} else
			System.out.println(getColoredName()+" called a friend wolf but the wolf's howls went unanswered.\n");
	}
	
	public Wolf callFriend() {
		return switch((int)(Math.random() * 5)) {
		// %20 success
		case 0 -> new Wolf(this);
		// %80 failure
		default -> null;
		};
	}

}
