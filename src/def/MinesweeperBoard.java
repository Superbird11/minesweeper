package def;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;

import java.util.Random;

/**
 * A class representing the board on which Minesweeper is played. Generally controls the game.
 * @author Louis Jacobowitz
 *
 */
public class MinesweeperBoard extends Application implements ActionListener {
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
	/** The biggest panel, the top panel */
	private VBox root;
	/** The top panel */
	private BorderPane topPanel;
	/** The bottom panel, in which the mines are placed */
	private GridPane bottomPanel;
	/** The time in the game so far */
	private int time;
	/** A timer for counting up the time */
	private Timer timeTimer;
	/** The label that displays the time */
	private Label timeLabel;
	/** The label that the mine counter uses */
	private Label mineLabel;
	/** The face label */
	private FaceLabel faceLabel;
	
	/**
	 * Creates a new MinesweeperBoard, initializing the display and calling newGame().
	 */
	public MinesweeperBoard() {
		
		
	}
	
	/**
	 * Starts a new game, initializing the board and the mines.
	 */
	public void newGame() {
		bottomPanel.getChildren().clear();
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
				bottomPanel.getChildren().add(board[i][j].getLabel());
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
					board[i][j].getLabel().setGraphic(icon.mine());
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
		launch(args);
		//MinesweeperBoard app = new MinesweeperBoard();
		System.out.println("Constructed");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// First set title
		primaryStage.setTitle(APP_NAME);
		// Set up main container
		root = new VBox();
		topPanel = new BorderPane();
		bottomPanel = new GridPane();
		root.getChildren().addAll(topPanel, bottomPanel);
		// Set default boardHeight and boardWidth
		boardHeight = 10;//16;
		boardWidth = 19;//30;
		numMines = 10;//99;
		
		// Initialize icon
		icon = LocalImage.initialize();
		
		// Put things in the top panel! Start with initializing the timer.
		timeLabel = new Label("Time: 000");
		timeTimer = new Timer(1000, this);
		timeTimer.start();
		topPanel.setRight(timeLabel);
		// Next, initialize the mine label
		currentNumMines = numMines;
		mineLabel = new Label(String.format("Mines: %3d", currentNumMines));
		topPanel.setLeft(mineLabel);
		// Finally, add the face label
		faceLabel = new FaceLabel(this);
		topPanel.setCenter(faceLabel);
		
		newGame();
		primaryStage.setScene(new Scene(root, 500, 500));
		System.out.println("Finished constructor");
		
	}
}
