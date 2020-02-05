package domain.movements;

public enum DirectionValueEnum {

		PLUS_X(1, 0),
		PLUS_Y(0, 1),
		MINUS_X(-1, 0),
		MINUS_Y(0, -1),
		MINUS_X_MINUS_Y(-1, -1),
		PLUS_X_MINUS_Y(1, -1),
		MINUS_X_PLUS_Y(-1, 1),
		PLUS_X_PLUS_Y(1, 1);
		
		int x;
		int y;
		
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
		private DirectionValueEnum(int x, int y){
			this.x = x;
			this.y = y;
		}
}
