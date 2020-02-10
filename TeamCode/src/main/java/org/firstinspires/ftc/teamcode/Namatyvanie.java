
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Lift", group="Linear Opmode")

public class Namatyvanie extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor liftDrive = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        liftDrive = hardwareMap.get(DcMotor.class, "m20");
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){

            double lift = gamepad1.dpad_up?1:(gamepad1.dpad_down?-1:0);

            liftDrive.setPower(lift);


        }

    }
}
