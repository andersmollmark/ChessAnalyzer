package domain;
import org.junit.Before;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.movements.Movement;
import domain.pieces.King;
import domain.pieces.Pawn;
import domain.pieces.Queen;

public class TestBase {

	public ChessBoard theBoard;

	@Before
	public void initBoard() {
		theBoard = new ChessBoard(0);
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.A, Num._2)));
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.B, Num._2)));
		theBoard.addPieceInSquare(Pawn.white(new Position(Letter.C, Num._2)));
		theBoard.addPieceInSquare(King.white(new Position(Letter.A, Num._1)));
		theBoard.addPieceInSquare(Queen.white(new Position(Letter.C, Num._1)));

		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.A, Num._7)));
		theBoard.addPieceInSquare(Pawn.black(new Position(Letter.H, Num._7)));
		theBoard.addPieceInSquare(King.black(new Position(Letter.A, Num._8)));

		Movement.getInstance().initAllMoveables(theBoard);
	}

}
