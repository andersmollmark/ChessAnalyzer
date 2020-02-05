package domain.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.board.Position.Letter;
import domain.board.Position.Num;
import domain.pieces.King;
import domain.pieces.Piece;

public class ChessBoard {

	// Brädet med alla rutor
	Square[][] theBoard = new Square[8][8];

	// Vilka pjäser som finns
	private List<Piece> whitePieces = new ArrayList<Piece>();
	private List<Piece> blackPieces = new ArrayList<Piece>();

	private Position whiteKingPos;
	private Position blackKingPos;

	private int whiteMoveNr = 1;
	private int blackMoveNr = 1;

	private int drawNr = 0;

	public ChessBoard(int drawNr) {
		this.drawNr = drawNr;
		for (Letter l : Letter.values()) {
			for (Num n : Num.values()) {
				addSquare(new Square(l, n));
			}
		}
	}

	public int getWhiteMoveNr() {
		return whiteMoveNr;
	}

	public void addBlackMove() {
		blackMoveNr++;
	}

	public void addWhiteMove() {
		whiteMoveNr++;
	}

	public int getMoveNr(Piece p) {
		return p.isWhite() ? whiteMoveNr : blackMoveNr;
	}

	public void setWhiteMoveNr(int move) {
		whiteMoveNr = move;
	}

	public void setBlackMoveNr(int move) {
		blackMoveNr = move;
	}

	public Position getWhiteKingPos() {
		return whiteKingPos;
	}

	public King getWhiteKing() {
		return (King) getSquare(getWhiteKingPos()).getPieceInSquare();
	}

	public void setWhiteKingPos(Position whiteKingPos) {
		this.whiteKingPos = whiteKingPos;
	}

	public Piece getPiece(String pos) {
		if (pos.length() != 2) {
			throw new IllegalArgumentException("pos får bara innehålla 2 tecken:" + pos);
		}
		pos = "X" + pos; // lägg på dummybokstav så att det går att använda samma metod
		int x = Position.getLetterX(pos);
		int y = Position.getNumberY(pos);
		return getSquare(new Position(x, y)).getPieceInSquare();
	}

	public Position getBlackKingPos() {
		return blackKingPos;
	}

	public King getBlackKing() {
		return (King) getSquare(getBlackKingPos()).getPieceInSquare();
	}

	public void setBlackKingPos(Position blackKingPos) {
		this.blackKingPos = blackKingPos;
	}

	public List<Piece> getWhitePieces() {
		return whitePieces;
	}

	public List<Piece> getBlackPieces() {
		return blackPieces;
	}

	public List<Square> getSquaresBetween(Square fromSquare, Square toSquare) {
		int fromX = fromSquare.getPosition().getX();
		int fromY = fromSquare.getPosition().getY();
		int toX = toSquare.getPosition().getX();
		int toY = toSquare.getPosition().getY();

		List<Integer> xPos = getIntsBetween(fromX, toX);
		List<Integer> yPos = getIntsBetween(fromY, toY);

		List<Square> result = null;
		if (xPos.size() == 0) {
			result = getSquaresSameLetterRow(fromX, yPos);
		}
		else if (yPos.size() == 0) {
			result = getSquaresSameNumberRow(fromY, xPos);
		}
		else {
			result = getSquaresDiffLetterAndNumber(xPos, yPos);
		}
		return result;
	}

	private List<Square> getSquaresDiffLetterAndNumber(List<Integer> xPos, List<Integer> yPos) {
		List<Square> result = new ArrayList<Square>();
		for (int i = 0; i < xPos.size(); i++) {
			Square aSquare = getSquare(xPos.get(i), yPos.get(i));
			result.add(aSquare);
		}
		return result;
	}

	private List<Square> getSquaresSameLetterRow(int fromX, List<Integer> yPos) {
		List<Square> result = new ArrayList<Square>();
		// samma bokstavsrad
		for (Integer y : yPos) {
			Square aSquare = getSquare(fromX, y);
			result.add(aSquare);
		}
		return result;
	}

	private List<Square> getSquaresSameNumberRow(int fromY, List<Integer> xPos) {
		List<Square> result = new ArrayList<Square>();
		// samma bokstavsrad
		for (Integer x : xPos) {
			Square aSquare = getSquare(x, fromY);
			result.add(aSquare);
		}
		return result;
	}

	private List<Integer> getIntsBetween(int from, int to) {
		List<Integer> pos = new ArrayList<Integer>();
		// -2 för att from och to- platserna inte ska med
		int antalPos = Math.abs(from - to) - 1;
		// +1 för att inte ngn av from och to-platserna ska utgöra indexstart
		int minst = (from < to ? from : to) + 1;
		for (int i = 0; i < antalPos; i++) {
			pos.add(Integer.valueOf(minst++));
		}
		return pos;
	}

	public void addSquare(Square sq) {
		if (isOkPos(sq.getPosition())) {
			theBoard[sq.getPosition().getX()][sq.getPosition().getY()] = sq;
		}
	}

	public Piece addPieceInSquare(Piece p) {
		if (!isOkPos(p.getPosition())) {
			throw new IllegalArgumentException("Ej ok position:" + p.getPosition());
		}
		getSquare(p.getPosition()).setPieceInSquare(p);
		setKingposIfKingPiece(p);
		addPieceToList(p);
		return p;
	}

	void setKingposIfKingPiece(Piece p) {
		if (p instanceof King) {
			if (p.isWhite()) {
				whiteKingPos = p.getPosition();
			}
			else {
				blackKingPos = p.getPosition();
			}
		}
	}

	void addPieceToList(Piece p) {
		if (p.isWhite()) {
			whitePieces.add(p);
		}
		else {
			blackPieces.add(p);
		}
	}

	public Square getSquare(Position p) {
		return theBoard[p.getX()][p.getY()];
	}

	public Square getSquare(Piece p) {
		return getSquare(p.getPosition());
	}

	public Position getPosition(int x, int y) {
		return theBoard[x][y].getPosition();
	}

	public Square getSquare(int x, int y) {
		return getSquare(getPosition(x, y));
	}

	public boolean isOkPos(Position p) {
		if (Position.isInside(p.getX()) && Position.isInside(p.getY())) {
			return true;
		}
		return false;
	}

	public List<Piece> getOppositePiecesThatCanCheck(King theKing) {
		Square theSquare = getSquare(theKing.getPosition());
		return theSquare.getOppositePiecesThatCanMoveHere();
	}

	public ChessBoard getCopy(int drawNr) {
		ChessBoard aBoard = copyJustTheBoard(drawNr);
		aBoard.setBlackPieces(copyPiecesToSquares(getBlackPieces(), aBoard));
		aBoard.setWhitePieces(copyPiecesToSquares(getWhitePieces(), aBoard));

		return aBoard;
	}

	public ChessBoard copyJustTheBoard(int drawNr) {
		ChessBoard aBoard = new ChessBoard(drawNr);
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				aBoard.addSquare(getSquare(x, y).getCopy());
			}
		}
		return aBoard;
	}

	public ArrayList<Piece> copyPiecesToSquares(Collection<Piece> pieceList, ChessBoard boardCopy) {
		ArrayList<Piece> returList = new ArrayList<Piece>();
		Piece copy = null;
		for (Piece p : pieceList) {
			copy = p.getCopy();
			returList.add(copy);
			boardCopy.addPieceInSquare(copy);
		}
		return returList;
	}

	public void movePiece(Piece thePiece, Position newPosition) {
		getSquare(thePiece.getPosition()).setPieceInSquare(null);
		getSquare(newPosition).setPieceInSquare(thePiece);
		thePiece.moveTo(newPosition);
	}

	public void setWhitePieces(List<Piece> whitePieces) {
		this.whitePieces = whitePieces;
	}

	public void setBlackPieces(List<Piece> blackPieces) {
		this.blackPieces = blackPieces;
	}

	@Override
	public String toString() {
		String s = "Vita pjäser:\n";
		for (Piece p : getWhitePieces()) {
			s += p.toString() + ", ";
		}
		s += "\nSvarta pjäser:\n";
		for (Piece p : getBlackPieces()) {
			s += p.toString() + ", ";
		}
		return s;
	}

	public int getDrawNr() {
		return drawNr;
	}
}
