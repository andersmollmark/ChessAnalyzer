package domain.board;

import domain.pieces.Piece;

public class AMove {

	private int moveNr;
	private Position fromPos;
	private Position toPos;
	private Piece thePiece;

	public AMove(Position toPos, Piece p, int drawNr) {
		this.toPos = toPos;
		thePiece = p;
		moveNr = drawNr;
	}

	public Position getFromPos() {
		return fromPos;
	}

	public void setFromPos(Position fromPos) {
		this.fromPos = fromPos;
	}

	public Position getToPos() {
		return toPos;
	}

	public void setToPos(Position toPos) {
		this.toPos = toPos;
	}

	public Piece getThePiece() {
		return thePiece;
	}

	public void setThePiece(Piece thePiece) {
		this.thePiece = thePiece;
	}

	public int getMoveNr() {
		return moveNr;
	}

	public void setMoveNr(int moveNr) {
		this.moveNr = moveNr;
	}

	@Override
	public String toString() {
		// String s = "Move nr:" + moveNr;
		String s = "\nPjäs : " + thePiece.toString();
		s += " To pos : " + toPos.toString();
		return s;
	}
}
