package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoRed extends Auto1 {
    @Override
    public void runOpMode() {
        startAngle = 90;
        bridgeTarget = redBridge;
        super.runOpMode();
    }
}
