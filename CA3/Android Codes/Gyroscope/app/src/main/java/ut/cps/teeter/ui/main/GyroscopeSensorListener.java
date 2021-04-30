package ut.cps.teeter.ui.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import static java.lang.Math.PI;

class GyroscopeSensorListener implements SensorEventListener {

    private final MutableLiveData<Coordination> liveTeta;

    public GyroscopeSensorListener(MutableLiveData<Coordination> liveTeta)  {
        this.liveTeta = liveTeta;
    }

    private static final String TAG = "GyroscopeSensorListener";
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
        Coordination newTeta = new Coordination();
        Log.d(TAG, "t: "+ Config.precision.format(deltaT) + "event:" + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        double rx = event.values[0];
        double ry = event.values[1];
        /**
         * Gyroscope sensor uses the Right Hand Method to measure rotation so:
         * rx changes teta.y
         * ry changes teta.x
         */
        if(liveTeta.getValue() == null) {
            newTeta.x = ry * deltaT;
            newTeta.y = rx * deltaT;
            newTeta.dt = deltaT;
        } else {
            newTeta.x = liveTeta.getValue().x + ry * deltaT;
            newTeta.y = liveTeta.getValue().y + rx * deltaT;
            newTeta.dt = deltaT;
            Log.d(TAG, "new teta:" + Config.toDeg(newTeta.x) + "," + Config.toDeg(newTeta.y));
       }
        liveTeta.postValue(newTeta);
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
