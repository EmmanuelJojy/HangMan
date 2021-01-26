
import java.util.*;

/**
 * {@code HangMan} class is the actual brain of the HangMan game and and
 * provides all associated utilities for the same. It is a partially independent
 * class, though it requires {@code Word} for obtaining the word being played.
 * This class does not implement nor extend any other interfaces and classes
 * respectively.
 * 
 * <p>
 * Collections framework has been extensively used for providing resizabiltiy
 * without needing to hardcode the size based on the word.
 * </p>
 * 
 * <p>
 * The class is a dependency for the {@code Game} class.
 * </p>
 * 
 * <p>
 * Copyright (c) 2021. All rights reserved to Emmanuel Jojy. Use is subject to
 * the above conditions.
 * </p>
 * 
 * @author Emmanuel Jojy
 * 
 */
public class HangMan {

	/**
	 * ArrayList of correct letters guessed. Is updated each time a letter needs to
	 * be valiated. {@code Game} class makes sure no duplicate letters reach for
	 * validation.
	 */
	ArrayList<Character> crt;

	/**
	 * ArrayList of wrong letters guessed. Is updated each time a letter needs to be
	 * valiated. {@code Game} class makes sure no duplicate letters reach for
	 * validation.
	 */
	ArrayList<Character> wrg;

	/**
	 * Total number of the letter guessed was wrong. Maximum allowable tries is 6.
	 * Valur ranges from 0 to 6.
	 */
	int tries;

	/**
	 * Check if the user guessed the complete word. Checked each time a letter is
	 * sent for validation.
	 */
	boolean win;

	/**
	 * The guessed word. Initially contains '{@code _}' replacing all characters of
	 * original word.
	 */
	String guess;

	/**
	 * Reference to {@code Main.word}. The application creates a copy when the
	 * instance of {@code HangMan} is created.
	 */
	String word;

	/**
	 * Default constructor. Initializes all fields with default values.
	 */
	public HangMan() {
		word = Main.word;
		crt = new ArrayList<Character>();
		wrg = new ArrayList<Character>();
		tries = 0;
		guess = " ";
		win = false;
		for (int i = 0; i < word.length(); i++) {
			guess += "_ ";
		}
	}

	private boolean isGuessed(char letter) {
		char ch;
		for (int i = 0; i < crt.size(); i++) {
			ch = crt.get(i).charValue();
			if (ch == letter) {
				return true;
			}
		}
		return false;
	}

	private boolean check(char letter) {
		Character ch = Character.valueOf(letter);
		boolean res;
		if (word.indexOf(letter) != -1) {
			crt.add(ch);
			res = true;
		} else {
			wrg.add(ch);
			tries++;
			res = false;
		}
		update();
		if (guess.indexOf('_') == -1)
			win = true;
		return res;
	}

	private void update() {
		int i;
		char letter;
		guess = "";
		for (i = 0; i < word.length(); i++) {
			letter = word.charAt(i);
			if (isGuessed(letter))
				guess += letter;
			else
				guess += '_';
			guess += ' ';
		}
	}

	private void debug() {
		System.out.println("try = " + tries);
		System.out.println("crt = " + crt);
		System.out.println("wrg = " + wrg);
		System.out.println("gus = " + guess);
		System.out.println("\n");
	}

	/**
	 * Validates a letter guessed by the {@code Game} class. The only directly
	 * callable method of {@code HangMan}.
	 * <p>
	 * All other methods are private and are chained to {@code validate()}
	 * </p>
	 * 
	 * @param letter The letter the user guesses. The caller must ensure no
	 *               duplictaes reach the method for a specific session.
	 */
	public void validate(char letter) {
		check(letter);
		debug();
	}
}
