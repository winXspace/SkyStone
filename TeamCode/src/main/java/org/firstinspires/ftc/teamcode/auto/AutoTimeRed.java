package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

@Autonomous
public class AutoTimeRed extends LinearOpMode {

    /* Declare OpMode members. */
    private Bot bot;
    private ElapsedTime runtime = new ElapsedTime();

    protected VectorF dir = new VectorF(0.0f, -1.0f);


    @Override
    public void runOpMode() {
        Utils.init(telemetry);
        bot = Bot.getInstance(hardwareMap);

        waitForStart();



        bot.go( new VectorF(1.0f, 0) );

        // первое движение
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }



        // второе движение
        float t = 3.5f;
        bot.go( dir );

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < t)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }


        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

}
