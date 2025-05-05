package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.yahoo_api.YahAPI;
import org.firstinspires.ftc.teamcode.yahoo_api.gamepad;

public class test_clas extends LinearOpMode {
    public gamepad controller1 = new gamepad(gamepad1);

    @Override
    public void runOpMode() throws InterruptedException {
        if (controller1.right_trigger == Boolean.valueOf(true)) {

        }
    }
}
