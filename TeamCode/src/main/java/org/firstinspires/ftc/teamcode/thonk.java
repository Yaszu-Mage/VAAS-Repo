package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import java.util.List;

@Autonomous(name = "M-Autotest1")
public class thonk extends LinearOpMode {
    DcMotor backRight, frontRight, backLeft, frontLeft,LeftLift,RightLift;
    EssentialMecanumRobot robot = new EssentialMecanumRobot(this);
    HuskyLens lens;
    Integer state = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        //Attaching the variables declared with the physical motors by name or id
        robot.initialize(true);
        {
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            backLeft = hardwareMap.dcMotor.get("backLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            backRight = hardwareMap.dcMotor.get("backRight");
            LeftLift = hardwareMap.dcMotor.get("LeftLift");
            RightLift = hardwareMap.dcMotor.get("RightLift");
            frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            backRight.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        waitForStart();
        while (opModeIsActive()) {
            // Move to bar
            if (state == 0) {
                robot.drive(20, 1, 0.1);
                robot.strafe(20, -1, 0.1);
                telemetry.addData("State", state);
            }
            telemetry.addData("State", state);
            telemetry.update();
        }
    }
}
