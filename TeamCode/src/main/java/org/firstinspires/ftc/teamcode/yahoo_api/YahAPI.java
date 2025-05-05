package org.firstinspires.ftc.teamcode.yahoo_api;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

public class YahAPI {
    public static Gamepad gamepad_instance;

    public YahAPI(Gamepad gamepad_instance2) {
        this.gamepad_instance = gamepad_instance;
    }
    public static gamepad controller = new gamepad(gamepad_instance);
}
