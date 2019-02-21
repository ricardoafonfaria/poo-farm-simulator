package pt.iul.ista.poo.farm.objects;

import java.util.stream.Collectors;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Movable;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;
import pt.iul.ista.poo.utils.Vector2D;

public class Farmer extends FarmObject implements Movable {

	private static final long serialVersionUID = 1L;

	public Farmer(Point2D a) {
		super(a);
	}

	@Override
	public String getName() {
		Vector2D direction = Farm.getInstance().getDirection();
		return super.getName() + "_" + (direction != null ? direction.toString() : "(0, 1)");
	}

	@Override
	public int getLayer() {
		return 4;
	}
	
	@Override
	public int getPriority() {
		return 4;
	}

	public void move(Point2D pos) {
		if (Direction.getNeighbourhoodPoints(getPosition()).stream()
				.filter(p -> ImageMatrixGUI.getInstance().isWithinBounds(p)
						&& !(Farm.getInstance().searchObject(p) instanceof Movable))
				.collect(Collectors.toList()).contains(pos)) {
			setPosition(pos);
		}
	}
}