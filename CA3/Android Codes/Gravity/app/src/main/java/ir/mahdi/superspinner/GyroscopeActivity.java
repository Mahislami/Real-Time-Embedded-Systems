package ir.mahdi.superspinner;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class GyroscopeActivity extends GravityActivity {

    private static final String TAG = "GyroscopeActivity";
    private Coordination teta = new Coordination(0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    static Long lastTimestamp = null;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "event ac:" + event.accuracy + "v:" +
                Config.precision.format(event.values[0]) + "," +
                Config.precision.format(event.values[1]) + "," +
                Config.precision.format(event.values[2]));
        if(configFirstTime(event.timestamp))
            return;
        double deltaT = (event.timestamp - lastTimestamp)/Math.pow(10,9);
        lastTimestamp = event.timestamp;
        Log.d(TAG, "t: "+ Config.precision.format(deltaT) + "event:" + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        double rx = event.values[0];
        double ry = event.values[1];
        if(teta == null) {
            teta.x = rx * deltaT;
            teta.y = ry * deltaT;
            teta.dt = deltaT;
        } else {
            teta.x += rx * deltaT;
            teta.y += ry * deltaT;
            teta.dt = deltaT;
        }
        xData = -(float) (Config.G * Math.sin(teta.y));
        yData = (float) (Config.G * Math.sin(teta.x));
        Log.d(TAG, String.format("teta:%.2f,%.2f\tdata:%.2f,%.2f", Config.toDeg(teta.x), Config.toDeg(teta.y), xData, yData));
    }

    private boolean configFirstTime(long timestamp) {
        if(this.lastTimestamp == null){
            this.lastTimestamp = timestamp;
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
