package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Movable;
import pt.iul.ista.poo.farm.interfaces.Interactable;
import pt.iul.ista.poo.farm.interfaces.Updatable;
import pt.iul.ista.poo.utils.Point2D;
import pt.iul.ista.poo.utils.Vector2D;

public abstract class Animal extends FarmObject implements Interactable, Updatable, Movable {

	private static final long serialVersionUID = 1L;

	private Vector2D direction;

	private int time;

	public Animal(Point2D p) {
		super(p);
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void update() {
		time++;
	}

	@Override
	public void move(Point2D pos) {
		direction = Vector2D.movementVector(getPosition(), pos);
		setPosition(pos);
	}

	protected void cropInteract() {
		Point2D pos = neighbourAvailablePosition();
		if (pos != null) {
			FarmObject object = Farm.getInstance().searchObject(pos);
			if (edible(object))
				eat(object);
			else {
				move(pos);
			}
		}
	}

	protected boolean edible(FarmObject object) {
		return object instanceof Vegetable ? true : false;
	}

	protected void eat(FarmObject object) {
		((Vegetable) object).harvest();
	};

	protected Vector2D getDirection() {
		return direction;
	}

	protected int getTime() {
		return time;
	}

	protected void resetTime() {
		this.time = 0;
	}
}