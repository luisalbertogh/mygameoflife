/**
 *
 */
package net.luisalbertogh.mygameoflife.domain;

import java.awt.Dimension;

/**
 * The board cell representation.
 *
 * @author lagarcia
 *
 */
public final class Cell {
	/** The x coordinate. */
	private float x;

	/** The y coordinate */
	private float y;

	/** The cell dimension. */
	private Dimension size;

	/** The alive flag. */
	private boolean alive;

	/**
	 * Constructor with arguments.
	 *
	 * @param xArg
	 * @param yArg
	 * @param sizeArg
	 * @param aliveArg
	 */
	public Cell(float xArg, float yArg, Dimension sizeArg, boolean aliveArg) {
		x = xArg;
		y = yArg;
		size = sizeArg;
		alive = aliveArg;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param xArg
	 *            the x to set
	 */
	public void setX(float xArg) {
		x = xArg;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param yArg
	 *            the y to set
	 */
	public void setY(float yArg) {
		y = yArg;
	}

	/**
	 * @return the size
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @param sizeArg
	 *            the size to set
	 */
	public void setSize(Dimension sizeArg) {
		size = sizeArg;
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param aliveArg
	 *            the alive to set
	 */
	public void setAlive(boolean aliveArg) {
		alive = aliveArg;
	}

}
