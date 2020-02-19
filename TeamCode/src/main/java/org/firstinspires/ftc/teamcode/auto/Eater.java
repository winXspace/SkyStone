package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

import static org.firstinspires.ftc.teamcode.auto.Utils.log;

public class Eater {
    // return velocity in robots coord system...
    public  VectorF getVelocity(List<Recognition> bricks){
        if (bricks != null && bricks.size() > 0){
            VectorF d = getD(bricks.get(0));
            //VectorF v = new VectorF(0, -d,0);
            return norm(d);
        }
        return new VectorF(0,0,0);// TODO: проверить, правильно ли так делать...
    }

    public boolean done(List<Recognition> bricks) {
        //float d = getD(bricks.get(0));
        //return Math.abs(d) < 10;// px
        //Recognition b = bricks.get(0);
        VectorF d = getD(bricks.get(0));

        log("d:", d);

        //return Math.abs(d.get(0)) < 20 && Math.abs(d.get(1)) < 10;
        return Math.abs(d.get(0)) < 30;
    }

    private VectorF getD(Recognition brick){
        //return (brick.getLeft() + brick.getRight() - brick.getImageWidth())/2;
        float dl = 660f - brick.getLeft();// установлено экспетиметально. Зависит от положения камеры и захвата.
        //float dw = 1700 - brick.getWidth();
        float dt = 450 - brick.getTop();// это будет направлени "вперёд" - координата x для робота
        return new VectorF(dt, dl, 0);
    }

    private static VectorF norm (VectorF v){
        float m = v.magnitude();
        if (m == 0){
            return new VectorF(0,0, 0);
        }else
        {
            return v.multiplied(1 / m);
        }

    }
}
