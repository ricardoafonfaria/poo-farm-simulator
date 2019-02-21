package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.utils.Point2D;

public class Cabbage extends Vegetable {

	private static final long serialVersionUID = 1L;

	private final int TIME_TO_GROW = 10;
	private final int TIME_TO_ROT = 20;
	private final int POINTS_FOR_COLLECTING = 2;

	public Cabbage(Point2D p) {
		super(p);
	}

	@Override
	public void interact() {
		advanceTime();
		super.interact();
	}

	@Override
	protected int getTimeToGrow() {
		return TIME_TO_GROW;
	}

	@Override
	protected int getTimeToRot() {
		return TIME_TO_ROT;
	}

	@Override
	protected int getPointsForCollecting() {
		return POINTS_FOR_COLLECTING;
	}
}