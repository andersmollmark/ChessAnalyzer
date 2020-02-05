package domain.pieces;

import domain.board.Position;

public class Tower extends AbstractPiece {

	private Tower(boolean isWhite, Position position) {
		super(isWhite, position, PieceTypeEnum.TOWER, 7);
		addTowerDirections();

	}

	public static Tower white(Position pos) {
		return new Tower(true, pos);
	}

	public static Tower black(Position pos) {
		return new Tower(false, pos);
	}

	@Override
	public Piece getCopy() {
		Tower copy = null;
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
