package domain.pieces;

import domain.board.Position;
import domain.movements.Direction;
import domain.movements.DirectionValueEnum;

public class Pawn extends AbstractPiece {

	private Pawn(boolean isWhite, Position position) {
		super(isWhite, position, PieceTypeEnum.PAWN, 1);

		if (isWhite) {
			setWhiteMovements();
		}
		else {
			setBlackMovements();
		}

	}

	private void setWhiteMovements() {
		Direction justMoveDirection = Direction.getDirection(DirectionValueEnum.PLUS_Y);
		Direction strikeDirection1 = Direction.getDirection(DirectionValueEnum.PLUS_X_PLUS_Y);
		Direction strikeDirection2 = Direction.getDirection(DirectionValueEnum.MINUS_X_PLUS_Y);
		justMoveDirection.setStrikeDirection(false);
		strikeDirection1.setMoveDirection(false);
		strikeDirection2.setMoveDirection(false);
		addMoveDirection(justMoveDirection);
		addMoveDirection(strikeDirection1);
		addMoveDirection(strikeDirection2);
	}

	private void setBlackMovements() {
		Direction justMoveDirection = Direction.getDirection(DirectionValueEnum.MINUS_Y);
		Direction strikeDirection1 = Direction.getDirection(DirectionValueEnum.MINUS_X_MINUS_Y);
		Direction strikeDirection2 = Direction.getDirection(DirectionValueEnum.PLUS_X_MINUS_Y);
		justMoveDirection.setStrikeDirection(false);
		strikeDirection1.setMoveDirection(false);
		strikeDirection2.setMoveDirection(false);
		addMoveDirection(justMoveDirection);
		addMoveDirection(strikeDirection1);
		addMoveDirection(strikeDirection2);
	}

	public static Pawn white(Position pos) {
		return new Pawn(true, pos);
	}

	public static Pawn black(Position pos) {
		return new Pawn(false, pos);
	}

	@Override
	public Piece getCopy() {
		Pawn copy = null;
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
