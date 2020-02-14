package org.firstinspires.ftc.teamcode.auto;


import android.content.Context;
import android.hardware.SensorManager;
import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.teamcode.auto.InitTF.initTF;
import static org.firstinspires.ftc.teamcode.auto.InitVuforia.initVuforia;
import static org.firstinspires.ftc.teamcode.auto.InitVuforia.readPos;
import static org.firstinspires.ftc.teamcode.auto.Utils.log;



enum State{
    LOCALISE,
    GO,
    STOP
}

@Autonomous(name = "Auto1", group = "SLAM")
public class Auto1 extends LinearOpMode {


    private State currentState = State.LOCALISE;

    private TrajectoryRegulator tr = new TrajectoryRegulator();

    private List<VectorF> squarePath = new ArrayList<>(Arrays.asList(
            new VectorF(500,-500),
            new VectorF(500,-1500),
            new VectorF(-500,-1500),
            new VectorF(-500,-500)
    ));

    private List<VectorF> squarePath2 = new ArrayList<>(Arrays.asList(
            new VectorF(250,-500),
            new VectorF(250,-1000),
            new VectorF(-250,-1000),
            new VectorF(-250,-500)
    ));

    Bot bot;
    IMU imu;
    //private static final float mmPerInch        = 25.4f;

    @Override
    public void runOpMode() {
        Utils.init(telemetry);
        Context ctx = hardwareMap.appContext;

        Phone phone = Phone.getInstance(ctx);


        // --- init Vuforia --------
        VuforiaLocalizer vuforia = initVuforia(
                ctx.getResources().getIdentifier("cameraMonitorViewId", "id", ctx.getPackageName()));


        // --- init TensorFlow -----
//        tfod = initTF(
//                vuforia,
//                ctx.getResources().getIdentifier("tfodMonitorViewId", "id", ctx.getPackageName()));

        // --- init Bot ------------
        bot = Bot.getInstance(hardwareMap);

        // --- init imu ------------
        imu = new IMU(hardwareMap);
        // calibration
        while (!isStopRequested() && !imu.isCalibrated())
        {
            sleep(50);
            idle();
        }



        /** Wait for the game to begin */
        log(">", "Press Play to start op mode");
        telemetry.update();


        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                Input input = readInput();

                log("Input.pos:", input);

                currentState = nextState(input, currentState);
                log("CurrState:", currentState);

                writeOutput( input.pos, currentState );

                telemetry.update();
            }
        }

        InitTF.shotdown();
        InitVuforia.deactivate();
    }

    private void writeOutput(VectorF currentPos, State currentState) {

        switch (currentState){
            case GO:
                if (currentPos != null) {
                    VectorF steering = tr.getVelocity(currentPos);
                    //steering.multiply(-1.0f);
                    bot.go(steering);
            }

                break;
            case STOP:
            case LOCALISE:
                bot.stop();

        }



    }

    private State nextState(Input i/*may be null*/, State s){

        switch (currentState){
            case LOCALISE:
                if(i.pos != null) {
                    //currentTarget = new VectorF(0, -100f, 152.4f);
                    tr.go(squarePath);
                    return State.GO;
                }
                else return State.LOCALISE;

            case GO:
                if (i.pos == null){
                    return State.LOCALISE;
                }
                else if( tr.finished()){
                    return State.STOP;
                }
                else {
                    //if (i.pos!= null && currentTarget != null) log("distance:", getDistance(i.pos, currentTarget));
                    return State.GO;
                }

        }



        return s;
    }

    private float getDistance(VectorF a, VectorF b){
        return a.subtracted(b).magnitude();
    }

    private Input readInput(){
        // TF handling
        // DEBUG
        //List<Recognition> brics = InitTF.getBricks();

        // Vuforia handling
        VectorF pos = InitVuforia.readPos();

        float phi = imu.getAngle();

        return new Input(pos);// Это жесть. Будучи получено, оно (lastPos) само апдейтиться....
    }

}


