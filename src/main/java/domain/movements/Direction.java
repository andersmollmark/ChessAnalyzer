package domain.movements;

import java.util.ArrayList;
import java.util.Collection;

public class Direction {

	private final int x;
	private final int y;
	private boolean strikeDirection = true;
	private boolean moveDirection = true;

	private static Collection<Direction> directions = new ArrayList<Direction>();

	private static Collection<Direction> horseDirections = new ArrayList<Direction>();

	static {
		for (DirectionValueEnum val : DirectionValueEnum.values()) {
			directions.add(new Direction(val));
		}

		for (HorseDirectionValueEnum val : HorseDirectionValueEnum.values()) {
			horseDirections.add(new Direction(val));
		}

	}

	private Direction(HorseDirectionValueEnum val) {
		x = val.getX();
		y = val.getY();
	}

	private Direction(DirectionValueEnum dirEnum) {
		x = dirEnum.getX();
		y = dirEnum.getY();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static Collection<Direction> getAllHorseDirections() {
		return horseDirections;
	}

	public static Direction getDirection(DirectionValueEnum valEnum) {
		return new Direction(valEnum);
	}

	public static Direction getOppositeDirection(Direction d) {
		Direction opposite = getDirection(d.getX() * -1, d.getY() * -1);
		return opposite;
	}

	private static Direction getDirection(int xDir, int yDir) {
		for (Direction dir : directions) {
			if (dir.getX() == xDir && dir.getY() == yDir) {
				return dir;
			}
		}
		throw new IllegalArgumentException("Felaktigt x eller y : x=" + xDir + " y=" + yDir);
	}

	public boolean isStrikeDirection() {
		return strikeDirection;
	}

	public void setStrikeDirection(boolean strikeDirection) {
		this.strikeDirection = strikeDirection;
	}

	public boolean isMoveDirection() {
		return moveDirection;
	}

	public void setMoveDirection(boolean moveDirection) {
		this.moveDirection = moveDirection;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Direction) {
			Direction tempdir = (Direction) o;
			if (tempdir.getX() == getX() && tempdir.getY() == getY()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Direction x:" + getX() + " y:" + getY();
	}
}
