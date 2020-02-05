package domain.pieces;

import domain.board.Position;

public class Queen extends AbstractPiece {

	private Queen(boolean isWhite, Position position) {
		super(isWhite, position, PieceTypeEnum.QUEEN, 7);
		addTowerDirections();
		addBishopDirections();
	}

	public static Queen white(Position pos) {
		return new Queen(true, pos);
	}

	public static Queen black(Position pos) {
		return new Queen(false, pos);
	}

	@Override
	public Piece getCopy() {
		Queen copy = null;
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
