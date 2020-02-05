package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.board.AMove;
import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Square;
import domain.movements.Movement;
import domain.pieces.Bishop;
import domain.pieces.Horse;
import domain.pieces.King;
import domain.pieces.Piece;

public class ChessProblemAnalyzer {

	private final ChessLogger theLogger = ChessLogger.getInstance();

	public Result makeAMove(ChessBoardSnapshots boardSnapshots, boolean isWhite, int drawNr) {
		// theLogger.appendToChessLog("making a move ... deep " + drawNr + " for the " + (isWhite ? "white " : "black ")
		// + "side");
		ChessBoard theBoard = boardSnapshots.getBoard(drawNr);
		King theKing = null;
		if (isWhite) {
			theBoard.addWhiteMove();
			theKing = theBoard.getWhiteKing();
		}
		else {
			theBoard.addBlackMove();
			theKing = theBoard.getBlackKing();
		}

		Result result = analyze(theKing, theBoard);
		result.setTheBoard(theBoard);
		// Om kungen står i schack ska detta draget göras
		if (result.getTheMove() != null) {
			theLogger.appendToChessLog("The " + (isWhite ? " white " : " black ") + "king is in check");
			ChessBoard boardCopy = move(result.getTheMove(), theBoard);
			if (drawNr < 8) {
				ChessProblemAnalyzer analyzer = new ChessProblemAnalyzer();
				drawNr++;
				boardSnapshots.setBoard(drawNr, boardCopy);
				result = analyzer.makeAMove(boardSnapshots, !isWhite, drawNr);
			}

		}
		else if (result.isRemi()) {
			theLogger.appendToChessLog("REMI");
			result.setRemi(true);
			return result;
		}
		else {
			Collection<Piece> pieces = isWhite ? theBoard.getWhitePieces() : theBoard.getBlackPieces();
			// System.out.println("antal pjäser: " + pieces.size());
			for (Piece p : pieces) {
				// theLogger.appendToChessLog("checking " + p.toString() + " if it has moveables");
				if (p.getMoveablePositions().size() > 0 && drawNr < 4) {
					drawNr++;
					Position pos = ((ArrayList<Position>) p.getMoveablePositions()).get(0);
					AMove aMove = new AMove(pos, p, drawNr);
					ChessBoard boardCopy = move(aMove, theBoard);
					Movement.getInstance().initAllMoveables(boardCopy);
					ChessProblemAnalyzer analyzer = new ChessProblemAnalyzer();
					boardSnapshots.setBoard(drawNr, boardCopy);

					result = analyzer.makeAMove(boardSnapshots, !isWhite, drawNr);
					if (result.isRemi()) {
						break;
					}
				}
			}

		}

		return result;

	}

	private Result analyze(King theKing, ChessBoard aBoard) {
		int moveNr = aBoard.getDrawNr();
		// theLogger.appendToChessLog(" analyzing ... draw " + moveNr);
		Result result = isInCheck(theKing, aBoard);
		if (result.isInCheck()) {
			theLogger
					.appendToChessLog((theKing.isWhite() ? "Vit " : "Svart ") + " kung är schack i drag nr: " + moveNr);
			return result;
		}
		else if (isPatt(theKing, aBoard)) {
			theLogger.appendToChessLog((theKing.isWhite() ? "Vit " : "Svart ") + " kung är patt i drag nr: " + moveNr);
			result.setRemi(true);
			return result;
		}
		else if (isRemi(aBoard, theKing)) {
			theLogger.appendToChessLog("Det är drag nr: " + moveNr + " och det är remi.");
			result.setRemi(true);
			return result;
		}

		return result;

	}

	/**
	 * Kollar om kungen är i schack. Om den är det så ska det provas att slå pjäsen, flytta något emellan eller flytta
	 * kungen. Om något av dessa saker går så sätts nextPieceToMove till den pjäsen som ska flytta. Annars sätts den
	 * till null. Om den sätts till null så är kungen matt.
	 * 
	 * @param isWhite
	 *            om kungen är vit eller inte.
	 * @param moveNr
	 *            är draget i ordningen
	 * @return true om kungen är i schack. fortsättningen är som ovan.
	 */
	public Result isInCheck(King theKing, ChessBoard aBoard) {
		// om det inte fanns några pjäser som kunde flytta så kolla kungen
		Result result = new Result();

		if (isNotInCheck(theKing, aBoard)) {
			return result;
		}

		AMove move = getAvoidCheckMove(theKing, aBoard);
		if (move != null) {
			result.setTheMove(move);
			return result;
		}
		// prova att flytta kungen
		if (theKing.getMoveablePositions().size() > 0) {
			Position pos = ((ArrayList<Position>) theKing.getMoveablePositions()).get(0);
			result.setTheMove(new AMove(pos, theKing, aBoard.getDrawNr()));
			return result;
		}
		return result;
	}

	public AMove getAvoidCheckMove(King theKing, ChessBoard aBoard) {
		// kolla om det går att slå den schackande pjäsen
		AMove theMove = tryToStrikeCheckingPiece(theKing, aBoard);
		if (theMove == null) {
			// det finns ingen som kan slå pjäsen, försök att sätta något emellan
			theMove = canAvoidCheck(theKing, aBoard);
		}
		return theMove;
	}

	public boolean isNotInCheck(King theKing, ChessBoard aBoard) {
		Square theSquare = aBoard.getSquare(theKing.getPosition());
		List<Piece> checkablePieces = theSquare.getOppositePiecesThatCanMoveHere();
		return checkablePieces.size() == 0;
	}

	/**
	 * Kollar om det går att slå den schackande pjäsen
	 * 
	 * @param kingsSquare
	 *            är den rutan som kungen står på
	 * @param isWhite
	 *            är true om det är den vita kungen man kollar
	 * @param drawMove
	 *            är vilket drag det är i ordningen
	 * @return ett AMoveobjekt om det fanns någon som kunde slå den schackande pjäsen, annars null.
	 */
	public AMove tryToStrikeCheckingPiece(King theKing, ChessBoard aBoard) {
		// det finns bara en pjäs
		List<Piece> checkablePieces = aBoard.getOppositePiecesThatCanCheck(theKing);
		if (checkablePieces.size() > 1) {
			return null;
		}

		Piece theCheckingPiece = checkablePieces.get(0);
		Square sqWithCheckingPiece = aBoard.getSquare(theCheckingPiece);
		// Kolla om det finns någon som kan slå den schackande pjäsen
		List<Piece> strikingPieces = sqWithCheckingPiece.getOppositePiecesThatCanMoveHere();
		if (strikingPieces.size() > 0) {
			for (Piece p : strikingPieces) {
				// kolla om pjäsen inte har dragit sitt andra drag
				Position posWithPiece = sqWithCheckingPiece.getPosition();
				if (!p.hasMoved(aBoard.getMoveNr(p)) && p.hasMoveablePosition(posWithPiece)) {
					// slå pjäsen
					AMove move = new AMove(posWithPiece, p, aBoard.getDrawNr());
					return move;
				}
			}
		}
		return null;
	}

	/**
	 * Metoden kollar om det går att ställa någonting emellan en kung och en pjäs.
	 * 
	 * @param isWhite
	 *            är true om det är den schackade kungen som är vit
	 * @param drawMove
	 *            är vilket drag det är i ordningen
	 * @param kingsSquare
	 *            är rutan som kungen står på
	 * @return ett AMoveobjekt om det fanns någon som kunde slå den schackande pjäsen, annars null.
	 */
	public AMove canAvoidCheck(King theKing, ChessBoard aBoard) {

		List<Piece> checkablePieces = aBoard.getOppositePiecesThatCanCheck(theKing);

		if (checkablePieces.size() > 1) {
			return null;
		}

		Square sqWithCheckingPiece = aBoard.getSquare(checkablePieces.get(0));
		Square kingSquare = aBoard.getSquare(theKing);
		List<Square> squaresBetween = aBoard.getSquaresBetween(kingSquare, sqWithCheckingPiece);

		AMove result = null;
		if (squaresBetween.size() == 0) {
			result = checkIfKingCanStrike(theKing, sqWithCheckingPiece, aBoard);
		}

		if (result == null) {
			result = checkIfMovePieceBetween(theKing, aBoard, squaresBetween);
		}

		return result;
	}

	public AMove checkIfMovePieceBetween(King theKing, ChessBoard aBoard, List<Square> squaresBetween) {
		AMove result = null;
		for (Square aSquare : squaresBetween) {
			List<Piece> movablePieces = theKing.isWhite() ? aSquare.getWhitePiecesThatCanMoveHere() : aSquare
					.getBlackPiecesThatCanMoveHere();
			if (movablePieces.size() > 0) {
				for (Piece p : movablePieces) {
					if (!p.hasMoved(aBoard.getMoveNr(p)) && p.hasMoveablePosition(aSquare.getPosition())) {
						result = new AMove(p.removeMoveablePosition(aSquare.getPosition()), p, aBoard.getDrawNr());
						break;
					}
				}
			}
		}
		return result;
	}

	private AMove checkIfKingCanStrike(King theKing, Square sqWithCheckingPiece, ChessBoard aBoard) {
		AMove move = null;
		boolean isWhite = sqWithCheckingPiece.getPieceInSquare().isWhite();
		List<Piece> protectingPieces = sqWithCheckingPiece.getPiecesThatCanMoveHere(isWhite);
		if (protectingPieces.size() == 0) {
			move = new AMove(sqWithCheckingPiece.getPosition(), theKing, aBoard.getDrawNr());
		}

		return move;
	}

	boolean isRemi(ChessBoard aBoard, King kingOfTheMovingSide) {
		Collection<Piece> whitePieces = aBoard.getWhitePieces();
		Collection<Piece> blackPieces = aBoard.getBlackPieces();

		if (whitePieces.size() == 1 && blackPieces.size() == 1) {
			return true;
		}
		else if (whitePieces.size() < 3 && blackPieces.size() < 3) {
			for (Piece p : whitePieces) {
				// om det är ngn annan typ av pjäs än kung, löpare eller häst så
				// är det inte remi
				if (!isRemiPiece(p)) {
					return false;
				}
			}
			return true;
		}
		return isPatt(kingOfTheMovingSide, aBoard);
	}

	private boolean isRemiPiece(Piece p) {
		return p instanceof King || p instanceof Bishop || p instanceof Horse;
	}

	/**
	 * Kollar om kungen är patt
	 * 
	 * @param isWhite
	 * @return
	 */
	boolean isPatt(King theKing, ChessBoard aBoard) {
		Collection<Piece> allPieces = theKing.isWhite() ? aBoard.getWhitePieces() : aBoard.getBlackPieces();
		// kolla först om inga pjäser kan flytta
		for (Piece p : allPieces) {
			if (p.getMoveablePositions().size() > 0) {
				return false;
			}
		}
		// om det inte fanns några pjäser som kunde flytta så kolla kungen
		if (theKing.getMoveablePositions().size() > 0) {
			return false;
		}
		return true; // ingen kan flytta men kungen är inte i schack
	}

	Piece getKing(boolean isWhite, ChessBoard aBoard) {
		Square sq = aBoard.getSquare(isWhite ? aBoard.getWhiteKingPos() : aBoard.getBlackKingPos());
		Piece piece = sq.getPieceInSquare();
		return piece;
	}

	private ChessBoard move(AMove theMove, ChessBoard theBoard) {
		theLogger.appendToChessLog("Move " + theBoard.getDrawNr() + ":" + theMove.toString());
		ChessBoard copyOfChessBoard = theBoard.getCopy(theBoard.getDrawNr() + 1);
		Piece pieceToMove = copyOfChessBoard.getSquare(theMove.getThePiece().getPosition()).getPieceInSquare();
		// flytta pjäsen från den gamla rutan
		Square oldSq = copyOfChessBoard.getSquare(pieceToMove.getPosition());
		Square newSq = copyOfChessBoard.getSquare(theMove.getToPos());
		// och ta bort från alla rutor som den kunde flytta till
		Movement.removeMoveablePositions(pieceToMove, copyOfChessBoard);
		// och flytta pjäsen till den nya rutan
		copyOfChessBoard.movePiece(pieceToMove, newSq.getPosition());

		Movement.getInstance().initAllMoveables(copyOfChessBoard);

		if (pieceToMove instanceof King) {
			if (pieceToMove.isWhite()) {
				copyOfChessBoard.setWhiteKingPos(newSq.getPosition());
			}
			else {
				copyOfChessBoard.setBlackKingPos(newSq.getPosition());
			}
		}

		// nu ska alla pjäser som påverkades av flytten från den gamla rutan
		// uppdateras
		updateMoveables(oldSq, copyOfChessBoard, true);
		updateMoveables(oldSq, copyOfChessBoard, false);

		// och alla pjäser som påverkades av flytten till den nya rutan
		updateMoveables(newSq, copyOfChessBoard, true);
		updateMoveables(newSq, copyOfChessBoard, false);

		// och kungarna
		King whiteKing = (King) copyOfChessBoard.getSquare(copyOfChessBoard.getWhiteKingPos()).getPieceInSquare();
		King blackKing = (King) copyOfChessBoard.getSquare(copyOfChessBoard.getBlackKingPos()).getPieceInSquare();
		// gör om flyttbara rutor
		Movement.removeMoveablePositions(whiteKing, copyOfChessBoard);
		Movement.getInstance().initMoveables(whiteKing, copyOfChessBoard);
		Movement.removeMoveablePositions(blackKing, copyOfChessBoard);
		Movement.getInstance().initMoveables(blackKing, copyOfChessBoard);
		// och så schacklinjerna
		Movement.getInstance().initMoveables(whiteKing, copyOfChessBoard);
		Movement.getInstance().initMoveables(blackKing, copyOfChessBoard);

		return copyOfChessBoard;
	}

	private void updateMoveables(Square aSquare, ChessBoard theBoard, boolean isWhite) {
		ArrayList<Piece> pieces = (ArrayList<Piece>) aSquare.getPiecesThatCanMoveHere(isWhite);
		ArrayList<Piece> copy = new ArrayList<Piece>();
		for (Piece p : pieces) {
			copy.add(p.getCopy());
		}
		pieces.clear();
		for (Piece aPiece : copy) {
			// ta bort alla gamla platser som den kunde flytta till
			// och ta bort att pjäsen kan flytta till resp ruta
			Movement.removeMoveablePositions(aPiece, theBoard);
			// lägg in nya rutor
			Movement.getInstance().initMoveables(aPiece, theBoard);
		}
		if (isWhite) {
			aSquare.setWhitePiecesThatCanMoveHere(copy);
		}
		else {
			aSquare.setBlackPiecesThatCanMoveHere(copy);
		}
	}
}
