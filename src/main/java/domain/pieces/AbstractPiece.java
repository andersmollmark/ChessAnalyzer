package domain.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.board.Position;
import domain.movements.Direction;
import domain.movements.DirectionValueEnum;

public abstract class AbstractPiece implements Piece {

	private final int maxNumberOfMoves;

	protected PieceTypeEnum thisType;

	private Long id;

	// if the piece is white or not
	protected boolean isWhite;

	private List<Position> moveablePositions = new ArrayList<Position>();

	private final List<Direction> moveDirections = new ArrayList<Direction>();

	// position on the board
	private Position position;

	// points that the piece is worth
	private int points;

	private int drawMoves = 0;

	public AbstractPiece(boolean pIsWhite, Position position, PieceTypeEnum type, int maxMoves) {
		isWhite = pIsWhite;
		moveTo(position);
		thisType = type;
		Random rnd = new Random();
		id = rnd.nextLong();
		maxNumberOfMoves = maxMoves;
	}

	public void addDrawMove(int drawMove) {
		drawMoves++;
	}

	public void removeDrawMove() {
		drawMoves--;
	}

	@Override
	public boolean hasMoved(int drawMove) {
		return drawMoves == drawMove;
	}

	@Override
	public int getMaxNumberOfMoves() {
		return maxNumberOfMoves;
	}

	@Override
	public boolean isWhite() {
		return isWhite;
	}

	@Override
	public boolean isBlack() {
		return !isWhite;
	}

	@Override
	public List<Position> getMoveablePositions() {
		return moveablePositions;
	}

	@Override
	public boolean hasMoveablePosition(Position pos) {
		int index = ((ArrayList<Position>) moveablePositions).indexOf(pos);
		return index != -1 ? true : false;
	}

	@Override
	public Position removeMoveablePosition(Position pos) {
		int index = moveablePositions.indexOf(pos);
		if (index != -1) {
			return moveablePositions.remove(index);
		}
		return null;
	}

	protected void setMoveablePositions(List<Position> moveablePositions) {
		this.moveablePositions = moveablePositions;
	}

	@Override
	public List<Direction> getMoveDirections() {
		return moveDirections;
	}

	public void addMoveDirection(Direction d) {
		getMoveDirections().add(d);
	}

	@Override
	public void addMoveablePosition(Position p) {
		getMoveablePositions().add(p);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void moveTo(Position position) {
		this.position = position;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public PieceTypeEnum getThisType() {
		return thisType;
	}

	public void fillCopyWithMoveables(Piece copy) {
		for (Position pos : getMoveablePositions()) {
			copy.addMoveablePosition(pos);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addTowerDirections() {
		addMoveDirection(Direction.getDirection(DirectionValueEnum.PLUS_Y));
		addMoveDirection(Direction.getDirection(DirectionValueEnum.MINUS_Y));
		addMoveDirection(Direction.getDirection(DirectionValueEnum.PLUS_X));
		addMoveDirection(Direction.getDirection(DirectionValueEnum.MINUS_X));
	}

	public void addBishopDirections() {
		addMoveDirection(Direction.getDirection(DirectionValueEnum.MINUS_X_MINUS_Y));
		addMoveDirection(Direction.getDirection(DirectionValueEnum.MINUS_X_PLUS_Y));
		addMoveDirection(Direction.getDirection(DirectionValueEnum.PLUS_X_MINUS_Y));
		addMoveDirection(Direction.getDirection(DirectionValueEnum.PLUS_X_PLUS_Y));
	}

	@Override
	public boolean isOppositeColor(Piece otherPiece) {
		return isWhite() == !otherPiece.isWhite();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Piece)) {
			return false;
		}
		AbstractPiece temp = (AbstractPiece) o;
		return temp.getId() == getId();
	}

	@Override
	public String toString() {
		String retur = (isWhite ? "Vit " : "Svart ") + " " + thisType.getTyp();
		retur = retur + " " + getPosition().toString();
		return retur;
	}
}
