package domain.pieces;

import domain.board.Position;
import domain.movements.Direction;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PawnTest {

    @Test
    public void testWhitePawn() {
        Pawn white = Pawn.white(new Position(Position.Letter.A, Position.Num._2));

        List<Direction> directions = white.getMoveDirections();
        assertTrue(directions.size() == 3);

        Direction moveDirection = directions.get(0);
        Direction strikeDirection1 = directions.get(1);
        Direction strikeDirection2 = directions.get(2);

        assertTrue(moveDirection.isMoveDirection());
        assertFalse(strikeDirection1.isMoveDirection());
        assertFalse(strikeDirection2.isMoveDirection());

        assertFalse(moveDirection.isStrikeDirection());
        assertTrue(strikeDirection1.isStrikeDirection());
        assertTrue(strikeDirection2.isStrikeDirection());
    }

    /*@Test
    public void testWhitePawn() {
        Pawn white = Pawn.white(new Position(Position.Letter.A, Position.Num._2));

        List<Direction> directions = white.getMoveDirections();
        assertTrue(directions.size() == 3);
        assertTrue(directions.get(0).isMoveDirection());
        assertFalse(directions.get(1).isMoveDirection());
        assertFalse(directions.get(2).isMoveDirection());
    }*/

}