package pt.iul.ista.poo.farm.objects;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.farm.interfaces.Movable;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public abstract class FarmObject implements ImageTile, Serializable {

	private static final long serialVersionUID = 1L;

	private Point2D position;

	public FarmObject(Point2D p) {
		this.position = p;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName().toLowerCase();
	}

	public abstract int getPriority();

	@Override
	public Point2D getPosition() {
		return position;
	}

	protected void setPosition(Point2D position) {
		this.position = position;
	}

	protected Point2D neighbourAvailablePosition() {
		List<Point2D> positions = Direction.getNeighbourhoodPoints(getPosition()).stream()
				.filter(p -> ImageMatrixGUI.getInstance().isWithinBounds(p)
						&& !(Farm.getInstance().searchObject(p) instanceof Movable))
				.collect(Collectors.toList());
		return positions.isEmpty() ? null : positions.get(new Random().nextInt(positions.size()));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getPosition().toString() + ":";
	}

}