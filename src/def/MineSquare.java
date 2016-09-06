package def;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * A class representing a single tile on a Minesweeper board.
 * @author Louis Jacobowitz
 */
public class MineSquare implements ActionListener{
	/** A serial version ID */
	private static final long serialVersionUID = 1L;
	/** This tile's X coordinate */
	private int xcoord;
	/** This tile's Y coordinate */
	private int ycoord;
	/** The board of which this tile is part */
	private MinesweeperBoard board;
	/** This tile's mechanism for retrieving images */
	private LocalImage icon;
	/** This tile's label */
	private Label label;
	/** Whether or not this tile has been revealed */
	private boolean revealed;
	/** Whether or not this tile is a mine */
	private boolean isMine;
	/** Whether or not this tile has been flagged */
	private boolean flagged;
	/** The number of mines surrounding this tile */
	private int number;
	/** A temporary variable for checking double clicks */
	private boolean clickedOnce;
	/** The timer we'll use for checking double clicks */
	private Timer timer;
	/** Whether or not this tile is interactable */
	private boolean interactable;
	
	/**
	 * Creates and initializes a new MineSquare with the given parameters
	 * @param x - the new MineSquare's X coordinate on the board
	 * @param y - the new MineSquare's Y coordinate on the board
	 * @param theBoard - the board on which this square is situated
	 */
	public MineSquare(int x, int y, MinesweeperBoard theBoard) {
		super();
		/*this.setSize(16, 16);
		this.setMaximumSize(new Dimension(16, 16));
		this.setMinimumSize(new Dimension(16, 16));
		this.setPreferredSize(new Dimension(16, 16));*/
		icon = LocalImage.initialize();
		// create essential components
		xcoord = x;
		ycoord = y;
		board = theBoard;
		revealed = false;
		isMine = false;
		flagged = false;
		interactable = true;
		clickedOnce = false;
		timer = new Timer(300, this);
		// create label
		label = new Label(null, icon.unknown());
		label.setMinSize(16, 16);
		label.setMaxSize(16, 16);
		label.setPrefSize(16, 16);
		ClickHandler click = new ClickHandler();
		ReleaseHandler release = new ReleaseHandler();
		PressedHandler press = new PressedHandler();
		// set mouse events
		label.setOnMouseClicked(click);
		label.setOnMouseExited(release);
		label.setOnMousePressed(press);
		label.setOnMouseDragExited(release);
		//this.addMouseListener(this);
	}
	
	/**
	 * Returns this object's label.
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}
	
	/**
	 * Sets this square's number of mines to the given value
	 * @param n - the number of mines surrounding this square
	 */
	public void setNumber(int n) {
		number = n;
	}
	
	/**
	 * Returns the number of mines surrounding this tile
	 * @return this object's number variable
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Returns true if this tile has been revealed already
	 * @return this object's revealed variable
	 */
	public boolean isRevealed() {
		return revealed;
	}
	
	/**
	 * Sets this tile to be a mine.
	 */
	public void setMine() {
		isMine = true;
	}
	
	/**
	 * Returns true if this tile is a mine, false otherwise
	 * @return the value of the private isMine variable
	 */
	public boolean isMine() {
		return isMine;
	}
	
	/**
	 * Flags this tile, if it has not yet been revealed and is not already flagged
	 */
	public void flag() {
		if(!revealed && !flagged) {
			flagged = true;
			label.setGraphic(icon.flag());
			board.incrementMineCount();
		}
	}
	
	/**
	 * Unflags this tile if it is flagged
	 */
	public void unflag() {
		if(!revealed && !flagged) {
			flagged = false;
			label.setGraphic(icon.unknown());
			board.decrementMineCount();
		}
	}
	
	/**
	 * Returns true if this tile has been flagged.
	 * @return the value of this object's flagged variable
	 */
	public boolean isFlagged() {
		return flagged;
	}
	
	/**
	 * Sets whether or not this square is interactable
	 * @param b - whether this square should be interactable
	 */
	public void setInteractable(boolean b) {
		interactable = b;
	}
	
	/**
	 * Sets this tile to a revealed state, changing the revealed variable and changing its image.
	 * If this tile is a mine, ends the game, because you clicked on a mine
	 */
	public void setRevealed() {
		revealed = true;
		if(isMine) {
			label.setGraphic(icon.activeMine());
			//board.failGame(this);
		}
		else {
			label.setGraphic(icon.known(number));
			if(number == 0) {
				board.clearAllAround(this);
			}
		}
	}
	
	/**
	 * Returns this tile's x coordinate on the board
	 * @return the value of this object's xcoord variable
	 */
	public int getx() {
		return xcoord;
	}
	
	/**
	 * Returns this tile's y coordinate on the board
	 * @return the value of this object's ycoord variable
	 */
	public int gety() {
		return ycoord;
	}
	
	/**
	 * A class for handling click events
	 * @author Louis Jacobowitz
	 */
	private class ClickHandler implements EventHandler<MouseEvent> {
		/**
		 * Registers when this square is clicked, and alerts the board of this fact.
		 * @param e - some mouse event
		 */
		@Override
		public void handle(MouseEvent e) {
			System.out.printf("x:%d, y:%d, n:%d\n", xcoord, ycoord, number);
			if(interactable) {
				if (!revealed && e.getButton() == MouseButton.SECONDARY) {
					if(flagged) unflag();
					else flag();
				}
				else if(e.getButton() == MouseButton.PRIMARY) {
					if(!revealed) {
						board.squareClicked(MineSquare.this);
					}
					else if(!clickedOnce) {
						clickedOnce = true;
						timer.start();
					}
					else {
						// double clicked.
						board.clearAllAround(MineSquare.this);
					}
				}
				else if(revealed && e.getButton() == MouseButton.MIDDLE) {
					board.clearAllAround(MineSquare.this);
				}
			}
		}
	}

	/**
	 * A class for handling mouse pressed events
	 * @author Louis Jacobowitz
	 */
	private class PressedHandler implements EventHandler<MouseEvent> {
		/**
		 * Registers when the mouse pressed down on this square, and changes its image temporarily
		 *  (to be changed back upon the mouseExited event)
		 * @param e - some mouse event
		 */
		@Override
		public void handle(MouseEvent e) {
			if(!revealed && !flagged && interactable) {
				label.setGraphic(icon.known(0));
			}
		}
	}

	/**
	 * A class for handling release events
	 * @author Louis Jacobowitz
	 */
	private class ReleaseHandler implements EventHandler<MouseEvent> {
		/**
		 * Registers when the mouse released on this square but did not press down in it; thus, not a click.
		 *  Changes the image back to what it should be if it would otherwise be changed.
		 * @param e - some mouse event
		 */
		@Override
		public void handle(MouseEvent e) {
			if(!revealed && !flagged) {
				label.setGraphic(icon.unknown());
			}
		}
	}

	/**
	 * Executes if an action was performed on this object. Should ONLY be invoked by a timer, in this implementation.
	 * Sets clickedOnce to be false, because the time to double click was too great.
	 * @param e - triggering event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == timer) {
			clickedOnce = false;
			timer.stop();
		}
	}
	
}
