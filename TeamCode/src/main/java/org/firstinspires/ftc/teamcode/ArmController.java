package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ArmController {
    private final RobotHardware robot;
    private final ArmControlStrategy strategy;

    public ArmController(RobotHardware robot, ArmControlStrategy strategy) {
        this.robot = robot;
        this.strategy = strategy;
    }

    public void update(Gamepad gamepad) {
        if (gamepad.a) {
            strategy.setAngle(robot.getArmServo(), 45);
        } else if (gamepad.b) {
            strategy.setAngle(robot.getArmServo(), 90);
        }
    }
}