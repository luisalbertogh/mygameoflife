/**
 *
 */
package net.luisalbertogh.mygameoflife;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import net.luisalbertogh.mygameoflife.domain.Board;
import net.luisalbertogh.mygameoflife.domain.Cell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Main class.
 *
 * @author lagarcia
 *
 */
public class MyGameOfLife extends PApplet {

	/** The logger. */
	private static final Logger logger = LogManager
			.getLogger(MyGameOfLife.class);

	/** The configuration properties . */
	private static final Properties config = new Properties();

	/** The game board. */
	private Board theBoard;

	/** The width in pixels. */
	private static int widthInPixels;

	/** The height in pixels. */
	private static int heightInPixels;

	/** The cell size in pixels. */
	private static int cellSize;

	/** The GUI font. */
	private static PFont font;

	/** Top margin in pixels. */
	private static final int TOP_MARGIN = 50;

	/** Bottom margin in pixels. */
	private static final int BOTTOM_MARGIN = 50;

	/** Side margins in pixels. */
	private static final int SIDE_MARGIN = 20;

	/** Font size. */
	private static final int FONT_SIZE = 12;

	/** Font size. */
	private static final int FONT_HEADER = 18;

	/** Black color. */
	private static final int BLACK = 0;

	/** White color. */
	private static final int WHITE = 255;

	/** Light gray color. */
	private static final int LIGHT_GRAY = 200;

	/** Buttons width. */
	private static final int BUTTON_WIDTH = 50;

	/** Buttons height. */
	private static final int BUTTON_HEIGHT = 20;

	/** Delay in millis. */
	private static int delayTime;

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Starting the game of life...");

		/* Launch applet */
		PApplet.main(new String[] { "--present",
				"net.luisalbertogh.mygameoflife.MyGameOfLife" });
	}

	/**
	 * The set-up method.
	 */
	@Override
	public void setup() {
		/* Load font */
		font = createFont("Arial", FONT_SIZE, true);

		/* Init the board */
		theBoard = new Board(Integer.parseInt(config
				.getProperty("panel.hcells")), Integer.parseInt(config
				.getProperty("panel.vcells")), Integer.parseInt(config
				.getProperty("panel.cell")));
		theBoard.initBoard();
	}

	/**
	 * The draw method.
	 */
	@Override
	public void draw() {
		background(WHITE);

		/* GUI controls */
		drawControls();

		/* Draw every cell from the board */
		for (int i = 0; i < theBoard.getWidth(); i++) {
			for (int j = 0; j < theBoard.getHeight(); j++) {
				stroke(BLACK);
				Cell c = theBoard.getTheBoard()[i][j];
				/* If active, color in black */
				if (c.isAlive()) {
					fill(BLACK);
				} else {
					fill(WHITE);
				}
				/* Add top margin (50) */
				rect(c.getX() + SIDE_MARGIN, c.getY() + TOP_MARGIN,
						theBoard.getCellSize(), theBoard.getCellSize());
			}
		}

		/* Play game after setting board, if game is active */
		if (theBoard.isPlaying()) {
			theBoard.play();
			/* Sleep time */
			delay(delayTime);
		} else {
			/* Minimum delay under no-play operations */
			delay(200);
		}
	}

	/**
	 * Draw button controls.
	 */
	public void drawControls() {
		/* Information */
		drawInfo();

		/* Top legend */
		textFont(font, FONT_HEADER);
		fill(BLACK);
		text("Game 4 Life - (C) 2016 luisalbertogh.net", widthInPixels - 350,
				25);

		/* Start - stop button */
		stroke(BLACK);
		fill(LIGHT_GRAY);
		rect(widthInPixels / 2 + (SIDE_MARGIN) - 10, heightInPixels
				+ TOP_MARGIN + 10, BUTTON_WIDTH, BUTTON_HEIGHT, 7);

		/* Text */
		textFont(font, FONT_SIZE);
		fill(BLACK);
		String text = "Start";
		if (theBoard.isPlaying()) {
			text = "Stop";
		}
		text(text, widthInPixels / 2 + (SIDE_MARGIN) + 3, heightInPixels
				+ TOP_MARGIN + 25);

		/* Clean button */
		stroke(BLACK);
		fill(LIGHT_GRAY);
		rect(widthInPixels / 2 + (SIDE_MARGIN) - 70, heightInPixels
				+ TOP_MARGIN + 10, BUTTON_WIDTH, BUTTON_HEIGHT, 7);
		fill(BLACK);
		text("Clean", widthInPixels / 2 + (SIDE_MARGIN) - 60, heightInPixels
				+ TOP_MARGIN + 25);
	}

	/**
	 * Draw game of life information.
	 */
	public void drawInfo() {
		/* Some information... */
		textFont(font, FONT_SIZE);
		fill(BLACK);
		text("Cycle: " + theBoard.getCycles(), 20, 25);
		fill(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue());
		text("Alive cells: " + theBoard.getAliveCells(), 100, 25);
		fill(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue());
		text("Dead cells: " + theBoard.getDeadCells(), 180, 25);
	}

	/**
	 * On mouse clicked event.
	 */
	@Override
	public void mouseClicked() {
		handleActions();
	}

	/**
	 * On mouse pressed event.
	 */
	@Override
	public void mouseDragged() {
		handleActions();
	}

	/**
	 * Handle user interface events.
	 */
	private void handleActions() {
		/* Click on start/stop button */
		if (isActionClicked()) {
			/* Stop action */
			if (theBoard.isPlaying()) {
				logger.info("Stop!");
				theBoard.setPlaying(false);
			}
			/* Start action */
			else {
				logger.info("Start!");
				theBoard.setPlaying(true);
			}
		}
		/* Click on clean button */
		else if (isCleanClicked()) {
			/* Stop playing first */
			theBoard.setPlaying(false);
			/* Clean board */
			theBoard.cleanBoard();
		}
		/* Board probably clicked */
		else {
			Cell clicked = theBoard.getCell(mouseX - SIDE_MARGIN, mouseY
					- TOP_MARGIN);
			if (clicked != null) {
				/* Set alive */
				if (!clicked.isAlive()) {
					logger.info("Set alived on " + mouseX + ", " + mouseY);
					clicked.setAlive(true);
					theBoard.countAliveCell();
				}
				/* Set dead */
				else {
					logger.info("Set dead on " + mouseX + ", " + mouseY);
					clicked.setAlive(false);
					theBoard.countDeadCell();
				}
			}
		}
	}

	/**
	 * Check if Start/Stop button is clicked.
	 *
	 * @return True if clicked, otherwise false
	 */
	private boolean isActionClicked() {
		if ((mouseX > widthInPixels / 2 + (SIDE_MARGIN) - 10)
				&& (mouseX < widthInPixels / 2 + (SIDE_MARGIN) - 10
						+ BUTTON_WIDTH)) {
			if ((mouseY > heightInPixels + TOP_MARGIN + 10)
					&& (mouseY < heightInPixels + TOP_MARGIN + 10
							+ BUTTON_HEIGHT)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if clean button is clicked.
	 *
	 * @return True if clicked, otherwise false
	 */
	private boolean isCleanClicked() {
		if ((mouseX > widthInPixels / 2 + (SIDE_MARGIN) - 70)
				&& (mouseX < widthInPixels / 2 + (SIDE_MARGIN) - 70
						+ BUTTON_WIDTH)) {
			if ((mouseY > heightInPixels + TOP_MARGIN + 10)
					&& (mouseY < heightInPixels + TOP_MARGIN + 10
							+ BUTTON_HEIGHT)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * The settings method.
	 */
	@Override
	public void settings() {
		/* Load configuration */
		loadConfig();
		cellSize = Integer.parseInt(config.getProperty("panel.cell"));
		widthInPixels = Integer.parseInt(config.getProperty("panel.hcells"))
				* cellSize;
		heightInPixels = Integer.parseInt(config.getProperty("panel.vcells"))
				* cellSize;
		delayTime = Integer.parseInt(config.getProperty("delay.millis"));

		/* Set canvas size with top and bottom margins */
		size(widthInPixels + SIDE_MARGIN * 2, heightInPixels + TOP_MARGIN
				+ BOTTOM_MARGIN);
	}

	/**
	 * Load configuration properties.
	 */
	private void loadConfig() {
		logger.info("Loading configuration...");
		FileInputStream is = null;
		try {
			is = new FileInputStream("config.properties");
			config.load(is);
		} catch (FileNotFoundException ex) {
			logger.error(ex);
		} catch (IOException ex) {
			logger.error(ex);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}
}
