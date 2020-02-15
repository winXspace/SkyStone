package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.teamcode.auto.Utils.log;

public class IMU {
    BNO055IMU imu;
    IMU(HardwareMap hardwareMap){
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.mode                = BNO055IMU.SensorMode.IMU;//BNO055IMU.SensorMode.
        params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled      = false;

        imu.initialize(params);



    }

    public boolean isCalibrated(){
        return imu.isGyroCalibrated();

    }

    public float getAngle() {
        Orientation o = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        // Нам нужна ось Y.
        float a = o.secondAngle;
        log("o.secondAngle:", o.secondAngle);
        a = a >= 0 ? a : 360 + a;// здесь есть ошибка датчика при a = 180;


        return a;//
    }
}
