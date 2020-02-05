package domain.board;

import javafx.geometry.Pos;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testPos() {
        Position position = new Position(Position.Letter.A, Position.Num._2);
        assertTrue(position.getX() == 0);
        assertTrue(position.getY() == 1);
    }

    @Test
    public void testGetLetterX() {
        assertTrue(Position.getLetterX("ba2") == 0);
        assertTrue(Position.getLetterX("bh2") == 7);

    }

    @Test
    public void testGetNumberY() {
        assertTrue(Position.getNumberY("ba2") == 1);
        assertTrue(Position.getLetterX("bh8") == 7);

    }

    @Test
    public void testGetXLetter() {
        assertTrue(Position.getXLetter(0) == 'a');
        assertTrue(Position.getXLetter(7) == 'h');

    }

    @Test
    public void testIsInsideWhenInside() {
        assertTrue(Position.isInside(new Position(0, 0)));
        assertTrue(Position.isInside(new Position(7, 7)));
    }

    @Test
    public void testIsInsideWhenNotInside() {
        assertFalse(Position.isInside(new Position(0, 11)));
    }

}