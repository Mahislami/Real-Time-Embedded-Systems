package ut.cps.teeter.ui.main;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ut.cps.teeter.BuildConfig;
import ut.cps.teeter.R;
import ut.cps.teeter.databinding.GameFragBinding;

public class GameFragment extends Fragment {

    int sensorType;
    GameTable gameTable = new GameTable();
    GameFragBinding binding;
    SensorManager sensorManager;
    Sensor gravitySensor;
    Sensor gyroscopeSensor;
    ScheduledExecutorService gameExecutor = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture gameFuture;
    ScheduledFuture uiUpdateFuture;
    private static String TAG = "GameFrag";

    public GameFragment(int SENSOR_TYPE) {
        this.sensorType = SENSOR_TYPE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        registerSensor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_frag, container, false);
        binding.btnStart.setOnClickListener(this::onStartBtn);
//        if(BuildConfig.DEBUG) {
//            binding.cor1.setText("0i+10j");
//            binding.vel1.setText("1i+5j");
//            binding.cor2.setText("20i+0j");
//            binding.vel2.setText("5i+1j");
//        }
        return binding.getRoot();
    }

    void configLiveData() {
        gameTable.ballOneCor.observe(this, coordination -> {
            if(coordination == null)
                return;
            binding.ballOne.setTranslationX((int) coordination.x);
            binding.ballOne.setTranslationY((int) coordination.y);
        });
        gameTable.ballTwoCor.observe(this, coordination -> {
            if(coordination == null)
                return;
            binding.ballOne.setTranslationX((int) coordination.x);
            binding.ballTwo.setTranslationY((int) coordination.y);
        });
    }

    @MainThread
    private void onStartBtn(View view) {
        checkSensors();
        initGameTable();
        gameFuture = gameExecutor.scheduleAtFixedRate(gameTable::update, 0, Config.GAME_TIME_UNIT, TimeUnit.MILLISECONDS);
        uiUpdateFuture = gameExecutor.scheduleAtFixedRate(this::updateUi, 0, Config.UI_TIME_UNIT, TimeUnit.MILLISECONDS);
        binding.btnStart.setText(R.string.stop);
        binding.btnStart.setBackgroundColor(Color.RED);
        binding.btnStart.setOnClickListener(this::stopBtn);
    }

    private void updateUi() {
        getActivity().runOnUiThread(() -> {
            try {
                binding.ballOne.animate().setDuration(0).//Config.UI_TIME_UNIT).
                        x(new Float(gameTable.ballOneCor.getValue().x)).
                        y(new Float(gameTable.ballOneCor.getValue().y)).start();
                binding.ballTwo.animate().setDuration(0).//Config.UI_TIME_UNIT).
                        x(new Float(gameTable.ballTwoCor.getValue().x)).
                        y(new Float(gameTable.ballTwoCor.getValue().y)).start();
            } catch (Exception e) {
                Log.e(TAG, "ex:" + e.getMessage());
            }
        });
    }

    @MainThread
    private void registerSensor() {
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        switch (sensorType) {
            case Sensor.TYPE_GRAVITY:
                sensorManager.registerListener(
                        new GravitySensorListener(gameTable.liveTeta), gravitySensor, Config.SENSOR_TIME);
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorManager.registerListener(
                        new GyroscopeSensorListener(gameTable.liveTeta), gyroscopeSensor, Config.SENSOR_TIME);
                break;
        }
        gameTable.liveTeta.observe(this, coordination -> {
            Log.d(TAG, String.format("set teta: %.2f, %.2f",   coordination.x, coordination.y));
        });
    }

    private void checkSensors() {
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        switch (sensorType) {
            case Sensor.TYPE_GRAVITY:
                if(!gyroExists || gyroscopeSensor == null) {
                    Toast.makeText(getActivity(), R.string.no_sensor, Toast.LENGTH_SHORT).show();
                    return;
                }
            case Sensor.TYPE_GYROSCOPE:
                if(gravitySensor == null) {
                    Toast.makeText(getActivity(), R.string.no_sensor, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    @MainThread
    private void stopBtn(View view) {
        gameFuture.cancel(false);
        binding.btnStart.setText(R.string.start);
        binding.btnStart.setBackgroundColor(Color.GREEN);
        binding.btnStart.setOnClickListener(this::onStartBtn);
    }

    @MainThread
    private void initGameTable() {
        Coordination corOne = Coordination.parseIJ(binding.cor1.getText().toString());
        Coordination corTwo = Coordination.parseIJ(binding.cor2.getText().toString());
        Coordination velOne = Coordination.parseIJ(binding.vel1.getText().toString());
        Coordination velTwo = Coordination.parseIJ(binding.vel2.getText().toString());
        Coordination reference = new Coordination();
        velOne.prod(Config.GAME_TIME_UNIT);
        velTwo.prod(Config.GAME_TIME_UNIT);
        gameTable.min = new Coordination(
            Config.MARGIN,
            Config.MARGIN
        );
        gameTable.ballDiameter = binding.ballOne.getWidth();
        gameTable.max = new Coordination(
                binding.gameDesk.getWidth() - Config.MARGIN - gameTable.ballDiameter,
                binding.gameDesk.getHeight() - Config.MARGIN - gameTable.ballDiameter);
        gameTable.initialize(corOne, corTwo, velOne, velTwo);
        prepareTable();
    }

    private void prepareTable() {
        binding.ballOne.setTranslationX(new Float(gameTable.ballOne.position.x));
        binding.ballOne.setTranslationY(new Float(gameTable.ballOne.position.y));
        binding.ballTwo.setTranslationX(new Float(gameTable.ballTwo.position.x));
        binding.ballTwo.setTranslationY(new Float(gameTable.ballTwo.position.y));
    }
}