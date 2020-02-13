package org.firstinspires.ftc.teamcode.auto;

import android.content.Context;
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

import java.util.List;

import static org.firstinspires.ftc.teamcode.auto.InitTF.initTF;
import static org.firstinspires.ftc.teamcode.auto.InitVuforia.initVuforia;
import static org.firstinspires.ftc.teamcode.auto.Utils.log;



enum State{
    LOCALISE,
    GO,
    STOP
}

@Autonomous(name = "Auto1", group = "SLAM")
public class Auto1 extends LinearOpMode {

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private State currentState = State.LOCALISE;
    private VectorF currentTarget;

    Bot bot;
    //private static final float mmPerInch        = 25.4f;

    private List<VuforiaTrackable> allTrackables;
    VectorF lastPos;
    OpenGLMatrix lastLoc;

    @Override
    public void runOpMode() {
        Utils.init(telemetry);
        Context ctx = hardwareMap.appContext;

        // --- init Vuforia --------
        Pair<VuforiaLocalizer,List<VuforiaTrackable>> res = initVuforia(
                ctx.getResources().getIdentifier("cameraMonitorViewId", "id", ctx.getPackageName()));

        vuforia = res.first; allTrackables = res.second;

        // --- init TensorFlow -----
//        tfod = initTF(
//                vuforia,
//                ctx.getResources().getIdentifier("tfodMonitorViewId", "id", ctx.getPackageName()));

        // --- init Bot ------------
        bot = Bot.getInstance(hardwareMap);

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

                //writeOutput(currentTarget, input.pos, currentState );



                telemetry.update();

            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }

        //TODO: Vuforial.deactivate();
    }

    private void writeOutput(VectorF currentTarget, VectorF currentPos, State currentState) {

        switch (currentState){
            case GO:
                if (currentPos != null) {
                VectorF steering3d = currentPos.subtracted(currentTarget).normalized3D();
                VectorF steering = new VectorF(steering3d.get(0), steering3d.get(1));
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
                    currentTarget = new VectorF(-1500f, -400f, 0);
                    return State.GO;
                }
                else return State.LOCALISE;

            case GO:
                if (i.pos == null){
                    return State.LOCALISE;
                }
                else if( getDistance(i.pos, currentTarget) < 100f){
                    return State.STOP;
                }
                else {
                    if (i.pos!= null && currentTarget != null) log("distance:", getDistance(i.pos, currentTarget));
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
        if (false && tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                // Временно закомментирую, ибо мешает смотреть лог с меток...
                //telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {

                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                }
                //telemetry.update();
            }
        }


        // Vuforia handling
        for (VuforiaTrackable trackable : allTrackables) {
            VuforiaTrackableDefaultListener l = (VuforiaTrackableDefaultListener)trackable.getListener();
            if (l.isVisible()){

                OpenGLMatrix loc = l.getUpdatedRobotLocation();
                if (loc != null) {
                    lastPos = loc.getTranslation();
                }
                break;
            }
        }

        return new Input(lastPos);// Это жесть. Будучи получено, оно (lastPos) само апдейтиться....
    }

}


