package ut.cps.teeter.ui.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import static java.lang.Math.PI;

public class GravitySensorListener implements SensorEventListener {

    private static final String TAG = "GravitySensorListener";
    MutableLiveData<Coordination> liveTeta;
    public GravitySensorListener(MutableLiveData<Coordination> liveTeta) {
        this.liveTeta = liveTeta;
    }
    Long lastTimestamp = null;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(configFirstTime(event.timestamp))
            return;
        double deltaT = (event.timestamp - lastTimestamp)/Math.pow(10,9);
        lastTimestamp = event.timestamp;
        Coordination newTeta = new Coordination();
        double ax = event.values[0];
        double ay = event.values[1];
        double size = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));

        /**
         * Gravity sensor shows the gravity among different sensors so:
         *  (ay/size of gravity) is equal to cos(teta.x)
         *  (ax/size of gravity) is equal to cos(teta.y)
         *  Game must start while phone is in flat state
         *  Be Careful!
         *  Gravity sensor is very inaccurate, so relax the values.
         */
        newTeta.x = relax(Math.acos(ay/size) - PI/2);
        newTeta.y = relax(Math.asin(ax/size));
        newTeta.dt = deltaT;
        Log.d(TAG, "new teta:" + Config.toDeg(newTeta.x) + "," + Config.toDeg(newTeta.y));
        liveTeta.postValue(newTeta);
    }

    private double relax(double value) {
        int relaxed = (int) (value * 10);
        return new Double(value / 10.0);
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
