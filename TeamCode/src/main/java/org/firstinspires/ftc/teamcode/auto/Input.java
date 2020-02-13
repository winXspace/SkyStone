package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

public class Input {
    public VectorF pos;
    Input(VectorF pos){
        this.pos = pos;
    }
    public String toString(){
        String s = "";
        s += "pos:" + (pos != null ? pos.toString(): "null");
        return s;
    }
}
