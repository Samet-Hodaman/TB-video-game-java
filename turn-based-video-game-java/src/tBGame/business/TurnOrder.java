package tBGame.business;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import tBGame.core.TBGame;


public class TurnOrder {
	private final String YELLOW_BRIGHT = "\033[1;93m";
	private Deque<Turn> queue;
	private int moveNumber;
	
	public TurnOrder() {
		queue = new LinkedList<>();
		moveNumber = 0;
	}
	
	public void add(Turn turn) {
		if(!turn.isTemp())
			queue.add(turn);
	}
	
	// Adding turn without checking if it is temp.
	public void addTemp(Turn turn) {
		queue.add(turn);
	}
	
	public void addFirst(Turn turn) {
		queue.addFirst(turn);
	}
	public void repeatTurn(Turn turn) {
		queue.addFirst(turn);
		moveNumber-=1;
	}
	
	public Turn getNextTurn() {
		return queue.remove();
	}
	
	public int getNextMove() {
		moveNumber += 1;
		return moveNumber;
	}
	
	public int getCurrentMove() {
		return moveNumber;
	}

	public boolean hasNext() {
		return (queue.peek() != null);
	}
	
	public void showTurns() {
		Iterator<Turn> iterator = queue.iterator();
		System.out.print(YELLOW_BRIGHT+"*** Turn Order: ");
		
		while(iterator.hasNext())
			System.out.print(iterator.next().getOwnersName()+", ");
		System.out.println("***\n"+TBGame.RESET);
	}
	
}
