package tBGame.entities.abstracts;

import java.util.concurrent.atomic.AtomicBoolean;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.exceptions.SpecialAlreadyUsedException;

public interface Character<W extends Weapon> {
	int punch(Opponent enemy, Turn turn);
	int attackWithWeapon(Opponent enemy, Turn turn, int attackType);
	void guard(Turn turn);
	void disableGuard();
	void specialAction(TurnOrder turnOrder, Turn turn) throws SpecialAlreadyUsedException;
	void run(AtomicBoolean isSomeoneRun);
	int getDamage(int damagedValue);
	int getAttackValue();
	int getSpeed();
	W getWeapon();
	String getName();
	String getColoredName();
	void modifyStamina(int staminaAdded);
	boolean isAlive();
}
