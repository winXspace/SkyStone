/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.Point;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Basic: Iterative OpMode", group="Iterative Opmode")
@Disabled
public class BasicOpMode_Iterative extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //private Servo servoArm1 = null;
    //private Servo servoArm2 = null;
    private Servo servoHand1 = null;
    private Servo servoHand2 = null;

    private DcMotor armDrive = null;
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    //double servoArm1StartPos;
    //double servoArm2StartPos;
    double servoHand1StartPos;
    double servoHand2StartPos;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        Point p = new Point(23, 33);

        //servoArm1 =  hardwareMap.get(Servo.class, "ServoArm1");
        //servoArm2 =  hardwareMap.get(Servo.class, "ServoArm2");
        armDrive  = hardwareMap.get(DcMotor.class, "ArmMotor");

        servoHand1 =  hardwareMap.get(Servo.class, "ServoHand1");
        servoHand2 =  hardwareMap.get(Servo.class, "ServoHand2");

        leftDrive  = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightDrive = hardwareMap.get(DcMotor.class, "RightMotor");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        armDrive.setDirection(DcMotor.Direction.FORWARD);


        //servoArm1StartPos = servoArm1.getPosition();
        //servoArm2StartPos = servoArm2.getPosition();

        servoHand1StartPos = servoHand1.getPosition();
        servoHand2StartPos = servoHand2.getPosition();


        telemetry.addData("Status", "Initialized");
        telemetry.addData("Status", "Hello, Elizaveta!");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        telemetry.addData("Test message", "Привет, Андрей");
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */

    double currentArmAngle = 0.0;
    double currentHandAngle = 0.0;

    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;

        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
        // leftPower  = -gamepad1.left_stick_y ;
        // rightPower = -gamepad1.right_stick_y ;


        // ---------------- drive --------------------

        double armPower = Range.clip( gamepad2.left_stick_y, -1.0, 1.0) ; //gamepad2.left_stick_y;// -1 -- 1 to 0 -> 45
        armDrive.setPower(armPower);

        //double aMin = 0.0;
        //double aMax = 45.0 / 180.0;
        //if (currentArmAngle > aMin || currentArmAngle < aMax) {
        //    currentArmAngle = currentArmAngle + armDelta;
        //}

        double handDelta = gamepad2.right_stick_x * 0.002;
        //double hMin = 0.0;
        //double hMax = 45.0 / 180.0;
        //if (currentHandAngle > hMin || currentHandAngle < hMax) {
        //  currentHandAngle = currentHandAngle + handDelta;
        //}

        //currentArmAngle = currentArmAngle + armDelta;
        currentArmAngle = Range.clip( currentArmAngle + handDelta , 0.0, 1.0);
        //servoArm1.setPosition(servoArm1StartPos + currentArmAngle);
        //servoArm2.setPosition(servoArm2StartPos - currentArmAngle);



        currentHandAngle = Range.clip( currentHandAngle + handDelta , 0.0, 1.0);

        servoHand1.setPosition(1.0 - currentHandAngle);
        servoHand2.setPosition(0.0 + currentHandAngle);

        //telemetry.addData("servoArm1StartPos :servoArm2StartPos ", "left (%.2f), right (%.2f)", servoArm1StartPos , servoArm2StartPos);
        telemetry.addData("currentArmAngle:currentHandAngle", "left (%.2f), right (%.2f)", currentArmAngle, currentHandAngle);





        // ----------------- move --------------
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
