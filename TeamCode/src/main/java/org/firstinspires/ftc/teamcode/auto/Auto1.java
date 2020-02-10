package org.firstinspires.ftc.teamcode.auto;

import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.MyKeys;


import java.util.List;

enum State{
    LOCALISE,
    GO,
    STOP
}

@Autonomous(name = "Auto1", group = "SLAM")
public class Auto1 extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY = MyKeys.vuforiaKey;

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;
    private State currentState = State.LOCALISE;
    private VectorF currentTarget;

    Bot bot;

    @Override
    public void runOpMode() {
        int vId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        Pair<VuforiaLocalizer,List<VuforiaTrackable>> res = InitVuforia.initVuforia(vId);

        vuforia = res.first;
        allTrackables = res.second;

        bot = Bot.getInstance(hardwareMap);

        // DEBUG
        if ( true && ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();





        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                Input input = readInput();

                //telemetry.addData("Input.pos:", input.pos.get(0));

                currentState = nextState(input, currentState);

                telemetry.addData("CurrState:", currentState);

                writeOutput(currentTarget, input.pos, currentState );



                telemetry.update();

            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    private void writeOutput(VectorF currentTarget, VectorF currentPos, State currentState) {

        switch (currentState){
            case GO:
                if (currentPos != null) {
                VectorF steering = currentPos.subtracted(currentTarget).normalized3D();
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
                else return State.GO;

        }


        return s;
    }

    private float getDistance(VectorF a, VectorF b){
        return a.subtracted(b).length();
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

        VectorF pos = null;
        // Vuforia handling
        for (VuforiaTrackable trackable : allTrackables) {
            //telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //

            OpenGLMatrix location = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
            if (location!= null) {
                //pos = location.transform(  new VectorF(0,0,0));
                pos = location.getTranslation();

                lastPos = pos;
                telemetry.addData(trackable.getName(), "Виден");
                telemetry.addData("Pos", location.formatAsTransform());

            }
        }

        return new Input(pos);

    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tfodParameters.minimumConfidence = 0.8;

        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }


    //private static final float mmPerInch        = 25.4f;


    private List<VuforiaTrackable> allTrackables = null;
    VectorF lastPos = null;




}
