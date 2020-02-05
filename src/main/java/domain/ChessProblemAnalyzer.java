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
		// Om kungen st�r i schack ska detta draget g�ras
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
			// System.out.println("antal pj�ser: " + pieces.size());
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
					.appendToChessLog((theKing.isWhite() ? "Vit " : "Svart ") + " kung �r schack i drag nr: " + moveNr);
			return result;
		}
		else if (isPatt(theKing, aBoard)) {
			theLogger.appendToChessLog((theKing.isWhite() ? "Vit " : "Svart ") + " kung �r patt i drag nr: " + moveNr);
			result.setRemi(true);
			return result;
		}
		else if (isRemi(aBoard, theKing)) {
			theLogger.appendToChessLog("Det �r drag nr: " + moveNr + " och det �r remi.");
			result.setRemi(true);
			return result;
		}

		return result;

	}

	/**
	 * Kollar om kungen �r i schack. Om den �r det s� ska det provas att sl� pj�sen, flytta n�got emellan eller flytta
	 * kungen. Om n�got av dessa saker g�r s� s�tts nextPieceToMove till den pj�sen som ska flytta. Annars s�tts den
	 * till null. Om den s�tts till null s� �r kungen matt.
	 * 
	 * @param isWhite
	 *            om kungen �r vit eller inte.
	 * @param moveNr
	 *            �r draget i ordningen
	 * @return true om kungen �r i schack. forts�ttningen �r som ovan.
	 */
	public Result isInCheck(King theKing, ChessBoard aBoard) {
		// om det inte fanns n�gra pj�ser som kunde flytta s� kolla kungen
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
		// kolla om det g�r att sl� den schackande pj�sen
		AMove theMove = tryToStrikeCheckingPiece(theKing, aBoard);
		if (theMove == null) {
			// det finns ingen som kan sl� pj�sen, f�rs�k att s�tta n�got emellan
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
	 * Kollar om det g�r att sl� den schackande pj�sen
	 * 
	 * @param kingsSquare
	 *            �r den rutan som kungen st�r p�
	 * @param isWhite
	 *            �r true om det �r den vita kungen man kollar
	 * @param drawMove
	 *            �r vilket drag det �r i ordningen
	 * @return ett AMoveobjekt om det fanns n�gon som kunde sl� den schackande pj�sen, annars null.
	 */
	public AMove tryToStrikeCheckingPiece(King theKing, ChessBoard aBoard) {
		// det finns bara en pj�s
		List<Piece> checkablePieces = aBoard.getOppositePiecesThatCanCheck(theKing);
		if (checkablePieces.size() > 1) {
			return null;
		}

		Piece theCheckingPiece = checkablePieces.get(0);
		Square sqWithCheckingPiece = aBoard.getSquare(theCheckingPiece);
		// Kolla om det finns n�gon som kan sl� den schackande pj�sen
		List<Piece> strikingPieces = sqWithCheckingPiece.getOppositePiecesThatCanMoveHere();
		if (strikingPieces.size() > 0) {
			for (Piece p : strikingPieces) {
				// kolla om pj�sen inte har dragit sitt andra drag
				Position posWithPiece = sqWithCheckingPiece.getPosition();
				if (!p.hasMoved(aBoard.getMoveNr(p)) && p.hasMoveablePosition(posWithPiece)) {
					// sl� pj�sen
					AMove move = new AMove(posWithPiece, p, aBoard.getDrawNr());
					return move;
				}
			}
		}
		return null;
	}

	/**
	 * Metoden kollar om det g�r att st�lla n�gonting emellan en kung och en pj�s.
	 * 
	 * @param isWhite
	 *            �r true om det �r den schackade kungen som �r vit
	 * @param drawMove
	 *            �r vilket drag det �r i ordningen
	 * @param kingsSquare
	 *            �r rutan som kungen st�r p�
	 * @return ett AMoveobjekt om det fanns n�gon som kunde sl� den schackande pj�sen, annars null.
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
				// om det �r ngn annan typ av pj�s �n kung, l�pare eller h�st s�
				// �r det inte remi
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
	 * Kollar om kungen �r patt
	 * 
	 * @param isWhite
	 * @return
	 */
	boolean isPatt(King theKing, ChessBoard aBoard) {
		Collection<Piece> allPieces = theKing.isWhite() ? aBoard.getWhitePieces() : aBoard.getBlackPieces();
		// kolla f�rst om inga pj�ser kan flytta
		for (Piece p : allPieces) {
			if (p.getMoveablePositions().size() > 0) {
				return false;
			}
		}
		// om det inte fanns n�gra pj�ser som kunde flytta s� kolla kungen
		if (theKing.getMoveablePositions().size() > 0) {
			return false;
		}
		return true; // ingen kan flytta men kungen �r inte i schack
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
		// flytta pj�sen fr�n den gamla rutan
		Square oldSq = copyOfChessBoard.getSquare(pieceToMove.getPosition());
		Square newSq = copyOfChessBoard.getSquare(theMove.getToPos());
		// och ta bort fr�n alla rutor som den kunde flytta till
		Movement.removeMoveablePositions(pieceToMove, copyOfChessBoard);
		// och flytta pj�sen till den nya rutan
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

		// nu ska alla pj�ser som p�verkades av flytten fr�n den gamla rutan
		// uppdateras
		updateMoveables(oldSq, copyOfChessBoard, true);
		updateMoveables(oldSq, copyOfChessBoard, false);

		// och alla pj�ser som p�verkades av flytten till den nya rutan
		updateMoveables(newSq, copyOfChessBoard, true);
		updateMoveables(newSq, copyOfChessBoard, false);

		// och kungarna
		King whiteKing = (King) copyOfChessBoard.getSquare(copyOfChessBoard.getWhiteKingPos()).getPieceInSquare();
		King blackKing = (King) copyOfChessBoard.getSquare(copyOfChessBoard.getBlackKingPos()).getPieceInSquare();
		// g�r om flyttbara rutor
		Movement.removeMoveablePositions(whiteKing, copyOfChessBoard);
		Movement.getInstance().initMoveables(whiteKing, copyOfChessBoard);
		Movement.removeMoveablePositions(blackKing, copyOfChessBoard);
		Movement.getInstance().initMoveables(blackKing, copyOfChessBoard);
		// och s� schacklinjerna
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
			// och ta bort att pj�sen kan flytta till resp ruta
			Movement.removeMoveablePositions(aPiece, theBoard);
			// l�gg in nya rutor
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
