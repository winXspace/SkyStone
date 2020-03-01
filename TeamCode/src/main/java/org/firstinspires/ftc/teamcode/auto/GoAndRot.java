package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous
public class GoAndRot extends LinearOpMode {

    /* Declare OpMode members. */
    private Bot bot;
    private ElapsedTime runtime = new ElapsedTime();

    protected VectorF dir = new VectorF(1.0f, 0.0f, 0);


    @Override
    public void runOpMode() {
        Utils.init(telemetry);
        bot = Bot.getInstance(hardwareMap);

        waitForStart();





        double a = 0;


        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.5)) {
            float t = (float)runtime.seconds();
            a = t * 0.15f;
            VectorF _d = Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC,
                    AxesOrder.ZXY,
                    AngleUnit.DEGREES, -45.0f * t, 0 , 0).transform(dir);
            VectorF d = new VectorF(_d.get(0), _d.get(1));
            bot.goAndRot( d, 0.4f, 0.2f );
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }


        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

}
