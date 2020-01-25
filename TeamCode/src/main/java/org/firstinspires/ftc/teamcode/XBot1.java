package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="HoloX", group="andrew")

public class XBot1 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive = null;
    private DcMotor rfDrive = null;

    private DcMotor lbDrive = null;
    private DcMotor rbDrive = null;

    private VectorF lf = new VectorF (1.0f, 1.0f);
    private VectorF rf = new VectorF (-1.0f, 1.0f);
    private VectorF lb = new VectorF (1.0f, -1.0f);
    private VectorF rb = new VectorF (-1.0f, -1.0f);

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        lfDrive  = hardwareMap.get(DcMotor.class, "lfDrive");
        rfDrive = hardwareMap.get(DcMotor.class, "rfDrive");

        lbDrive  = hardwareMap.get(DcMotor.class, "lbDrive");
        rbDrive = hardwareMap.get(DcMotor.class, "rbDrive");

        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);

        rfDrive.setDirection(DcMotor.Direction.FORWARD);
        rbDrive.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            float rot = gamepad1.right_stick_x;


            VectorF steering = new VectorF(gamepad1.left_stick_x, -gamepad1.left_stick_y);// На геймпаде по-дефолту Y-ось с мотрит вниз. Здесь мы делаем наверх...

            float lfP = steering.dotProduct(lf) + rot;
            float rfP = steering.dotProduct(rf) + rot;
            float lbP = steering.dotProduct(lb) + rot;
            float rbP = steering.dotProduct(rb) + rot;

            // float lfP = lf.dotProduct(steering) + rot;
            // float rfP = rf.dotProduct(steering) + rot;
            // float lbP = lb.dotProduct(steering) + rot;
            // float rbP = rb.dotProduct(steering) + rot;



            lfP = Range.clip(lfP, -1.0f, 1.0f) ;
            rfP = Range.clip(rfP, -1.0f, 1.0f) ;
            lbP = Range.clip(lbP, -1.0f, 1.0f) ;
            rbP = Range.clip(rbP, -1.0f, 1.0f) ;


            lfDrive.setPower(lfP);
            rfDrive.setPower(rfP);
            lbDrive.setPower(lbP);
            rbDrive.setPower(rbP);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "lf (%.2f), rf (%.2f), lb (%.2f), rb (%.2f)", lfP, rfP, lbP, rbP);
            telemetry.addLine("Bla bla bla");
            telemetry.update();
        }
    }
}