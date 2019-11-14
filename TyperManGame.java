import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class TyperManGame extends JPanel implements KeyListener, ActionListener {
	/* A class that inherits JPanel and defines methods of KeyListener and
	   ActionListener */

	JTextField currentString;
	JTextArea pointBox;
	JTextField pauseBox;
	ArrayList<String> bank;

	/* An ArrayList is used here since it doesn't have a defined size and is
	   allocated dynamically. bank is used to store the dictionary words */
	ArrayList<FallingWord> wordsOnBoard;

	private int points;
	private Timer time;
	private int currentTime;
	private int difficulty;
	private boolean timerstate;

	public TyperManGame() throws FileNotFoundException {
		setSize(400, 400);
		setLayout(null);
		bank = Dictionary.getWords("words.txt");
		setBackground(Color.BLACK);
		currentString = new JTextField("");

		// Initializes field to type the words
		currentString.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendString();
			}
		});

		currentString.setSize(400, 30);
		currentString.setLocation(0, 342);
		currentString.setBackground(Color.BLUE);
		currentString.setEditable(true);
		currentString.setForeground(Color.white);
		currentString.setFont(currentString.getFont().deriveFont(20f));

		pointBox = new JTextArea("Points");
		pointBox.setEditable(false);
		pointBox.setSize(40, 40);
		pointBox.setBackground(Color.PINK);
		pointBox.setForeground(Color.white);
		pointBox.setLocation(360, 0);

		pauseBox = new JTextField("Pause");
		pauseBox.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (timerstate == false) {
						resumeGame();
					} else if (timerstate == true) {
						pauseGame();
					}
				}
			});

		pauseBox.setEditable(false);
		pauseBox.setSize(60,30);
		pauseBox.setBackground(Color.MAGENTA);
		pauseBox.setForeground(Color.white);
		pauseBox.setLocation(0, 10);

		add(pauseBox);
		add(pointBox);
		add(currentString);
		setVisible(true);
		time = new Timer(100, this);
		startNewGame();
	}

	public void startNewGame() {
		points = 0;
		currentTime = 0;
		wordsOnBoard = new ArrayList<FallingWord>();
		difficulty = 0;

		time.start();
		timerstate = true;
	}

	public void pauseGame() {
		time.stop();
		timerstate = false;
		pauseBox.setText("Resume");
	}

	public void resumeGame() {
		time.start();
		timerstate = true;
		pauseBox.setText("Pause");
	}

	public void sendString() {
		// Get the word typed by the user
		String entry = currentString.getText();

		// Clears the text field to allow the user to type further words
		currentString.setText("");

		if(wordIsOnBoard(entry)) {
			// Points are increased based on length of the word and the difficulty.
			points = points + entry.length() + difficulty;

			// Update the points
			pointBox.setText("" + points);

			// Remove the word if successfully typed
			removeWord(entry);

			// The UI is reset
			updateUI();
		}
	}

	public boolean wordIsOnBoard(String entry) {
		// This object traverses a collection of objects one by one.
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();

		while(it.hasNext()) {
			// Returns the next element
			FallingWord current = it.next();

			if(current.equals(entry)) {
				return true;
			}
		}

		// False if entered word does not match
		return false;
	}

	private void removeWord(String entry) {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
		boolean found = false;

		while(it.hasNext() && !found) {
			FallingWord current = it.next();
			if(current.equals(entry)) {
				// Checks if the current word is equal to the entered word
				remove(current.box);

				// If equal, it removes the word from the current iteration.
				it.remove();

				// Reinitialized to true so that not all the words are removed.
				found = true;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		currentTime++;
		moveAllDown();

		if(collison()) {
			endGame();
		}

		adjustDifficulty();
	}

	private void adjustDifficulty() {
		/* wordFrequency keeps decreasing as difficulty increases until it
		  * reaches 9 when it is again set to 10. It does not make sense dropping
		  * wordFrequency's value below 10 as the velocity of the falling words also
		  * increases with time (difficulty). */
		int wordFrequency = 40 - ((difficulty * 2) / 5);

		/* The counter increases from 1 to wordFrequency and then, as soon as
		 * currentTime is a multiple of wordFrequency, it resets to 0 and is
		 * responsible for creation of a new word. */
		int counter = currentTime % wordFrequency;

		if(wordFrequency < 10) {
			wordFrequency = 10;
		}

		/* A new word is created (with increased velocity -- difficulty) as soon
		 * as the counter resets to 0. This 0 value will increasingly recur,
		 * causing increased rate of word creation as well as increased rate of
		 * their descend. */
		if(counter == 0) {
			difficulty++;
			makeNewWord();
		}
	}

	private void makeNewWord() {
		String randomWord = getRandomWord();
		FallingWord newWord = new FallingWord(randomWord, difficulty + 1);

		wordsOnBoard.add(newWord);
	}

	private void endGame() {
		time.stop();

		End game = new End();
		this.setVisible(false);
		game.setVisible(true);
	}

	public boolean collison() {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();

		while(it.hasNext()) {
			FallingWord current = it.next();

			if(current.atBottom()) {
				return true;
			}
		}

		return false;
	}


	private void moveAllDown() {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();

		while(it.hasNext()) {
			FallingWord current = it.next();
			current.updateBox();
		}

		updateUI();
	}

	private String getRandomWord() {
		Random ran = new Random();
		int randomIndex = ran.nextInt(bank.size());

		return bank.get(randomIndex);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		;
	}

	@Override
	public void keyTyped(KeyEvent key) {
		;
	}

	private class FallingWord {
		private String word;
		private JTextField box;
		private int boxVel;
		private int xLoc;
		private int yLoc;

		public FallingWord(String word, int boxVel) {
			Random ran = new Random();
			xLoc = ran.nextInt(300);
			yLoc = 0;
			this.word = word;
			this.boxVel = boxVel;
			createBox();
		}

		public boolean atBottom() {
			if(yLoc >=340) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean equals(Object other) {
			if(other instanceof String) {
				String otherword = (String) other;
				return this.word.equals(otherword);
			} else {
				return false;
			}
		}

		public void updateBox() {
			yLoc = yLoc + boxVel;
			box.setLocation(xLoc, yLoc);

			if(yLoc > 235) {
				box.setForeground(Color.white);
				box.setBackground(Color.red);
			} else if(yLoc > 110) {
				box.setBackground(Color.yellow);
			}
		}

		public void createBox() {
			box = new JTextField(word);
			box.setLocation(xLoc, yLoc);
			box.setSize(8 * word.length() + 10, 30);
			box.setBackground(Color.GREEN);
			add(box);
		}
	}
}
