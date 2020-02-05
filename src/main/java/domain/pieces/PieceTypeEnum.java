package domain.pieces;

public enum PieceTypeEnum {

	PAWN("Bonde"),
	HORSE("H�st"),
	BISHOP("L�pare"),
	TOWER("Torn"),
	QUEEN("Dam"),
	KING("Kung");
	
	String typ;
	
	private PieceTypeEnum(String typ){
		this.typ = typ;
	}

	public String getTyp() {
		return typ;
	}
}
