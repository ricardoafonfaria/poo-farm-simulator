package pt.iul.ista.poo.farm;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import pt.iul.ista.poo.farm.interfaces.Interactable;
import pt.iul.ista.poo.farm.interfaces.Movable;
import pt.iul.ista.poo.farm.interfaces.Updatable;
import pt.iul.ista.poo.farm.objects.Chicken;
import pt.iul.ista.poo.farm.objects.Egg;
import pt.iul.ista.poo.farm.objects.FarmObject;
import pt.iul.ista.poo.farm.objects.Farmer;
import pt.iul.ista.poo.farm.objects.Land;
import pt.iul.ista.poo.farm.objects.Sheep;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;
import pt.iul.ista.poo.utils.Vector2D;

@SuppressWarnings("deprecation")
public class Farm implements Observer {

	private static final String SAVE_FNAME = "config/savedGame", START_CONFIG = "config/startConfig";

	private final int NUMBER_OF_CHICKENS = 2, NUMBER_OF_SHEEPS = 2;

	private List<FarmObject> objects = new ArrayList<FarmObject>();

	private static final int MIN_X = 5, MIN_Y = 5;

	private int max_x, max_y, lastKey, points = 0;

	private static Farm INSTANCE;

	private Farmer farmer;

	private Vector2D direction;

	private Farm() {
		int max_x = 0;
		int max_y = 0;
		try (Scanner scanner = new Scanner(new File(START_CONFIG))) {
			max_x = scanner.nextInt();
			max_y = scanner.nextInt();
		} catch (FileNotFoundException e) {
			while (max_x == 0 || max_y == 0) {
				String input = (String) JOptionPane.showInputDialog(new Frame(), "Set the farm size!", "Launcher",
						JOptionPane.PLAIN_MESSAGE, null, null, "5 7");
				try (Scanner scanner = new Scanner(input)) {
					max_x = scanner.nextInt();
					max_y = scanner.nextInt();
				} catch (NoSuchElementException e1) {
					e1.printStackTrace();
				}
				try (PrintWriter writer = new PrintWriter(new File(START_CONFIG))) {
					writer.print(max_x + " " + max_y);
					writer.close();
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
			}
		}
		if (max_x < MIN_X || max_y < MIN_Y)
			throw new IllegalArgumentException("Farm size too small!");
		this.max_x = max_x;
		this.max_y = max_y;
		INSTANCE = this;
		ImageMatrixGUI.setSize(max_x, max_y);
		loadScenario();
	}

	public Vector2D getDirection() {
		return direction != null ? direction : null;
	}

	public void awardPoints(int a) {
		points += a;
	}

	public void add(FarmObject a) {
		objects.add(a);
		ImageMatrixGUI.getInstance().addImage(a);
	}

	public void remove(FarmObject a) {
		objects.remove(a);
		ImageMatrixGUI.getInstance().removeImage(a);
	}

	public FarmObject searchObject(Point2D p) {
		return objects.stream().filter(o -> o.getPosition().equals(p))
				.sorted((FarmObject o1, FarmObject o2) -> o2.getPriority() - o1.getPriority()).findFirst().orElse(null);
	}

	public Land searchLand(Point2D p) {
		return (Land) objects.stream().filter(o -> o.getPosition().equals(p) && o instanceof Land).findFirst()
				.orElse(null);
	}

	public boolean isThereAnEggAlready(Point2D p) {
		return !objects.stream().filter(o -> o.getPosition().equals(p)).noneMatch(o -> o instanceof Egg);
	}

	private void updateScoreboard() {
		ImageMatrixGUI.getInstance().setStatusMessage("Points: " + points);
	}

	private void saveGame() {
		int[] stats = { max_x, max_y, points };
		try (FileOutputStream fos = new FileOutputStream(new File(SAVE_FNAME));
				ObjectOutputStream oos = new ObjectOutputStream(fos);) {
			oos.writeObject(stats);
			oos.writeObject(objects);
			JOptionPane.showMessageDialog(new Frame(), "Farm saved!", null, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void restoreGame() {
		List<FarmObject> tempObjects = null;
		int[] tempStats = null;
		try (FileInputStream fis = new FileInputStream(SAVE_FNAME);
				ObjectInputStream ois = new ObjectInputStream(fis);) {
			tempStats = (int[]) ois.readObject();
			tempObjects = (ArrayList<FarmObject>) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (tempStats[0] == max_x && tempStats[1] == max_y) {
			ImageMatrixGUI.getInstance().clearImages();
			objects = tempObjects;
			points = tempStats[2];
			farmer = (Farmer) objects.get(0);
			updateScoreboard();
			registerAll();
		} else
			JOptionPane.showMessageDialog(new Frame(), "Can't import farm! Farms must have the same size!", null,
					JOptionPane.ERROR_MESSAGE);

	}

	private void updateAll() {
		objects.stream().filter(o -> o instanceof Updatable).collect(Collectors.toList())
				.forEach(o -> ((Updatable) o).update());
	}

	private List<ImageTile> toImageTile() {
		List<ImageTile> images = new ArrayList<ImageTile>();
		images.addAll(objects);
		return images;
	}

	private Point2D randomPosition() {
		Random r = new Random();
		Point2D pos = new Point2D(r.nextInt(max_x), r.nextInt(max_y));
		while (searchObject(pos) instanceof Movable)
			pos = new Point2D(r.nextInt(max_x), r.nextInt(max_y));
		return pos;
	}

	private void registerAll() {
		ImageMatrixGUI.getInstance().addImages(toImageTile());
		ImageMatrixGUI.getInstance().update();
	}

	private void loadScenario() {
		objects.add(farmer = new Farmer(new Point2D(max_x / 2, max_y / 2)));
		for (int a = 0; a < NUMBER_OF_SHEEPS; a++)
			objects.add((new Sheep(randomPosition())));
		for (int a = 0; a < NUMBER_OF_CHICKENS; a++)
			objects.add((new Chicken(randomPosition())));
		for (int x = 0; x < max_x; x++) {
			for (int y = 0; y < max_y; y++) {
				Land l = new Land(new Point2D(x, y));
				if (new Random().nextDouble() < 0.1)
					l.setRock();
				objects.add(l);
			}
		}
		registerAll();
	}

	@Override
	public void update(Observable gui, Object a) {
		Point2D pos;
		if (a instanceof Integer) {
			int key = (Integer) a;
			if (Direction.isDirection(key)) {
				direction = Direction.directionFor(key).asVector();
				pos = farmer.getPosition().plus(direction);
				updateAll();
				if (lastKey == KeyEvent.VK_SPACE) {
					if (ImageMatrixGUI.getInstance().isWithinBounds(pos))
						((Interactable) searchObject(pos)).interact();
				} else {
					farmer.move(pos);
				}
				ImageMatrixGUI.getInstance().update();
				updateScoreboard();
			} else if (key == KeyEvent.VK_S)
				saveGame();
			else if (key == KeyEvent.VK_I)
				restoreGame();
			lastKey = key;
		}
	}

	private void play() {
		ImageMatrixGUI.getInstance().addObserver(this);
		ImageMatrixGUI.getInstance().go();
		updateScoreboard();
	}

	public static Farm getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Farm();
		return INSTANCE;
	}

	public static void main(String[] args) {
		Farm f = new Farm();
		f.play();
	}
}