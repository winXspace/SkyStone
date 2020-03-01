package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class Input {
    public VectorF pos;
    public float angle;
    public List<Recognition> bricks;
    Input(VectorF pos, float angle, List<Recognition> bricks){
        this.pos = pos;
        this.angle = angle;
        this.bricks = bricks;
    }

    public String toString(){
        String s = "";
        s += "pos:" + (pos != null ? pos.toString(): "null");
        s += "angle:" + String.valueOf(angle);//(angle != null ? angle.toString(): "null");
        s += "brics num:" + (bricks != null ? String.valueOf(bricks.size()): "0");
        return s;
    }
}
