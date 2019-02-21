package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Collectable;
import pt.iul.ista.poo.utils.Point2D;

public class Chicken extends Animal implements Collectable {

	private static final long serialVersionUID = 1L;

	private final int TIME_TO_LAYEGG = 10, POINTS_FOR_COLLECTING = 2;

	public Chicken(Point2D p) {
		super(p);
	}

	@Override
	public String getName() {
		return super.getName() + "_" + (getDirection() != null ? getDirection().toString() : "(0, -1)");
	}

	@Override
	public void interact() {
		collect();
	}

	@Override
	public void update() {
		super.update();
		if (getTime() % 2 == 0)
			cropInteract();
		if (getTime() == TIME_TO_LAYEGG) {
			if (!Farm.getInstance().isThereAnEggAlready(getPosition()))
				Farm.getInstance().add(new Egg(getPosition()));
			resetTime();
		}
	}

	@Override
	public void collect() {
		Farm.getInstance().remove(this);
		Farm.getInstance().awardPoints(POINTS_FOR_COLLECTING);
	}

	@Override
	protected boolean edible(FarmObject object) {
		return object instanceof Tomato ? true : false;
	}

	
}