package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import java.util.List;

@Autonomous(name = "Thonk")
public class thonk extends LinearOpMode {
    DcMotor backRight, frontRight, backLeft, frontLeft,axial,lateral,LeftLift,RightLift;
    SimplifiedOdometryRobot robot = new SimplifiedOdometryRobot(this);
    RevBlinkinLedDriver lights;
    HuskyLens lens;

    @Override
    public void runOpMode() throws InterruptedException {
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        //Attaching the variables declared with the physical motors by name or id
        {
            lights = hardwareMap.get(RevBlinkinLedDriver.class, "lights");
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            backLeft = hardwareMap.dcMotor.get("backLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            backRight = hardwareMap.dcMotor.get("backRight");
            LeftLift = hardwareMap.dcMotor.get("LeftLift");
            RightLift = hardwareMap.dcMotor.get("RightLift");
            axial = hardwareMap.dcMotor.get("axial");
            lateral = hardwareMap.dcMotor.get("lateral");
            frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            backRight.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BREATH_RED);
        while (opModeIsActive()) {
            // Move to bar
            lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            robot.drive(24,1,1);
            robot.strafe(24,-1,1);
            robot.drive(24,1,1);
            LeftLift.setTargetPosition(3600);
            RightLift.setTargetPosition(3600);
            lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.GOLD);
            LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftLift.setPower(1);
            RightLift.setPower(1);
            sleep(1000);
            lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            //put lift up
            LeftLift.setTargetPosition(0);
            RightLift.setTargetPosition(0);
            LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftLift.setPower(-1);
            RightLift.setPower(-1);
            LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //put lift down
            //do the block thing
            robot.drive(12,-1,1);
            robot.strafe(36,1,1);
            robot.drive(36,1,1);
            robot.strafe(12,1,1);
            robot.drive(36,-1,5);
            robot.drive(36,1,5);
            robot.strafe(12,1,1);
            robot.drive(36,-1,5);
            robot.drive(36,1,5);
            robot.strafe(12,1,1);
            robot.drive(36,-1,5);
            //Move lift back down
            LeftLift.setTargetPosition(0);
            RightLift.setTargetPosition(0);
            LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftLift.setPower(-1);
            RightLift.setPower(-1);
            telemetry.update();
        }
    }
}
