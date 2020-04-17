package com.example.gmapsnavisens;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class CustomSensors implements SensorEventListener {

    Float azimut;
    float[] mGravity;
    float[] mGeomagnetic;

    public CustomSensors(android.content.Context context)
    {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

//        Log.d("SENSOR", "onSensorChanged: ");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
            }
        }
        if (azimut!=null)
        {
            if (azimut<0)
            {
                azimut = (float)Math.toDegrees(azimut.doubleValue()) + 360;
            }
            else
            {
                azimut = (float)Math.toDegrees(azimut.doubleValue());
            }
            Log.d("CUSTOMSENSOR","Deg: "+ String.valueOf(azimut));
        }

    }

    public float getMagAzimuth()
    {
        return azimut;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
