package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class PreciseAngleControlStrategy implements ArmControlStrategy {
    private static final double SERVO_MIN_POS = 0.0;
    private static final double SERVO_MAX_POS = 1.0;
    private static final double ANGLE_RANGE = 180.0;

    @Override
    public void setAngle(Servo servo, double angle) {
        double normalizedAngle = Math.max(0, Math.min(angle, ANGLE_RANGE));
        double position = (normalizedAngle / ANGLE_RANGE) * (SERVO_MAX_POS - SERVO_MIN_POS) + SERVO_MIN_POS;
        servo.setPosition(position);
    }
}