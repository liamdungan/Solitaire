package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112 / Liam Dungan
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
		
		//printList(deckRear);
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		CardNode ptr = deckRear.next;
		
		while(ptr.cardValue != 27){
			ptr = ptr.next;
		}
		
		ptr.cardValue = ptr.next.cardValue;
		ptr.next.cardValue = 27;
		
		//System.out.println("method 1");
		//printList(deckRear);
		
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		CardNode ptr = deckRear.next;
		
		while(ptr.cardValue != 28){
			ptr = ptr.next;
		}
		
		ptr.cardValue = ptr.next.cardValue;
		ptr = ptr.next;
		ptr.cardValue = ptr.next.cardValue;
		ptr = ptr.next;
		ptr.cardValue = 28;
		
		//System.out.println("method 2");
		//printList(deckRear);
		
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		
		CardNode first = deckRear.next;
		CardNode firstprev = deckRear;
		CardNode second= deckRear;
		CardNode secondnext = deckRear.next;
		
		while(first.cardValue != 27 && first.cardValue != 28){
			firstprev = first;
			first = first.next;
		}
		second = first.next;
		secondnext = second.next;
		while(second.cardValue != 27 && second.cardValue != 28){
			second = second.next;
			secondnext = second.next;
		}
		
		if(first == deckRear.next){
			deckRear = second;
			return;
		}
		
		if(second == deckRear){
			deckRear = firstprev;
			return;
		}
		
		second.next = deckRear.next;
		firstprev.next = secondnext;
		deckRear.next = first;
		deckRear = firstprev;
		
		//System.out.println("method 3");
		//printList(deckRear);
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD
		
		CardNode ptr = deckRear.next;
		CardNode ptr2 = deckRear;
		CardNode temp;
		
		while(ptr.next != deckRear){
			ptr = ptr.next;
		}
		
		int num = deckRear.cardValue;
		if(num == 28 || num == 27){
			return;
		}

		for(int i = 0; i< num; i++){
			ptr2 = ptr2.next;
		}
		temp = deckRear.next;
		
		deckRear.next = ptr2.next;
		ptr2.next = deckRear;
		ptr.next = temp;
		
		
		//System.out.println("method 4");
		//printList(deckRear);
		
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD	
		int key;	
		do{
			jokerA();
			jokerB();
			tripleCut();
			countCut();
			
			int num = deckRear.next.cardValue;
			if(num == 28)
				num = 27;
			
			CardNode temp = deckRear.next;
			for(int i = 0; i < num; i++){
				temp = temp.next;
			}
			
			key = temp.cardValue;
		}while(key == 27 || key == 28);
		
		//System.out.println("KEY: " + key);
	    return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
		char ch;	
		String encryptedStr = "";
		int temp;
		
		for(int i = 0; i <= message.length() - 1; i++){
			ch = message.charAt(i);
			if( !Character.isLetter(ch) || !Character.isUpperCase(ch) )
				continue;	
			temp = (int)ch;
			temp -= 64;
			temp += getKey();
			
			if(temp > 26)
				temp -= 26;
			
			temp+=64;		
			encryptedStr += (char)temp; 
		}
	    return encryptedStr;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
		char ch;	
		String decryptedStr = "";
		int temp;
		int key;
		
		for(int i = 0; i <= message.length() - 1; i++){
			ch = message.charAt(i);
			if( !Character.isLetter(ch) || !Character.isUpperCase(ch) )
				continue;
			
			temp = (int)ch;
			temp -=  64;
			key = getKey();
			if(temp <= key)
				temp += 26;
			temp -= key;
			temp+=64;
					
			decryptedStr += (char)temp;	
		}
	    return decryptedStr;
	}
}
