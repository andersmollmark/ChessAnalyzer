package domain.movements;

import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void testGetOppositeOfPlusXDirection() {
        Direction plusX = Direction.getDirection(DirectionValueEnum.PLUS_X);

        assertTrue(plusX.getX() == 1);
        assertTrue(plusX.getY() == 0);
        Direction opposite = Direction.getOppositeDirection(plusX);
        assertTrue(opposite.getX() == -1);
        assertTrue(opposite.getY() == 0);
    }

    @Test
    public void testGetOppositeOfPlusXMinusYDirection() {
        Direction plusXMinusY = Direction.getDirection(DirectionValueEnum.PLUS_X_MINUS_Y);

        assertTrue(plusXMinusY.getX() == 1);
        assertTrue(plusXMinusY.getY() == -1);
        Direction opposite = Direction.getOppositeDirection(plusXMinusY);
        assertTrue(opposite.getX() == -1);
        assertTrue(opposite.getY() == 1);
    }

}