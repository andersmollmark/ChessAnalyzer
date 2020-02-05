package domain.board;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import domain.TestBase;
import org.junit.Test;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.board.Square;
import domain.pieces.Piece;

public class ChessBoardTest extends TestBase {

	@Test
	public void testGetWhiteKing() {
		assertTrue(theBoard.getWhiteKingPos().equals(new Position(Letter.A, Num._1)));
	}

	@Test
	public void testGetBlackKing() {
		assertTrue(theBoard.getBlackKingPos().equals(new Position(Letter.A, Num._8)));
	}

	@Test
	public void testGetWhitePieces() {
		assertTrue("Fel antal pj�ser, size=" + theBoard.getWhitePieces().size(), theBoard.getWhitePieces().size() == 5);
	}

	@Test
	public void testGetBlackPieces() {
		assertTrue("Fel antal pj�ser, size=" + theBoard.getBlackPieces().size(), theBoard.getBlackPieces().size() == 3);
	}

	@Test
	public void testGetSquarePosition() {
		Position test = theBoard.getSquare(0, 0).getPosition();
		assertTrue("Fel ruta, square.position:" + test,
				test.equals(theBoard.getSquare(new Position(Letter.A, Num._1)).getPosition()));
	}

	@Test
	public void testIsOkPos() {
		Position ok = new Position(Letter.C, Num._4);
		Position ok2 = new Position(Letter.H, Num._8);
		assertTrue(theBoard.isOkPos(ok));
		assertTrue(theBoard.isOkPos(ok2));
	}

	@Test
	public void testGetCopy() {
		ChessBoard aCopy = theBoard.getCopy(1);
		assertTrue(aCopy.getWhitePieces().size() == 5);
		assertTrue(aCopy.getBlackPieces().size() == 3);
		assertTrue(aCopy.getWhiteKingPos().equals(new Position(Letter.A, Num._1)));
		assertTrue(aCopy.getBlackKingPos().equals(new Position(Letter.A, Num._8)));
	}

	@Test
	public void testCopyJustTheBoard() {
		ChessBoard aCopy = theBoard.copyJustTheBoard(1);
		assertTrue(aCopy.getWhitePieces().size() == 0);
		assertTrue(aCopy.getBlackPieces().size() == 0);
		assertTrue(aCopy.getWhiteKingPos() == null);
		assertTrue(aCopy.getBlackKingPos() == null);
	}

	@Test
	public void testCopyPiecesToSquares() {
		ChessBoard copy = theBoard.copyJustTheBoard(1);
		List<Piece> whitePieces = theBoard.copyPiecesToSquares(theBoard.getWhitePieces(), copy);
		assertTrue(whitePieces.size() == 5);
		int index = 0;
		for (Piece whitePiece : theBoard.getWhitePieces()) {
			Piece pieceCopy = whitePieces.get(index++);
			assertFalse(pieceCopy.equals(whitePiece));
			assertTrue(pieceCopy.getPosition().equals(whitePiece.getPosition()));
			assertTrue(pieceCopy.getThisType().equals(whitePiece.getThisType()));
		}
	}

	@Test
	public void testMovePiece() {
		Piece p1 = theBoard.getWhitePieces().get(0);
		Position newPosition = new Position(Letter.A, Num._4);
		Square oldSquare = theBoard.getSquare(p1.getPosition());
		assertTrue(oldSquare.getPieceInSquare().equals(p1));

		theBoard.movePiece(p1, newPosition);
		Square newSquare = theBoard.getSquare(p1.getPosition());
		assertFalse(newSquare.equals(oldSquare));
		assertTrue(newSquare.getPosition().equals(newPosition));
	}

	@Test
	public void testGetSquaresBetweenY() {
		Square from = theBoard.getSquare(new Position(0, 0));
		Square to = theBoard.getSquare(new Position(0, 7));
		List<Square> sqBetween = theBoard.getSquaresBetween(from, to);
		assertTrue(sqBetween.size() == 6);
		int pos = 1;
		for (Square s : sqBetween) {
			assertTrue(s.getPosition().getX() == 0);
			assertTrue(s.getPosition().getY() == pos++);
		}

	}

	@Test
	public void testGetSquaresBetweenX() {
		Square from = theBoard.getSquare(new Position(0, 0));
		Square to = theBoard.getSquare(new Position(7, 0));
		List<Square> sqBetween = theBoard.getSquaresBetween(from, to);
		assertTrue(sqBetween.size() == 6);
		int pos = 1;
		for (Square s : sqBetween) {
			assertTrue(s.getPosition().getY() == 0);
			assertTrue(s.getPosition().getX() == pos++);
		}

	}

	@Test
	public void testGetSquaresBetweenXthroughY() {
		Square from = theBoard.getSquare(new Position(0, 0));
		Square to = theBoard.getSquare(new Position(7, 7));
		List<Square> sqBetween = theBoard.getSquaresBetween(from, to);
		assertTrue(sqBetween.size() == 6);
		int pos = 1;
		for (Square s : sqBetween) {
			assertTrue(s.getPosition().getY() == pos);
			assertTrue(s.getPosition().getX() == pos++);
		}

	}

	@Test
	public void testGetSquaresBetweenXthroughYWithSmallerLast() {
		Square to = theBoard.getSquare(new Position(0, 0));
		Square from = theBoard.getSquare(new Position(7, 7));
		List<Square> sqBetween = theBoard.getSquaresBetween(from, to);
		assertTrue(sqBetween.size() == 6);
		int pos = 1;
		for (Square s : sqBetween) {
			assertTrue(s.getPosition().getY() == pos);
			assertTrue(s.getPosition().getX() == pos++);
		}

	}

	// private void assertSquare(Square s, int index){
	// assertTrue(sqBetween.get(0).getPosition().getX() == 0);
	// assertTrue(sqBetween.get(0).getPosition().getY() == 0);
	// }

}
