package domain;

import java.util.ArrayList;
import java.util.List;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.pieces.Bishop;
import domain.pieces.Horse;
import domain.pieces.King;
import domain.pieces.Pawn;
import domain.pieces.Piece;
import domain.pieces.Queen;
import domain.pieces.Tower;

/**
 * Huvudhanteraren...
 * 
 * @author amollmar
 * 
 */
public class ChessBoardSnapshots {

	private final List<ChessBoard> allTheBoards = new ArrayList<ChessBoard>();

	public ChessBoardSnapshots() {
		for (int i = 0; i < 8; i++) {
			allTheBoards.add(null);
		}
		ChessBoard aChessBoard = new ChessBoard(0);
		allTheBoards.set(0, aChessBoard);
	}

	// public void init() throws IOException {
	//
	// // Hämta pjäser
	// System.out.println("Nu ska det matas in pjäser.");
	// System.out.println("Först mata in vilka vita pjäser som det ska finnas med.");
	// System.out.println("När dom vita är klara skriv svart.");
	// System.out.println("Skriv första bokstaven i pjäsnamnet följt av ruta den ska stå i, \n"
	// + " ex: ba2, för en bonde på a2");
	//
	// handlePieceInput();
	//
	// System.out
	// .println("Antal vita pjäser=" + whitePieces.size() + " och antal svarta pjäser=" + blackPieces.size());
	// // initiera alla rutor som går att flytta till
	// // for (int i = 0; i < 2; i++) {
	// // Collection<Piece> pieces = i == 0 ? whitePieces : blackPieces;
	// // for (Piece p : pieces) {
	// // movement.initMoveables(p);
	// // }
	// // }
	// setMovablePositions(whitePieces);
	// setMovablePositions(blackPieces);
	//
	// // sätt ut schacklinjerna för kungen
	// // vit
	// King theWhiteKing = theBoard.getWhiteKing();
	// movement.initMoveables(theWhiteKing);
	// // och svart
	// King theBlackKing = theBoard.getBlackKing();
	// movement.initMoveables(theBlackKing);
	//
	// // gör om kungarnas flyttbara rutor pga att den inte får flytta till
	// // rutor som står i slag.
	// // och så får inte den andra kungen stå intill en flyttbar ruta
	// // theWhiteKing.getMoveablePositions().clear();
	// Movement.removeMoveablePositions(theWhiteKing, theBoard);
	// movement.initMoveables(theWhiteKing);
	// // gör om kungarnas flyttbara rutor pga att den inte får flytta till
	// // rutor som står i slag.
	// // theBlackKing.getMoveablePositions().clear();
	// Movement.removeMoveablePositions(theBlackKing, theBoard);
	// movement.initMoveables(theBlackKing);
	//
	// for (int i = 0; i < 2; i++) {
	// Collection<Piece> pieces = i == 0 ? whitePieces : blackPieces;
	// for (Piece p : pieces) {
	// System.out.println(p.toString());
	// Collection<Position> moveablePosistions = p.getMoveablePositions();
	// if (moveablePosistions.size() == 0) {
	// System.out.println("Pjäsen får inte flytta");
	// }
	// for (Position pos : moveablePosistions) {
	// System.out.print(pos.toString() + " ");
	// }
	// if (p instanceof King) {
	// Collection<Position> checkable = ((King) p).getCheckAblePositions();
	// System.out.println("\nKungens schacklinjer:\n");
	// for (Position pos : checkable) {
	// System.out.print(pos.toString() + " ");
	// }
	//
	// }
	// System.out.println("");
	// }
	// }
	//
	// }

	// public void setMovablePositions(List<Piece> pieces) {
	// for (Piece p : pieces) {
	// movement.initMoveables(p);
	// }
	// }

	// private void handlePieceInput() {
	// try {
	// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	// String input = br.readLine();
	//
	// System.out.println("input=" + input);
	// boolean isWhite = true;
	// int indexOfWhiteKing = 0;
	// int indexOfBlackKing = 0;
	// int index = 0;
	// while (!input.equals("klar")) {
	//
	// List<Piece> pieces = isWhite ? whitePieces : blackPieces;
	// if (input.length() != 3) {
	// System.out.println(" du skrev:" + input + " och det är fel antal tecken. Skriv ex. ba2");
	// }
	//
	// if (Position.getLetterX(input) > 7) {
	// System.out.println(" du skrev:" + input + " och det är en felaktig ruta");
	// }
	// // hämta positionen på denna kordinaten
	// Position pos = getPos(input);
	// Piece p = null;
	// p = getPiece(input, isWhite, pos, p);
	// // Lägg till pjäsen till collectionen i denna klassen
	// pieces.add(p);
	// // 'Kom ihåg' index för kungarna så att dom ska kunna hanteras först
	// // nedan
	// if (p instanceof King) {
	// if (isWhite) {
	// indexOfWhiteKing = index;
	// theBoard.setWhiteKingPos(pos);
	// }
	// else {
	// indexOfBlackKing = index;
	// theBoard.setBlackKingPos(pos);
	// }
	// }
	// // och lägg till den till rutan på brädet
	// theBoard.addPieceInSquare(p);
	//
	// System.out.println("Du matade in:" + input + " Mata in en ny pjäs eller skriv klar:");
	// input = br.readLine();
	//
	// index++;
	// if (input.equals("svart")) {
	// index = 0;
	// isWhite = false;
	// System.out.println("Du har valt att mata in svarta pjäser.");
	// System.out.println("Mata in en ny pjäs eller skriv klar:");
	// input = br.readLine();
	// }
	// }
	// br.close();
	// }
	// catch (IOException ioe) {
	// System.out.println("Nu gick något fel..." + ioe.getMessage());
	// }
	// }

	private Piece getPiece(String input, boolean isWhite, Position pos, Piece p) {
		String pLetter = input.substring(0, 1);
		if ("b".equals(pLetter)) {
			p = isWhite ? Pawn.white(pos) : Pawn.black(pos);
		}
		else if ("l".equals(pLetter)) {
			p = isWhite ? Bishop.white(pos) : Bishop.black(pos);
		}
		else if ("s".equals(pLetter) || "h".equals(pLetter)) {
			p = isWhite ? Horse.white(pos) : Horse.black(pos);
		}
		else if ("t".equals(pLetter)) {
			p = isWhite ? Tower.white(pos) : Tower.black(pos);
		}
		else if ("d".equals(pLetter)) {
			p = isWhite ? Queen.white(pos) : Queen.black(pos);
		}
		else if ("k".equals(pLetter)) {
			p = isWhite ? King.white(pos) : King.black(pos);
		}
		else {
			String mess = "Du skrev en felaktig bokstav:" + pLetter + "\n";
			mess = mess + getPieceString();
			throw new IllegalArgumentException(mess);
		}
		return p;
	}

	// private Position getPos(String input) {
	// int x = Position.getLetterX(input);
	// int y = getY(input);
	// return getBoard().getPosition(x, y);
	// }

	private String getPieceString() {
		return "b = bonde, s = springare, l = löpare, \nt = torn, d = dam och k = kung";
	}

	private int getY(String input) {
		return (Integer.parseInt(Character.toString(input.charAt(2)))) - 1;
	}

	public ChessBoard getBoard(int index) {
		return allTheBoards.get(index);

	}

	public void setBoard(int index, ChessBoard board) {
		allTheBoards.set(index, board);
	}
}
