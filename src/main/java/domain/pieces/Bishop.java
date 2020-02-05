package domain.pieces;

import domain.board.Position;

public class Bishop extends AbstractPiece {

	private Bishop(boolean isWhite, Position position) {
		super(isWhite, position, PieceTypeEnum.BISHOP, 7);
		addBishopDirections();
	}

	public static Bishop white(Position pos) {
		return new Bishop(true, pos);
	}

	public static Bishop black(Position pos) {
		return new Bishop(false, pos);
	}

	@Override
	public Piece getCopy() {
		Bishop copy = null;
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
