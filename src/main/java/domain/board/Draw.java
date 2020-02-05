package domain.board;

import domain.pieces.Piece;

public class Draw {

	// the position to move to
	private Position aPosition;
	private Piece aPiece;
	
	public Draw(Position pos, Piece p){
		aPiece = p;
		aPosition = pos;
	}

	public Position getAPosition() {
		return aPosition;
	}

	public Piece getAPiece() {
		return aPiece;
	}
}
