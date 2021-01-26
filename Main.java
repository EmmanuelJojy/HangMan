import java.sql.*;

/**
 * 
 * The {@code Main} class is the entry point for the HangMan application. It
 * initializes the database and invokes the {@code Word} and {@code GUI}
 * constructors by creating instances of the respective classes.
 * 
 * <p>
 * The database driver used is PostgreSQL JDBC Driver (PgJDBC v42.2.18).
 * ElephantSQL was the PostgreSQL service used. Database connections are routed
 * to the instance HangMan created for Emmanuel Jojy (outmail*****@gmail.com) -
 * Plan Tiny Turtle. Other details of the database is hidden for maintaining
 * integrity of the database and data contained within it.
 * </p>
 * 
 * <p>
 * WARNING: This project was created without keeping in mind about the privacy
 * concerns. There is no sort of encryption provided for neither the username
 * and password. Everything exist a raw text ({@code VARCHAR}). Emmanuel Jojy
 * can view entire contents of the database if he requires
 * </p>
 * 
 * <p>
 * Copyright (c) 2021. All rights reserved to Emmanuel Jojy. Use is subject to
 * the above conditions.
 * </p>
 * 
 * <p>
 * Hope it gives a feel of a classified and confidential document. I'm just
 * joking and the entire project was created for fun. With lots of love @emmanu
 * ;)
 * </p>
 * 
 * @author Emmanuel Jojy
 * 
 */
public class Main {
	/**
	 * The current word being played in HangMan. The {@code Word} class initializes
	 * this field.
	 */
	static String word;

	/**
	 * An instance of {@code HangMan} class. Initially set to {@code null} and is
	 * modified by the {@code start()} method.
	 */
	static HangMan hm;

	/**
	 * The {@code Statement} object for performing queries to the database. The
	 * entire applications uses this instance for communication to the database.
	 */
	static Statement st;

	/**
	 * Default constructor left as is, since there are no instance fields specific
	 * to the class. All fields are of type static.
	 */
	public Main() {
	}

	/**
	 * Self explanatory. Intended for call single time and origin of call the
	 * {@code Main} class.
	 */
	private void db() {
		try {
			/*
			 * Class.forName("org.sqlite.JDBC"); Connection c =
			 * DriverManager.getConnection("jdbc:sqlite:db.db");
			 */

			Class.forName("org.postgresql.Driver");
			Connection c = DriverManager.getConnection("jdbc:postgresql://john.db.elephantsql.com:5432/deauwbtx",
					"deauwbtx", "T-JrcXbHqBoOcEbbbdfC-5_JMbXJv7aI");

			st = c.createStatement();
		} catch (SQLException e) {
			System.out.println("#DB Error - " + e);
			System.exit(0);
		} catch (ClassNotFoundException e) {
			System.out.println("#postgresql Driver Error - " + e);
			System.exit(0);
		}
		System.out.println("Main");
		System.out.println(" L db - Complete");
	}

	/**
	 * Modifies the {@code word} and {@code hm} fields on invokation. This method is
	 * only and only called when an instance of {@code Game} class is to be created
	 * by the {@code game()} method in {@code GUI}.
	 */
	public static void start() {
		word = Word.getWord().toUpperCase();
		// Comment Line Below
		//
		// ------------------
		hm = new HangMan();
	}

	/**
	 * The one and only intended entry point. Kickstarts the entire application. The
	 * only {@code main()} method in the entire application.
	 * <p>
	 * Invokes a private method {@code db()} for intializing the database and sets
	 * up the {@code GUI}.
	 * </p>
	 * 
	 * @param args Not required. Optional
	 */
	public static void main(String[] args) {
		Main obj = new Main();
		obj.db();
		new GUI();
	}
}
