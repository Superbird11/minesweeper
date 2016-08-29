package def;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * A class to hold all instances of images, to diminish the time it would take to load them
 * individually. It is more efficient to access them all from here, preloaded. A singleton
 * architecture is used for the task.
 * @author Louis Jacobowitz
 */
public class LocalImage {
	/** The singleton instance of this class */
	static LocalImage singleton = null;
	/** An icon for a mine */
	private ImageIcon mine;
	/** An icon for a triggered mine */
	private ImageIcon activeMine;
	/** An icon for a flagged tile */
	private ImageIcon flag;
	/** An icon for a mistakenly flagged tile */
	private ImageIcon notMine;
	/** An array of icons representing clicked, non-mine tiles with varying number values */
	private ImageIcon[] known;
	/** An icon for a tile that has not been clicked on */
	private ImageIcon unknown;
	/** The maximum number of mines that can surround a square - eight, on a grid. */
	private static final int MAX_NUM_MINES = 8;
	
	/**
	 * Constructs a new LocalImage, initializing all of the ImageIcons to the respective images they represent
	 */
	private LocalImage() {
		try {
			mine = new ImageIcon(ImageIO.read(new File("src/assets/mine.png")));
			activeMine = new ImageIcon(ImageIO.read(new File("src/assets/activeMine.png")));
			flag = new ImageIcon(ImageIO.read(new File("src/assets/flag.png")));
			notMine = new ImageIcon(ImageIO.read(new File("src/assets/notMine.png")));
			unknown = new ImageIcon(ImageIO.read(new File("src/assets/unknown.png")));
			known = new ImageIcon[MAX_NUM_MINES + 1];
			for(int i = 0; i < MAX_NUM_MINES + 1; i++) {
				known[i] = new ImageIcon(ImageIO.read(new File("src/assets/known".concat(String.valueOf(i)).concat(".png"))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an image of a mine tile (viewed at endgame)
	 * @return an ImageIcon for an active mine
	 */
	public ImageIcon mine() {
		return mine;
	}
	
	/**
	 * Returns an image of the mine tile that was clicked on
	 * @return an ImageIcon for an active mine
	 */
	public ImageIcon activeMine() {
		return activeMine;
	}
	
	/**
	 * Returns an image of a tile that was flagged
	 * @return an ImageIcon for a flagged tile
	 */
	public ImageIcon flag() {
		return flag;
	}
	
	/**
	 * Returns an image of a tile that was flagged but ended up actually not being a mine
	 * @return an ImageIcon for a mistakenly flagged tile
	 */
	public ImageIcon notMine() {
		return notMine;
	}
	
	/**
	 * Returns an image of an unclicked tile
	 * @return an imageIcon for an unclicked tile
	 */
	public ImageIcon unknown() {
		return unknown;
	}
	
	/**
	 * Returns an ImageIcon corresponding to the known square with the given number of surrounding mines
	 * @param num - number of surrounding mines
	 * @return an image to represent that tile
	 */
	public ImageIcon known(int num) {
		return known[num];
	}
	
	/**
	 * Returns the existing singleton instance of LocalImage if it exists, or creates one if it doesn't
	 * @return the singleton instance of LocalImage
	 */
	public static LocalImage initialize() {
		if(singleton == null) {
			singleton = new LocalImage();
		}
		return singleton;
	}
}
