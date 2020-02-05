package domain;

import org.junit.Test;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.movements.Movement;
import domain.pieces.King;
import domain.pieces.Pawn;
import domain.pieces.Queen;

public class ProblemTester {

	@Test
	public void testMakeAMove() {
		ChessBoardSnapshots handler = new ChessBoardSnapshots();
		ChessBoard theBoard = handler.getBoard(0);
		// borde bli matt vid vits första drag
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.A, Num._2)));
		theBoard.addPieceInSquare(King.white(new Position(Letter.A, Num._1)));
		theBoard.addPieceInSquare(Queen.white(new Position(Letter.C, Num._1)));

		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.A, Num._7)));
		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.B, Num._7)));
		theBoard.addPieceInSquare(King.black(new Position(Letter.A, Num._8)));
		// sätt alla drag som kan göras
		Movement.getInstance().initAllMoveables(theBoard);

		ChessProblemAnalyzer a1 = new ChessProblemAnalyzer();
		Result res = a1.makeAMove(handler, true, 0);

		System.out.println("Result : " + res.toString());
		System.out.println("Ställningen blev:\n");
		System.out.println(res.getTheBoard().toString());
	}

}
