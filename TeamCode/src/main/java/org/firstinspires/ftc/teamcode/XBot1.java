package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.auto.Utils;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.auto.Utils.log;

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

//    in old frame
//    private VectorF lf = new VectorF (1.0f, 1.0f);
//    private VectorF rf = new VectorF (1.0f, -1.0f);
//    private VectorF lb = new VectorF (-1.0f, 1.0f);
//    private VectorF rb = new VectorF (-1.0f, -1.0f);


    // in correct robot's frame
    private VectorF lf = new VectorF (-1.0f, 1.0f);
    private VectorF rf = new VectorF (1.0f, 1.0f);
    private VectorF lb = new VectorF (-1.0f, -1.0f);
    private VectorF rb = new VectorF (1.0f, -1.0f);


    private DcMotor liftDrive = null;

    private DcMotor bdr = null;




    @Override
    public void runOpMode() {

        Utils.init(telemetry);

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


        //lfDrive.setDirection(DcMotor.Direction.REVERSE);
        //lbDrive.setDirection(DcMotor.Direction.REVERSE);

        //rfDrive.setDirection(DcMotor.Direction.REVERSE);
        //rbDrive.setDirection(DcMotor.Direction.REVERSE);


        liftDrive = hardwareMap.get(DcMotor.class, "m20");
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bdr = hardwareMap.get(DcMotor.class, "m21");
        bdr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



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

        OpenGLMatrix gamePadToRobotTrans = OpenGLMatrix
                .identityMatrix()
                .scaled(1,-1,1)
                .rotated(AxesReference.EXTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES,90, 0,0)

                ;



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
            /*double newPos = servoPosition + (gamepad2.right_stick_x)*0.005;
            servoPosition = Range.clip(newPos, 0.0, 0.5);
            servo.setPosition(servoPosition);*/
            /* servoPosition = gamepad2.right_stick_x;*/

            //bdr
            double backw = gamepad2.right_stick_x * 0.1; //?1:(gamepad.dpad_down?-1:0);
            bdr.setPower(backw);


            //lift servo
            double newLift = servoP + (gamepad2.dpad_down?-1:(gamepad2.dpad_up?1.0:0))*0.005;


            servoP = Range.clip(newLift, 0.0, 1.0);
            s.setPosition(servoP);

            // movement
            float k = gamepad1.left_bumper?1.0f:0.2f;

            float rot = k * -gamepad1.right_stick_x;

            // vector in gamepad coords
            VectorF _steering = new VectorF(gamepad1.left_stick_x, gamepad1.left_stick_y, 0);
            VectorF steering3d = gamePadToRobotTrans.transform(_steering);// vector in robot coords
            VectorF steering = new VectorF(steering3d.get(0), steering3d.get(1));// 2d

            steering.multiply(k);


            //log("steering:", steering);


            float lfP = steering.dotProduct(lf) + rot;
            float rfP = steering.dotProduct(rf) + rot;
            float lbP = steering.dotProduct(lb) + rot;
            float rbP = steering.dotProduct(rb) + rot;



            lfP = Range.clip(lfP, -1.0f, 1.0f);
            rfP = Range.clip(rfP, -1.0f, 1.0f);
            lbP = Range.clip(lbP, -1.0f, 1.0f);
            rbP = Range.clip(rbP, -1.0f, 1.0f);


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