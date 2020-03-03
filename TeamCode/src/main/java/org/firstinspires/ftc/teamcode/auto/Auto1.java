package org.firstinspires.ftc.teamcode.auto;


import android.content.Context;
import android.hardware.SensorManager;
import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.teamcode.auto.InitTF.initTF;
import static org.firstinspires.ftc.teamcode.auto.InitVuforia.initVuforia;
import static org.firstinspires.ftc.teamcode.auto.InitVuforia.readPos;
import static org.firstinspires.ftc.teamcode.auto.Utils.log;



enum State{
    LOCALISE,
    EAT,
    EAT_CATCH,
    GO,
    STOP
}

@Autonomous(name = "Auto1", group = "SLAM")
public class Auto1 extends LinearOpMode {

    public float startAngle;


    private State currentState = State.LOCALISE;

    private TrajectoryRegulator tr = new TrajectoryRegulator();
    private Eater eater = new Eater();

    // Constants for perimeter targets
    private static final float halfField = 1828.8f;
    private static final float quadField  = 914.4f;

    protected List<VectorF> squarePath = new ArrayList<>(Arrays.asList(
            new VectorF(500,-500),
            new VectorF(500,-1500),
            new VectorF(-500,-1500),
            new VectorF(-500,-500)
    ));

    protected List<VectorF> triPath2 = new ArrayList<>(Arrays.asList(
            new VectorF(0,-500),
            new VectorF(500,-1500),
            new VectorF(-500,-1500),
            new VectorF(0,-500)
    ));


    protected List<VectorF> linePath = new ArrayList<>(Arrays.asList(
            new VectorF(0,-500),
            new VectorF(0,-1500),
            new VectorF(0,-500)
    ));


    protected List<VectorF> path4 = new ArrayList<>(Arrays.asList(
            new VectorF(0,-1000),
            new VectorF(1000,-1500),
            new VectorF(-1000,-1500)
    ));



    protected List<VectorF> redBridge = new ArrayList<>(Arrays.asList(
            new VectorF(0,-halfField + 450)
    ));


    protected List<VectorF> blueBridge = new ArrayList<>(Arrays.asList(
            new VectorF(0,halfField - 450)
    ));

    protected List<VectorF> bridgeTarget;


    Bot bot;
    IMU imu;
    //private static final float mmPerInch        = 25.4f;



    @Override
    public void runOpMode() {
        Utils.init(telemetry);
        Context ctx = hardwareMap.appContext;

        //Phone phone = Phone.getInstance(ctx);


        // --- init Vuforia --------
        VuforiaLocalizer vuforia = initVuforia(
                ctx.getResources().getIdentifier("cameraMonitorViewId", "id", ctx.getPackageName()));


        // --- init TensorFlow -----
        initTF(
                vuforia,
                ctx.getResources().getIdentifier("tfodMonitorViewId", "id", ctx.getPackageName()));

        // --- init Bot ------------
        bot = Bot.getInstance(hardwareMap);

        // --- init imu ------------
        //imu = new IMU(hardwareMap);
        // calibration
//        while (!isStopRequested() && !imu.isCalibrated())
//        {
//            sleep(50);
//            idle();
//        }

        //startPhi = imu.getAngle();

        OpenGLMatrix gamePadToRobotTrans = OpenGLMatrix
                .identityMatrix()
                .scaled(1,-1,1)
                .rotated(AxesReference.EXTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES,90, 0,0)

                ;


        /** Wait for the game to begin */
        log(">", "Press Play to start op mode");
        telemetry.update();




        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {



                Input input = readInput();
                log("Input.pos:", input);


                if (input != null){

                    currentState = nextState(input, currentState);
                    log("CurrState:", currentState);

                    writeOutput( input.pos, input.angle, input.bricks, currentState );
                }else{
                    log("Input null!", "bad");
                }



                //debug
                //VectorF _s = new VectorF(gamepad1.left_stick_x, gamepad1.left_stick_y, 0);
                //VectorF s = gamePadToRobotTrans.transform(_s);
                //bot.go(new VectorF(s.get(0), s.get(1)));




                telemetry.update();
            }
        }

        InitTF.shotdown();
        InitVuforia.deactivate();
    }

    private Input readInput(){
        // TF handling
        // DEBUG
        List<Recognition> bricks = InitTF.getBricks();

        // Vuforia handling
        OpenGLMatrix loc = null;//InitVuforia.readLoc(); DEBUG
        if (loc != null){
            VectorF pos = loc.getTranslation();
            Orientation rot = Orientation.getOrientation(loc, EXTRINSIC, XYZ, DEGREES);

            return new Input(pos, rot.thirdAngle, bricks);// Это жесть. Будучи получено, оно (lastPos) само апдейтиться....

        }else {
            return new Input(null, 0, bricks);
        }

        //float phi = imu.getAngle();


    }



    private State nextState(Input i/*may be null*/, State s){

        switch (currentState){
            case LOCALISE:
                if(i.pos != null) {
                    //currentTarget = new VectorF(0, -100f, 152.4f);
                    startAngle = i.angle;
                    //tr.go(triPath2);

                    return State.EAT;
                } else if(i.bricks != null){
                    return State.EAT;
                }
                else return State.LOCALISE;
                //break;
            case EAT:
                if (i.bricks == null || i.bricks.size() == 0 ) {
                    return State.EAT_CATCH;
                    //return State.STOP;
                    //startAngle = i.angle;
                    //tr.go(triPath2);// TODO: swtich to path to the building base
                    //return State.GO;
                }else if (eater.done(i.bricks)){
                    return State.EAT_CATCH;
                }
                break;
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

    private void writeOutput(VectorF currentPos, float currentAngle, List<Recognition> bricks, State currentState) {

        VectorF s;

        switch (currentState){

            case EAT:
                bot.servoSet(0.25f);
                s = eater.getVelocity(bricks);
                log("s:", s);
                bot.go(new VectorF(s.get(0), s.get(1)), 0.15f);

                break;
            case EAT_CATCH:
                bot.stop();
                bot.servoSet(1);
                break;

            case GO:
                if (currentPos != null) {

                    VectorF _steering = tr.getVelocity(currentPos);// в координатах поля

                    VectorF steering = new VectorF(_steering.get(0), _steering.get(1), 0);
                    OpenGLMatrix _m = Orientation.getRotationMatrix(EXTRINSIC, AxesOrder.ZXY, DEGREES, startAngle, 0, 0);
                    //OpenGLMatrix m = _m.inverted();
                    VectorF _s = _m.transform(steering);
                    s = new VectorF(_s.get(0),_s.get(1));
                    float a = currentAngle - startAngle;



                    log("a:", a);

                    //bot.go(s);
                    //bot.rot(-a * 0.01f);

                    bot.goAndRot(s, -a * 0.1f, 0.2f);
                }

                break;
            case STOP:
            case LOCALISE:
                bot.stop();
        }
    }


    private float getDistance(VectorF a, VectorF b){
        return a.subtracted(b).magnitude();
    }


}


