package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import java.util.Locale;

public class OdometryManager {
    private final RobotHardware robot;
    private final TelemetryManager telemetryManager;
    private final ElapsedTime runtime;
    private final double alpha;
    private double fusedHeading;
    private double lastWheelHeading;
    private double lastImuHeading;
    private double lastTime;

    private OdometryManager(Builder builder) {
        this.robot = builder.robot;
        this.telemetryManager = builder.telemetryManager;
        this.alpha = builder.alpha;
        this.runtime = new ElapsedTime();
        this.fusedHeading = 0;
        this.lastWheelHeading = 0;
        this.lastImuHeading = 0;
        this.lastTime = 0;
    }

    public void update() {
        robot.getPinpoint().update();
        Pose2D pose = robot.getPinpoint().getPosition();
        double wheelHeading = robot.getPinpoint().getHeading(AngleUnit.RADIANS);
        double imuHeading = getRawImuHeading();

        double deltaWheel = normalizeAngle(wheelHeading - lastWheelHeading);
        double deltaImu = normalizeAngle(imuHeading - lastImuHeading);

        fusedHeading = normalizeAngle(alpha * (fusedHeading + deltaImu) + (1 - alpha) * (fusedHeading + deltaWheel));
        lastWheelHeading = wheelHeading;
        lastImuHeading = imuHeading;

        double currentTime = runtime.seconds();
        double loopTime = currentTime - lastTime;
        double frequency = loopTime > 0 ? 1 / loopTime : 0;
        lastTime = currentTime;

        telemetryManager.addData("X (in)", String.format(Locale.US, "%.3f", pose.getX(DistanceUnit.INCH)));
        telemetryManager.addData("Y (in)", String.format(Locale.US, "%.3f", pose.getY(DistanceUnit.INCH)));
        telemetryManager.addData("Heading (deg)", String.format(Locale.US, "%.3f", Math.toDegrees(fusedHeading)));
        telemetryManager.addData("Runtime", String.format(Locale.US, "%.2f", currentTime));
        telemetryManager.addData("Device Status", robot.getPinpoint().getDeviceStatus().toString());
        telemetryManager.addData("Pinpoint Frequency", String.format(Locale.US, "%.2f", robot.getPinpoint().getFrequency()));
        telemetryManager.addData("Loop Frequency", String.format(Locale.US, "%.2f", frequency));
    }

    private double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }

    private double getRawImuHeading() {
        return robot.getPinpoint().getHeading(AngleUnit.RADIANS);
    }

    public static class Builder {
        private RobotHardware robot;
        private TelemetryManager telemetryManager;
        private double alpha = 0.98;

        public Builder setRobotHardware(RobotHardware robot) {
            this.robot = robot;
            return this;
        }

        public Builder setTelemetryManager(TelemetryManager telemetryManager) {
            this.telemetryManager = telemetryManager;
            return this;
        }

        public Builder setAlpha(double alpha) {
            this.alpha = Math.max(0, Math.min(1, alpha));
            return this;
        }

        public OdometryManager build() {
            return new OdometryManager(this);
        }
    }
}