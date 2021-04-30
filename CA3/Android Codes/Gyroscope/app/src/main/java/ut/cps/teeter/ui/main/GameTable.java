package ut.cps.teeter.ui.main;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.lifecycle.MutableLiveData;


public class GameTable {

    MutableLiveData<Coordination> ballOneCor = new MutableLiveData<>();
    MutableLiveData<Coordination> ballTwoCor = new MutableLiveData<>();
    MutableLiveData<Coordination> liveTeta = new MutableLiveData<>();
    Ball ballOne = new Ball(Config.M1, this);
    Ball ballTwo = new Ball(Config.M2, this);
    Coordination max, min;
    double ballDiameter;

    @MainThread
    public void initialize(
            Coordination corOne,
            Coordination corTwo,
            Coordination velOne,
            Coordination velTwo) {
        ballOneCor.setValue(corOne);
        ballTwoCor.setValue(corTwo);
        liveTeta.setValue(new Coordination(0,0));
        ballOne.init(corOne, velOne);
        ballTwo.init(corTwo, velTwo);
    }

    @AnyThread
    public synchronized void update() {
        double tInSeconds = Config.GAME_TIME_UNIT/1000.0;
        ballOne.accelerate(tInSeconds);
        ballTwo.accelerate(tInSeconds);
        ballOne.move(tInSeconds);
        ballTwo.move(tInSeconds);
        doPossibleCollisions();
    }

    private void doPossibleCollisions() {
        collideWalls(ballOne);
        collideWalls(ballTwo);
        collide(ballOne, ballTwo);
    }

    private void collide(Ball ballOne, Ball ballTwo) {
        switch (ballOne.position.collides(ballTwo.position, ballDiameter)) {
            case 'x':
                ballTwo.velocity.mirror('x');
                ballOne.velocity.mirror('x');
                break;
            case 'y':
                ballTwo.velocity.mirror('y');
                ballOne.velocity.mirror('y');
                break;
        }
    }

    private boolean collideWalls(Ball ball) {
        boolean collision = false;
        if(ball.position.x >= max.x &&
                ball.velocity.x > 0) {
            ball.velocity.mirror('x');
            collision = true;
        }
        if(ball.position.y >= max.y &&
                ball.velocity.y > 0) {
            ball.velocity.mirror('y');
            collision = true;
        }
        if(ball.position.x <= min.x &&
                ball.velocity.x < 0) {
            ball.velocity.mirror('x');
            collision = true;
        }
        if(ball.position.y <= min.y
                && ball.velocity.y < 0) {
            ball.velocity.mirror('y');
            collision = true;
        }
        return collision;
    }

    public Coordination teta() {
        return liveTeta.getValue();
    }
}
