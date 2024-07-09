package edu.csueb.android.weatherdemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    private SensorManager sensorManager;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView lightTextView;

    private float currentTemperature = Float.NaN;
    private float currentPressure = Float.NaN;
    private float currentLight = Float.NaN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = (TextView) findViewById(R.id.textViewTemperature);
        pressureTextView = (TextView) findViewById(R.id.textViewPressure);
        lightTextView = (TextView) findViewById(R.id.textViewLight);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Timer updateTimer = new Timer("weatherUpdate");
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
    }
    private final SensorEventListener tempSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            currentTemperature = sensorEvent.values[0];

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private final SensorEventListener pressureSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            currentPressure = sensorEvent.values[0];

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private final SensorEventListener lightSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            currentLight = sensorEvent.values[0];

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    protected void onResume() {
        super.onResume();

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
            lightTextView.setText("Light Sensor Unavailable");

        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressureSensor != null){
            sensorManager.registerListener(pressureSensorEventListener,
                    pressureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
            pressureTextView.setText("Barometer Unavailable");

        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(temperatureSensor != null){
            sensorManager.registerListener(tempSensorEventListener,
                    temperatureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
            temperatureTextView.setText("Thermometer Unavailable");

    }

    protected void onPause() {
        sensorManager.unregisterListener(pressureSensorEventListener);
        sensorManager.unregisterListener(tempSensorEventListener);
        sensorManager.unregisterListener(pressureSensorEventListener);
        super.onPause();
    }

    private void updateGUI(){
        runOnUiThread(new Runnable(){
            public void run(){
                if(!Float.isNaN(currentPressure)){
                    pressureTextView.setText(currentPressure + "(mBars)");
                    pressureTextView.invalidate();
                }
                if(!Float.isNaN(currentLight)){
                    String lightStr = "Sunny";
                    if(currentLight <= SensorManager.LIGHT_CLOUDY)
                        lightStr = "Night";
                    else if (currentLight <= SensorManager.LIGHT_OVERCAST)
                        lightStr = "Cloudy";
                    else if (currentLight <= SensorManager.LIGHT_SUNLIGHT)
                        lightStr = "Overcast";
                    lightTextView.setText(lightStr);
                    lightTextView.invalidate();
                }
                if(!Float.isNaN(currentTemperature)){
                    temperatureTextView.setText(currentTemperature + "deg C");
                    temperatureTextView.invalidate();
                }
            }
        });
    };


}





