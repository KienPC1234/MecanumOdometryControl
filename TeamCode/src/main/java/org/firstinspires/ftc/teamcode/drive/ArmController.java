package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;

public class ArmController {
    private final Servo armServo;
    private final Telemetry telemetry;
    private double currentAngle;
    private static final double SERVO_MIN_POS = 0.0;
    private static final double SERVO_MAX_POS = 1.0;
    private static final double ANGLE_RANGE = 300.0;

    public ArmController(Servo armServo, Telemetry telemetry) {
        this.armServo = armServo;
        this.telemetry = telemetry;
        this.currentAngle = 0;
    }

    public void update(Gamepad gamepad) {
        double targetAngle = currentAngle;
        if (gamepad.a) {
            targetAngle = 45;
        } else if (gamepad.b) {
            targetAngle = 90;
        }

        if (targetAngle != currentAngle) {
            setAngle(armServo, targetAngle);
            currentAngle = targetAngle;
        }

        telemetry.addData("Arm Angle (deg)", String.format(Locale.US, "%.1f", currentAngle));
    }

    private void setAngle(Servo servo, double angle) {
        double normalizedAngle = Math.max(0, Math.min(angle, ANGLE_RANGE));
        double position = (normalizedAngle / ANGLE_RANGE) * (SERVO_MAX_POS - SERVO_MIN_POS) + SERVO_MIN_POS;
        servo.setPosition(position);
    }
}