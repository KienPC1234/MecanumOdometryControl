package org.firstinspires.ftc.teamcode.hardware;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;

import java.util.Locale;

public class OdometryManager {
    private final GoBildaPinpointDriver pinpoint;
    private final Telemetry telemetry;
    final double alpha = 0.98;
    private double fusedHeading;
    private double lastWheelHeading;
    private double lastImuHeading;

    public OdometryManager(GoBildaPinpointDriver pinpoint, Telemetry telemetry) {
        this.pinpoint = pinpoint;
        this.telemetry = telemetry;
        this.fusedHeading = 0;
        this.lastWheelHeading = 0;
        this.lastImuHeading = 0;
    }

    public void update() {
        pinpoint.update();
        Pose2D pose = pinpoint.getPosition();
        double wheelHeading = pinpoint.getHeading(AngleUnit.RADIANS);
        double imuHeading = pinpoint.getHeading(AngleUnit.RADIANS);

        double deltaWheel = normalizeAngle(wheelHeading - lastWheelHeading);
        double deltaImu = normalizeAngle(imuHeading - lastImuHeading);

        fusedHeading = normalizeAngle(alpha * (fusedHeading + deltaImu) + (1 - alpha) * (fusedHeading + deltaWheel));
        lastWheelHeading = wheelHeading;
        lastImuHeading = imuHeading;


        telemetry.addData("X (in)", String.format(Locale.US, "%.3f", pose.getX(DistanceUnit.INCH)));
        telemetry.addData("Y (in)", String.format(Locale.US, "%.3f", pose.getY(DistanceUnit.INCH)));
        telemetry.addData("Heading (deg)", String.format(Locale.US, "%.3f", Math.toDegrees(fusedHeading)));
    }

    private double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}