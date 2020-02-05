package domain.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.pieces.Piece;

public class Square {

	// vilken position p� br�det
	private final Position p;

	// Vilken pj�s som st�r p� denna rutan
	private Piece pieceInSquare;

	// Vilka vita pj�ser som kan g� till denna rutan
	private List<Piece> whitePiecesMoves = new ArrayList<Piece>();

	// Vilka svarta pj�ser som kan g� till denna rutan
	private List<Piece> blackPiecesMoves = new ArrayList<Piece>();

	// Om rutan ligger i linje med den svarta kungens schacklinjer
	private final boolean blackKingCanBeChecked = false;

	// Om rutan ligger i linje med den vita kungens schacklinjer
	private final boolean whiteKingCanBeChecked = false;

	public Square(Letter l, Num n) {
		p = new Position(l, n);
	}

	// private boolean isNextToKing(boolean isWhite) {
	// for (Piece p : isWhite ? whitePiecesMoves : blackPiecesMoves) {
	// // pj�sen �r kungen, dvs rutan �r bredvid den en kung
	// if (p instanceof King) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public boolean isNextToWhiteKing() {
	// return isNextToKing(true);
	// }
	//
	// public boolean isNextToBlackKing() {
	// return isNextToKing(false);
	// }

	public Piece getPieceInSquare() {
		return pieceInSquare;
	}

	public void setPieceInSquare(Piece pieceInSquare) {
		this.pieceInSquare = pieceInSquare;
	}

	public Position getPosition() {
		return p;
	}

	public void setWhitePiecesThatCanMoveHere(List<Piece> piecesMoves) {
		whitePiecesMoves = piecesMoves;
	}

	public void setBlackPiecesThatCanMoveHere(List<Piece> piecesMoves) {
		blackPiecesMoves = piecesMoves;
	}

	/**
	 * H�mtar de pj�ser som kan flyttas till denna rutan
	 * 
	 * @param isWhite
	 *            �r true om man vill ha tillbaka dom vita pj�serna
	 * @return en collection med dom vita eller dom svarta pj�serna
	 */
	public List<Piece> getPiecesThatCanMoveHere(boolean isWhite) {
		return isWhite ? whitePiecesMoves : blackPiecesMoves;
	}

	public List<Piece> getOppositePiecesThatCanMoveHere() {
		if (pieceInSquare == null) {
			throw new IllegalArgumentException("Det finns ingen pj�s i denna rutan:" + toString());
		}
		return pieceInSquare.isWhite() ? blackPiecesMoves : whitePiecesMoves;
	}

	public List<Piece> getWhitePiecesThatCanMoveHere() {
		return getPiecesThatCanMoveHere(true);
	}

	public List<Piece> getBlackPiecesThatCanMoveHere() {
		return getPiecesThatCanMoveHere(false);
	}

	public void addPieceThatCanMoveHere(Piece p) {
		if (p.isWhite() && !whitePiecesMoves.contains(p)) {
			whitePiecesMoves.add(p);
		}
		else if (p.isBlack() && !blackPiecesMoves.contains(p)) {
			blackPiecesMoves.add(p);
		}
	}

	/**
	 * Ta bort denna pj�sen som m�jlig att flytta hit till denna rutan.
	 * 
	 * @param p
	 *            �r den pj�sen som ska tas bort
	 */
	public void removePieceMove(Piece remPiece) {
		Collection<Piece> moveablePieces = remPiece.isWhite() ? whitePiecesMoves : blackPiecesMoves;
		for (Piece p : moveablePieces) {
			if (p.equals(remPiece)) {
				moveablePieces.remove(p);
				break;
			}
		}
	}

	public Square getCopy() {
		Square copy = new Square(Position.getLetter(getPosition().getX()), Position.getNum(getPosition().getY()));
		// kopiera alla pj�ser som kan flytta hit
		List<Piece> wMoves = new ArrayList<Piece>();
		List<Piece> bMoves = new ArrayList<Piece>();
		for (Piece p : getPiecesThatCanMoveHere(true)) {
			wMoves.add(p.getCopy());
		}
		for (Piece p : getPiecesThatCanMoveHere(false)) {
			bMoves.add(p.getCopy());
		}

		copy.setBlackPiecesThatCanMoveHere(bMoves);
		copy.setWhitePiecesThatCanMoveHere(wMoves);

		return copy;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Square) {
			Square temp = (Square) o;
			if (temp.getPosition().equals(getPosition())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String retur = "Pos:" + getPosition().toString();
		retur = retur + "\nPieces who can move here:\n";
		List<Piece> allPieces = new ArrayList<Piece>();
		allPieces.addAll(getPiecesThatCanMoveHere(true));
		allPieces.addAll(getPiecesThatCanMoveHere(false));
		for (Piece p : allPieces) {
			retur = retur + "\n" + p.toString();
		}
		return retur;
	}

}
