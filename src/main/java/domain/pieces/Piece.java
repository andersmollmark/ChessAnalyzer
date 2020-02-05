package domain.pieces;

import java.util.List;

import domain.board.Position;
import domain.movements.Direction;

public interface Piece {

	public Piece getCopy();

	public void addMoveablePosition(Position p);

	public List<Direction> getMoveDirections();

	public Position getPosition();

	public int getMaxNumberOfMoves();

	public boolean isWhite();

	public boolean isBlack();

	public List<Position> getMoveablePositions();

	public boolean hasMoved(int drawMove);

	public boolean hasMoveablePosition(Position pos);

	public Position removeMoveablePosition(Position pos);

	public void moveTo(Position pos);

	public PieceTypeEnum getThisType();

	@Override
	public boolean equals(Object o);

	@Override
	public String toString();

	public boolean isOppositeColor(Piece otherPiece);
}
