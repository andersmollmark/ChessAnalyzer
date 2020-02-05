package domain.pieces;

import domain.board.Position;
import domain.movements.Direction;

public class Horse extends AbstractPiece {

	private Horse(boolean isWhite, Position position) {
		super(isWhite, position, PieceTypeEnum.HORSE, 1);

		for (Direction dir : Direction.getAllHorseDirections()) {
			addMoveDirection(dir);
		}
	}

	public static Horse white(Position pos) {
		return new Horse(true, pos);
	}

	public static Horse black(Position pos) {
		return new Horse(false, pos);
	}

	@Override
	public Piece getCopy() {
		Horse copy = null;
		if (isWhite) {
			copy = white(getPosition());
		}
		else {
			copy = black(getPosition());
		}
		copy.setMoveablePositions(getMoveablePositions());
		return copy;
	}

}
