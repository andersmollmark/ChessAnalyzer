package domain.movements;

import java.util.Collection;
import java.util.List;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Square;
import domain.pieces.Bishop;
import domain.pieces.King;
import domain.pieces.Piece;
import domain.pieces.Queen;
import domain.pieces.Tower;

public class Movement {

	private static Movement instance = null;

	private Movement() {
		// empty by design
	}

	public static Movement getInstance() {
		if (instance == null) {
			instance = new Movement();
		}
		return instance;
	}

	public void initAllMoveables(ChessBoard theBoard) {
		List<Piece> whitePieces = theBoard.getWhitePieces();
		for (Piece p : whitePieces) {
			initMoveables(p, theBoard);
		}
		List<Piece> blackPieces = theBoard.getBlackPieces();
		for (Piece p : blackPieces) {
			initMoveables(p, theBoard);
		}
	}

	/**
	 * Uppdaterar br�det s� att alla rutor dit pj�sen kan flytta till 'markeras'.
	 * 
	 * @param the piece vars f�rflyttningar ska l�ggas in p� br�det
	 * @param theBoard
	 *            �r br�det
	 * @param setCheckable
	 *            �r true om det �r en kung som ska s�tta upp vilka rutor som ligger i schacklinje
	 */
	public void initMoveables(Piece pieceToMove, ChessBoard theBoard) {

		List<Direction> allDirections = pieceToMove.getMoveDirections();
		for (Direction direction : allDirections) {
			initDirectionMoves(pieceToMove, direction, theBoard);

		}
	}

	public void initDirectionMoves(Piece piece, Direction direction, ChessBoard theBoard) {
		Square aSquare = theBoard.getSquare(piece.getPosition());
		for (int i = 0; i < piece.getMaxNumberOfMoves(); i++) {
			Square nextSquare = getNextSquare(aSquare, direction, theBoard);
			if (nextSquare == null) {
				break;
			}
			if (isEmptySquare(nextSquare) && direction.isMoveDirection()) {
				initMoves(piece, nextSquare, direction, theBoard);
				aSquare = nextSquare;
			}
			else if (hasPieceInOppositeColor(nextSquare, piece) && direction.isStrikeDirection()) {
				initMoves(piece, nextSquare, direction, theBoard);
				break;
			}
			else {
				break;
			}
		}
	}

	private Square getNextSquare(Square oldSquare, Direction direction, ChessBoard theBoard) {
		Square newSquare = null;
		Position nextPosition = getNextPosition(oldSquare.getPosition(), direction);
		if (Position.isInside(nextPosition)) {
			newSquare = theBoard.getSquare(nextPosition);
		}
		return newSquare;
	}

	private Position getNextPosition(Position oldPos, Direction direction) {
		int oldX = oldPos.getX();
		int oldY = oldPos.getY();
		int newX = oldX + direction.getX();
		int newY = oldY + direction.getY();
		return new Position(newX, newY);
	}

	private void initMoves(Piece piece, Square newSquare, Direction direction, ChessBoard theBoard) {
		if (piece instanceof King) {
			initKingStuff(newSquare, (King) piece, direction, theBoard);
		}
		else {
			newSquare.addPieceThatCanMoveHere(piece);
			piece.addMoveablePosition(newSquare.getPosition());
		}
	}

	private boolean isEmptySquare(Square newSquare) {
		return newSquare.getPieceInSquare() == null;
	}

	private boolean hasPieceInOppositeColor(Square newSquare, Piece piece) {
		Piece otherPiece = newSquare.getPieceInSquare();
		if (otherPiece == null) {
			return false;
		}
		return piece.isOppositeColor(otherPiece);
	}

	private void initKingStuff(Square newSquare, King theKing, Direction direction, ChessBoard theBoard) {
		Piece squarePiece = newSquare.getPieceInSquare();
		if (squarePiece != null) {
			removeCheckableMoves(theKing, direction, newSquare, theBoard);
		}

		addCheckablePositions(theKing, newSquare, direction, theBoard);

		List<Piece> oppositePieces = theKing.isWhite() ? newSquare.getBlackPiecesThatCanMoveHere() : newSquare
				.getWhitePiecesThatCanMoveHere();
		Position otherKingPos = theKing.isWhite() ? theBoard.getBlackKingPos() : theBoard.getWhiteKingPos();
		if (isOkPositionForTheKing(otherKingPos, newSquare, oppositePieces)) {
			newSquare.addPieceThatCanMoveHere(theKing);
			theKing.addMoveablePosition(newSquare.getPosition());
		}
	}

	private void addCheckablePositions(King theKing, Square newSquare, Direction direction, ChessBoard theBoard) {
		Square tempSquare = newSquare;
		while (tempSquare != null && tempSquare.getPieceInSquare() == null) {
			theKing.getCheckAblePositions().add(tempSquare.getPosition());
			tempSquare = getNextSquare(tempSquare, direction, theBoard);
		}
	}

	private boolean isOkPositionForTheKing(Position otherKingPos, Square newSquare, List<Piece> oppositePieces) {
		return oppositePieces.size() == 0 && !isToCloseToOtherKing(otherKingPos, newSquare);
	}

	private boolean isToCloseToOtherKing(Position otherKingPos, Square newSquare) {
		int xDistToOtherKing = Math.abs(otherKingPos.getX() - newSquare.getPosition().getX());
		int yDistToOtherKing = Math.abs(otherKingPos.getY() - newSquare.getPosition().getY());
		return xDistToOtherKing < 2 && yDistToOtherKing < 2;
	}

	/**
	 * N�r en kung ska l�gga ut sina schacklinjer (dom linjer som kan s�tta kungen i schack) anropas denna n�r man
	 * kommer till en ruta med en pj�s. H�r m�ste kollas om det �r en pj�s i samma f�rg som kungen har. Om det �r s� ska
	 * dess m�jliga flyttbara rutor kanske g�ras om. Orsaken �r att om den flyttas kan den kanske s�tta kungen i schack
	 * om det finns n�gon pj�s i den andra f�rgen som sl�r mot kungen. T.ex. om en bonde st�r mellan en l�pare och en
	 * kung och om bonden flyttar s� s�tts kungen i schack. D� ska bondens flyttbara rutor �ndras.
	 * 
	 * @param theKing
	 *            kungen som det g�ller
	 * @param theBoard
	 *            schackbr�det
	 * @param direction
	 *            schacklinjen som ska kollas
	 * @param newSquare
	 *            rutan som har en pj�s som ska kollas
	 * @param squarePiece
	 *            pj�sen i rutan
	 */
	public void removeCheckableMoves(King theKing, Direction direction, Square newSquare, ChessBoard theBoard) {
		Piece squarePiece = newSquare.getPieceInSquare();
		if (!theKing.isOppositeColor(squarePiece)) {
			List<Piece> oppositePieces = theKing.isWhite() ? newSquare.getBlackPiecesThatCanMoveHere() : newSquare
					.getWhitePiecesThatCanMoveHere();
			Direction oppositeDirection = Direction.getOppositeDirection(direction);
			if (oppositePieces.size() > 0 && hasCheckablePiece(oppositePieces, oppositeDirection)) {
				// m�ste rensa var pj�sen f�r flytta
				reinitializeMoves(squarePiece, direction, theBoard);
			}

		}
	}

	private void reinitializeMoves(Piece p, Direction direction, ChessBoard theBoard) {
		p.getMoveablePositions().clear();
		Direction oppositeDirection = Direction.getOppositeDirection(direction);
		for (Direction pieceDirection : p.getMoveDirections()) {
			// Kolla om det �r en ok direction att flytta l�ngs
			if (pieceDirection.equals(oppositeDirection) || pieceDirection.equals(direction)) {

				initDirectionMoves(p, pieceDirection, theBoard);
			}
		}
	}

	private boolean hasCheckablePiece(List<Piece> theOtherTeam, Direction checkableDirection) {
		for (Piece otherPiece : theOtherTeam) {
			if (isCheckablePiece(otherPiece, checkableDirection)) {
				return true;
			}
		}
		return false;
	}

	private boolean isCheckablePiece(Piece p, Direction checkableDirection) {
		return isCheckableType(p) && hasCheckableDirection(p, checkableDirection);
	}

	private boolean hasCheckableDirection(Piece p, Direction checkableDirection) {
		for (Direction otherDirection : p.getMoveDirections()) {
			if (otherDirection.equals(checkableDirection)) {
				return true;
			}
		}
		return false;
	}

	private boolean isCheckableType(Piece p) {
		return p instanceof Bishop || p instanceof Queen || p instanceof Tower;
	}

	public static void removeMoveablePositions(Piece remPiece, ChessBoard theBoard) {
		Collection<Position> moveablePositions = remPiece.getMoveablePositions();
		for (Position pos : moveablePositions) {
			theBoard.getSquare(pos).removePieceMove(remPiece);
		}
		moveablePositions.clear();
	}

}
