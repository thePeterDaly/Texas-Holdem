package core;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	private ArrayList<Card> cards;
	private int currentIndex = 0;
	
	//Constructor
	public Deck() {
		cards = new ArrayList<>();
		
		int count = 0;
		for (int y = 1; y <= 4; y++) {
			for (int z = 2; z <= 14; z++) {
				cards.add(new Card(z,y));
				count++;
			}
		}
	}
	
	//method to shuffle cards from deck
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
	//method to deal one card
	public Card dealCard() {
		if (currentIndex >= 52) {
			throw new IllegalStateException("Out of cards");
		}
		return cards.get(currentIndex++);
	}
	//method to deal 2 hole cards to each player
	public void dealHoleCards(ArrayList<Player> players) {
		for (int i = 0; i < 2; i++) {
			for (Player p : players) {
				p.addCard(dealCard());
			}
		}
	}
	//method to reveal Flop (community cards all players see)
	public void flop(ArrayList<Player> players) {
		Card flop1 = dealCard();
		Card flop2 = dealCard();
		Card flop3 = dealCard();
		for(Player p: players) {
			p.addCard(flop1);
			p.addCard(flop2);
			p.addCard(flop3);
		}
//		System.out.printf("Flop cards: %s, %s, and %s.\n", flop1, flop2, flop3);
	}
	//method to reveal Turn
	public void turn(ArrayList<Player> players) {
		Card turn = dealCard();
		for(Player p: players) {
			p.addCard(turn);
		}
//		System.out.println("Turn: " + turn);
	}
	//method to reveal River
	public void river(ArrayList<Player> players) {
		Card turn = dealCard();
		for(Player p: players) {
			p.addCard(turn);
		}
//		System.out.println("River: " + turn);
	}
	

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	
}
