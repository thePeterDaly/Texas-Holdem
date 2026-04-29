package core;
import java.util.ArrayList;

public class Hand implements Comparable<Hand>{
	
	int category;
	int[] tieBreaker; 
	
	
	public Hand() {
	}
	
	public Hand(int category) {
		this.category = category;
	}
	
	public Hand(int category, int[] tieBreaker) {
		this.category = category;
		this.tieBreaker = tieBreaker;
	}

	//CompareTo method to compare Hands by looking at category and TieBreaker if needed
	@Override
	public int compareTo(Hand best) {
		int[] l1 = this.tieBreaker;
		int[] l2 = best.getTieBreaker();
		try {
			if (this.category < best.getCategory())
				return -1;
			else if (this.category == best.getCategory()) {
				if (this.category == 10)
					return 0;
				else {
					for (int i = 0; i < this.tieBreaker.length; i++) {
						if (l1[i] > l2[i])
							return 1;
						if (l1[i] < l2[i])
							return -1;
					}
					return 0;
				}
			}
			else return 1;
		}
		catch(IndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	//method to get written information on Hand
	@Override
	public String toString() {
		Card card = new Card();
		switch(category) {
			case 1: return String.format("High card: %s followed by a %s, %s, %s, and a %s.", 
					card.getRank(tieBreaker[0]), card.getRank(tieBreaker[1]), card.getRank(tieBreaker[2]), card.getRank(tieBreaker[3]), card.getRank(tieBreaker[4]));
			case 2: return String.format("Pair of %ss followed by a %s, %s, and a %s.",
					card.getRank(tieBreaker[0]), card.getRank(tieBreaker[1]), card.getRank(tieBreaker[2]), card.getRank(tieBreaker[3]));
			case 3: return String.format("Two Pairs of %ss and %ss, plus a %s.", card.getRank(tieBreaker[0]), card.getRank(tieBreaker[1]), card.getRank(tieBreaker[2]));
			case 4: return String.format("Three of a Kind, rank: %s.", card.getRank(tieBreaker[0]));
			case 5: return String.format("Straight, highest card rank: %s.", card.getRank(tieBreaker[0]));
			case 6: return String.format("Flush, highest card rank: %s.", card.getRank(tieBreaker[0]));
			case 7: return String.format("Full House, rank: %s.", card.getRank(tieBreaker[0]));
			case 8: return String.format("Four of a Kind, rank: %s.", card.getRank(tieBreaker[0]));
			case 9: return String.format("Straight flush, highest rank: %s", card.getRank(tieBreaker[0]));
			case 10: return "Royal Flush";
			default: return "Something unexpected occured";
		}
	}
	

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int[] getTieBreaker() {
		return tieBreaker;
	}

	public void setTieBreaker(int[] tieBreaker) {
		this.tieBreaker = tieBreaker;
	}
	
	

	

}
