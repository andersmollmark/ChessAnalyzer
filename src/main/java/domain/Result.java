package domain;

import domain.board.AMove;
import domain.board.ChessBoard;

public class Result {

	private boolean isRemi = false;
	private boolean isInCheck = false;
	private boolean blackSuccess = false;
	private boolean whiteSuccess = false;
	private AMove theMove;
	
	private ChessBoard theBoard;
	
	public Result(){}
	
	public boolean isInCheck() {
		return isInCheck;
	}

	public void setInCheck(boolean isInCheck) {
		this.isInCheck = isInCheck;
	}

	public Result(AMove theMove){
		this.theMove = theMove;
	}
	
	public AMove getTheMove() {
		return theMove;
	}
	public void setTheMove(AMove theMove) {
		this.theMove = theMove;
	}
	public boolean isRemi() {
		return isRemi;
	}
	public void setRemi(boolean isRemi) {
		this.isRemi = isRemi;
	}
	public boolean isBlackSuccess() {
		return blackSuccess;
	}
	public void setBlackSuccess(boolean blackSuccess) {
		this.blackSuccess = blackSuccess;
	}
	public boolean isWhiteSuccess() {
		return whiteSuccess;
	}
	public void setWhiteSuccess(boolean whiteSuccess) {
		this.whiteSuccess = whiteSuccess;
	}
	
	@Override
	public String toString(){
		String s = "Remi:" + isRemi;
		s += "\nSvart vinst:" + blackSuccess;
		s += "\nVit vinst:" + whiteSuccess;
		if(theMove != null && theMove.getThePiece() != null){
			s += "\nDrag : " + theMove.getThePiece().toString() + " " + theMove.getToPos().toString();
		}
		return s;
	}

	public ChessBoard getTheBoard() {
		return theBoard;
	}

	public void setTheBoard(ChessBoard theBoard) {
		this.theBoard = theBoard;
	}
	
}
