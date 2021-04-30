package ut.cps.teeter.ui.main;

import android.util.Log;

import static ut.cps.teeter.ui.main.Config.mu_k;
import static ut.cps.teeter.ui.main.Config.mu_s;

public class Ball {

    final double g = 9.8;
    GameTable gameTable;
    private Coordination acceleration;

    Ball(double m, GameTable gameTable) {
        this.m = m;
        this.gameTable = gameTable;
    }

    Coordination position;
    Coordination velocity;
    double m;

    public void accelerate(double dt) {
        Coordination kineticFriction = friction(gameTable.teta(), velocity);
        acceleration = acceleration(gameTable.teta(), kineticFriction);
        velocity.x = velocity(acceleration.x, velocity.x, dt);
        velocity.y = velocity(acceleration.y, velocity.y, dt);
    }

    public void move(double dt) {
        moveX(dt);
        moveY(dt);
        Log.d("ball", String.format("m:%.2f teta:%.2f,%.2f a:%.2f,%.2f v:%.2f,%.2f xy:%2.2f,%.2f", m, gameTable.teta().x, gameTable.teta().y, acceleration.x, acceleration.y, velocity.x, velocity.y, position.x, position.y));
    }

    Coordination acceleration(Coordination teta, Coordination kineticFriction) {
        Coordination movingF = movingForce(teta);
        double maxFriction = this.maxFriction(teta);
        Coordination acceleration = new Coordination(0,0);
        if(standing() && movingF.size() < maxFriction) {
            return acceleration;
        }
        acceleration.x = acceleration(movingF.x, kineticFriction.x);
        acceleration.y = acceleration(movingF.y, kineticFriction.y);
        return acceleration;
    }

    Coordination movingForce(Coordination teta) {
        Coordination movingF = new Coordination(0,0);
        movingF.x = teta.x == 0? 0: m * g / Math.sin(teta.x) * Config.MOVING_FORCE_PROPORTION;
        movingF.y = teta.y == 0? 0: m * g / Math.sin(teta.y) * Config.MOVING_FORCE_PROPORTION;
        return movingF;
    }

    void moveX(double dt) {
        double Ax = acceleration.x * 1000;//todo convert by screen resolution
        position.x = position(Ax, velocity.x, dt, position.x, gameTable.min.x, gameTable.max.x);
    }

    double position(double A, double vel, double dt, double pos0, double min, double max) {
        double pos = position(A, vel, dt, pos0);
        pos = Math.min(pos, max);
        pos = Math.max(pos, min);
        return pos;
    }

    void moveY(double dt) {
        double Ay = acceleration.y * 1000;//todo convert by screen resolution
        position.y = position(Ay, velocity.y, dt, position.y, gameTable.min.y, gameTable.max.y);
    }

    double acceleration(double movingF, double kineticFriction) {
        double totalF = movingF + kineticFriction;
        double acc = totalF/m;
        return acc;
    }

    boolean standing() {
        return velocity == null || (velocity.x == 0 && velocity.y == 0);
    }

    /**
     *
     * @param A in pixel/s
     * @param v0 in pixel/s
     * @param t in seconds
     * @param position0 in pixel
     */
    double position(double A, double v0, double t, double position0) {
        double newPosition = 0.5 * A * t * t + v0 * t + position0;
        return newPosition;
    }

    double velocity(double A, double v0, double t) {
        return A * t + v0;
    }

    public void init(Coordination pos, Coordination vel) {
        position = pos;
        velocity = vel;
    }

    double maxFriction(Coordination teta) {
        Coordination N = new Coordination();
        N.x = m * g * Math.cos(teta.x);
        N.y = m * g * Math.cos(teta.y);
        double fricSize = N.size() * mu_s;
        return fricSize;
    }

    Coordination friction(Coordination teta, Coordination vel) {
        Coordination N = new Coordination();
        N.x = m * g * Math.cos(teta.x);
        N.y = m * g * Math.cos(teta.y);
        Coordination fric = new Coordination(0,0);
        if(vel.size() == 0) {
            return fric;
        }
        double fricSize = N.size() * mu_k;
        fric.x = fricSize * Math.abs(vel.x) / vel.size();
        fric.x = vel.x < 0? fric.x: -fric.x;
        fric.y = fricSize * Math.abs(vel.y) / vel.size();
        fric.y = vel.y < 0? fric.y: -fric.y;
        return fric;
    }
}
