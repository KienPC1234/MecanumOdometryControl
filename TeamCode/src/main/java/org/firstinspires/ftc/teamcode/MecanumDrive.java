package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MecanumDrive {
    private final RobotHardware robot;
    private static final double MAX_POWER = 1.0;

    public MecanumDrive(RobotHardware robot) {
        this.robot = robot;
    }

    public void update(Gamepad gamepad) {
        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx = gamepad.right_stick_x;

        double frontLeftPower = y + x + rx;
        double frontRightPower = y - x - rx;
        double backLeftPower = y - x + rx;
        double backRightPower = y + x - rx;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        frontLeftPower *= MAX_POWER;
        frontRightPower *= MAX_POWER;
        backLeftPower *= MAX_POWER;
        backRightPower *= MAX_POWER;

        robot.getFrontLeft().setPower(frontLeftPower);
        robot.getFrontRight().setPower(frontRightPower);
        robot.getBackLeft().setPower(backLeftPower);
        robot.getBackRight().setPower(backRightPower);
    }
}