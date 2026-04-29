package core;

public class Card {
	
	//data field
	private int rankNumber;
	private int suitNumber;
	private String suit;
	private String rank;
	
	//Constructors
	public Card() {
	}
	
	public Card(int rankNumber, int suitNumber) {
		try {
			if (rankNumber < 2 || rankNumber > 14)
				throw new InvalidRankException("Invalid Rank Exception Occured");
			else if (suitNumber < 1 || suitNumber > 4)
				throw new InvalidSuitException("Invalid Suit Exception Occured");
			
			this.rankNumber = rankNumber;
			this.suitNumber = suitNumber;
			this.suit = getSuit();
			this.rank = getRank();
		}
		catch(InvalidRankException e) {
			System.out.println(e.getMessage());
		}
		catch(InvalidSuitException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println("Something unexpected occured when creating Card object");
		}
		
	}
	
	//methods
	@Override
	public String toString() {
		return String.format("%s of %s", getRank(), getSuit());
	}

	//Getters and Setters
	public int getRankNumber() {
		return rankNumber;
	}

	public void setRankNumber(int number) {
		this.rankNumber = rankNumber;
	}

	public int getSuitNumber() {
		return suitNumber;
	}

	public void setSuitNumber(int suitNumber) {
		this.suitNumber = suitNumber;
	}

	public String getSuit() {
		switch(getSuitNumber()) {
			case 1: suit = "Clubs";
			break;
			case 2: suit = "Diamonds";
			break;
			case 3: suit = "Hearts";
			break;
			case 4: suit = "Spades";
			break;
		}
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}
	
	public String getRank() {
		switch(getRankNumber()) {
			case 11: rank = "Jack";
			break;
			case 12: rank = "Queen";
			break;
			case 13: rank = "King";
			break;
			case 14: rank = "Ace";
			break;
			default: rank = Integer.toString(rankNumber);
		}
		return rank;
	}
	
	//method to convert any given rank number into string of rank name
	public String getRank(int rankNumber) {
		String rank;
		switch(rankNumber) {
			case 11: rank = "Jack";
			break;
			case 12: rank = "Queen";
			break;
			case 13: rank = "King";
			break;
			case 14: rank = "Ace";
			break;
			default: rank = Integer.toString(rankNumber);
		}
		return rank;
	}

}
