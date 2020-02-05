package domain.movements;

public enum HorseDirectionValueEnum {

	HORSE_1(-1, 2),
	HORSE_2(1, 2),
	HORSE_3(2, 1),
	HORSE_4(2, -1),
	HORSE_5(1, -2),
	HORSE_6(-1, -2),
	HORSE_7(-2, -1),
	HORSE_8(-2, 1);
	
	int x;
	int y;
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	private HorseDirectionValueEnum(int x, int y){
		this.x = x;
		this.y = y;
	}
}
