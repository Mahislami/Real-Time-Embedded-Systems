package ut.cps.teeter.ui.main;

import android.hardware.SensorManager;

import java.text.DecimalFormat;

import static java.lang.Math.PI;

class Config {

    /**
     * to achieve better user experience in game:
     * the table is too small and the real moving force is too large and the balls move too fast
     * so it makes sense to use a relaxing proportion
     */
    public static final double MOVING_FORCE_PROPORTION = 0.4;
    static final int SENSOR_TIME = SensorManager.SENSOR_DELAY_GAME;
    static final double MARGIN = 2;
    static long GAME_TIME_UNIT = 50;
    static long UI_TIME_UNIT = 50;
    /**
     * According to the Project description:
     */
    static double M1 = 0.05;
    static double M2 = 0.01;
    static double mu_s = 0.15;
    static double mu_k = 0.10;
    static DecimalFormat precision = new DecimalFormat("0.00");
    static String toDeg(double rad) {
        return precision.format(rad * 180.0 / PI);
    }
}
