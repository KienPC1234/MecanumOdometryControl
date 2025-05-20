package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public interface ArmControlStrategy {
    void setAngle(Servo servo, double angle);
}