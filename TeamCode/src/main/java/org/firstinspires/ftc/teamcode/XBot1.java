package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="XBot1", group="andrew")

public class XBot1 extends LinearOpMode {
    Servo servo;
    double servoPosition = 0.5;

    Servo s;
    double servoP = 0.5;

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive = null;
    private DcMotor rfDrive = null;

    private DcMotor lbDrive = null;
    private DcMotor rbDrive = null;

    //private DcMotor ilDrive = null;
    //private DcMotor irDrive = null;

    private VectorF lf = new VectorF (1.0f, 1.0f);
    private VectorF rf = new VectorF (1.0f, -1.0f);
    private VectorF lb = new VectorF (-1.0f, 1.0f);
    private VectorF rb = new VectorF (-1.0f, -1.0f);



    private DcMotor liftDrive = null;



    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        lfDrive = hardwareMap.get(DcMotor.class, "m10");
        lfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rfDrive = hardwareMap.get(DcMotor.class, "m11");
        rfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lbDrive = hardwareMap.get(DcMotor.class, "m12");
        lbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rbDrive = hardwareMap.get(DcMotor.class, "m13");
        rbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        lfDrive.setDirection(DcMotor.Direction.REVERSE);
        lbDrive.setDirection(DcMotor.Direction.REVERSE);

        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);


        liftDrive = hardwareMap.get(DcMotor.class, "m20");
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        servo = hardwareMap.servo.get("servo");

        s = hardwareMap.servo.get("servo1");


        /*// intake
        ilDrive = hardwareMap.get(DcMotor.class, "m20");
        irDrive = hardwareMap.get(DcMotor.class, "m21");

        ilDrive.setDirection(DcMotor.Direction.REVERSE);
        irDrive.setDirection(DcMotor.Direction.FORWARD);
        */

        //servo
        servo = hardwareMap.get(Servo.class, "servo");
        servo.setPosition(servoPosition);

        s = hardwareMap.get(Servo.class, "servo1");
        s.setPosition(servoP);
        //servo = hardwareMap.servo.get("servo");



        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

          /*  // intake
            float intakeUp = gamepad1.left_bumper ? 1 : 0;
            float intakeDown = gamepad1.right_bumper ? -1 : 0;
            float intake = intakeUp + intakeDown;

            ilDrive.setPower(intake);
            irDrive.setPower(intake);
            */


            //lift

            double lift = gamepad2.left_stick_y; //?1:(gamepad1.dpad_down?-1:0);
            liftDrive.setPower(lift);


            //servo
            double newPos = servoPosition + (gamepad2.right_stick_x)*0.005;
            servoPosition = Range.clip(newPos, 0.0, 0.5);
            servo.setPosition(servoPosition);
            /* servoPosition = gamepad2.right_stick_x;*/


            //lift servo
            double newLift = servoP + (gamepad2.dpad_down?-1:0)*0.005;
            servoP = Range.clip(newLift, 0.0, 1.0);

            // movement
            float rot = gamepad1.right_stick_x;


            VectorF steering = new VectorF(-gamepad1.left_stick_x, -gamepad1.left_stick_y);// На геймпаде по-дефолту Y-ось с мотрит вниз. Здесь мы делаем наверх...

            float lfP = steering.dotProduct(lf) + rot;
            float rfP = steering.dotProduct(rf) + rot;
            float lbP = steering.dotProduct(lb) + rot;
            float rbP = steering.dotProduct(rb) + rot;


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
            telemetry.update();
        }
    }
}