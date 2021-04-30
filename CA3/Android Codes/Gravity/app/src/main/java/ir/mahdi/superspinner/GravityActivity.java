package ir.mahdi.superspinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class GravityActivity extends AppCompatActivity implements SensorEventListener {

    protected SensorManager mSensorManager;
    protected Sensor mSensor;
    protected float frameTime = 0.001f;
    protected Circle circle1;
    protected Circle circle2;
    protected float xData;
    protected float yData;
    protected float xMax;
    protected float yMax;
    protected Handler handler = new Handler();




    private void handleBallCollision(){
        if(Math.sqrt(Math.pow(circle1.getBall().getX() - circle2.getBall().getX(),2) +
                Math.pow(circle1.getBall().getY() - circle2.getBall().getY(),2)) < 130){
            float ux1 = circle1.getxVelocity();
            float uy1 = circle1.getyVelocity();
            float ux2 = circle2.getxVelocity();
            float uy2 = circle2.getyVelocity();
            circle1.setxVelocity((ux1 * 0.04f + 0.02f * ux2) / 0.06f);
            circle1.setyVelocity((uy1 * 0.04f + 0.02f * uy2) / 0.06f);
            circle2.setxVelocity((-ux2 * 0.04f + 0.1f * ux1) / 0.06f);
            circle2.setyVelocity((-uy2 * 0.04f + 0.1f * uy1) / 0.06f);
        }
    }

    private float string2Float(String string){
        float ans = 0;
        for (int i = 0 ; i < string.length() ; i++ )
            ans += ans * 10 + string.charAt(i);
        return ans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        xMax = displayMetrics.widthPixels - 130;
        yMax = displayMetrics.heightPixels - 130;
        xData = 0;
        yData = 0;

        Intent intent1 = getIntent();
        String xMoon = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_XMOON);
        String yMoon = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_YMOON);
        String xSun  = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_XSUN);
        String ySun  = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_YSUN);
        String VxMoon = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_VxMOON);
        String VyMoon = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_VyMOON);
        String VxSun  = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_VxSUN);
        String VySun  = intent1.getStringExtra(WelcomeActivity.EXTRA_MESSAGE_VySUN);

        View ball1 = findViewById(R.id.gooy);
        View ball2 = findViewById(R.id.gooy2);

        TextView xmoon;
        TextView ymoon;
        TextView vxmoon;
        TextView vymoon;
        TextView xsun;
        TextView ysun;
        TextView vxsun;
        TextView vysun;

        circle1 = new Circle(ball1, 0f , 0f,0f,0f,0f, 0f,
                    0f,0f,0f,0f);
        circle2 = new Circle(ball2 ,0f , 0f,0f,0f,0f, 0f,
                0f,0f,0f,0f);

        circle1.getBall().animate().x(Float.parseFloat(xSun)).y(Float.parseFloat(ySun)).setDuration(0);
        circle1.setxVelocity(Float.parseFloat(VxSun)*1000);
        circle1.setyVelocity(Float.parseFloat(VySun)*1000);

        circle2.getBall().animate().x(Float.parseFloat(xMoon)).y(Float.parseFloat(yMoon)).setDuration(0);
        circle2.setxVelocity(Float.parseFloat(VxMoon)*1000);
        circle2.setyVelocity(Float.parseFloat(VyMoon)*1000);

        updateBalls();
    }

    private void updateBalls(){
        handler.postDelayed(runnable, (long) (frameTime*1000));
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //if(xData > 1f || xData < -1f) {
            if(96.04f - xData * xData > 0) {
                if(circle1.getxVelocity() < 0){
                    circle1.setxFrictionDirection(-1);
                }else {
                    circle1.setxFrictionDirection(1);
                }
                if((xData > 0 && circle2.getxVelocity() < 0) || (xData < 0 && circle2.getxVelocity() < 0)){
                    circle2.setxFrictionDirection(-1);
                }else {
                    circle2.setxFrictionDirection(1);
                }
                circle1.calculateXAcceleration(xData);
                circle2.calculateXAcceleration(xData);
            } else{
                circle1.setxAcceleration(-xData * 10000);
                circle2.setxAcceleration(-xData * 10000);
            }

            if(96.04f - yData * yData > 0) {
                if(circle1.getyVelocity() > 0){
                    circle1.setyFrictionDirection(-1f);
                }else {
                    circle1.setyFrictionDirection(1f);
                }
                if((yData >= 0 && circle2.getyVelocity() > 0) ||
                        (yData < 0 && circle2.getyVelocity() > 0)){
                    circle2.setyFrictionDirection(-1f);
                }else {
                    circle2.setyFrictionDirection(1f);
                }

                circle1.calculateYAcceleration(yData);
                circle2.calculateYAcceleration(yData);

            }else {
                circle1.setyAcceleration(yData * 10000);
                circle2.setyAcceleration(yData * 10000);
            }

            //Calculate new speed
            circle1.setxVelocity(circle1.getxVelocity() + circle1.getxAcceleration() * frameTime);
            circle1.setyVelocity(circle1.getyVelocity() + circle1.getyAcceleration() * frameTime);
            circle2.setxVelocity(circle2.getxVelocity() + circle2.getxAcceleration() * frameTime);
            circle2.setyVelocity(circle2.getyVelocity() + circle2.getyAcceleration() * frameTime);

            //Calc distance travelled in that time
            circle1.setxDelta((circle1.getxVelocity()/2) * frameTime);
            circle1.setyDelta((circle1.getyVelocity()/2) * frameTime);
            circle2.setxDelta((circle2.getxVelocity()/2) * frameTime);
            circle2.setyDelta((circle2.getyVelocity()/2) * frameTime);

            //Add to position negative due to sensor
            //readings being opposite to what we want!
            circle1.setX(circle1.getX() + circle1.getxDelta());
            circle1.setY(circle1.getY() + circle1.getyDelta());
            circle2.setX(circle2.getX() + circle2.getxDelta());
            circle2.setY(circle2.getY() + circle2.getyDelta());

            circle1.handleWallCollision(xMax,yMax);
            circle2.handleWallCollision(xMax,yMax);

            handleBallCollision();

            updateBalls();
        }
    };

    @Override
    protected void onResume() {

        super.onResume();
        if(mSensor == null) {
            Toast.makeText(this, "Sensor Not Supported!", Toast.LENGTH_LONG).show();
            return;
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(xData > 1f || xData < -1f) {
            xData = 0;
        }else {
            xData = event.values[0];
        }
        if(event.values.length>1) {

            if(yData > 1f || yData < -1f) {
                yData = 0;
            }else {
                yData = event.values[1];
            }
        } if(event.values.length>2) {
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}