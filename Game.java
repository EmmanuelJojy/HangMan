import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;

/**
 * {@code Game} class provides the actual HangMan game and and all associated
 * utilities for the same. It is highly dependent on the {@code HangMan} for
 * validaing each and every letter guessed by the current user. This class
 * implements both {@code ActionListener} and {@code KeyListener} for purpose of
 * taking input from both mouse click and keyboard (specifically for gueesing
 * the letter).
 * 
 * <p>
 * KeyListener might be a bit glitched since it demands Window Focus. I'm
 * working on solution to solve the same.
 * <p>
 * 
 * <p>
 * The class provides for a pluggable panel {@code p}.
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
public class Game implements ActionListener, KeyListener {

	/**
	 * Pluggable panel which is plugged in to the frame generated by the {@code GUI}
	 * class.
	 */
	JPanel p;
	private JPanel plug = null;
	private JLabel lgus, lcrt, lwrg, head, status;
	private JButton end, exit;
	private HangMan hm;
	private ArrayList<JButton> alpha = new ArrayList<JButton>();

	/**
	 * Default constrctor provided for {@code Game} class. Initializes the pluggable
	 * JPanel p, creates instances of {@code Figure} class.
	 * 
	 * To be invoked only from {@code GUI} class.
	 */
	public Game() {
		p = new JPanel();
		p.setSize(1366, 720);
		p.setLayout(null);
		p.addKeyListener(this);
		p.setFocusable(true);
		p.requestFocus();

		hm = Main.hm;

		head = new JLabel("Welcome, to HangMan!", JLabel.CENTER);
		head.setBounds(283, 10, 800, 40);
		head.setFont(new Font("Calibri", Font.PLAIN, 28));
		p.add(head);

		status = new JLabel("");
		status.setBounds(20, 600, 450, 50);
		status.setFont(new Font("Consolas", Font.PLAIN, 14));
		p.add(status);

		lgus = new JLabel("", JLabel.CENTER);
		lgus.setBounds(50, 100, 580, 50);
		lgus.setFont(new Font("Consolas", Font.PLAIN, 38));
		lcrt = new JLabel("ACCURATE : ", JLabel.LEFT);
		lcrt.setBounds(50, 220, 400, 30);
		lcrt.setFont(new Font("Calibri", Font.PLAIN, 20));
		lwrg = new JLabel("INCORRECT: ", JLabel.LEFT);
		lwrg.setBounds(50, 255, 400, 30);
		lwrg.setFont(new Font("Calibri", Font.PLAIN, 20));
		p.add(lgus);
		p.add(lcrt);
		p.add(lwrg);

		addKey();
		update();

		end = new JButton("END GAME");
		end.setBounds(250, 580, 100, 30);
		end.addActionListener(this);
		p.add(end);

		exit = new JButton("EXIT GAME");
		exit.setBounds(633, 580, 100, 30);
		exit.setVisible(false);
		exit.addActionListener(this);
		p.add(exit);

		p.setVisible(true);
	}

	private void addKey() {
		int i, x = 20, y = 300;
		char ch;
		for (i = 0; i < 26; i++) {
			ch = (char) (i + 65);
			JButton key = new JButton(ch + "");
			if (i % 9 == 0) {
				y += 60;
				x = i == 18 ? 50 + 5 : 20;
			}
			key.setBounds(x, y, 50, 50);
			x += 50 + 20;
			alpha.add(key);
			key.addActionListener(this);
			p.add(key);
		}
	}

	private void disableAll() {
		for (int i = 0; i < 26; i++) {
			alpha.get(i).setEnabled(false);
		}
		end.setVisible(false);
		exit.setVisible(true);
		complete();
	}

	private void complete() {
		String query;
		ResultSet res;
		Statement st = Main.st;
		int tot = 1, win = 0, loss = 0;
		try {
			query = "SELECT * FROM game WHERE NAME = '" + GUI.name + "';";
			res = st.executeQuery(query);
			res.next();
			tot = res.getInt("TOTAL") + 1;
			win = res.getInt("WIN");
			loss = res.getInt("LOSS");
			query = "DELETE FROM game WHERE NAME = '" + GUI.name + "';";
			st.executeUpdate(query);
		} catch (SQLException e) {
		}

		if (hm.win == true) {
			head.setText("You guessed the word right");
			query = "INSERT INTO game(NAME, TOTAL, WIN, LOSS) VALUES ('" + GUI.name + "', " + tot + ", " + (win + 1)
					+ ", " + loss + ");";
		} else if (hm.tries == 6) {
			head.setText("Sorry, You Lost! The word was " + hm.word);
			query = "INSERT INTO game(NAME, TOTAL, WIN, LOSS) VALUES ('" + GUI.name + "', " + tot + ", " + win + ", "
					+ (loss + 1) + ");";
		} else {
			head.setText("You have end the game!");
			query = "INSERT INTO game(NAME, TOTAL, WIN, LOSS) VALUES ('" + GUI.name + "', " + tot + ", " + win + ", "
					+ loss + ");";
		}
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Could Not Update!" + e);
		}
	}

	private void update() {
		try {
			plug.setVisible(false);
			p.remove(plug);
		} catch (NullPointerException e) {
		}
		lgus.setText(hm.guess);
		if (!hm.crt.isEmpty())
			lcrt.setText("ACCURATE : " + hm.crt.toString());
		if (!hm.wrg.isEmpty())
			lwrg.setText("INCORRECT: " + hm.wrg.toString());
		plug = new Figure();
		p.add(plug);
		status.setText("Used Life: " + hm.tries + "/6.");
		p.setFocusable(true);
		p.requestFocusInWindow();
	}

	/**
	 * Overriden method of {@code ActionListener}. Refer original documentation of
	 * the same for more details.
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton key = (JButton) ae.getSource();
		if (key == exit) {
			GUI.dashboard();
			return;
		}
		if (key == end) {
			disableAll();
			return;
		}
		char ch = key.getText().charAt(0);
		hm.validate(ch);
		update();
		key.setEnabled(false);
		if (hm.win == true || hm.tries == 6)
			disableAll();
	}

	/**
	 * Overriden method of {@code KeyListener}. Refer original documentation of the
	 * same for more details.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		char ch = Character.toUpperCase(e.getKeyChar());
		if (ch > 64 && ch < 91) {
			JButton key = alpha.get(ch - 65);
			if (key.isEnabled()) {
				hm.validate(ch);
				update();
				key.setEnabled(false);
			} else {
				status.setText(ch + " already guessed");
			}
		} else {
			status.setText(ch + " Invalid Key");
		}
		if (hm.win == true || hm.tries == 6)
			disableAll();
	}

	/**
	 * Overriden method of {@code KeyListener}. Empty body override. Refer original
	 * documentation of the same for more details.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Overriden method of {@code KeyListener}. Empty body override. Refer original
	 * documentation of the same for more details.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
}

/**
 * {@code Figure} draws the actual hangman and encapsulates it into a
 * {@code Figure} object. The class extends {@code JPanel} so is in itself a
 * JPanel. Mere creation of the {@code Figure} instance generates a pluggable
 * {@code JPanel}.
 * 
 * <p>
 * The class in itself provides for a pluggable panel {@code p}.
 * </p>
 * 
 * <p>
 * Copyright (c) 2021. All rights reserved to Emmanuel Jojy. Use is subject to
 * the above conditions. Special thanks to Bivin C Benny for help in the
 * Graphics class.
 * </p>
 * 
 * @author Emmanuel Jojy
 * 
 */
class Figure extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int tries;

	/**
	 * Iniatializes plug and other relevant fields. Dimensio set to 683 X 720
	 */
	public Figure() {
		tries = Main.hm.tries;
		setBounds(683, 0, 683, 720);
	}

	/**
	 * Draws the HangMan using standard methods in the Graphics class. Refer
	 * Graphics Class.
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.drawLine(250, 150, 250, 550); // Vertical Bar
		g.drawLine(225, 550, 375, 550); // Base
		g.drawLine(250, 150, 450, 150); // Horizontal Bar
		g.drawLine(450, 150, 450, 200); // Rope
		if (tries > 0)
			g.drawOval(413, 200, 76, 76); // Head
		if (tries > 1)
			g.drawLine(450, 276, 450, 430); // Body
		if (tries > 2)
			g.drawLine(450, 293, 410, 348); // Left Hand
		if (tries > 3)
			g.drawLine(450, 293, 490, 348); // Right Hand
		if (tries > 4)
			g.drawLine(450, 430, 400, 490); // Left Leg
		if (tries > 5)
			g.drawLine(450, 430, 500, 490); // Right Leg
	}
}