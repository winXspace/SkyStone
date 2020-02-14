package org.firstinspires.ftc.teamcode.auto;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.auto.Utils.log;

@TeleOp(name="TestGyro", group="sensors")
public class TestGyro extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Utils.init(telemetry);
        Context ctx = hardwareMap.appContext;

        //Phone phone = Phone.getInstance(ctx);

        telemetry.update();

        // --- init imu ------------
        IMU imu = new IMU(hardwareMap);
        // calibration
        while (!isStopRequested() && !imu.isCalibrated())
        {
            sleep(50);
            idle();
        }




        waitForStart();

        while (opModeIsActive()){

            //float [] vs = phone.getGyro();
            //log("vs:", vs);


            float a = imu.getAngle();
            log("a:", a);

            telemetry.update();
        }
    }
}
