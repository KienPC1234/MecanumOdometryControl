package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;

public class ArmController {
    private final RobotHardware robot;
    private final ArmControlStrategy strategy;
    private double currentAngle;
    private final Telemetry telemetry;

    public ArmController(RobotHardware robot, ArmControlStrategy strategy , Telemetry telemetry) {
        this.telemetry = telemetry;
        this.robot = robot;
        this.strategy = strategy;
        this.currentAngle = 0;
    }

    public void update(Gamepad gamepad) {
        if (gamepad.a) {
            strategy.setAngle(robot.getArmServo(), 45);
            currentAngle = 45;
        } else if (gamepad.b) {
            strategy.setAngle(robot.getArmServo(), 90);
            currentAngle = 90;
        }
        telemetry.addData("Arm Angle (deg)", String.format(Locale.US, "%.1f", currentAngle));
    }
}