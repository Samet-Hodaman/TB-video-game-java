package tBGame.entities.abstracts;

import java.util.ArrayList;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;

public abstract class Opponent {
	public static int OPPONENT_ID = 0;
	private int ID;
	private String name;
	private int points;
	private int attack;
	private int speed;
	private boolean isGuarded;
	
	public Opponent() {
		this.ID = getNextID();
		this.name = "Opponent "+ID;
		this.points = (int)(Math.random() * 101)+50;
		this.attack = (int)(Math.random() * 21)+5;
		this.speed = (int)(Math.random() * 91)+1;
		this.isGuarded = false;
	}
	
	public Opponent(Opponent enemy) { // Copy Constructor.
		this.ID = getNextID();
		this.name = "Opponent "+ID;
		this.points = enemy.getPoints();
		this.attack = enemy.getAttackValue();
		this.speed = enemy.getSpeed();
		this.isGuarded = enemy.isGuarded();
	}	
	
	public <W extends Weapon> int attack(Character<W> character, Turn turn) {
		int attackDamage = (int)(attack * turn.getAndModifyAttackModifier());
		return character.getDamage(attackDamage);
	}
	
	public boolean isGuarded() {
		return isGuarded;
	}
	
	public void guard(Turn turn) {
		turn.getAndModifyAttackModifier();
		isGuarded = true;
		System.out.println(getColoredName() + " starts guarding.\n");
	}
	public void disableGuard() {
		isGuarded = false;
	}
	
	public abstract <W extends Weapon> void special(Character<W> character, Turn turn, TurnOrder turnOrder, ArrayList<Opponent> opponents);
	
	private static int getNextID() {
		OPPONENT_ID += 1;
		return OPPONENT_ID;
	}
	
	public static void resetIDs() {
		OPPONENT_ID = 0;
	}
	
	public int getDamage(int damageValue) {
		if (isGuarded)
			damageValue -= (int)(damageValue * 0.5);
		points -= damageValue;
		if(points < 0)
			points = 0;
		return damageValue;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = (points > 150 ? 150 : points);
	}
	
	public boolean isAlive() {
		return points > 0;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getAttackValue() {
		return attack;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColoredName() {
		return "\033[0;31m"+name+"\033[0m"; // RED NAME RESET
	}
	
	@Override
	public String toString() {
		return "Id: "+ID+
				", Type: "+getClass().getSimpleName()+
				", Points: "+points+
				", Attack: "+attack+
				", Speed: "+speed;
	}
}
