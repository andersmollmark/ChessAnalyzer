package domain.board;

public class Position {

	int x;

	int y;

	public enum Letter {
		A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);

		private final int xpos;

		private Letter(int pos) {
			xpos = pos;
		}

		public int getPos() {
			return xpos;
		}

		public static Letter getLetter(int num){
			for(Letter l : Letter.values()){
				if(l.getPos() == num){
					return l;
				}
			}
			throw new IllegalArgumentException("Value: " + num + " is not ok");
		}

	}

	public enum Num {
		_1(0), _2(1), _3(2), _4(3), _5(4), _6(5), _7(6), _8(7);

		private final int ypos;

		private Num(int pos) {
			ypos = pos;
		}

		public int getPos() {
			return ypos;
		}

		public static Num getNum(int num){
			for(Num n : Num.values()){
				if(n.getPos() == num){
					return n;
				}
			}
			throw new IllegalArgumentException("Value: " + num + " is not ok");
		}


	}

	public Position(Letter l, Num n) {
		this.x = l.xpos;
		this.y = n.ypos;
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Letter getLetter(int xpos) {
		Letter result = null;
		for (Letter l : Letter.values()) {
			if (xpos == l.xpos) {
				result = l;
				break;
			}
		}
		return result;
	}

	public static Num getNum(int ypos) {
		Num result = null;
		for (Num n : Num.values()) {
			if (ypos == n.ypos) {
				result = n;
				break;
			}
		}
		return result;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Pos:" + getXLetter(getX()) + (getY() + 1);
	}

	public static int getLetterX(String input) {
		char letter = input.charAt(1);
		switch (letter) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		default:
			return 10;
		}
	}

	public static int getNumberY(String input) {
		char letter = input.charAt(2);
		switch (letter) {
		case '1':
			return 0;
		case '2':
			return 1;
		case '3':
			return 2;
		case '4':
			return 3;
		case '5':
			return 4;
		case '6':
			return 5;
		case '7':
			return 6;
		case '8':
			return 7;
		default:
			throw new IllegalArgumentException("Det andra tecknet m�ste vara mellan 1-8:" + input);
		}
	}

	public static char getXLetter(int num) {
		switch (num) {
		case 0:
			return 'a';
		case 1:
			return 'b';
		case 2:
			return 'c';
		case 3:
			return 'd';
		case 4:
			return 'e';
		case 5:
			return 'f';
		case 6:
			return 'g';
		case 7:
			return 'h';
		default:
			return 'q';
		}
	}

	public static boolean isInside(int p) {
		return p > -1 && p < 8;
	}

	public static boolean isInside(Position pos) {
		return isInside(pos.getX()) && isInside(pos.getY());
	}

	public static Position getPosition(String pos) {
		if (pos.length() != 2) {
			throw new IllegalArgumentException("pos f�r bara inneh�lla 2 tecken:" + pos);
		}
		pos = "X" + pos; // l�gg p� dummybokstav s� att det g�r att anv�nda samma metod
		int x = Position.getLetterX(pos);
		int y = Position.getNumberY(pos);
		return new Position(x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position temp = (Position) o;
			if (temp.getX() == getX() && temp.getY() == getY()) {
				return true;
			}
		}
		return false;
	}
}
