/**
 *
 */
package net.luisalbertogh.mygameoflife.domain;

import java.awt.Dimension;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The board representation.
 *
 * @author lagarcia
 *
 */
public final class Board {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(Board.class);

	/** The board width. */
	private int width;

	/** The board height. */
	private int height;

	/** The cell size. */
	private int cellSize;

	/** The cell board. */
	private Cell[][] theBoard;

	/** The playing flag. */
	private boolean playing;

	/** The number of alive cells. */
	private int aliveCells;

	/** The number of dead cells */
	private int deadCells;

	/** The number of run cycles. */
	private long cycles;

	/**
	 * Constructor with arguments.
	 *
	 * @param widthArg
	 * @param heightArg
	 * @param cellSizeArg
	 */
	public Board(int widthArg, int heightArg, int cellSizeArg) {
		width = widthArg;
		height = heightArg;
		cellSize = cellSizeArg;
		theBoard = new Cell[width][height];
		deadCells = width * height;
	}

	/**
	 * Perform game based on Game of Life rules. The rules are: - Filled cell is
	 * alive if there are 2 or 3 alived next to it, otherwise, it dies - Emtpy
	 * cells create a new life if there are 3 alived next to it.
	 */
	public void play() {
		/* New board with new configuration for the next step */
		Cell[][] newBoard = new Cell[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				/* Original cell */
				Cell thisCell = theBoard[i][j];
				/* Cloned cell */
				Cell newCell = cloneCell(thisCell);
				/* Counted alived neighbours */
				int alivedBours = countNeighbours(i, j);

				/* Alived cell and too few or too many bours */
				if (thisCell.isAlive() && (alivedBours < 2 || alivedBours > 3)) {
					logger.debug("Die!");
					/* Cell is dead in new board */
					newCell.setAlive(false);
					countDeadCell();
				}
				/* Empty cell and enough to reproduce */
				else if (!thisCell.isAlive() && alivedBours == 3) {
					logger.debug("Come to life!");
					/* Cell is live in new board */
					newCell.setAlive(true);
					countAliveCell();
				}

				/* Set new cell in new board */
				newBoard[i][j] = newCell;
			}
		}

		/* Replace old board with new board */
		theBoard = newBoard;

		/* Count cycle */
		cycles++;
	}

	/**
	 * Clone a cell.
	 *
	 * @param original
	 *            - The original cell
	 * @return The cloned cell
	 */
	private Cell cloneCell(Cell original) {
		return new Cell(original.getX(), original.getY(), original.getSize(),
				original.isAlive());
	}

	/**
	 * Count how many cell neighbours are alived around the passed coordinates.
	 *
	 * @param i
	 * @param j
	 * @return The number of alived neighbours cells
	 */
	private int countNeighbours(int i, int j) {
		/*
		 * Neighbours are:
		 * 
		 * i - 1, j - 1 | i, j - 1 | i + 1, j - 1 | i - 1, j | i + 1, j | i - 1,
		 * j + 1 | i, j + 1 | i + 1, j + 1
		 */

		/* Maximum number of neighbours */
		Cell[] neighbours = new Cell[8];
		if ((i - 1) >= 0 && (j - 1) >= 0) {
			neighbours[0] = theBoard[i - 1][j - 1];
		}
		if ((j - 1) >= 0) {
			neighbours[1] = theBoard[i][j - 1];
		}
		if ((i + 1) < width && (j - 1) >= 0) {
			neighbours[2] = theBoard[i + 1][j - 1];
		}
		if ((i - 1) >= 0) {
			neighbours[3] = theBoard[i - 1][j];
		}
		if ((i + 1) < width) {
			neighbours[4] = theBoard[i + 1][j];
		}
		if ((i - 1) >= 0 && (j + 1) < height) {
			neighbours[5] = theBoard[i - 1][j + 1];
		}
		if ((j + 1) < height) {
			neighbours[6] = theBoard[i][j + 1];
		}
		if ((i + 1) < width && (j + 1) < height) {
			neighbours[7] = theBoard[i + 1][j + 1];
		}

		int count = 0;
		for (int k = 0; k < neighbours.length; k++) {
			Cell c = neighbours[k];
			if (c != null && c.isAlive()) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Clean the board. Set all cells to dead.
	 */
	public void cleanBoard() {
		logger.info("Clean board.");
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				theBoard[i][j].setAlive(false);
			}
		}

		aliveCells = 0;
		deadCells = width * height;
		cycles = 0;
	}

	/**
	 * Find cell closer cell to x and y coordinates.
	 *
	 * @param x
	 * @param y
	 * @return The found cell
	 */
	public Cell getCell(float x, float y) {
		int i = (int) x / cellSize;
		int j = (int) y / cellSize;
		if (i < width && j < height && i >= 0 && j >= 0) {
			return theBoard[i][j];
		}

		return null;
	}

	/**
	 * Set up the board.
	 */
	public void initBoard() {
		logger.info("Init the board: width=" + width + "; height=" + height
				+ "; cellSize=" + cellSize);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				theBoard[i][j] = new Cell(i * cellSize, j * cellSize,
						new Dimension(cellSize, cellSize), false);
			}
		}
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param widthArg
	 *            the width to set
	 */
	public void setWidth(int widthArg) {
		width = widthArg;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param heightArg
	 *            the height to set
	 */
	public void setHeight(int heightArg) {
		height = heightArg;
	}

	/**
	 * @return the theBoard
	 */
	public Cell[][] getTheBoard() {
		return theBoard;
	}

	/**
	 * @param theBoardArg
	 *            the theBoard to set
	 */
	public void setTheBoard(Cell[][] theBoardArg) {
		theBoard = theBoardArg;
	}

	/**
	 * @return the cellSize
	 */
	public int getCellSize() {
		return cellSize;
	}

	/**
	 * @param cellSizeArg
	 *            the cellSize to set
	 */
	public void setCellSize(int cellSizeArg) {
		cellSize = cellSizeArg;
	}

	/**
	 * @return the playing
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * @param playingArg
	 *            the playing to set
	 */
	public void setPlaying(boolean playingArg) {
		playing = playingArg;
	}

	/**
	 * @return the aliveCells
	 */
	public int getAliveCells() {
		return aliveCells;
	}

	/**
	 * Count plus one for a new alive cell and substract for a dead cell.
	 */
	public void countAliveCell() {
		aliveCells++;
		if (deadCells > 0) {
			deadCells--;
		}
	}

	/**
	 * @return the deadCells
	 */
	public int getDeadCells() {
		return deadCells;
	}

	/**
	 * Count plus one for a dead cell and substract for alive cell.
	 */
	public void countDeadCell() {
		if (aliveCells > 0) {
			aliveCells--;
		}
		deadCells++;
	}

	/**
	 * @return the cycles
	 */
	public long getCycles() {
		return cycles;
	}
}
