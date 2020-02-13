package org.firstinspires.ftc.teamcode.auto;


import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Utils {
    private static Telemetry t;
    public static void init (Telemetry t){
        Utils.t = t;
    }

    public static void log (String caption, String format, Object... args){
        // TODO
        //if(t != null){t.addData(caption,format)};
    }
    public static void log(String caption, Object o){
        if (t != null){ t.addData(caption, o);}
    }
    public static void log(String caption,  String format, Object o){
        if (t != null){ t.addData(caption, format, o );}
    }


    public static void log(String caption, Func v){
        if (t != null){ t.addData(caption, v);}
    }
    public static void log(String caption,  String format, Func valueProducer){
        if (t != null){ t.addData(caption, format, valueProducer);}
    }



}
