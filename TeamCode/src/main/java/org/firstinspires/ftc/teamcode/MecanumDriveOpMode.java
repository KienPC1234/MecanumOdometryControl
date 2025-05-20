package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.external.samples.RobotHardware;

@TeleOp(name = "MecanumDriveOpMode", group = "Linear Opmode")
public class MecanumDriveOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        HardwareFactory hardwareFactory = new GoBildaHardwareFactory(hardwareMap);
        RobotHardware robot = hardwareFactory.createRobotHardware();
        TelemetryManager telemetryManager = TelemetryManager.getInstance(telemetry);
        MecanumDrive drive = new MecanumDrive(robot);
        OdometryManager odometry = new OdometryManager.Builder()
                .setRobotHardware(robot)
                .setTelemetryManager(telemetryManager)
                .setAlpha(0.98)
                .build();
        ArmController arm = new ArmController(robot, new PreciseAngleControlStrategy());

        waitForStart();

        while (opModeIsActive()) {
            drive.update(gamepad1);
            odometry.update();
            arm.update(gamepad1);
            telemetryManager.update();
        }
    }
}