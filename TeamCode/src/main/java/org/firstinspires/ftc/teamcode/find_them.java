package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;

@Autonomous(name = "Find Them.")
public class find_them extends LinearOpMode {
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
        lens.selectAlgorithm(HuskyLens.Algorithm.FACE_RECOGNITION);
        while (opModeIsActive()) {
            for (HuskyLens.Block block : lens.blocks()){
                if (block.id == 1){
                    lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
                    if (block.x > 120) {
                        robot.turnTo(10,1,1000);
                    } else if (block.x < 120) {
                        robot.turnTo(10,-1,1000);
                    }
                } else {
                    lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                }

            }
        }
    }
}
