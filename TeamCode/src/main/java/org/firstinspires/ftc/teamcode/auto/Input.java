package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

public class Input {
    public VectorF pos;
    public float angle;
    Input(VectorF pos, float angle){
        this.pos = pos;
        this.angle = angle;
    }

    public String toString(){
        String s = "";
        s += "pos:" + (pos != null ? pos.toString(): "null");
        s += "angle:" + String.valueOf(angle);//(angle != null ? angle.toString(): "null");
        return s;
    }
}
