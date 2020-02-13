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
    private ArrayList<Data> chassis;
    private ArrayList<DcMotor> intake;

    public static Bot getInstance(HardwareMap hardwareMap){
        if (bot == null){
            bot = new Bot(hardwareMap);
        }
        return bot;
    }

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
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // intake
        intake = new ArrayList<>();
        intake.add(hardwareMap.get(DcMotor.class, "m21"));
        intake.add(hardwareMap.get(DcMotor.class, "m22"));
        for(DcMotor m : intake){ m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);}
        intake.get(0).setDirection(DcMotorSimple.Direction.FORWARD);
        intake.get(1).setDirection(DcMotorSimple.Direction.REVERSE);

    }

    /**
     * Motion of robot in desired direction
     * @param steering should be 2d vector(not 3d)!
     */
    public void go(VectorF steering){
        for(Data d : chassis){
            float p = steering.dotProduct(d.dir);
            p *= 0.2;// ATTENTION: намеренно уменьшаем скорость в целях дебага... TODO: убрать эту строчку
            d.motor.setPower(Range.clip(p, -1.0f, 1.0f));
        }
    }

    // stop the robot
    public void stop() {
        for(Data d : chassis){
            d.motor.setPower(0);
        }
    }

    // lifting the brick
    public void lift(double p){
        liftDrive.setPower(Range.clip(p, -1.0f, 1.0f));
    }

    // taking or freeing the brick to/from intake
    public void take(double p){
        for(DcMotor m : intake){
            m.setPower(Range.clip(p, -1.0f, 1.0f));
        }
    }
}
