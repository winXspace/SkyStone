package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.matrices.VectorF

data class Data(val motor:DcMotor, val dir:VectorF)


@TeleOp(name = "XBot", group = "andrew")
class XBot() : LinearOpMode() {

    private  val t : ElapsedTime = ElapsedTime()

    override fun runOpMode() {
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        val robo : List<Data> = listOf(
                Data(hardwareMap.get(DcMotor::class.java , "lfDrive"), VectorF(1f, 1f)),
                Data(hardwareMap.get(DcMotor::class.java , "rfDrive"), VectorF(-1f, 1f)),
                Data(hardwareMap.get(DcMotor::class.java , "lbDrive"), VectorF(1f, -1f)),
                Data(hardwareMap.get(DcMotor::class.java , "rbDrive"), VectorF(-1f, -1f))
        )

        robo.onEach { (motor) -> motor.direction = DcMotorSimple.Direction.FORWARD }

        waitForStart()
        t.reset()

        while(opModeIsActive()){

            val rot : Float = gamepad1.right_stick_x;
            val steering : VectorF = VectorF(gamepad1.left_stick_x, -gamepad1.left_stick_y) // We invert Y axis here!...

            robo.onEach { (motor, dir) ->
                val p : Float = steering.dotProduct(dir) + rot
                motor.power =  Range.clip(p, -1.0f, 1.0f).toDouble()
             }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("Motors", "lf (%.2f), rf (%.2f), lb (%.2f), rb (%.2f)", lfP, rfP, lbP, rbP);
            telemetry.addData("Motors:", robo.map { (motor) -> motor.power })
            telemetry.update()

        }

    }
}