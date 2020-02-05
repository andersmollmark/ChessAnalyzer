package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.board.Position;
import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.movements.Direction;
import domain.movements.DirectionValueEnum;
import domain.movements.Movement;
import domain.pieces.Bishop;
import domain.pieces.Horse;
import domain.pieces.King;
import domain.pieces.Pawn;
import domain.pieces.Piece;
import domain.pieces.Queen;
import domain.pieces.Tower;

public class MovementTest extends TestBase {

	@Test
	public void testRemoveCheckableMovesWithBlackPiece() {
		Bishop blackBishop = Bishop.black(new Position(Letter.E, Num._5));
		Piece whiteKing = theBoard.getSquare(new Position(Letter.A, Num._1)).getPieceInSquare();
		Piece b2 = theBoard.getSquare(new Position(Letter.B, Num._2)).getPieceInSquare();

		addAndAssertCommon(blackBishop, whiteKing, b2);

		Movement.getInstance().removeCheckableMoves((King) whiteKing,
				Direction.getDirection(DirectionValueEnum.PLUS_X_PLUS_Y), theBoard.getSquare(b2.getPosition()),
				theBoard);

		assertTrue(b2.getMoveablePositions().size() == 0);

	}

	@Test
	public void testRemoveCheckableMovesWithWhitePiece() {
		Bishop whiteBishop = Bishop.white(new Position(Letter.E, Num._5));
		Piece whiteKing = theBoard.getSquare(new Position(Letter.A, Num._1)).getPieceInSquare();
		Piece b2 = theBoard.getSquare(new Position(Letter.B, Num._2)).getPieceInSquare();

		addAndAssertCommon(whiteBishop, whiteKing, b2);

		int movesBefore = b2.getMoveablePositions().size();

		Movement.getInstance().removeCheckableMoves((King) whiteKing,
				Direction.getDirection(DirectionValueEnum.PLUS_X_PLUS_Y), theBoard.getSquare(b2.getPosition()),
				theBoard);

		assertTrue(b2.getMoveablePositions().size() == movesBefore);

	}

	private void addAndAssertCommon(Bishop bishop, Piece whiteKing, Piece pawnB2) {
		theBoard.addPieceInSquare(bishop);
		Movement.getInstance().initMoveables(bishop, theBoard);

		assertTrue(whiteKing instanceof King);
		assertTrue(whiteKing.isWhite());
		assertTrue(pawnB2 instanceof Pawn);
		assertTrue(pawnB2.getMoveablePositions().size() > 0);
	}

	@Test
	public void testInitDirectionMovesPawn() {
		Piece pawnF2 = theBoard.addPieceInSquare(Pawn.white(new Position(Letter.F, Num._2)));
		assertTrue(pawnF2 instanceof Pawn);
		assertTrue(pawnF2.getMoveablePositions().size() == 0);

		Movement.getInstance().initMoveables(pawnF2, theBoard);
		assertTrue(pawnF2.getMoveablePositions().size() == 1);

	}

	@Test
	public void testInitDirectionMovesKing() {
		Piece kingF2 = theBoard.addPieceInSquare(King.white(new Position(Letter.F, Num._2)));
		assertTrue(kingF2 instanceof King);
		assertTrue(kingF2.getMoveablePositions().size() == 0);

		Movement.getInstance().initMoveables(kingF2, theBoard);
		assertTrue(kingF2.getMoveablePositions().size() == 8);

	}

	@Test
	public void testInitDirectionMovesTower() {
		Piece towerF2 = theBoard.addPieceInSquare(Tower.white(new Position(Letter.F, Num._2)));
		assertTrue(towerF2 instanceof Tower);
		assertTrue(towerF2.getMoveablePositions().size() == 0);

		Movement.getInstance().initMoveables(towerF2, theBoard);
		assertTrue(towerF2.getMoveablePositions().size() == 11);

	}

	@Test
	public void testInitDirectionMovesHorse() {
		Piece horseF2 = theBoard.addPieceInSquare(Horse.white(new Position(Letter.F, Num._2)));
		assertTrue(horseF2 instanceof Horse);
		assertTrue(horseF2.getMoveablePositions().size() == 0);

		Movement.getInstance().initMoveables(horseF2, theBoard);
		assertTrue(horseF2.getMoveablePositions().size() == 6);

	}

	@Test
	public void testInitDirectionMovesBishop() {
		Piece bishopF2 = theBoard.addPieceInSquare(Bishop.white(new Position(Letter.F, Num._2)));
		assertTrue(bishopF2 instanceof Bishop);
		assertTrue(bishopF2.getMoveablePositions().size() == 0);

		Movement.getInstance().initMoveables(bishopF2, theBoard);
		assertTrue(bishopF2.getMoveablePositions().size() == 9);

	}

	@Test
	public void testInitDirectionMovesQueen() {
		Piece queenF2 = theBoard.addPieceInSquare(Queen.white(new Position(Letter.F, Num._2)));
		assertTrue(queenF2 instanceof Queen);
		assertTrue(queenF2.getMoveablePositions().size() == 0);

		Movement.getInstance().initMoveables(queenF2, theBoard);
		assertTrue(queenF2.getMoveablePositions().size() == 20);

	}

	@Test
	public void testGetMovablePositionsWhiteKing() {
		Piece ka1 = theBoard.getPiece("a1");
		assertTrue(ka1 instanceof King);
		assertTrue(ka1.getMoveablePositions().size() == 1);
	}

}
