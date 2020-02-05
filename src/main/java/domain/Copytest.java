package domain;

import java.util.ArrayList;

import domain.board.ChessBoard;
import domain.pieces.Pawn;
import domain.pieces.Piece;

public class Copytest {

	public static void main(String[] args) {
		ChessBoardSnapshots theHandler = new ChessBoardSnapshots();
		ChessBoard theBoard = new ChessBoard(0);

		ArrayList<Piece> w = new ArrayList<Piece>();
		ArrayList<Piece> b = new ArrayList<Piece>();
		w.add(Pawn.white(theBoard.getPosition(1, 1)));
		w.add(Pawn.white(theBoard.getPosition(2, 1)));
		w.add(Pawn.black(theBoard.getPosition(7, 7)));
		w.add(Pawn.black(theBoard.getPosition(6, 6)));
		theBoard.setBlackPieces(b);
		theBoard.setWhitePieces(w);

		for (Piece p : w) {
			System.out.println("w pjäs i 1 : " + p.toString());
		}
		for (Piece p : b) {
			System.out.println("b pjäs i 1 : " + p.toString());
		}
		ChessBoard board2 = theBoard.getCopy(1);

		((ArrayList<Piece>) board2.getBlackPieces()).clear();
		// ((ArrayList<Piece>)board2.getWhitePieces())
		skrivUtAllaPjaser(theBoard, board2);

		System.out.println("Flyttar en bonde i 1...");
		((ArrayList<Piece>) theBoard.getWhitePieces()).get(0).moveTo(theBoard.getPosition(3, 3));

		skrivUtAllaPjaser(theBoard, board2);

	}

	private static void skrivUtAllaPjaser(ChessBoard theBoard, ChessBoard board2) {
		System.out.println("Och nu ska kopian skrivas ut");
		for (Piece p : board2.getWhitePieces()) {
			System.out.println("w pjäs i 2: " + p.toString());
		}
		for (Piece p : board2.getBlackPieces()) {
			System.out.println("b pjäs i 2 : " + p.toString());
		}

		System.out.println(" och orginalet");
		for (Piece p : theBoard.getWhitePieces()) {
			System.out.println("w pjäs i 1 : " + p.toString());
		}
		for (Piece p : theBoard.getBlackPieces()) {
			System.out.println("b pjäs i 1 : " + p.toString());
		}
	}
}
