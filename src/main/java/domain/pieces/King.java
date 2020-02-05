package domain.pieces;

import java.util.ArrayList;
import java.util.Collection;

import domain.board.Position;

public class King extends AbstractPiece {

	private final Collection<Position> checkAblePositions = new ArrayList<Position>();

	private King(boolean isWhite, Position position) {
		super(isWhite, position, PieceTypeEnum.KING, 1);
		addTowerDirections();
		addBishopDirections();

	}

	public Collection<Position> getCheckAblePositions() {
		return checkAblePositions;
	}

	public static King white(Position pos) {
		return new King(true, pos);
	}

	public static King black(Position pos) {
		return new King(false, pos);
	}

	@Override
	public Piece getCopy() {
		King copy = null;
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
