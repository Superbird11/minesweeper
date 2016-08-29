package def;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import java.util.Random;

/**
 * A class representing the board on which Minesweeper is played. Generally controls the game.
 * @author Louis Jacobowitz
 *
 */
public class MinesweeperBoard extends JFrame implements ActionListener {
	/** A default Serial Version ID */
	private static final long serialVersionUID = 1L;
	/** The app name */
	private static final String APP_NAME = "Minesweeper";
	/** The board - an array of MineSquares that share responsibility for running the game */
	private MineSquare[][] board;
	/** The height of the board */
	private int boardHeight;
	/** The width of the board */
	private int boardWidth;
	/** The number of mines on the board */
	private int numMines;
	/** The number of mines currently not flagged */
	private int currentNumMines;
	/** An instance of LocalImage for assigning icons when necessary */
	private LocalImage icon;
	/** The bottom panel, in which the mines are placed */
	private JPanel bottomPanel;
	/** The time in the game so far */
	private int time;
	/** A timer for counting up the time */
	private Timer timeTimer;
	/** The label that displays the time */
	private JLabel timeLabel;
	/** The label that the mine counter uses */
	private JLabel mineLabel;
	/** The face label */
	private FaceLabel faceLabel;
	
	/**
	 * Creates a new MinesweeperBoard, initializing the display and calling newGame().
	 */
	public MinesweeperBoard() {
		// First, make it able to close
		addWindowListener(new WindowAdapter () {
			public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
		});
		// Set up container
		Container c = getContentPane();
		setTitle(APP_NAME);
		//setSize(750, 500); //maybe not necessary?
		c.setLayout(new GridLayout(2, 1));
		// Set default boardHeight and boardWidth
		boardHeight = 10;//16;
		boardWidth = 19;//30;
		numMines = 10;//99;
		bottomPanel = new JPanel();
		
		// Initialize icon now
		icon = LocalImage.initialize();
		
		// Top has a timer. For now, just put in a JPanel
		JPanel topPanel = new JPanel(new BorderLayout());
		c.add(topPanel);
		// Put things in the top panel! Start with initializing the timer.
		timeLabel = new JLabel("Time: 000");
		timeTimer = new Timer(1000, this);
		timeTimer.start();
		topPanel.add(timeLabel, BorderLayout.EAST);
		// Next, initialize the mine label
		currentNumMines = numMines;
		mineLabel = new JLabel(String.format("Mines: %3d", currentNumMines));
		topPanel.add(mineLabel, BorderLayout.WEST);
		// Finally, add the face label
		faceLabel = new FaceLabel(this);
		topPanel.add(faceLabel, BorderLayout.CENTER);
		
		c.add(bottomPanel);
		newGame();
		
		setSize(600, 600);
		setVisible(true);
		System.out.println("Finished constructor");
	}
	
	/**
	 * Starts a new game, initializing the board and the mines.
	 */
	public void newGame() {
		Container c = getContentPane();
		c.remove(bottomPanel);
		bottomPanel = new JPanel(new GridLayout(boardHeight, boardWidth, 1, 1));
		c.add(bottomPanel);
		
		// Set up board
		board = new MineSquare[boardHeight][boardWidth];
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				board[i][j] = new MineSquare(i, j, this);
			}
		}
		// Set up mines
		Random rand = new Random();
		for(int k = 0; k < numMines; k++) {
			MineSquare randomSquare;
			do {
				randomSquare = board[rand.nextInt(boardHeight)][rand.nextInt(boardWidth)];
			} while (randomSquare.isMine());
			randomSquare.setMine();
		}
		// Add the tiles to the board
		// Also set the number of adjacent mines.
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				bottomPanel.add(board[i][j]);
				//board[i][j].setSize(16,16);
				int mineCount = 0;
				if(i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1].isMine()) mineCount++;
				if(i - 1 >= 0 && board[i - 1][j].isMine()) mineCount++;
				if(i - 1 >= 0 && j + 1 < boardWidth && board[i - 1][j + 1].isMine()) mineCount++;
				if(j - 1 >= 0 && board[i][j - 1].isMine()) mineCount++;
				if(j + 1 < boardWidth && board[i][j + 1].isMine()) mineCount++;
				if(i + 1 < boardHeight && j - 1 >= 0 && board[i + 1][j - 1].isMine()) mineCount++;
				if(i + 1 < boardHeight && board[i + 1][j].isMine()) mineCount++;
				if(i + 1 < boardHeight && j + 1 < boardWidth && board[i + 1][j + 1].isMine()) mineCount++;
				board[i][j].setNumber(mineCount);
				//if(board[i][j].isMine()) board[i][j].setIcon(icon.mine());
				//else board[i][j].setIcon(icon.known(board[i][j].getNumber()));
			}
		}
		// Initialize time
		time = 0;
		timeLabel.setText("Time: 000");
		timeTimer.stop();
		timeTimer.start();
	}

	public void squareClicked(MineSquare sender) {
		if(sender.isRevealed()) return;
		sender.setRevealed();
	}
	
	/**
	 * Increments the mine counter, changing the label to match
	 */
	public void incrementMineCount() {
		currentNumMines++;
		mineLabel.setText(String.format("Mines: %3d", currentNumMines));
	}
	
	/**
	 * Decrements the mine counter, changing the label to match
	 */
	public void decrementMineCount() {
		currentNumMines--;
		mineLabel.setText(String.format("Mines: %3d", currentNumMines));
	}
	
	public void failGame(MineSquare sender) {
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				board[i][j].setInteractable(false);
				if(board[i][j].isMine() && board[i][j] != sender) {
					board[i][j].setIcon(icon.mine());
				}
			}
		}
		timeTimer.stop();
	}
	
	public void clearAllAround(MineSquare sender) {
		// First, check the number of mines around the sender
		int minesAround = 0;
		int i = sender.getx();
		int j = sender.gety();
		if(i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1].isFlagged()) minesAround++;
		if(i - 1 >= 0 && board[i - 1][j].isFlagged()) minesAround++;
		if(i - 1 >= 0 && j + 1 < boardHeight && board[i - 1][j + 1].isFlagged()) minesAround++;
		if(j - 1 >= 0 && board[i][j - 1].isFlagged()) minesAround++;
		if(j + 1 < j && board[i][j + 1].isFlagged()) minesAround++;
		if(i + 1 < boardWidth && j - 1 >= 0 && board[i + 1][j - 1].isFlagged()) minesAround++;
		if(i + 1 < boardWidth && board[i + 1][j].isFlagged()) minesAround++;
		if(i + 1 < boardWidth && j + 1 < boardHeight && board[i + 1][j + 1].isFlagged()) minesAround++;
		// If number of flagged mines is equal to the number there are supposed to be, then clear all non-flagged
		if(minesAround == sender.getNumber() || sender.getNumber() == 0) {
			if(i - 1 >= 0 && j - 1 >= 0 && !board[i - 1][j - 1].isFlagged() && !board[i - 1][j - 1].isRevealed()) board[i - 1][j - 1].setRevealed();
			if(i - 1 >= 0 && !board[i - 1][j].isFlagged() && !board[i - 1][j].isRevealed()) board[i - 1][j].setRevealed();
			if(i - 1 >= 0 && j + 1 < boardHeight && !board[i - 1][j + 1].isFlagged() && !board[i - 1][j + 1].isRevealed()) board[i - 1][j + 1].setRevealed();
			if(j - 1 >= 0 && !board[i][j - 1].isFlagged() && !board[i][j - 1].isRevealed()) board[i][j - 1].setRevealed();
			if(j + 1 < j && !board[i][j + 1].isFlagged() && !board[i][j + 1].isRevealed()) board[i][j + 1].setRevealed();
			if(i + 1 < boardWidth && j - 1 >= 0 && !board[i + 1][j - 1].isFlagged() && !board[i + 1][j - 1].isRevealed()) board[i + 1][j - 1].setRevealed();
			if(i + 1 < boardWidth && !board[i + 1][j].isFlagged() && !board[i + 1][j].isRevealed()) board[i + 1][j].setRevealed();
			if(i + 1 < boardWidth && j + 1 < boardHeight && !board[i + 1][j + 1].isFlagged() && !board[i + 1][j + 1].isRevealed()) board[i + 1][j + 1].setRevealed();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("Start timer");
		if(e.getSource() == timeTimer) {
			// Increment time
			time++;
			timeLabel.setText(String.format("Time: %3d", time));
			
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MinesweeperBoard app = new MinesweeperBoard();
		System.out.println("Constructed");
	}
}
