/** Class to evaluate hand */
package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class HandEvaluator {
	
	/** Evaluate best 5-hand card combination */
	public Hand evaluatePlayerHand(ArrayList<Card> cards) {
	    Hand best = null;
	    
	    //Create every possible combination of 5 cards and evaluate it
	    for (int i = 0; i < 6; i++) {
	        for (int j = i + 1; j < 7; j++) {
	        	
	            ArrayList<Card> fiveCardHand = new ArrayList<>();
	            for (int k = 0; k < 7; k++) {
	                if (k != i && k != j) {
	                    fiveCardHand.add(cards.get(k));
	                }
	            }
	            Hand current = evaluate(fiveCardHand);
	            if (best == null || current.compareTo(best) > 0) {
	                best = current;
	            }
	        }
	    }
	    return best;
	}


	/**Evaluate 5 card hand*/
	public Hand evaluate (ArrayList<Card> cards) {
		
		//create a sorted array list of just the card ranks
		ArrayList<Integer> ranks = new ArrayList<>();
		for (int i = 0; i < cards.size(); i++) {
			ranks.add(cards.get(i).getRankNumber());
		}
		Collections.sort(ranks);
		
		//create an array list of just the suits
		ArrayList<Integer> suits = new ArrayList<>();
		for (int i = 0; i < cards.size(); i++) {
			suits.add(cards.get(i).getSuitNumber());
		}
		
		
		//check for highest hand ranking and create Hand Object with data on that hand
		//Straight flush is category 9, four of a kind is 8, etc.
		
		if (isRoyalFlush(ranks, suits)) {
			return new Hand(10);
		}
		else if (isStraightFlush(ranks, suits) != 0) {
			return new Hand(9, new int[] {isStraightFlush(ranks, suits)});
		}
		else if (isFourOfAKind(ranks) != 0) {
			return new Hand(8, new int[] {isFourOfAKind(ranks)});
		}
		else if (isFullHouse(ranks) != null) {
			return new Hand(7, isFullHouse(ranks));
		}
		else if (isFlush(suits, ranks) != null) {
			return new Hand(6, isFlush(ranks, suits));
		}
		else if (isStraight(ranks) != 0) {
			return new Hand(5, new int[] {isStraight(ranks)});
		}
		else if (isThreeOfAKind(ranks) != 0) {
			return new Hand(4, new int[] {isThreeOfAKind(ranks)});
		}
		else if (isTwoPair(ranks) != null) {
			return new Hand(3, isTwoPair(ranks));
		}
		else if (isPair(ranks) != null) {
			return new Hand(2, isPair(ranks));
		}
		else {
			return new Hand(1, isHighCard(ranks));
		}
		
	}
	
	
	/** Evaluate best 5-card hand from 6 cards (Turn) */
	public Hand evaluateTurn(ArrayList<Card> cards) {
	    Hand best = null;

	    for (int skip = 0; skip < 6; skip++) {

	        ArrayList<Card> fiveCardHand = new ArrayList<>();

	        for (int i = 0; i < 6; i++) {
	            if (i != skip) {
	                fiveCardHand.add(cards.get(i));
	            }
	        }

	        Hand current = evaluate(fiveCardHand);

	        if (best == null || current.compareTo(best) > 0) {
	            best = current;
	        }
	    }

	    return best;
	}
	
	
	/**determine whether the hand has a ROYAL FLUSH */
	public boolean isRoyalFlush(ArrayList<Integer> ranks, ArrayList<Integer> suits) {
		
		if(isStraightFlush(ranks, suits) == 14) 
			return true;
		else
			return false;
	}
	
	/**determine whether the hand has a STRAIGHT FLUSH (0 if not, highest rank if yes)*/
	public int isStraightFlush(ArrayList<Integer> ranks, ArrayList<Integer> suits) {
		
		//check if they form a flush
		if (isFlush(suits, ranks) == null)
			return 0;
			
		
		//check if they form a straight
		if (isStraight(ranks) != 0)
			return isStraight(ranks);
		else
			return 0;
		
	}
	
	/**check if there's FOUR OF A KIND (return the rank of 4 cards)*/
	public int isFourOfAKind(ArrayList<Integer> ranks) {
		
	
		//test if 4 first cards are the same rank
		boolean fourOfAKind = true;
		
		for(int i = 1; i < ranks.size() - 1; i++) {
			if (ranks.get(i) != ranks.get(i-1)) {
				fourOfAKind = false;
				break;
			}
		}
		if (fourOfAKind) {
			return ranks.get(0);
		}
		
		//test if 4 last cards are the same rank
		fourOfAKind = true;
		for(int i = 2; i < ranks.size(); i++) {
			if (ranks.get(i) != ranks.get(i-1)) {
				fourOfAKind = false;
				break;
			}
		}

		if (fourOfAKind) {
			return ranks.get(1);
		}
		else 
			return 0;
	}

	
	/**check for a FULL HOUSE (3 of a Kind plus 2 of another kind)*/
	public int[] isFullHouse(ArrayList<Integer> ranks) {
		
		//check if first 3 cards are the same and if so then if 2 next cards are the same
		if (ranks.get(0) == ranks.get(1) && ranks.get(1) == ranks.get(2)) {
			if (ranks.get(3) == ranks.get(4)) {
				return new int[]{ranks.get(0), ranks.get(4)};
			}
			else
				return null;
			
		}
		else if (ranks.get(0) == ranks.get(1)) {
			if (ranks.get(2) == ranks.get(3) && ranks.get(3) == ranks.get(4)) {
				return new int[] {ranks.get(4), ranks.get(0)};
			}
			else
				return null;
		}
		else 
			return null;
		
		
	}
	
	/**check if hand is a FLUSH (five of same suit)*/
	public int[] isFlush(ArrayList<Integer> ranks, ArrayList<Integer> suits) {
		for(int i = 1; i < suits.size(); i++) {
			if(suits.get(i) != suits.get(i-1)) {
				return null;
			}
		}
		return isHighCard(ranks);
	}
	
	/**check if hand has a STRAIGHT (0 if not, highest rank if yes)*/
	public int isStraight(ArrayList<Integer> ranks) {
		
		boolean straight = true;
		//Keep in mind, and Ace can act as a One or Above a King (14) in Texas Hold'em, so we have to check for both
		
		//If an Ace is worth 14 (default)
		for(int i = 1; i < ranks.size(); i++) {
			if(ranks.get(i) != ranks.get(i-1) + 1) {
				straight = false;
				break;
			}
		}
		if (straight) {
			return ranks.get(ranks.size()-1);
		}
		
		
		//If an Ace is worth One
		//changing rank numbers 14 to 1
		for(int i = 0; i < ranks.size(); i++) {
			if (ranks.get(i) == 14)
				ranks.set(i, 1);
		}
		Collections.sort(ranks);
		
		straight = true;
		for(int i = 1; i < ranks.size(); i++) {
			if(ranks.get(i) != ranks.get(i-1) + 1) {
				straight = false;
			}
		}
		
		//changing rank numbers 1 back to 14 for the rest before returning a result
		for(int i = 0; i < ranks.size(); i++) {
			if (ranks.get(i) == 1) {					
				ranks.set(i, 14);
			}
		}
		
		if (straight)
			return ranks.get(ranks.size()-1);
		else
			return 0;
	}
	
	/**check for a THREE OF A KIND* (rank of 3, or 0 if none)*/
	public int isThreeOfAKind(ArrayList<Integer> ranks) {
		
		//check for 3 of a kind in the first three, middle three, and last three
		if (ranks.get(0) == ranks.get(1) && ranks.get(1) == ranks.get(2)) {
			return ranks.get(0);
		}
		else if (ranks.get(1) == ranks.get(2) && ranks.get(2) == ranks.get(3)) {
			return ranks.get(1);
		}
		else if (ranks.get(2) == ranks.get(3) && ranks.get(3) == ranks.get(4)) {
			return ranks.get(2);
		}
		else
			return 0;
	}
	
	/**check for TWO PAIR (return rank of each in descending order and the kicker if yes, null if no)*/
	public int[] isTwoPair(ArrayList<Integer> ranks) {
		
		if (ranks.get(0) == ranks.get(1)) {
			if (ranks.get(2) == ranks.get(3)) {
				return new int[] {ranks.get(2), ranks.get(0), ranks.get(4)};
			}
			else if (ranks.get(3) == ranks.get(4)) {
				return new int[] {ranks.get(3), ranks.get(0), ranks.get(2)};
			}
			else {
				return null;
			}	
		}
		else if (ranks.get(1) == ranks.get(2)) {
			if (ranks.get(3) == ranks.get(4)) {
				return new int[] {ranks.get(3), ranks.get(1), ranks.get(0)};
			}
			else
				return null;
		}
		else {
			return null;
		}
	}
	
	/**check for one PAIR (return rank of pair plus rank of following cards in descending order, null if none)*/
	public int[] isPair(ArrayList<Integer> ranks) {
		
		if (ranks.get(0) == ranks.get(1))
			return new int[] {ranks.get(0), ranks.get(4), ranks.get(3), ranks.get(2)};
		
		else if (ranks.get(1) == ranks.get(2))
			return new int[] {ranks.get(1), ranks.get(4), ranks.get(3), ranks.get(0)};
		
		else if (ranks.get(2) == ranks.get(3))
			return new int[] {ranks.get(2), ranks.get(4), ranks.get(1), ranks.get(0)};
		
		else if (ranks.get(3) == ranks.get(4))
			return new int[] {ranks.get(3), ranks.get(2), ranks.get(1), ranks.get(0)};
		
		else 
			return null;
	}
	
	/**check for the HIGHEST CARD (return all the cards from highest to lowest)*/
	public int[] isHighCard(ArrayList<Integer> ranks) {
		return new int[] {ranks.get(4),ranks.get(3),ranks.get(2),ranks.get(1),ranks.get(0)};
	}

}
