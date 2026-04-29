package core;
import java.util.ArrayList;

public class Game {
	private ArrayList<Player> players;
	private ArrayList<Player> winners; // in very rare case of a tie (more than one player having royal flush)
	private Deck deck;
	private Player winner;
	
	public Game() {
		this.players = new ArrayList<>();
		players.add(new Player("You"));
		players.add(new Player("John"));
		players.add(new Player("Paul"));
		players.add(new Player("Keefe"));
		
		this.deck = new Deck();
	}
	public Game(ArrayList<Player> players) {
		this.players = players;
		this.deck = new Deck();
	}
	
	public Game(ArrayList<Player> players, Deck deck) {
		this.players = players;
		this.deck = deck;
	}
	
	//method for flow of a game
	public void noBetRound() {
		
		deck.shuffle();
		deck.setCurrentIndex(0);
		
		//Deal 2 hole-cards to each player
		deck.dealHoleCards(players);
		
		//reveal Flop (3 community cards)
		deck.flop(players);
		
		//reveal Turn card (4th community card)
		deck.turn(players);
		
		//reveal River card (5th and last community card)
		deck.river(players);
		
		System.out.printf("\nEveryone's hands:\n\n");
		for (Player p: players) {
			System.out.println(p);
		}
		//determine winner
		try {
			winner = determineWinner(players);
			winner.won();
		}
		catch(TieException e) {
			ArrayList<Player> winners = determineWinners(players);
			for (Player p: winners) {
				p.won();
			}
		}
	
	}
	
	//method to find which player has best hand (find winner of round)
	public Player determineWinner(ArrayList<Player> players) throws TieException{
		Player winner = null;
		//find player with best hand 
		for (Player p: players) {
			if (winner == null || p.getHand().compareTo(winner.getHand()) == 1) {
				winner = p;
			}
		}
		this.winner = winner; //if there is a tie, this will still help to find all the winners by comparing to the best hand
		
		//make sure no other player has the same hand
		for (Player p: players) {
			if (p != winner && p.getHand().compareTo(winner.getHand()) == 0) {
				throw new TieException();
			}
		}
		return winner;
	}
	
	//method to get all winners if there is a tie
	public ArrayList<Player> determineWinners(ArrayList<Player> players){
		ArrayList<Player> winners = new ArrayList<>();
		
		for (Player p: players) {
			if (p.getHand().compareTo(winner.getHand()) == 0) {
				winners.add(p);
			}
		}
		return winners;
	}
	
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public Player getWinner() {
		return winner;
	}
	public void setWinner(Player winner) {
		this.winner = winner;
	}
	
	
	
	
}
