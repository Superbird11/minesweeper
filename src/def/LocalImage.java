package def;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
	private ImageView mine;
	/** An icon for a triggered mine */
	private ImageView activeMine;
	/** An icon for a flagged tile */
	private ImageView flag;
	/** An icon for a mistakenly flagged tile */
	private ImageView notMine;
	/** An array of icons representing clicked, non-mine tiles with varying number values */
	private ImageView[] known;
	/** An icon for a tile that has not been clicked on */
	private ImageView unknown;
	/** The maximum number of mines that can surround a square - eight, on a grid. */
	private static final int MAX_NUM_MINES = 8;
	
	/**
	 * Constructs a new LocalImage, initializing all of the ImageViews to the respective images they represent
	 */
	private LocalImage() {
		try {
			mine = new ImageView(new Image("assets/mine.png", 16, 16, true, false));
			activeMine = new ImageView(new Image("assets/activeMine.png", 16, 16, true, false));
			flag = new ImageView(new Image("assets/flag.png", 16, 16, true, false));
			notMine = new ImageView(new Image("assets/notMine.png", 16, 16, true, false));
			unknown = new ImageView(new Image("/assets/unknown.png", 16, 16, true, false));
			known = new ImageView[MAX_NUM_MINES + 1];
			for(int i = 0; i < MAX_NUM_MINES + 1; i++) {
				known[i] = new ImageView(new Image("assets/known".concat(String.valueOf(i)).concat(".png"), 16, 16, true, false));
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an image of a mine tile (viewed at endgame)
	 * @return an ImageView for an active mine
	 */
	public ImageView mine() {
		return mine;
	}
	
	/**
	 * Returns an image of the mine tile that was clicked on
	 * @return an ImageView for an active mine
	 */
	public ImageView activeMine() {
		return activeMine;
	}
	
	/**
	 * Returns an image of a tile that was flagged
	 * @return an ImageView for a flagged tile
	 */
	public ImageView flag() {
		return flag;
	}
	
	/**
	 * Returns an image of a tile that was flagged but ended up actually not being a mine
	 * @return an ImageView for a mistakenly flagged tile
	 */
	public ImageView notMine() {
		return notMine;
	}
	
	/**
	 * Returns an image of an unclicked tile
	 * @return an ImageView for an unclicked tile
	 */
	public ImageView unknown() {
		return unknown;
	}
	
	/**
	 * Returns an ImageView corresponding to the known square with the given number of surrounding mines
	 * @param num - number of surrounding mines
	 * @return an image to represent that tile
	 */
	public ImageView known(int num) {
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
