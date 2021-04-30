package ir.mahdi.superspinner;

import android.view.View;

public class Circle {

    private View ball;
    private float x;
    private float y;
    private float xDelta;
    private float yDelta;
    private float xVelocity;
    private float yVelocity;
    private float xAcceleration;
    private float yAcceleration;
    private float xFrictionDirection;
    private float yFrictionDirection;

    public float getxDelta() {
        return xDelta;
    }

    public Circle(View ball , float x, float y, float xDelta, float yDelta, float xVelocity, float yVelocity, float xAcceleration, float yAcceleration, float xFrictionDirection, float yFrictionDirection) {
        this.ball = ball;
        this.ball.setX(x);
        this.ball.setY(y);
        this.x = x;
        this.y = y;
        this.xDelta = xDelta;
        this.yDelta = yDelta;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.xFrictionDirection = xFrictionDirection;
        this.yFrictionDirection = yFrictionDirection;
    }

    public void setxDelta(float xDelta) {
        this.xDelta = xDelta;
    }

    public float getyDelta() {
        return yDelta;
    }

    public void setyDelta(float yDelta) {
        this.yDelta = yDelta;
    }

    public View getBall() {
        return this.ball;
    }

    public void setBall(View ball) {
        this.ball = ball;
    }


    public float getxFrictionDirection() {
        return xFrictionDirection;
    }

    public void setxFrictionDirection(float xFrictionDirection) {
        this.xFrictionDirection = xFrictionDirection;
    }

    public float getyFrictionDirection() {
        return yFrictionDirection;
    }

    public void setyFrictionDirection(float yFrictionDirection) {
        this.yFrictionDirection = yFrictionDirection;
    }

    public float getX() { return this.ball.getX(); }

    public void setX(float x) {
        this.ball.setX(x);
    }

    public float getY() {
        return this.ball.getY();
    }

    public void setY(float y) {
        this.ball.setY(y);
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public float getxAcceleration() {
        return xAcceleration;
    }

    public void setxAcceleration(float xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public float getyAcceleration() {
        return yAcceleration;
    }

    public void setyAcceleration(float yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public void handleWallCollision(float xMax , float yMax){

        if (this.ball.getX() > xMax) {
            this.ball.setX(xMax);
            this.x = xMax;
            this.xVelocity = -this.xVelocity;
        } else if (this.ball.getX() < 0) {
            this.ball.setX(0f);
            this.x = 0f;
            this.xVelocity = -this.xVelocity;
        }

        if (this.ball.getY() > yMax) {
            this.ball.setY(yMax);
            this.y = yMax;
            this.yVelocity = -this.yVelocity;
        } else if (this.ball.getY() < 0) {
            this.ball.setY(0f);
            this.y = 0f;
            this.yVelocity = -this.yVelocity;
        }

    }

    public void calculateXAcceleration(float xData){
        if(this.getxAcceleration() == 0) {
            this.xAcceleration = -((float) ((xData) + this.xFrictionDirection * ((Math.sqrt(96.04f - xData * xData)) * 0.15))) * 10000;
        }
        else {
            this.xAcceleration = -((float) ((xData) + this.xFrictionDirection * ((Math.sqrt(96.04f - xData * xData)) * 0.1))) * 10000;

        }

    }

    void  calculateYAcceleration(float yData){
        if(this.getyAcceleration() == 0) {
            this.yAcceleration = ((float) ((yData) + this.yFrictionDirection * ((Math.sqrt(96.04f - yData * yData)) * 0.15))) * 10000;
        }
        else {
            this.yAcceleration = ((float) ((yData) + this.yFrictionDirection * ((Math.sqrt(96.04f - yData * yData)) * 0.1))) * 10000;

        }
    }

}
