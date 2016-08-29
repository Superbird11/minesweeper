package def;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A class representing the face label.
 * @author Louis Jacobowitz
 *
 */
public class FaceLabel extends JLabel implements MouseListener {
	/** A default Serial Version ID */
	private static final long serialVersionUID = 1L;
	/** An icon for a happy face */
	public static ImageIcon happyFace;
	/** An icon for a pressed-down happy face */
	public static ImageIcon pressedFace;
	/** An icon for a surprised face */
	public static ImageIcon surprisedFace;
	/** An icon for a dead face */
	public static ImageIcon deadFace;
	/** The board holding this label */
	private MinesweeperBoard board;
	
	/**
	 * Constructs a new FaceLabel and initializes all the possible image icons
	 * @param b - board on which this is
	 */
	public FaceLabel(MinesweeperBoard b) {
		super();
		board = b;
		try {
			happyFace = new ImageIcon(ImageIO.read(new File("src/assets/smileyFace.png")));
			pressedFace = new ImageIcon(ImageIO.read(new File("src/assets/pressedFace.png")));
			surprisedFace = new ImageIcon(ImageIO.read(new File("src/assets/surprisedFace.png")));
			deadFace = new ImageIcon(ImageIO.read(new File("src/assets/deadFace.png")));
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Activates when clicked. Starts a new game.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		board.newGame();
	}

	/**
	 * Activates when the button is pressed. Changes this label's image.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		this.setIcon(pressedFace);
	}

	/**
	 * Activates when the mouse is released. Changes this label's image.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		this.setIcon(happyFace);
	}

	/** Activates when the mouse passes out of this label. Changes its image. */
	@Override
	public void mouseExited(MouseEvent e) {
		this.setIcon(happyFace);
	}

	/** doesn't do a damn thing. */
	@Override
	public void mouseEntered(MouseEvent e) {
		// pass
	}
	
}
