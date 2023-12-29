package tBGame.business;

import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;
import tBGame.entities.abstracts.Character;


public class Turn {
	private String ownersName;
	private double attackModifier;
	private boolean isSkipped; // Check if the turn is skipped.
	private boolean isTemp;
	// If adds a template turn to the queue, that Turn execute only 1 time.
	
	public Turn(Opponent opponent) {
		this.isSkipped = false;
		this.isTemp = false;
		this.ownersName = opponent.getName();
		this.attackModifier = 1;
	}
	public <W extends Weapon> Turn(Character<W> character) {
		this.ownersName= character.getName();
		this.isSkipped = false;
		this.isTemp = false;
		this.attackModifier = 1;
	}
	public <W extends Weapon> Turn(Character<W> character, boolean tempTurn) {
		this(character);
		this.isTemp = tempTurn;
	}
	
	public Turn(Opponent enemy, boolean tempTurn) {
		this(enemy);
		this.isTemp = tempTurn;
	}
	
	public Turn(Turn turn) { // Copy Constructor
		this.ownersName = turn.getOwnersName();
		this.attackModifier = turn.getAttackModifier();
		this.isSkipped = turn.isSkipped();
		this.isTemp = turn.isTemp();
	}
	
	
	public double getAndModifyAttackModifier() {
		double attackModified = attackModifier;
		
		// Modify to 1.
		if(attackModified != 1)
			attackModifier = 1;
		
		return attackModified;
	}
	
	public double getAttackModifier() {
		return attackModifier;
	}
	
	public void setAttackModifier(double attackModifier) {
		this.attackModifier = attackModifier;
	}
	
	public boolean isTemp() {
		return isTemp;
	}
	
	public boolean isSkipped() {
		if(isSkipped) {
			isSkipped = false;
			return true;
		} else
			return false;
	}
	
	public void skipNextTurn() {
		this.isSkipped = true;
	}
	
	public String getOwnersName() {
		return ownersName;
	}
	
	@Override
	public String toString() {
		return "the turn of "+ownersName;
	}
}