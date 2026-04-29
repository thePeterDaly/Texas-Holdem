package core;

import java.util.Random;

public class ComputerPlayer extends Player{
	
	private Random rand = new Random();
	private HandEvaluator handEval = new HandEvaluator();
	
	public ComputerPlayer(String name) {
		this.name = name;
	}
	
	public int chooseActionPreFlop() {
		//code that returns a number (0,1, or 2, for fold, call, or raise) depending on hole cards with a hint of chance
		//the point of adding a level of chance to each decision is to make the computer less predictable, 
		//especially when the player doesn't know the probabilities
		
		if(cards.get(0).getRankNumber() == cards.get(1).getRankNumber()) {
			if (rand.nextInt(5) == 0)
				return 1;
			else 
				return 2;
		}
		else if (cards.get(0).getRankNumber() >= 10 || cards.get(1).getRankNumber() >= 10) {
			if (rand.nextInt(8) == 0)
				return 2;
			else 
				return 1;
		}
		else {
			if (rand.nextInt(6) == 0)
				return 0;
			else if (rand.nextInt(10) == 0)
				return 2;
			else
				return 1;
		}
	}
	
	public int chooseActionFlop() {
		//code that returns a number (0,1, or 2, for fold, call, or raise) depending on hand during flop with a hint of chance
		//since player has 5 cards, we can use HandEvaluator method to measure strength of hand
		hand = handEval.evaluate(cards);
		if (hand.getCategory() >= 5) {
			return 2;
		}
		else if (hand.getCategory() >= 3) {
			if (rand.nextInt(3) == 0)
				return 1;
			else return 2;
		}
		else {
			if (rand.nextInt(4) == 0)
				return 0;
			else return 1;
		}
	}
	
	public int chooseActionTurn() {
		//code that returns a number (0,1, or 2, for fold, call, or raise) depending on hand during turn with a hint of chance
		hand = handEval.evaluateTurn(cards);
		if (hand.getCategory() >= 5) {
			if (rand.nextInt(10) == 0)
				return 1;
			else return 2;
		}
		else if (hand.getCategory() >= 3) {
			if (rand.nextInt(3) == 0)
				return 1;
			else return 2;
		}
		else {
			if (rand.nextInt(7) == 0)
				return 0;
			else return 1;
		}
		
	}
	
	public int chooseActionRiver() {
		//code that returns a number (0,1, or 2, for fold, call, or raise) depending on hand during river with a hint of chance
		hand = getHand();
		if (hand.getCategory() >= 5) {
			return 2;
		}
		else if (hand.getCategory() >= 3) {
			if (rand.nextInt(3) == 0)
				return 1;
			else return 2;
		}
		else {
			if (rand.nextInt(7) == 0)
				return 0;
			else return 1;
		}
	}

}
