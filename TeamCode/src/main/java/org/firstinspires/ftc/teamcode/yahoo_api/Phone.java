package org.firstinspires.ftc.teamcode.yahoo_api;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Phone {
    public static Telemetry normal;
    public static Telemetry addon;

    public Phone(Telemetry normal, Telemetry addon){
        this.normal = normal;
        this.addon = addon;
    }

    public void add_line(String line) {
        normal.addLine(line);
        addon.addLine(line);
    }
    public void addLine(String line) {
        normal.addLine(line);
        addon.addLine(line);
    }
    public void print(String value, Object number){
        normal.addData(value,number);
        addon.addData(value,number);
    }
}
