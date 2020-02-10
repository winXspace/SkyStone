package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.util.ArrayList;
import java.util.Arrays;

class Data{
    public final DcMotor motor;
    public final VectorF dir;

    public Data(DcMotor motor, VectorF dir){
      this.motor = motor;
      this.dir = dir;
  }
}

public class Bot {
    private static Bot bot;
    private final DcMotor liftDrive;

    public static Bot getInstance(HardwareMap hardwareMap){
        if (bot == null){
            bot = new Bot(hardwareMap);
        }
        return bot;
    }
    private ArrayList<Data> chassis;
    private Bot(HardwareMap hardwareMap){
        // chassis
        chassis = new ArrayList<>(
                Arrays.asList(
                        new Data(hardwareMap.get(DcMotor.class, "m10"), new VectorF (1.0f, 1.0f)),
                        new Data(hardwareMap.get(DcMotor.class, "m11"), new VectorF (1.0f, -1.0f)),
                        new Data(hardwareMap.get(DcMotor.class, "m12"), new VectorF (-1.0f, 1.0f)),
                        new Data(hardwareMap.get(DcMotor.class, "m13"), new VectorF (-1.0f, -1.0f))
        ));

        for(Data d : chassis){
            d.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.motor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        // lift
        liftDrive = hardwareMap.get(DcMotor.class, "m20");

        // intake
        //ilDrive = hardwareMap.get(DcMotor.class, "m20");
        //irDrive = hardwareMap.get(DcMotor.class, "m21");

    }

    public void go(VectorF steering3d){
        VectorF steering = new VectorF(steering3d.get(0), steering3d.get(1));
        for(Data d : chassis){
            float p = steering.dotProduct(d.dir);
            p *= 0.2;
            d.motor.setPower(Range.clip(p, -1.0f, 1.0f));
        }


    }

    public void stop() {
        for(Data d : chassis){
            d.motor.setPower(0);
        }
    }
}
