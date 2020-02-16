package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

@Autonomous
public class AutoTimeBlue extends AutoTimeRed {
    @Override
    public void runOpMode() {
        dir = new VectorF(0.0f, 1.0f);
        super.runOpMode();
    }
}
