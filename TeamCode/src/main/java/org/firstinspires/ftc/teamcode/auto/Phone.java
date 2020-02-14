package org.firstinspires.ftc.teamcode.auto;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

import static org.firstinspires.ftc.teamcode.auto.Utils.log;

public class Phone implements SensorEventListener {
    private static Phone phone;
    public static Phone getInstance(Context ctx){
        if (phone == null){
            phone = new Phone(ctx);
        }
        return phone;
    }

    private Phone (Context ctx){
        SensorManager mSensorManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
        //get a list of all of the sensors available
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        log("Sensors.size:", sensors.size());
        //get a reference to the default Rotation Vector
        Sensor mRotVec = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //register this opmode as a Sensor Listener, and register to listen for the Rotation Vector
        mSensorManager.registerListener(this, mRotVec, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        log("gyro:", e.values);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
