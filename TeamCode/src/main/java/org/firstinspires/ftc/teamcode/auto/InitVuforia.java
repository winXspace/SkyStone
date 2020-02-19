package org.firstinspires.ftc.teamcode.auto;
import android.util.Pair;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.MyKeys;

import java.util.ArrayList;
import java.util.List;

public class InitVuforia {


    private static final float mmPerInch        = 25.4f;

    // Здесь непонятно. Первое значение(5.75 дюйма) в этом документе:https://www.firstinspires.org/sites/default/files/uploads/resource_library/ftc/field-setup-guide.pdf
    // Второе значение(6 дюймов) - из кода на оффрепозитории..
    // TODO: уточнить правильное значение...
    //private static final float mmTargetHeight   = 146f;// (5.75) * mmPerInch;// the height of the center of the target image above the floor
    private static final float mmTargetHeight   = 152.4f;// (6) * mmPerInch;// the height of the center of the target image above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;                                 // Units are degrees
    private static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    private static final float halfField = 1828.8f;//72 * mmPerInch;
    private static final float quadField  = 914.4f;//36 * mmPerInch;

    private static VuforiaTrackables targetsSkyStone;
    private static List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();


    public static VuforiaLocalizer initVuforia(int cameraMonitorViewId ) {

        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = MyKeys.vuforiaKey;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;// Works...

        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);



/*

        targetsSkyStone = vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");

        // bridge
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");

        //red
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        // front
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        // blue
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        // rear
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");



        allTrackables.addAll(targetsSkyStone);


        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        blueFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        blueRearBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        redFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        redRearBridge.setLocation(OpenGLMatrix
                .translation(bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        red1.setLocation(OpenGLMatrix
                .translation(quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));





        red2.setLocation(OpenGLMatrix
                .translation(-quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        // Повесим Red2 временно в центр системы координат(центр поля). Ориентация картинки - прежняя..
//        red2.setLocation(OpenGLMatrix
//                .translation( 0, 0, mmTargetHeight)
//                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES,
//                        90,
//                        0,
//                        0)));
//



        //front1.setLocation(OpenGLMatrix
        //      .translation(-halfField, -quadField, mmTargetHeight)
        //    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));



        front1.setLocation(OpenGLMatrix
                .translation(0, 0, mmTargetHeight)// в центре
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES,// ориентация как у blue1/blue2
                        90, // повернули вокруг оси X на 90 градусов, по правиу буравчика
                        0 ,
                        0)));





        front2.setLocation(OpenGLMatrix
                .translation(-halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blue1.setLocation(OpenGLMatrix
                .translation(-quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        blue2.setLocation(OpenGLMatrix
                .translation(quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        rear1.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

        rear2.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));



        //--- Задняя камера, лендскейп режим
        float phoneYRotate = -90;// так как у нас задняя камера
        float phoneXRotate = 0;
        float phoneZRotate = 0;
        //float phoneZRotate = 180;// сама камера смотрит назад


        // Next, translate the camera lens to where it is on the robot.
        final float CAMERA_FORWARD_DISPLACEMENT  = 80f;
        final float CAMERA_VERTICAL_DISPLACEMENT = 115.0f; // 30 мм над землёй
        final float CAMERA_LEFT_DISPLACEMENT     = 90f;

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));


        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        targetsSkyStone.activate();



        */
        return vuforia;
    }

    private static OpenGLMatrix lastLoc;

    public static OpenGLMatrix readLoc(){
        for (VuforiaTrackable trackable : allTrackables) {
            VuforiaTrackableDefaultListener l = (VuforiaTrackableDefaultListener)trackable.getListener();
            if (l.isVisible()){

                OpenGLMatrix loc = l.getUpdatedRobotLocation();
                if (loc != null) {
                    lastLoc = loc;
                }
                break;// нашли первый видимый
            }
        }
        return lastLoc;
    }

    public static VectorF readPos(){
        OpenGLMatrix loc = readLoc();
        if (loc != null){
            return loc.getTranslation();
        }else {
            return null;
        }

    }

    public static float readAngle(){
        OpenGLMatrix loc = readLoc();
        if (loc != null){
            Orientation rot = Orientation.getOrientation(loc, EXTRINSIC, XYZ, DEGREES);
            return rot.thirdAngle;
        }else {
            return 0;// holly shit! TODO: refactor to use kind of maybe type or ideomatic andorid @Nullable annotaition...
        }


    }



    public static void deactivate(){
        if (targetsSkyStone != null){
            targetsSkyStone.deactivate();
        }
    }


}
