package tBGame.core;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import tBGame.business.Turn;
import tBGame.business.TurnOrder;
import tBGame.entities.abstracts.Character;
import tBGame.entities.abstracts.Opponent;
import tBGame.entities.abstracts.Weapon;
import tBGame.entities.concretes.characters.Hunter;
import tBGame.entities.concretes.characters.Knight;
import tBGame.entities.concretes.characters.Squire;
import tBGame.entities.concretes.characters.Villager;
import tBGame.entities.concretes.opponents.Goblin;
import tBGame.entities.concretes.opponents.Orc;
import tBGame.entities.concretes.opponents.Slime;
import tBGame.entities.concretes.opponents.Wolf;
import tBGame.exceptions.InsufficientNameLengthException;
import tBGame.exceptions.NotAUniqueNameException;
import tBGame.exceptions.SpecialAlreadyUsedException;

/* 
 * All game is running here.
 * Some colors are coded. For better performance, you should use dark theme.
 * The game is 3 categories. initializing battling and ending game.
 * 
 * 								  			  play()
 * 					|	  						|						    	|
 * 			  initialize()    				startBattle()				 	endGame()
 * 	   (characters,Opponents,Turns)		   (handling turns)			(Results and ask for play again?)
 * 
 * Some helper class (Initializer, InputHandler, Menu) is involved. */


public class TBGame {
	public static final String BOLD_RED = "\033[4;31m";
	public static final String BOLD_GREEN = "\033[1;32m";
	public static final String RESET = "\033[0m";
	public static final String YELLOW = "\033[0;33m";
	public static final String RED_BRIGHT = "\033[0;91m";
	
	private ArrayList<Character<Weapon>> characters;
	private ArrayList<Opponent> opponents;
	private Menu menu;
	private TurnOrder turnOrder;
	private Initializer initializer;
	private InputHandler inputHandler;
	private AtomicBoolean isSomeoneRun;
	private int waitingTime; //ms
	
	// Constructor.
	public TBGame() {
		menu = new Menu();
		initializer = new Initializer();
		inputHandler = new InputHandler();
		waitingTime = 1000;
	}
	
	// Starts the game here. (Initialize -> Start -> End) and repeating for the new game.
	public void play() {
		initializeGame();
		startBattle();
		endGame();
		
	}
	
	// Initializations for a new game. (Opponents, Characters and Turns initialization)
	public void initializeGame() {
		System.out.println("\nWelcome to TBGame!\n");
		Opponent.resetIDs();
		opponents = initializer.initializeOpponents((int)(Math.random() * 4)+1);
		System.out.println("These opponents appeared in front of you:");
		menu.showOpponents();
		characters = initializer.initializeCharacters();
		menu.introduceCharacters();
		turnOrder = initializer.initializeTurnOrder();
		System.out.println();
	}
	
	// Starting the game.
	public void startBattle() {
		System.out.println("The battle starts!\n");
		waiting(waitingTime);
		turnOrder.showTurns();
		isSomeoneRun = new AtomicBoolean(false);
		
		while(!gameOver()) {
			menu.determineTurn(turnOrder.getNextTurn());
			waiting(waitingTime);
		}
	}
	
	// Showing results and determining who wins. 
	public void endGame() {
		System.out.println("\nGame ended");
		if(!characters.isEmpty()) {
			System.out.println("\nSurviving Characters:");
			menu.showCharacters();
		}
		if(!opponents.isEmpty()) {
			System.out.println("\nSurviving Opponents:");
			menu.showOpponents();			
		}
		System.out.print("Play again? [1] yes [2] no : ");
		switch(inputHandler.getNumber(2)) {
		case 1 -> play();
		case 2 -> System.out.println("\nThanks for playing...");
		}
	}
	
	// When a situation that ends the game occurs, then this method detects it and ends the game.
	public boolean gameOver() {
		if (opponents.size() == 0) {
			System.out.println("You Win. Congratulations...");
			return true;
		}
		if(characters.size() == 0) {
			System.out.println("Opponents win. You lose...");
			return true;
		}
		if (isSomeoneRun.get()) {
			System.out.println("Your character(s) started running away. The battle ends!");
			return true;
		}
		if (!turnOrder.hasNext()) {
			System.out.println("All turns are finished. Game ended."); // Probably an error occurs.
			return true;
		}
		return false;
	}
	
	/** Waiting between the operations. This is coded to feel some reality.*/
	public void waiting(int waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/** Menu class is coded for asking some inputs and determining the next operation.*/
	private class Menu{
		
		// determining whose turn it is. (Opponent or character)
		public void determineTurn(Turn turn) {
			if(turn.isSkipped()) { // If the turn is skipped do not handle it.
				System.out.println(turn.getOwnersName()+"'s turn is skipped.\n");
				turnOrder.add(turn);
				return;
			}
			String name = turn.getOwnersName();
			// If opponent's turn, handle it.
			for(Opponent opponent:opponents)
				if(opponent.getName().equalsIgnoreCase(name)) {
					opponentMove(opponent,turn);
					turnOrder.add(turn);
					return;
				}
			// If character's turn, handle it.
			for(Character<Weapon> character: characters)
				if(character.getName().equalsIgnoreCase(name)) {
					characterMove(character,turn);
					turnOrder.add(turn);
					turnOrder.showTurns();
					return;
				}
			// if the owners of the turn is not found, that means it doesn't live anymore. So not add the turn to the turnOrder again.
		}
		
		// Handling character's move. Firstly an option from the user is taken.
		public void characterMove(Character<Weapon> character, Turn turn) {
			character.disableGuard();
			System.out.println("Move "+turnOrder.getNextMove()+" - "+"It is the turn of "+character.getColoredName());
			System.out.print("[1] Punch\n[2] Attack with weapon\n[3] Guard\n[4] Special Action\n[5] Run\nPlease select an option: ");
			int option = inputHandler.getNumber(5);
			switch(option) {
			case 1 -> handlePunch(character, turn);
			case 2 -> handleAttackWithWeapon(character, turn);
			case 3 -> character.guard(turn);
			case 4 -> handleSpecial(character, turn);
			case 5 -> character.run(isSomeoneRun);
			}
		}
		
		// Handling character punch action.
		public void handlePunch(Character<Weapon> character, Turn turn) {
			Opponent opponent = inputHandler.chooseOpponent(); // Selects the opponent.
			int damage = character.punch(opponent, turn); // punch it and gets the damage value to print.
			System.out.println("Move "+turnOrder.getCurrentMove()+" Result: "+character.getColoredName()+" punched "+opponent.getColoredName()+" with "+YELLOW+damage+" damage."+RESET);
			System.out.println(character.toString());
			System.out.println(opponent.toString()+"\n");
			removeIfDead(opponent);
		}
		
		// Character attack with weapon action.
		public void handleAttackWithWeapon(Character<Weapon> character, Turn turn) {
			int attackType = inputHandler.chooseAttackType(character.getWeapon());/*** inputHandler.getPreferences(character.getWeapon().getAttackTypes());**/
			Opponent opponent = inputHandler.chooseOpponent();
			int damage = character.attackWithWeapon(opponent, turn, attackType);
			System.out.println("Move "+turnOrder.getCurrentMove()+" Result: "+character.getColoredName()+" attacks "+opponent.getColoredName()+" with "+character.getWeapon().getAttackTypes()[attackType-1]+". Deals "+YELLOW+damage+" damage."+RESET);
			System.out.println(character.toString());
			System.out.println(opponent.toString()+"\n");
			waiting(waitingTime);
			removeIfDead(opponent);
		}
		
		// Character special.
		public void handleSpecial(Character<Weapon> character, Turn turn) {
			try {
				character.specialAction(turnOrder, turn);
			} catch (SpecialAlreadyUsedException e) {
				System.out.println(e.getMessage());
				Turn tempTurn = new Turn(character, true);
				turnOrder.repeatTurn(tempTurn);
			}
		}
		
		// Handling opponents move. Firstly randomly selected character is determined.
		public void opponentMove(Opponent opponent,Turn turn) {
			opponent.disableGuard(); // Disable guarding at the beginning of the turn.
			Character<Weapon> character = characters.get((int)(Math.random() * characters.size()));
			System.out.print("Move "+turnOrder.getNextMove()+" - ");
			switch((int)(Math.random()*3)) {
			case 0 -> handleOpponentAttack(opponent,character, turn);
			case 1 -> opponent.guard(turn);
			case 2 -> handleOpponentSpecial(opponent,character,turn);
			default -> System.out.println("Default choosen.");
			}
		}
		
		// Opponent attack.
		public void handleOpponentAttack(Opponent opponent, Character<Weapon> character, Turn turn) {
			int damage = opponent.attack(character, turn);
			System.out.println(opponent.getColoredName()+" attacks "+character.getColoredName()+". Deals "+RED_BRIGHT+damage+" damage."+RESET);
			System.out.println(character.toString());
			System.out.println(opponent.toString()+"\n");
			removeIfDead(character);
			
		}
		
		// Opponent special.
		public void handleOpponentSpecial(Opponent opponent,Character<Weapon> character, Turn turn) {
			opponent.special(character, turn, turnOrder, opponents);
			removeIfDead(character);
		}
		
		// Showing all opponents on the console.
		public void showOpponents() {
			for(Opponent opponent:opponents)
				System.out.println(opponent);
			System.out.println();
		}
		
		// Showing all characters on the console.
		public void showCharacters() {
			for(int i=0; i<characters.size(); i++) {
				System.out.println(characters.get(i));
			}
		}
		
		// Initially introducing all characters on the console.
		public void introduceCharacters() {
			for(int i=0; i<characters.size(); i++) {
				Character<Weapon> character = characters.get(i);
				System.out.println("The stats of your "+(i+1)+". character:");
				System.out.println(character.toString()+", Attack: "+character.getAttackValue()+", Speed: "+character.getSpeed()+", Weapon: "+character.getWeapon().toString()+"\n");
			}
		}
		
		// If an opponent is dead after a turn. It is removed from the opponents ArrayList.
		public void removeIfDead(Opponent opponent) {
			if(!opponent.isAlive()) {
				if(opponents.contains(opponent)) {
					opponents.remove(opponent);
					System.out.println(BOLD_GREEN+opponent.getName()+" is defeated.\n"+RESET);
				}
			}
		}
		
		// If a character is dead after the opponent's turn, It is removed from the characters ArrayList
		public <W extends Weapon> void removeIfDead(Character<W> character) {
			if(!character.isAlive())
				if(characters.contains(character)) {
					characters.remove(character);
					System.out.println(BOLD_RED+character.getName()+" is defeated.\n"+RESET);
				}
		}
	}
	
	
	/** All communication between program and user is handled by this class.*/
	private class InputHandler{
		private Scanner scanner;
		
		public InputHandler() {
			scanner = new Scanner(System.in);
		}

		// Ask the user for a number which is bounded with the argument.
		public int getNumber(int upperBound) {
			while(true) {
				try {
					String userInput = scanner.nextLine();
					int number = Integer.parseInt(userInput);
					if(number <= upperBound && number > 0)
						return number;
					else 
						throw new IllegalArgumentException();
				} 
				catch (Exception e) {
					System.out.print("The number must be between [1,"+upperBound+"] : ");
				}
			}
		}
		
		// Ask the user to choose an opponent.
		public Opponent chooseOpponent() {
			for(int i=0; i<opponents.size(); i++) {
				System.out.println("["+(i+1)+"] "+opponents.get(i));
			}
			System.out.print("Please select the opponent: ");
			int preference = getNumber(opponents.size());
			System.out.println();
			return opponents.get(preference - 1);
		}
		
		// Ask the user to choose an attack type which are determined with the argument.
		public int chooseAttackType(Weapon weapon) {
			System.out.print("Please select weapon attack type (");
			String[] attackTypes = weapon.getAttackTypes();
			for(int i=0; i<attackTypes.length; i++) {
				System.out.print(" ["+(i+1)+"] "+attackTypes[i]);
			}
			System.out.print("): ");
			return getNumber(weapon.getAttackTypes().length);
		}
		
		// Ask the user to determine the character names to initialize them.
		public ArrayList<String> getCharacterNames(int amountOfNames) {
			int numberOfName = getNumber(amountOfNames);
			ArrayList<String> inputNames = new ArrayList<>();
			int number = 1;
			for (int i=0; i<numberOfName; i++) {
				while(true) {
					try {
						System.out.print("Enter name for your "+number+". character: ");
						String name = scanner.nextLine();
						if (inputNames.contains(name))
							throw new NotAUniqueNameException();
						else if(name.length() < 3)
							throw new InsufficientNameLengthException("Name must be at least 4 characters long :");
						else {
							inputNames.add(name);
							number++;
							break;
						}
					} catch (NotAUniqueNameException e) {
						System.out.println("Please enter a unique name: ");
					} catch (InsufficientNameLengthException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			return inputNames;
		}
	}
	
	
	/** All initializations is handled in this inner class.*/
	private class Initializer{
		private Random randomizer;
		private final int MAXIMUM_CHARACTER = 3;
		
		public Initializer() {
			randomizer = new Random();
		}
		// Creating the initial opponents according to a given number. 
		public ArrayList<Opponent> initializeOpponents(int numberOfOpponents){
			ArrayList<Opponent> initialOpponents = new ArrayList<>();
			for(int i=0;i<numberOfOpponents; i++) {
				switch(randomizer.nextInt(numberOfOpponents)) {
					case 0 -> initialOpponents.add(new Goblin());
					case 1 -> initialOpponents.add(new Orc());
					case 2 -> initialOpponents.add(new Slime());
					case 3 -> initialOpponents.add(new Wolf());
					default -> throw new IllegalArgumentException();
				}
			}
			return initialOpponents;
		}
		
		// Initialization of characters which are named by the user.
		public ArrayList<Character<Weapon>> initializeCharacters(){
			System.out.print("Please enter the number of characters to create (max "+MAXIMUM_CHARACTER+" characters): ");
			ArrayList<String> characterNames = inputHandler.getCharacterNames(MAXIMUM_CHARACTER);
			System.out.println();
			ArrayList<Character<Weapon>> initialCharacters = new ArrayList<>();
			for(String name:characterNames) {
				switch(randomizer.nextInt(4)) {
				case 0 -> initialCharacters.add(new Knight(name));
				case 1 -> initialCharacters.add(new Hunter(name));
				case 2 -> initialCharacters.add(new Squire(name));
				case 3 -> initialCharacters.add(new Villager(name));
				default -> throw new IllegalArgumentException();
				}
			}
			return initialCharacters;
		}
		
		// sorting and initializing the turns.
		public TurnOrder initializeTurnOrder() {
			ArrayList<Turn> tempTurns = new ArrayList<>();
			for(Character<Weapon> character:characters)
				tempTurns.add(new Turn(character));
			
			for(Opponent opponent:opponents)
				tempTurns.add(new Turn(opponent));
			
			tempTurns.sort((turn1, turn2) -> Integer.compare(getSpeedOfTurn(turn2), getSpeedOfTurn(turn1)));
			
			TurnOrder turnOrder = new TurnOrder();
			for(int i=0; i<tempTurns.size(); i++) {
				turnOrder.add(tempTurns.get(i));
			}
			return turnOrder;
		}
		
		// It is used for sorting the turns.
		private int getSpeedOfTurn(Turn turn) {
			for(int i=0; i< opponents.size(); i++) {
				Opponent opponent = opponents.get(i);
				if(opponent.getName().equals(turn.getOwnersName())) {
					return opponent.getSpeed();
				}
			}
			
			for(int i=0; i<characters.size(); i++) {
				if(characters.get(i).getName().equalsIgnoreCase(turn.getOwnersName()))
					return characters.get(i).getSpeed();
			}
			
			throw new NoSuchElementException("No such turn owner.");
		}
	}
}