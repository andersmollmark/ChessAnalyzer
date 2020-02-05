package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.movements.Movement;
import domain.pieces.King;
import domain.pieces.Pawn;
import domain.pieces.Piece;
import domain.pieces.Queen;
import domain.pieces.Tower;

public class ChessProblemHandlerTest {

	private ChessBoardSnapshots handler;
	private ChessBoard theBoard;

	@Before
	public void initBoard() {
		handler = new ChessBoardSnapshots();
		theBoard = handler.getBoard(0);
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.A, Num._2)));
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.B, Num._2)));
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.C, Num._2)));
		theBoard.addPieceInSquare(King.white(new Position(Letter.A, Num._1)));
		theBoard.addPieceInSquare(Queen.white(new Position(Letter.C, Num._1)));

		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.A, Num._7)));
		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.H, Num._7)));
		theBoard.addPieceInSquare(King.black(new Position(Letter.A, Num._8)));

	}

	@Test
	public void testTheBoardsPieces() {
		Movement.getInstance().initAllMoveables(theBoard);

		assertWhitePieces();

		assertBlackPieces();
	}

	private void assertBlackPieces() {
		Piece a7 = theBoard.getPiece("a7");
		assertTrue(a7.getMoveablePositions().size() == 1);
		Piece h7 = theBoard.getPiece("h7");
		assertTrue(h7.getMoveablePositions().size() == 1);
		Piece ka8 = theBoard.getPiece("a8");
		assertTrue(ka8.getMoveablePositions().size() == 2);
	}

	private void assertWhitePieces() {
		Piece a2 = theBoard.getPiece("a2");
		assertTrue(a2.getMoveablePositions().size() == 1);
		Piece b2 = theBoard.getPiece("b2");
		assertTrue(b2.getMoveablePositions().size() == 1);
		Piece c2 = theBoard.getPiece("c2");
		assertTrue(c2.getMoveablePositions().size() == 1);
		Piece ka1 = theBoard.getPiece("a1");
		assertTrue(ka1.getMoveablePositions().size() == 1);
		Piece dc1 = theBoard.getPiece("c1");
		assertTrue(dc1.getMoveablePositions().size() == 11);
	}

	@Test
	public void testAddingTower() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d7")));
		Movement.getInstance().initAllMoveables(theBoard);
		assertWhitePieces();
		Piece td7 = theBoard.getPiece("d7");
		assertTrue(td7.getMoveablePositions().size() == 14);
		Piece a7 = theBoard.getPiece("a7");
		assertTrue(a7.getMoveablePositions().size() == 1);
		Piece h7 = theBoard.getPiece("h7");
		assertTrue(h7.getMoveablePositions().size() == 1);
		Piece ka8 = theBoard.getPiece("a8");
		assertTrue(ka8.getMoveablePositions().size() == 1);

	}

	@Test
	public void testAddingWhiteTowerBlackTower() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d7")));
		theBoard.addPieceInSquare(Tower.black(Position.getPosition("c7")));
		Movement.getInstance().initAllMoveables(theBoard);
		assertWhitePieces();
		Piece td7 = theBoard.getPiece("d7");
		assertTrue(td7.getMoveablePositions().size() == 12);
		Piece a7 = theBoard.getPiece("a7");
		assertTrue(a7.getMoveablePositions().size() == 1);
		Piece h7 = theBoard.getPiece("h7");
		assertTrue(h7.getMoveablePositions().size() == 1);
		Piece ka8 = theBoard.getPiece("a8");
		assertTrue(ka8.getMoveablePositions().size() == 2);
		Piece tc7 = theBoard.getPiece("c7");
		assertTrue(tc7.getMoveablePositions().size() == 8);

	}

	@Test
	public void testAddingTwoWhiteTowers() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("b4")));
		Movement.getInstance().initAllMoveables(theBoard);
		assertWhitePieces();
		Piece td8 = theBoard.getPiece("d8");
		assertTrue(td8.getMoveablePositions().size() == 14);
		Piece a7 = theBoard.getPiece("a7");
		assertTrue(a7.getMoveablePositions().size() == 1);
		Piece h7 = theBoard.getPiece("h7");
		assertTrue(h7.getMoveablePositions().size() == 1);
		Piece ka8 = theBoard.getPiece("a8");
		assertTrue(ka8.getMoveablePositions().size() == 0);

	}
}
