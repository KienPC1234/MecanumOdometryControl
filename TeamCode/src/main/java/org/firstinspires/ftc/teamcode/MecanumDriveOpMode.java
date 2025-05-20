package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
        ArmController arm = new ArmController(robot, new PreciseAngleControlStrategy(),telemetry);

        robot.getPinpoint().setOffsets(-84.0, -168.0, DistanceUnit.MM);
        robot.getPinpoint().setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        robot.getPinpoint().setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        robot.getPinpoint().resetPosAndIMU();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("X offset (mm)", robot.getPinpoint().getXOffset(DistanceUnit.MM));
        telemetry.addData("Y offset (mm)", robot.getPinpoint().getYOffset(DistanceUnit.MM));
        telemetry.addData("Device Version", robot.getPinpoint().getDeviceVersion());
        telemetry.update();

        waitForStart();
        resetRuntime();

        while (opModeIsActive()) {
            drive.update(gamepad1);
            odometry.update();
            arm.update(gamepad1);
            telemetryManager.update();
        }
    }
}