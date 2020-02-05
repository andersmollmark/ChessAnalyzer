package domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.board.AMove;
import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.board.Square;
import domain.movements.Movement;
import domain.pieces.Bishop;
import domain.pieces.King;
import domain.pieces.Pawn;
import domain.pieces.Queen;
import domain.pieces.Tower;

public class ChessProblemAnalyzerTest {

	private ChessBoardSnapshots handler;
	private ChessBoard theBoard;
	ChessProblemAnalyzer analyzer = new ChessProblemAnalyzer();

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
	public void testIsNotInCheckWhenInCheck() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("b4")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		// den ska vara i schack
		assertFalse(analyzer.isNotInCheck(theBoard.getBlackKing(), theBoard));
	}

	@Test
	public void testIsNotInCheck() {
		Movement.getInstance().initAllMoveables(theBoard);
		;

		assertTrue(analyzer.isNotInCheck(theBoard.getBlackKing(), theBoard));
	}

	@Test
	public void testCanAvoidCheck() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("b8")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		AMove move = analyzer.canAvoidCheck(theBoard.getBlackKing(), theBoard);
		assertNotNull(move);
		assertTrue(move.getToPos().equals(Position.getPosition("b8")));
		assertTrue(move.getThePiece().equals(theBoard.getBlackKing()));
	}

	@Test
	public void testCanAvoidCheckWithAnotherPiece() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Tower.black(Position.getPosition("b7")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		AMove move = analyzer.canAvoidCheck(theBoard.getBlackKing(), theBoard);
		assertNotNull(move);
		assertTrue(move.getToPos().equals(Position.getPosition("b8")));
		assertTrue(move.getThePiece().equals(theBoard.getPiece("b7")));
	}

	@Test
	public void testCanNotAvoidCheckWithManyCheckablePieces() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Bishop.white(Position.getPosition("c6")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		AMove move = analyzer.canAvoidCheck(theBoard.getBlackKing(), theBoard);
		assertNull(move);
	}

	@Test
	public void testCanMovePieceInBetweenWhenInCheck() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Tower.black(Position.getPosition("b4")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		List<Square> squaresBetween = theBoard.getSquaresBetween(theBoard.getSquare(Position.getPosition("a8")),
				theBoard.getSquare(Position.getPosition("d8")));

		AMove theMove = analyzer.checkIfMovePieceBetween(theBoard.getBlackKing(), theBoard, squaresBetween);
		assertTrue(theMove != null);

	}

	@Test
	public void testTryToStrikeCheckingPiece() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Tower.black(Position.getPosition("d4")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		AMove theStrike = analyzer.tryToStrikeCheckingPiece(theBoard.getBlackKing(), theBoard);
		assertNotNull(theStrike);
		assertTrue(theStrike.getToPos().equals(Position.getPosition("d8")));
		assertTrue(theStrike.getThePiece().equals(theBoard.getPiece("d4")));

	}

	@Test
	public void testTryToStrikeCheckingPieceWithManyCheckablePieces() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Bishop.white(Position.getPosition("c6")));
		theBoard.addPieceInSquare(Tower.black(Position.getPosition("d4")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		AMove theStrike = analyzer.tryToStrikeCheckingPiece(theBoard.getBlackKing(), theBoard);
		assertNull(theStrike);

	}

	@Test
	public void testTryToStrikeCheckingPieceWithNoStrikingPieces() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		AMove theStrike = analyzer.tryToStrikeCheckingPiece(theBoard.getBlackKing(), theBoard);
		assertNull(theStrike);

	}

	@Test
	public void testIsInCheckWithStrikingPiece() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Tower.black(Position.getPosition("d4")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		Result aResult = analyzer.isInCheck(theBoard.getBlackKing(), theBoard);
		assertNotNull(aResult);

		AMove theMove = aResult.getTheMove();
		assertNotNull(theMove);
		assertTrue(theMove.getToPos().equals(Position.getPosition("d8")));
		assertTrue(theMove.getThePiece().equals(theBoard.getPiece("d4")));
	}

	@Test
	public void testIsInCheckWhenKingIsMovable() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		Result aResult = analyzer.isInCheck(theBoard.getBlackKing(), theBoard);
		assertNotNull(aResult);

		AMove theMove = aResult.getTheMove();
		assertNotNull(theMove);
		assertTrue(theMove.getToPos().equals(Position.getPosition("b7")));
		assertTrue(theMove.getThePiece().equals(theBoard.getBlackKing()));
	}

	@Test
	public void testIsInCheckWhenKingIsMatt() {
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("d8")));
		theBoard.addPieceInSquare(Pawn.black(Position.getPosition("b7")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		Result aResult = analyzer.isInCheck(theBoard.getBlackKing(), theBoard);
		assertNotNull(aResult);

		AMove theMove = aResult.getTheMove();
		assertNull(theMove);
	}

	@Test
	public void testIsPatt() {
		theBoard.addPieceInSquare(Pawn.white(Position.getPosition("a6")));
		theBoard.addPieceInSquare(Pawn.white(Position.getPosition("h6")));
		theBoard.addPieceInSquare(Tower.white(Position.getPosition("b5")));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		boolean patt = analyzer.isPatt(theBoard.getBlackKing(), theBoard);
		assertTrue(patt);
	}

	@Test
	public void testIsNotPatt() {
		Movement.getInstance().initAllMoveables(theBoard);
		;
		boolean patt = analyzer.isPatt(theBoard.getBlackKing(), theBoard);
		assertFalse(patt);
	}

	@Test
	public void testIsNotRemi() {
		Movement.getInstance().initAllMoveables(theBoard);
		;
		boolean remi = analyzer.isRemi(theBoard, theBoard.getWhiteKing());
		assertFalse(remi);
	}

	@Test
	public void testIsRemi() {
		handler = new ChessBoardSnapshots();
		theBoard = handler.getBoard(0);
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.A, Num._2)));
		theBoard.addPieceInSquare(King.white(new Position(Letter.A, Num._6)));
		theBoard.addPieceInSquare(Tower.white(new Position(Letter.B, Num._1)));

		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.A, Num._7)));
		theBoard.addPieceInSquare(King.black(new Position(Letter.A, Num._8)));
		Movement.getInstance().initAllMoveables(theBoard);
		;
		boolean remi = analyzer.isRemi(theBoard, theBoard.getBlackKing());
		assertTrue(remi);
	}

}
