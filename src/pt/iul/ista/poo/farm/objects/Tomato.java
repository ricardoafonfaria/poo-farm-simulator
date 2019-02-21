package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.utils.Point2D;

/**
 * Represents a Tomato
 * 
 * @author Ricardo Faria
 * @version 1.1
 **/

public class Tomato extends Vegetable {

	private static final long serialVersionUID = 1L;

	/**
	 * Time needed to the Tomato grow.
	 **/
	private final int TIME_TO_GROW = 15;

	/**
	 * Time needed to the Tomato rot.
	 **/
	private final int TIME_TO_ROT = 25;

	/**
	 * Points awarded for collecting a Tomato.
	 **/
	private final int POINTS_FOR_COLLECTING = 3;

	/**
	 * State of the Tomato, true if treated, otherwise false.
	 **/
	private boolean treated;

	/**
	 * Creates a new Tomato
	 * 
	 * @param pos
	 *            position
	 * @throws IllegalArgumentException
	 *             if null position
	 **/
	public Tomato(Point2D pos) {
		super(pos);
		if (pos != null) {
			treated = false;
		} else
			throw new IllegalArgumentException();
	}

	/**
	 * Interacts with the Tomato accordingly with his state. If the tomato has not
	 * yet been treated, treats it, then, does the usual vegetable interaction.
	 **/
	@Override
	public void interact() {
		if (!treated)
			treated = true;
		super.interact();
	}

	/**
	 * Updates the Tomato state accordingly with his lifespan and treated state.
	 **/
	@Override
	public void update() {
		if (treated)
			super.update();
		else {
			advanceTime();
			if (getTime() >= getTimeToRot())
				setRotten();
		}
	}

	/**
	 * Returns the time needed to the Tomato grow.
	 * 
	 * @return The time needed to the Tomato grow.
	 **/
	@Override
	protected int getTimeToGrow() {
		return TIME_TO_GROW;
	}

	/**
	 * Returns the time needed to the Tomato rot.
	 * 
	 * @return The time needed to the Tomato rot.
	 **/
	@Override
	protected int getTimeToRot() {
		return TIME_TO_ROT;
	}

	/**
	 * Returns the points awarded for collecting a Tomato.
	 * 
	 * @return The points awarded for collecting a Tomato.
	 **/
	@Override
	protected int getPointsForCollecting() {
		return POINTS_FOR_COLLECTING;
	}
}