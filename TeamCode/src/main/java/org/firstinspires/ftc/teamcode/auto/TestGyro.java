package org.firstinspires.ftc.teamcode.auto;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import static org.firstinspires.ftc.teamcode.auto.InitVuforia.initVuforia;
import static org.firstinspires.ftc.teamcode.auto.Utils.log;

@Autonomous(name="TestGyro", group="SLAM")
public class TestGyro extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Utils.init(telemetry);
        Context ctx = hardwareMap.appContext;

        //Phone phone = Phone.getInstance(ctx);

        //telemetry.update();

        // --- init Vuforia --------
        VuforiaLocalizer vuforia = initVuforia(
                ctx.getResources().getIdentifier("cameraMonitorViewId", "id", ctx.getPackageName()));




        // --- init imu ------------
        IMU imu = new IMU(hardwareMap);
        // calibration

//        while (!isStopRequested() && !imu.isCalibrated())
//        {
//            sleep(50);
//            idle();
//        }



        waitForStart();

        while (opModeIsActive()){

            //float [] vs = phone.getGyro();
            //log("vs:", vs);

            // read imu
            //float a = imu.getAngle();
            //log("imu.a:", a);

            // read vuforia
            //VectorF pos = InitVuforia.readPos();
            float a2 = InitVuforia.readAngle();
            //log("vuforia.a:", a2);
            log("pos:", InitVuforia.readPos());


            telemetry.update();
        }

        InitVuforia.deactivate();
    }
}
