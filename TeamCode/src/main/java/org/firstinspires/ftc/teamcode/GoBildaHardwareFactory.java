package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class GoBildaHardwareFactory implements HardwareFactory {
    private final HardwareMap hardwareMap;

    public GoBildaHardwareFactory(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public RobotHardware createRobotHardware() {
        return new RobotHardware(hardwareMap);
    }
}