package def;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * A class representing the face label.
 * @author Louis Jacobowitz
 *
 */
public class FaceLabel extends Label {
	/** A default Serial Version ID */
	private static final long serialVersionUID = 1L;
	/** An icon for a happy face */
	public static ImageView happyFace;
	/** An icon for a pressed-down happy face */
	public static ImageView pressedFace;
	/** An icon for a surprised face */
	public static ImageView surprisedFace;
	/** An icon for a dead face */
	public static ImageView deadFace;
	/** The board holding this label */
	private MinesweeperBoard board;
	
	/**
	 * Constructs a new FaceLabel and initializes all the possible image icons
	 * @param b - board on which this is
	 */
	public FaceLabel(MinesweeperBoard b) {
		super();
		board = b;
		happyFace = new ImageView(new Image("assets/smileyFace.png", 32, 32, true, false));
		pressedFace = new ImageView(new Image("assets/pressedFace.png", 32, 32, true, false));
		surprisedFace = new ImageView(new Image("assets/surprisedFace.png", 32, 32, true, false));
		deadFace = new ImageView(new Image("assets/deadFace.png", 32, 32, true, false));
		ClickHandler click = new ClickHandler();
		PressedHandler press = new PressedHandler();
		ReleasedHandler release = new ReleasedHandler();
		this.setOnMouseClicked(click);
		this.setOnMousePressed(press);
		this.setOnMouseReleased(release);
		this.setOnMouseDragExited(release);
	}

	private class ClickHandler implements EventHandler<MouseEvent> {
		/**
		 * Activates when clicked. Starts a new game.
		 */
		@Override
		public void handle(MouseEvent e) {
			board.newGame();
		}
	}

	private class PressedHandler implements EventHandler<MouseEvent> {
		/**
		 * Activates when the button is pressed. Changes this label's image.
		 */
		@Override
		public void handle(MouseEvent e) {
			FaceLabel.this.setGraphic(pressedFace);
		}
	}

	private class ReleasedHandler implements EventHandler<MouseEvent> {
		/**
		 * Activates when the mouse is released. Changes this label's image.
		 */
		@Override
		public void handle(MouseEvent e) {
			FaceLabel.this.setGraphic(happyFace);
		}
	}
}
