package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.ArrayList;
import java.util.List;

public class TelemetryManager {
    private static TelemetryManager instance = null;
    private final Telemetry telemetry;
    private final List<TelemetryData> dataList;

    private TelemetryManager(Telemetry telemetry) {
        this.telemetry = telemetry;
        this.dataList = new ArrayList<>();
    }

    public static TelemetryManager getInstance(Telemetry telemetry) {
        if (instance == null && telemetry != null) {
            instance = new TelemetryManager(telemetry);
        }
        return instance;
    }

    public void addData(String caption, String value) {
        dataList.add(new TelemetryData(caption, value));
    }

    public void update() {
        if (telemetry == null) {
            dataList.clear();
            return;
        }
        for (TelemetryData data : dataList) {
            telemetry.addData(data.caption, data.value);
        }
        telemetry.update();
        dataList.clear();
    }

    private static class TelemetryData {
        String caption;
        String value;

        TelemetryData(String caption, String value) {
            this.caption = caption;
            this.value = value;
        }
    }
}