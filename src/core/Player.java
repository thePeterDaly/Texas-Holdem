package core;
import java.util.ArrayList;

public class Player {
	
	//data field
	protected String name;
	
	protected Hand hand = new Hand();
	protected ArrayList<Card> cards = new ArrayList<>();
	protected int roundsWon = 0;
	protected int chips;
	protected int chipsInPot = 0;
	protected boolean isFolded = false;
	protected String lastMove;
	
	//Constructors
	public Player() {
		
	}
	public Player(String name) {
		this.name = name;
	}
	
	//methods
	public void won() {
		this.roundsWon++;
//		System.out.println(name + " won this hand. Congratulations!");
	}
	public void addCard(Card card) {
		this.cards.add(card);
	}
	public void resetCards() {
		this.cards = null;
		this.hand = null;
	}
	//toString method to get info on player
	@Override
	public String toString() {
		return String.format("Player name: %s\n"
				+ "Hole cards: %s and %s\n"
				+ "Best hand (with community cards): %s\n"
				+ "Rounds won: %d\n", name, cards.get(0).toString(), cards.get(1).toString(), getHand().toString(), roundsWon);
	}
	
	public void fold() {
		this.isFolded = true;
		this.lastMove = "folded";
	}
	
	//getters and setters
	public Hand getHand() {
		HandEvaluator handEv = new HandEvaluator();
		this.hand = handEv.evaluatePlayerHand(cards);
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public int getRoundsWon() {
		return roundsWon;
	}

	public void setRoundsWon(int roundsWon) {
		this.roundsWon = roundsWon;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public int getChips() {
		return chips;
	}
	public void setChips(int chips) {
		this.chips = chips;
	}
	public int getChipsInPot() {
		return chipsInPot;
	}
	public void setChipsInPot(int chipsInPot) {
		this.chipsInPot = chipsInPot;
	}
	public boolean isFolded() {
		return isFolded;
	}
	public void setFolded(boolean isFolded) {
		this.isFolded = isFolded;

	}
	public String getLastMove() {
		return lastMove;
	}
	public void setLastMove(String lastMove) {
		this.lastMove = lastMove;
	}
	
	
	

}
