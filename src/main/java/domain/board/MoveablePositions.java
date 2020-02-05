package domain.board;

import java.util.ArrayList;
import java.util.Collection;

public class MoveablePositions {

	private Collection<Position> movablePositions = new ArrayList<Position>();
	
	private ChessBoard theBoard;

	public MoveablePositions(ChessBoard theBoard){
		this.theBoard = theBoard;
	}
	
	public void addPosition(Position pos){
		
	}
	
	public Collection<Position> getMovablePositions() {
		return movablePositions;
	}

}
