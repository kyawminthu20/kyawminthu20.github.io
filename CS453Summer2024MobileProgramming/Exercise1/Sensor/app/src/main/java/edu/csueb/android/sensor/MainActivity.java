package edu.csueb.android.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    SensorManager sm;
    List<Sensor> ss;
    TextView sslist;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sslist = (TextView) findViewById(R.id.sensorList);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        ss = sm.getSensorList(Sensor.TYPE_ALL);

        for (Sensor s: ss){
            sslist.append("\n\nName: " + s.getName() + "\n Power: " + s.getPower() + "\n Maximum Range: " + s.getMaximumRange());
        }
    }

}