package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.utils.Point2D;

public class Sheep extends Animal {

	private static final long serialVersionUID = 1L;

	private final int TIME_TO_STARVE = 10, TIME_TO_FAMISH = 20, POINTS_FOR_HEALTHY = 1;

	private boolean famished = false;

	protected void setFamished() {
		famished = true;
	}

	public Sheep(Point2D p) {
		super(p);
	}

	@Override
	public String getName() {
		return (famished ? "famished_" : "") + super.getName();
	}

	@Override
	public void interact() {
		getFed();
	}

	@Override
	public void update() {
		super.update();
		if (getTime() > TIME_TO_FAMISH)
			famished = true;
		else if (getTime() > TIME_TO_STARVE) {
			cropInteract();
		} else
			Farm.getInstance().awardPoints(POINTS_FOR_HEALTHY);
	}

	@Override
	protected void eat(FarmObject vegetable) {
		super.eat(vegetable);
		getFed();
	}

	private void getFed() {
		famished = false;
		resetTime();
	}
}