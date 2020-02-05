package domain.pieces;

import domain.board.Position;
import org.junit.Test;

import static org.junit.Assert.*;

public class BishopTest {

    @Test
    public void getCopy() {
        Bishop test = Bishop.white(new Position(Position.Letter.C, Position.Num._1));

        Bishop copy = (Bishop)test.getCopy();
        assertTrue(copy.isWhite);
        assertTrue(copy.getMoveablePositions().size() == test.getMoveablePositions().size());
    }
}