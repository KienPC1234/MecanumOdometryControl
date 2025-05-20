package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.drive.ArmController;
import org.firstinspires.ftc.teamcode.hardware.OdometryManager;

@TeleOp(name = "MecanumDriveOpMode", group = "Linear Opmode")
public class MecanumDriveOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        HardwareMap hardwareMap = this.hardwareMap;
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        Servo armServo = hardwareMap.get(Servo.class, "armServo");
        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        MecanumDrive drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
        OdometryManager odometry = new OdometryManager(pinpoint, telemetry);
        ArmController arm = new ArmController(armServo, telemetry);

        pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        pinpoint.resetPosAndIMU();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("X offset (mm)", pinpoint.getXOffset(DistanceUnit.MM));
        telemetry.addData("Y offset (mm)", pinpoint.getYOffset(DistanceUnit.MM));
        telemetry.addData("Device Version", pinpoint.getDeviceVersion());
        telemetry.update();

        waitForStart();
        resetRuntime();

        while (opModeIsActive()) {
            drive.update(gamepad1);
            odometry.update();
            arm.update(gamepad1);
            telemetry.update();
        }
    }
}