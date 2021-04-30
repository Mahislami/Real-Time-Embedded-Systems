package ut.cps.teeter.ui.main;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BallTest {

    @Test
    public void accelerationTest() {
        Ball ball = new Ball(1, new GameTable());
        ball.init(new Coordination(0,0), new Coordination(0,0));
        ball.m = 1;
        Assert.assertEquals("zero acc", 0.0, ball.acceleration(1, -1), 0);
        Assert.assertEquals("zero acc", 10.0, ball.acceleration(11, -1), 0);
    }

    @Test
    public void posTest() {
        Ball ball = new Ball(1, new GameTable());
        ball.init(new Coordination(0,0), new Coordination(0,0));
        ball.m = 1;
        double x1 = ball.position(1, 1, 1, 0);
        Assert.assertEquals(x1, 1.5, 0);
        double x2 = ball.position(1, 1, 1, x1);
        Assert.assertEquals(x2, 3, 0);
    }
}