package ir.mahdi.superspinner;

import android.view.View;

import androidx.annotation.Nullable;

class Coordination {

    double x;
    double y;
    double dt;

    public Coordination() {}

    public Coordination(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Nullable
    public static Coordination parseIJ(String iAndJ) {
        try {
            Coordination cor = new Coordination();
            cor.x = Double.valueOf(iAndJ.split("\\+")[0].replace("i",""));
            cor.y = Double.valueOf(iAndJ.split("\\+")[1].replace("j",""));
            return cor;
        } catch (Exception e) {
            return new Coordination(0, 0);
        }
    }

    public static Coordination parseIJ(String movement, @Nullable View reference) {
        Coordination cor = parseIJ(movement);
        if(reference == null) {
            return cor;
        }
        cor.x += reference.getX();
        cor.y += reference.getY();
        return cor;
    }

    public void plus(Coordination velocity) {
        this.x += velocity.x;
        this.y += velocity.y;
    }

    public double size() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y,2));
    }

    public void prod(long gameTimeUnit) {
        x *= gameTimeUnit;
        y *= gameTimeUnit;
    }

    public void mirror(char axis) {
        switch (axis) {
            case 'x':
                x = -x;
                break;
            case 'y':
                y = -y;
                break;
        }
    }

    public char collides(Coordination point, double ballRadius) {
        double xCollision = point.x - x;
        double yCollision = point.y - y;
        if(collides(xCollision, ballRadius) || collides(yCollision, ballRadius)) {
            if(xCollision > yCollision)
                return 'x';
            else
                return 'y';
        }
        return 'n';
    }

    boolean collides(double distance, double radius) {
        return distance > 0 && distance < radius;
    }
}