package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.Locale;

public class ArmController {
    private final RobotHardware robot;
    private final ArmControlStrategy strategy;
    private double currentAngle;

    public ArmController(RobotHardware robot, ArmControlStrategy strategy) {
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
        TelemetryManager.getInstance(null).addData("Arm Angle (deg)", String.format(Locale.US, "%.1f", currentAngle));
    }
}