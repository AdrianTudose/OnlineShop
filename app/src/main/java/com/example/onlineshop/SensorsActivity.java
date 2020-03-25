package com.example.onlineshop;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;
    private TextView textView;
    private List<Sensor> sensors;
    private HashMap<String,String> sensorValues = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_activity);

        textView = (TextView)findViewById(R.id.text);
        textView.setKeyListener(null);

        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        assert manager != null;
        sensors = manager.getSensorList(Sensor.TYPE_ALL);

        for(Sensor sensor : sensors) {
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorValues.put(sensor.getName(), "");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String values = new String();
        for (float value : event.values)
        {
            values += value + " ";
        }
        sensorValues.put(event.sensor.getName(),values);

        textView.setText("");
        for(Map.Entry<String, String> entry : sensorValues.entrySet())
            textView.setText(textView.getText() + entry.getKey() + "\n" + entry.getValue() + "\n\n");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
