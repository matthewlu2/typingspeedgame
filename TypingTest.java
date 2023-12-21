/**
 * 
 * TypingTest is a program that allows the user to play a game that tests how fast
 * someone can type. The program will prompt the user with a sentence(s) and give
 * a count-down before the user can begin. While the user is typing, if the user makes
 * a mistake, the text will turn red, signaling that the user must fix the error. Once
 * the user is done, it will display WPM, accuracy, and average WPM for the session. 
 * Two buttons will then appear, one saying to play again and one saying to go back.
 * If you go back, the user will go back to the home screen where they can access
 * the top 5 best scores, which are saved to a text file to be accessed every time the
 * program is booted up.
 * 
 * @author Matthew Lu <pamalu.225@gmail.com>
 * 
 * 
 */

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;


public class TypingTest extends JFrame implements ActionListener{

	BufferedWriter myWriter;
	private KeyListener hi;
	private ArrayList<String> sentenceBank;
	private ArrayList<Integer> highscores;
	private ArrayList<Integer> wpms;
	private JFrame frame;
	private JPanel panel;
	private JLabel counterLabel;
	private JTextField typeHere;
	private Timer timer;
	private Timer timer2;
	private Timer timer3;
	private int second;
	private int second2;
	private int second3;
	private int wpmseconds;
	private int v;
	private int wpm;
	private int acc;

	
	
	/**
	 * 
	 * Initializes TypingTest object
	 * Reads in high-scores.
	 * 
	 * 
	 * @throws IOException
	 */
	public TypingTest() throws IOException{
		sentenceBank = new ArrayList<String>();
		highscores = new ArrayList<Integer>();
		wpms = new ArrayList<Integer>();
		sentenceBank.add("A quick brown fox jumps over the lazy dog.");
		sentenceBank.add("The fastest typing speed ever, "
				+ "216 words per minute, was achieved by Stella Pajunas-Garnand.");
		sentenceBank.add("This is a sample text. Hopefully this program goes well.");
		
		
		FileReader readFile = new FileReader("scores.txt");
		BufferedReader inFile = new BufferedReader(readFile);
		String inputString = inFile.readLine();
		
		while (inputString != null) {
			int temp = Integer.parseInt(inputString);
			highscores.add(temp);
			inputString = inFile.readLine(); 
			
		}
		
		inFile.close();
		
		
	
	}
	
	
	/**
	 * 
	 * Writes a string into a text file
	 * 
	 * 
	 * @param s 	string written to text-file
	 */
	public void writeToFile(String s){
		try {
		    myWriter.write(s);
		    myWriter.newLine();
		    myWriter.flush(); 
		    } 
		catch (IOException e) {
		    System.out.println("An error occurred.");
		    e.printStackTrace();
		   }
	}
	
	/**
	 * 
	 * Boots up the start window where the user can access the game, high-scores,
	 * and exit
	 * 
	 */
	private void startWindow() {
		panel = new JPanel();
		frame = new JFrame("Typing Test");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 1440, 900);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel clientLogin = new JLabel("Typing Test");
		clientLogin.setFont(new Font("Helvetica", Font.BOLD, 35));
		panel.add(clientLogin);
		
		JButton startGame = new JButton("Start Game");
		startGame.setActionCommand("start");
		startGame.addActionListener(this);
		panel.add(startGame);
	
		
		JButton highscore = new JButton("Highscores");
		highscore.setActionCommand("highscore");
		highscore.addActionListener(this);
		panel.add(highscore);
	
		JButton exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		panel.add(exit);
		
		frame.add(panel);
		
		frame.setVisible(true);
	}
	
	/**
	 * 
	 * Boots up the actual typing game where the user plays the game
	 * 
	 */
	private void typingGame() {
		acc = 0;
		v = (int)(Math.random() * (sentenceBank.size()));
		JLabel typeThis = new JLabel(sentenceBank.get(v));
		typeThis.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		panel.add(typeThis);
		
		typeHere = new JTextField();
		panel.add(typeHere);
		typeHere.setEditable(false);
		typeHere.setHorizontalAlignment(JTextField.LEFT);
		
		JButton back = new JButton("Back");
		back.setActionCommand("back");
		back.addActionListener(this);
		back.setVisible(false);
		panel.add(back);
		
		JButton playAgain = new JButton("Play Again");
		playAgain.setActionCommand("playAgain");
		playAgain.addActionListener(this);
		playAgain.setVisible(false);
		panel.add(playAgain);
		
		JLabel wpmLabel = new JLabel("WPM: ");
		wpmLabel.setFont(new Font("Helvetica", Font.PLAIN, 30));
		panel.add(wpmLabel);
		
		JLabel accuracy = new JLabel("Accuracy: ");
		accuracy.setFont(new Font("Helvetica", Font.PLAIN, 30));
		panel.add(accuracy);
		
		JLabel avgWPM = new JLabel("Average WPM: ");
		avgWPM.setFont(new Font("Helvetica", Font.PLAIN, 30));
		panel.add(avgWPM);
		
		
		//all timer stuff
		counterLabel = new JLabel("");
		counterLabel.setFont(new Font("Helvetica", Font.BOLD, 50));
		panel.add(counterLabel);
		counterLabel.setText("3");
		second = 3;
		countdownTimer();
		timer.start();
		//end of timer stuff
		
		second2 = 0;
		timerToThree(typeHere);
		timer2.start();
		
		second3 = 0;
		wpmTimer();
		timer3.start();
		
		
		
			hi = new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}
				
	

			@Override
			public void keyTyped(KeyEvent e) {
			
				
			}
			
			public void keyReleased(KeyEvent e) {
				String x = sentenceBank.get(v);
				String t = typeHere.getText();
				if (t.equals(x)) {
				   	 timer3.stop();
			         typeHere.setEditable(false);
			         wpmseconds = second3 - 3;
			         double min = wpmseconds / 60.0;
			         double length = sentenceBank.get(v).length();
			         wpm = (int)((length / 5.0) / min);
			         wpms.add(wpm);
			         addToScores(wpm);
			         int m = 0;
			         for (int i = 0; i < wpms.size(); i++) {
			        	 m += wpms.get(i);
			         }
			         m = m / wpms.size();
			         int g = (int)((length - acc) / length * 100);
			         String w = "WPM: " + wpm;
			         String a = "Accuracy: " + g + "%";
			         String l = "Average WPM: " + m; 
			     	wpmLabel.setText(w);
			     	accuracy.setText(a);
			     	
			     	avgWPM.setText(l);
			     	back.setVisible(true);
			     	playAgain.setVisible(true);
			     	typeHere.removeKeyListener(hi);
				}
				if (t.length() < x.length() + 1 && t.equals(x.substring(0, t.length()))) {
					typeHere.setForeground(Color.BLACK);
				}
				else {
					typeHere.setForeground(Color.RED);
					acc++;
				}
				
			}
			
			};
			typeHere.addKeyListener(hi);
	}

	/**
	 * 
	 * Boots up the list of top 5 high-scores
	 * 
	 * @throws IOException
	 */
	public void leaderboard() throws IOException{

		JLabel title = new JLabel("Best WPM");
		title.setFont(new Font("Helvetica", Font.BOLD, 40));
		panel.add(title);
		
		JLabel one = new JLabel("1. " + gets(0));
		one.setFont(new Font("Helvetica", Font.PLAIN, 35));
		panel.add(one);
		
		JLabel two = new JLabel("2. " + gets(1));
		two.setFont(new Font("Helvetica", Font.PLAIN, 35));
		panel.add(two);
		
		JLabel three = new JLabel("3. " + gets(2));
		three.setFont(new Font("Helvetica", Font.PLAIN, 35));
		panel.add(three);
		
		JLabel four = new JLabel("4. " + gets(3));
		four.setFont(new Font("Helvetica", Font.PLAIN, 35));
		panel.add(four);
		
		JLabel five = new JLabel("5. " + gets(4));
		five.setFont(new Font("Helvetica", Font.PLAIN , 35));
		panel.add(five);
		
		
		JButton back = new JButton("Back");
		back.setActionCommand("back");
		back.addActionListener(this);
		panel.add(back);
		
		JButton reset = new JButton("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		panel.add(reset);
	}
	
	/**
	 * 
	 * Helper method that deals with null values in array-list and puts 0 in place of 
	 * null.
	 * 
	 * @param i		number being put into leader-board
	 * @return		0 if null or actual value being put into leader-board
	 */
	private int gets(int i) {
		
		if (i > highscores.size() - 1) {
			return 0;
		}
		else {
			return highscores.get(i);
		}
		
	}
	
	/**
	 * 
	 * Sees what button the user pressed and does an action based on 
	 * what button it is.
	 * 
	 * @param e 	command of button
	 */
	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand().equals("start")) {
			panel.removeAll();
			typingGame();
			panel.repaint();
			panel.revalidate();
		}
		else if (e.getActionCommand().equals("highscore")) {
			panel.removeAll();
			try {
				leaderboard();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			panel.repaint();
			panel.revalidate();
		}
		else if (e.getActionCommand().equals("back")) {
			panel.removeAll();
			startWindow();
			panel.repaint();
			panel.revalidate();
		}
		else if (e.getActionCommand().equals("playAgain")) {
			panel.removeAll();
			typingGame();
			panel.repaint();
			panel.revalidate();
		}
		else if (e.getActionCommand().equals("exit")) {
			try {
				myWriter = new BufferedWriter(new FileWriter("scores.txt", false));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for (int i = 0; i < 5; i++) {
				 writeToFile(Integer.toString(gets(i)));
			}
			 System.exit(0);
		}
		else if (e.getActionCommand().equals("reset")) {
			try {
				myWriter = new BufferedWriter(new FileWriter("scores.txt", false));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < 5; i++) {
				 writeToFile(Integer.toString(0));
				 highscores.set(i, 0);
			}
			panel.removeAll();
			try {
				leaderboard();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			panel.repaint();
			panel.revalidate();
		}
		
	}
	
	/**
	 * 
	 * Timer that counts down from 3 before the game allows you to type
	 * 
	 */
	public void countdownTimer() {
		
		timer = new Timer(1000, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				second--;
				counterLabel.setText("" + second);
				
				if (second == 0) {
					timer.stop();
					panel.remove(counterLabel);
					panel.repaint();
					panel.revalidate();
				}
			}
					
		});
		
	}
	
	/**
	 * 
	 * Timer that controls when the text-field is available to be used
	 * 
	 * @param x		text-field that the user types in
	 */
	private void timerToThree(JTextField x) {
		timer2 = new Timer(1000, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				second2++;
				
				if (second2 == 3) {
					timer2.stop();
					x.setEditable(true);
				}
			}
					
		});
	}
	
	/**
	 * 
	 * Timer that keeps track of how long the user has been typing
	 * 
	 */
	public void wpmTimer() {
		timer3 = new Timer(1000, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				second3++;
			}
					
		});
	}
	
	/**
	 * 
	 * Method that adds high-score temporally into array-list to be
	 * 
	 * @param x		high-score being added
	 */
	public void addToScores(int x) {
		int i = 0;
		while (i <= highscores.size() - 1 && x < highscores.get(i)) {
			i++;
		}
		highscores.add(i, x);	
	}
	
	public static void main(String[] args) throws IOException{
		TypingTest hi = new TypingTest();
		hi.startWindow();
	}

	
	
}
