package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.MotionDetection;

import java.util.Iterator;
import java.util.List;

import static org.firstinspires.ftc.teamcode.auto.Utils.log;

public class TrajectoryRegulator {
    private List<VectorF> currentPath;
    VectorF currentTarget;
    Iterator <VectorF> iterator;
    public void go (List<VectorF> path){
        currentPath = path;
        iterator = path.iterator();
        currentTarget = iterator.next();//path.get(0);

        _finished = false;
    }
    private boolean _finished = false;
    public boolean finished() {
        return _finished;
    }

    public VectorF getVelocity(VectorF _currentPos) {

        VectorF currentPos = new VectorF(_currentPos.get(0), _currentPos.get(1));
        VectorF e = currentPos.subtracted(currentTarget);
        float d = e.magnitude();
        log("d:", d);
        log("currentTarget:", currentTarget);
        if (d < 50f){
            if(iterator.hasNext()){
                currentTarget = iterator.next();
                return norm(currentPos.subtracted(currentTarget));
            }else {
                _finished = true;
                return new VectorF(0,0);
            }
        }else{
            return norm(e);
        }


    }

    private VectorF norm (VectorF v){
        float m = v.magnitude();
        if (m == 0){
            return new VectorF(0,0);
        }else
        {
            return v.multiplied(1 / m);
        }

    }
}
