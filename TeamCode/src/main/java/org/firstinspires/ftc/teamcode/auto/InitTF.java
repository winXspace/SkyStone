package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.LABEL_SKY_STONE;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.LABEL_STONE;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.TFOD_MODEL_ASSET;
import static org.firstinspires.ftc.teamcode.auto.Utils.log;

public class InitTF {
    private static TFObjectDetector tfod;
    public static void initTF(VuforiaLocalizer vuforia, int camId){

        if ( ClassFactory.getInstance().canCreateTFObjectDetector()) {

            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(camId);

            tfodParameters.minimumConfidence = 0.8;

            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_STONE, LABEL_SKY_STONE);

            if (tfod != null) {
                tfod.activate();
            }

        } else {
            log("Sorry!", "This device is not compatible with TFOD");
        }
    }

    private static List<Recognition> lastBricks;
    public static List<Recognition> getBricks(){
        List<Recognition> updatedRecognitions = null;
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            updatedRecognitions = tfod.getUpdatedRecognitions();
            //updatedRecognitions = tfod.getRecognitions();
            if (updatedRecognitions != null) {
                lastBricks = updatedRecognitions;
                // Временно закомментирую, ибо мешает смотреть лог с меток...
                //telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
//                int i = 0;
//                for (Recognition recognition : updatedRecognitions) {
//
//                    log(String.format("label (%d)", i), recognition.getLabel());
//                    log(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                            recognition.getLeft(), recognition.getTop());
//                    log(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                            recognition.getRight(), recognition.getBottom());
//                    i++;
//                }
                //telemetry.update();
            }
        }

        return lastBricks;
        //return updatedRecognitions;
    }

    public static void shotdown() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }
}
