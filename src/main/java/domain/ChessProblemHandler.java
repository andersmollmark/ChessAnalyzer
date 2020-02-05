package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import domain.board.ChessBoard;
import domain.board.Position;
import domain.board.Square;
import domain.movements.Movement;
import domain.pieces.Bishop;
import domain.pieces.Horse;
import domain.pieces.Pawn;
import domain.pieces.Piece;
import domain.pieces.Queen;
import domain.pieces.Tower;
import domain.pieces.King;

/**
 * Huvudhanteraren...
 * @author amollmar
 *
 */
public class ChessProblemHandler {

	private ChessBoard theBoard = new ChessBoard(0);
		
	private Movement movement = Movement.getInstance();
	
	public void init() throws IOException{
		// initiera br�det
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				theBoard.addSquare(new Square(Position.Letter.getLetter(i), Position.Num.getNum(j)));
			}
		}
		Collection<Piece> whitePieces = theBoard.getWhitePieces();
		Collection<Piece> blackPieces = theBoard.getBlackPieces();
		
		// Get the pieces to be in the game
		System.out.println("Input which white pieces that should be within the game.");
		System.out.println("When youre done, type black.");
		System.out.println("Type the first (swedish for now) letter for the piece followed by the the square, \n" +
				" ex: ba2, to place a pawn on a2");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		
		System.out.println("input=" + input);
		boolean isWhite = true;
		int indexOfWhiteKing = 0;
		int indexOfBlackKing = 0;
		int index = 0;
		while(!input.equals("klar")){
			
			Collection<Piece> pieces = isWhite ? whitePieces : blackPieces;
			if(input.length()!=3){
				System.out.println(" du skrev:" + input + " och det �r fel antal tecken. Skriv ex. ba2");				
			}
			
			if(Position.getLetterX(input)>7){
				System.out.println(" du skrev:" + input + " och det �r en felaktig ruta");
			}
			// h�mta positionen p� denna kordinaten
			Position pos = getPos(input);
			Piece p = null;
			p = getPiece(input, isWhite, pos, p);
			// L�gg till pj�sen till collectionen i denna klassen
			pieces.add(p);
			// 'Kom ih�g' index f�r kungarna s� att dom ska kunna hanteras f�rst nedan
			if(p instanceof King){
				if(isWhite){
					indexOfWhiteKing = index;
					theBoard.setWhiteKingPos(pos);
				}
				else{
					indexOfBlackKing = index;
					theBoard.setBlackKingPos(pos);
				}
			}
			// och l�gg till den till rutan p� br�det
			theBoard.addPieceInSquare(p);
			
			System.out.println("Du matade in:" + input + " Mata in en ny pj�s eller skriv klar:");
			input = br.readLine();
			
			index++;
			if(input.equals("svart")){
				index = 0;
				isWhite = false;
				System.out.println("Du har valt att mata in svarta pj�ser.");
				System.out.println("Mata in en ny pj�s eller skriv klar:");
				input = br.readLine();
			}
		}
		
		System.out.println("Antal vita pj�ser=" + whitePieces.size() + " och antal svarta pj�ser=" + blackPieces.size());
		// initiera alla rutor som g�r att flytta till
		for(int i=0; i<2; i++){
			Collection<Piece> pieces = i == 0 ? whitePieces : blackPieces;
			for(Piece p : pieces){
			// FIXME	movement.initMoveables(p, theBoard, false);
			}
		}
		
		// s�tt ut schacklinjerna f�r kungen
		// vit
		Piece theWhiteKing = ((ArrayList<Piece>)whitePieces).get(indexOfWhiteKing);
	// FIXME	movement.initMoveables(theWhiteKing, theBoard, true);
		// och svart
		Piece theBlackKing = ((ArrayList<Piece>)blackPieces).get(indexOfBlackKing);
	// FIXME	movement.initMoveables(theBlackKing, theBoard, true);

		
		// g�r om kungarnas flyttbara rutor pga att den inte f�r flytta till rutor som st�r i slag.
		// och s� f�r inte den andra kungen st� intill en flyttbar ruta
//		theWhiteKing.getMoveablePositions().clear();
		Movement.removeMoveablePositions(theWhiteKing, theBoard);
	// FIXME	movement.initMoveables(theWhiteKing, theBoard, false);
		// g�r om kungarnas flyttbara rutor pga att den inte f�r flytta till rutor som st�r i slag.
//		theBlackKing.getMoveablePositions().clear();
		Movement.removeMoveablePositions(theBlackKing, theBoard);
		// FIXME movement.initMoveables(theBlackKing, theBoard, false);
		
		
		
		for(int i=0; i<2; i++){
			Collection<Piece> pieces = i == 0 ? whitePieces : blackPieces;
			for(Piece p : pieces){
				System.out.println(p.toString());
				Collection<Position> moveablePosistions = p.getMoveablePositions();
				if(moveablePosistions.size()==0){
					System.out.println("Pj�sen f�r inte flytta");
				}
				for(Position pos : moveablePosistions){
					System.out.print(pos.toString() + " ");
				}
				if(p instanceof King){
					Collection<Position> checkable = ((King)p).getCheckAblePositions();
					System.out.println("\nKungens schacklinjer:\n");
					for(Position pos : checkable){
						System.out.print(pos.toString() + " ");
					}
					
					
				}
				System.out.println("");
			}
		}
		
		br.close();
	}

	private Piece getPiece(String input, boolean isWhite, Position pos, Piece p) {
		String pLetter = input.substring(0, 1);
		if("b".equals(pLetter)){
			p = isWhite ? Pawn.white(pos) : Pawn.black(pos);
		}
		else if("l".equals(pLetter)){
			p = isWhite ? Bishop.white(pos) : Bishop.black(pos);
		}
		else if("s".equals(pLetter) || "h".equals(pLetter)){
			p = isWhite ? Horse.white(pos) : Horse.black(pos);
		}
		else if("t".equals(pLetter)){
			p = isWhite ? Tower.white(pos) : Tower.black(pos);
		}
		else if("d".equals(pLetter)){
			p = isWhite ? Queen.white(pos) : Queen.black(pos);
		}
		else if("k".equals(pLetter)){
			p = isWhite ? King.white(pos) : King.black(pos);
		}
		else{
			String mess = "Du skrev en felaktig bokstav:" + pLetter + "\n";
			mess = mess + getPieceString();
			throw new IllegalArgumentException(mess);
		}
		return p;
	}
	
	public Movement getMovement(){
		return movement;
	}
	
	private Position getPos(String input){
		int x = Position.getLetterX(input);
		int y = getY(input);
		return getBoard().getPosition(x, y);
	}
	
	private String getPieceString(){
		return "b = bonde, s = springare, l = l�pare, \nt = torn, d = dam och k = kung";
	}

	private int getY(String input){
		return (Integer.parseInt(Character.toString(input.charAt(2))))-1;
	}

	public ChessBoard getBoard() {
		return theBoard;
		
	}
	
	public void setBoard(ChessBoard board){
		theBoard = board;
	}
}
