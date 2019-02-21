package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Collectable;
import pt.iul.ista.poo.farm.interfaces.Interactable;
import pt.iul.ista.poo.farm.interfaces.Updatable;
import pt.iul.ista.poo.utils.Point2D;

public abstract class Vegetable extends FarmObject implements Interactable, Updatable, Collectable {

	private static final long serialVersionUID = 1L;

	private boolean grown = false, rotten = false;

	private int time;

	public Vegetable(Point2D p) {
		super(p);
	}

	@Override
	public String getName() {
		return (isRotten() ? "bad_" : (isGrown() ? "" : "small_")) + super.getName();
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void interact() {
		if (isGrown() || isRotten()) {
			collect();
		}
	}

	@Override
	public void update() {
		advanceTime();
		if (getTime() >= getTimeToRot())
			setRotten();
		else if (getTime() >= getTimeToGrow())
			setGrown();
	}

	@Override
	public void collect() {
		if (!isRotten())
			Farm.getInstance().awardPoints(getPointsForCollecting());
		harvest();

	}

	public void harvest() {
		Farm.getInstance().remove(this);
		Farm.getInstance().searchLand(getPosition()).reset();
	}

	protected abstract int getTimeToGrow();

	protected abstract int getTimeToRot();

	protected abstract int getPointsForCollecting();

	protected int getTime() {
		return time;
	}

	protected void advanceTime() {
		this.time++;
	}

	protected boolean isGrown() {
		return grown;
	}

	protected void setGrown() {
		grown = true;
	}

	protected boolean isRotten() {
		return rotten;
	}

	protected void setRotten() {
		rotten = true;
	}

	@Override
	public String toString() {
		return super.toString() + (isGrown() ? "grown:" : "notGrown:") + (isRotten() ? "rot" : "notRot");
	}

}