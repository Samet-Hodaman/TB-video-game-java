package tBGame.entities.abstracts;

import java.util.concurrent.atomic.AtomicBoolean;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.core.TBGame;
import tBGame.exceptions.InsufficentStaminaException;

public abstract class Human<W extends Weapon> implements Character<W>{
	private String name;
	private int points;
	private int stamina;
	private int initialStamina;
	private int attack;
	private int speed;
	private boolean isGuarded;
	protected boolean isSpecialUsed;
	private W weapon;

	public Human(String name){
		this.name = name;
		this.initialStamina = 10;
		this.points = (int)(Math.random() * 51) + 100;
		this.attack = (int)(Math.random() * 21) + 20;
		this.speed = (int)(Math.random() * 90) + 10;
		this.stamina = this.initialStamina;
		isSpecialUsed = false;
	}
	
	@Override
	public int punch(Opponent enemy, Turn turn) {
		return enemy.getDamage((int)(attack * 0.8 * turn.getAndModifyAttackModifier()));
	}
	
	
	@Override
	public int attackWithWeapon(Opponent enemy, Turn turn, int attackType) {
		return weapon.weaponAttack(this, enemy, turn, attackType);
	}

	@Override
	public void guard(Turn turn){
		turn.getAndModifyAttackModifier();
		System.out.println("\n"+getColoredName()+TBGame.RESET+" starts guarding.\n");
		modifyStamina(3);
		isGuarded = true;
	}
	
	@Override
	public void disableGuard() {
		isGuarded = false;
	}
	
	public boolean isGuarded() {
		return isGuarded;
	}
	
	@Override
	public boolean isAlive() {
		return points > 0;
	}
	
	public boolean isSpecialUsed() {
		return isSpecialUsed;
	}

	@Override
	public void run(AtomicBoolean isSomeoneRun) { // It takes a flag for terminating while loop.
		isSomeoneRun.set(true);
	}
	
	@Override
	public int getDamage(int damagedValue) {
		if (isGuarded())
			damagedValue -= (int)(damagedValue * 0.75);
		increasePoint(damagedValue);
		return damagedValue;
	}
	
	public void increasePoint(int increasePoint) {
		if (increasePoint > points)
			points = 0;
		else
			points -= increasePoint;
	}
	
	public abstract void specialAction(TurnOrder turnOrder, Turn turn);
	
	protected void equipWeapon(W weapon) {
		this.weapon = weapon;
	}
	
	public void modifyStamina(int staminaAdded) {
		int totalStamina = staminaAdded + stamina;
		
		if (totalStamina < 0)
			throw new InsufficentStaminaException();
		
		if(totalStamina > initialStamina) {
			System.out.println("Stamina is full!\n");
			this.stamina = initialStamina;
		}else
			stamina = totalStamina;
	}
	
	protected void refillStamina() {
		this.stamina = initialStamina;
	}

	public String getName() {
		return name;
	}
	public String getColoredName() {
		return "\033[0;32m"+name+"\033[0m"; // GREEN NAME RESET
	}

	public int getPoints() {
		return points;
	}
	
	public int getStamina() {
		return stamina;
	}

	public int getAttackValue() {
		return attack;
	}
	public void setAttackValue(int attackValue) {
		this.attack = attackValue;
	}

	public int getSpeed() {
		return speed;
	}

	public W getWeapon() {
		return weapon;
	}
	@Override
	public String toString() {
		return name
				+", Job: "+getClass().getSimpleName()
				+", Points: "+getPoints()
				+", Stamina: "+getStamina();
	}
}
