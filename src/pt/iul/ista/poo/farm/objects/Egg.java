package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Collectable;
import pt.iul.ista.poo.farm.interfaces.Interactable;
import pt.iul.ista.poo.farm.interfaces.Movable;
import pt.iul.ista.poo.farm.interfaces.Updatable;
import pt.iul.ista.poo.utils.Point2D;

public class Egg extends FarmObject implements Interactable, Updatable, Movable, Collectable {

	private static final long serialVersionUID = 1L;

	private final int TIME_TO_HACTH = 20, POINTS_FOR_COLLECTING = 1;

	private int time;

	public Egg(Point2D p) {
		super(p);
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	public void interact() {
		collect();
	}

	@Override
	public void update() {
		time++;
		if (time == TIME_TO_HACTH) {
			move(neighbourAvailablePosition());
		}
	}

	@Override
	public void move(Point2D pos) {
		if (pos != null)
			Farm.getInstance().add(new Chicken(pos));
		Farm.getInstance().remove(this);
	}

	@Override
	public void collect() {
		Farm.getInstance().remove(this);
		Farm.getInstance().awardPoints(POINTS_FOR_COLLECTING);
	}
}