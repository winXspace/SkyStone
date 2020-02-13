package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.LABEL_SKY_STONE;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.LABEL_STONE;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone.TFOD_MODEL_ASSET;
import static org.firstinspires.ftc.teamcode.auto.Utils.log;

public class InitTF {
    public static TFObjectDetector initTF(VuforiaLocalizer vuforia, int camId){

        TFObjectDetector tfod = null;

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

        return tfod;
    }

}
