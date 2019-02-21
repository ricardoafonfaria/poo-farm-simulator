package pt.iul.ista.poo.farm.objects;

import java.util.Random;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Interactable;
import pt.iul.ista.poo.utils.Point2D;

public class Land extends FarmObject implements Interactable {

	private static final long serialVersionUID = 1L;

	private boolean plowed, cultivated, rock;

	public Land(Point2D p) {
		super(p);
		this.plowed = false;
		this.cultivated = false;
		this.rock = false;
	}

	@Override
	public String getName() {
		return rock ? "rock" : (plowed ? "plowed" : super.getName());
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void interact() {
		if (!rock)
			if (!plowed) {
				plowed = true;
			} else if (!cultivated) {
				plant();
				cultivated = true;
			}
	}

	public void setRock() {
		rock = true;
	}

	protected void reset() {
		plowed = false;
		cultivated = false;
	}

	private void plant() {
		Point2D pos = getPosition();
		if (new Random().nextBoolean())
			Farm.getInstance().add(new Tomato(pos));
		else
			Farm.getInstance().add(new Cabbage(pos));
	}
}