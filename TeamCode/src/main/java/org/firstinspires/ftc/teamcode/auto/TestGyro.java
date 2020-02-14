package org.firstinspires.ftc.teamcode.auto;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestGyro", group="sensors")
public class TestGyro extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Utils.init(telemetry);
        Context ctx = hardwareMap.appContext;

        Phone phone = Phone.getInstance(ctx);
        
        telemetry.update();

        while (opModeIsActive()){
            telemetry.update();
        }
    }
}
