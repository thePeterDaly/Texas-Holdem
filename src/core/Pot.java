package core;


import java.util.ArrayList;

public class Pot {
	private ArrayList<Player> players = new ArrayList<>(); 
	private int pot = 0;
	private int currentBet = 0;
	private int raiseAmount = 2;
	private int smallBlind = 1;
	private int bigBlind = 2;
	
	//Constructors
	public Pot(ArrayList<Player> players) {
		this.players = players;
	}
	
	public Pot(ArrayList<Player> players, int smallBlind, int bigBlind) {
		this.players = players;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
	}
	
	//method to reset pot for new round
	public void resetPot() {
		this.pot = 0;
		for (Player p: players) {
			p.setChipsInPot(0);
		}
		currentBet = 0;
	}
	
	//method when player gives smallBlind
	public void smallBlind(Player p) {
		p.setChips(p.getChips() - smallBlind);
		p.setChipsInPot(p.getChipsInPot() + smallBlind);
		this.pot += smallBlind;
		currentBet = smallBlind;
		p.setLastMove("posted the small blind");
	}
	
	//method when player gives bigBlind
	public void bigBlind(Player p) {
		p.setChips(p.getChips() - bigBlind);
		p.setChipsInPot(p.getChipsInPot() + bigBlind);
		this.pot += bigBlind;
		currentBet = bigBlind;
		p.setLastMove("posted the big blind");
	}
	
	public void call(Player player) throws NegativeChipsException{
		//See what is the required amount to move on (to match the highest bet)
		int amountNeeded = currentBet - player.getChipsInPot();
		
		if (amountNeeded > player.getChips()) { //Can't let a player go below 0 chips. Must fold in that case
			throw new NegativeChipsException();
		}
		
		player.setChips(player.getChips() - amountNeeded);
		this.pot += amountNeeded;
		player.setChipsInPot(player.getChipsInPot() + amountNeeded);
		player.setLastMove("called");
	}
	
	//for this limit hold'em, you can raise by 2 chips
	public void raise(Player p) throws NegativeChipsException{
		call(p);
		
		if(p.getChips() < raiseAmount) {
			throw new NegativeChipsException();
		}
		
		p.setChips(p.getChips() - raiseAmount);
		this.pot += raiseAmount;
		p.setChipsInPot(p.getChipsInPot() + raiseAmount);
		p.setLastMove("raised");
		currentBet = p.getChipsInPot();
	}
	
	//returns whether all active players have matched the current bet
	public boolean betsMatch() {
//		if (currentBet == 0) return false;
		for (Player p: players) {
			if (p.isFolded == false && p.getChipsInPot() != currentBet) {
				return false;
			}
		}
		return true;
	}
	
//	public int getCurrentBet() {
//		int maxContribution = 0;
//		for (Player p: players) {
//			if (p.isFolded == false && p.getChipsInPot() > maxContribution)
//				maxContribution = p.getChipsInPot();
//		}
//		return maxContribution;
//	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public int getRaiseAmount() {
		return raiseAmount;
	}

	public void setRaiseAmount(int raiseAmount) {
		this.raiseAmount = raiseAmount;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(int bigBlind) {
		this.bigBlind = bigBlind;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}
	
}
