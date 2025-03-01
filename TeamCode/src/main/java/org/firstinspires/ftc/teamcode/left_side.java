package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import android.os.Environment;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

@Autonomous(name = "mPlay_left_side")
public class left_side extends LinearOpMode {

    String FILENAME = "20525autoleftside";


    ArrayList<HashMap<String, Double>> recording = new ArrayList<>();
    public void runOpMode() {
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        {
            LeftLift = hardwareMap.dcMotor.get("LeftLift");
            RightLift = hardwareMap.dcMotor.get("RightLift");
            HighClaw = hardwareMap.servo.get("HighClaw");
            LeftHClaw = hardwareMap.servo.get("LeftHClaw");
            RightHClaw = hardwareMap.servo.get("RightHClaw");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
            backLeftMotor = hardwareMap.dcMotor.get("backLeft");
            frontRightMotor = hardwareMap.dcMotor.get("frontRight");
            backRightMotor = hardwareMap.dcMotor.get("backRight");
            LeftSlide = hardwareMap.servo.get("LeftSlide");
            RightSlide = hardwareMap.servo.get("RightSlide");
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            LeftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            RightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LeftLift.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        loadDatabase();
        waitForStart();
        if (opModeIsActive()) {
            runtime.reset();
            while(opModeIsActive()){
                playRecording(recording);
            }

        }

    }
    final ElapsedTime runtime = new ElapsedTime();
    DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,LeftLift,RightLift;
    Servo RightHClaw, LeftHClaw, HighClaw, LeftSlide, RightSlide; //Declaring variables used for motors


    //Reads the saved recording from file. You have to change the file path when saving and reading.
    private boolean loadDatabase() {
        boolean loadedProperly = false;
        String path = String.format("%s/FIRST/" + FILENAME + ".fil",
                Environment.getExternalStorageDirectory().getAbsolutePath());
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            recording = (ArrayList<HashMap<String, Double>>) ois.readObject();
            telemetry.addLine("got past this part");
            telemetry.update();
            if(recording instanceof ArrayList){
                telemetry.addData("Update", "It worked!");
            }else{
                telemetry.addData("Update", "Did not work smh");
            }
            ois.close();
        } catch (IOException e) {
            telemetry.addData("Error", "IOException");
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            telemetry.addData("Error", "ClassNotFoundException");
            e.printStackTrace();
        }
        telemetry.addData("recording", recording.toString());
        telemetry.update();
        return loadedProperly;
    }


    //Think of each frame as a collection of every input the driver makes in one moment, saved like a frame in a video is
    private void playRecording(ArrayList<HashMap<String, Double>> recording){
        //Gets the correct from from the recording

        //The connection between the robot and the hub is not very consistent, so I just get the inputs from the closest timestamp
        //and use that
        double largestTime = 0;
        int largestNum = 0;
        int correctTimeStamp = 0;
        for(int i = 0; i < recording.size();i++){
            if(recording.get(i).get("time") > largestTime){
                if(recording.get(i).get("time") <= runtime.time()){
                    largestTime = recording.get(i).get("time");
                    largestNum = i;
                }
                else{
                    correctTimeStamp = largestNum;
                }
            }
        }
        //Only used inputs are saved to the final recording, the file is too large if every single timestamp is saved.
        telemetry.addData("correctTimeStamp", correctTimeStamp + "");
        telemetry.update();
        HashMap<String, Double> values = recording.get(correctTimeStamp);
        double lift = values.getOrDefault("lift", 0.0);
        double lift_power = values.getOrDefault("lift_power", 0.0);
        double hclaws = values.getOrDefault("hclaws", 0.0);
        double high_claw_pos = values.getOrDefault("high_claw_pos", 0.0);
        double forwardBackwardValue = values.getOrDefault("rotY", 0.0);
        double leftRightValue = values.getOrDefault("rotX", 0.0);
        double turningValue = values.getOrDefault("rx", 0.0);

        double highestValue = Math.max(Math.abs(forwardBackwardValue) + Math.abs(leftRightValue) + Math.abs(turningValue), 1);

        //Calculates amount of power for each wheel to get the desired outcome
        //E.G. You pressed the left joystick forward and right, and the right joystick right, you strafe diagonally while at the same time turning right, creating a circular strafing motion.
        //E.G. You pressed the left joystick forward, and the right joystick left, you drive like a car and turn left
        LeftSlide.setPosition(0);
        RightSlide.setPosition(0);
        if(highestValue >= 0.1){
            frontLeftMotor.setPower((-forwardBackwardValue + leftRightValue + turningValue) / highestValue);
            backLeftMotor.setPower((-forwardBackwardValue - leftRightValue + turningValue) / highestValue);
            frontRightMotor.setPower((-forwardBackwardValue - leftRightValue - turningValue) / highestValue);
            backRightMotor.setPower((-forwardBackwardValue + leftRightValue - turningValue) / highestValue);
        }
        if (lift_power >= 0.1) {
            LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftLift.setTargetPosition((int) lift);
            RightLift.setTargetPosition((int) lift);
            LeftLift.setPower(lift_power);
            RightLift.setPower(lift_power);
        } else {
            LeftLift.setPower(0);
            RightLift.setPower(0);
            LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (hclaws != LeftHClaw.getPosition()) {
            LeftHClaw.setPosition(hclaws);
            RightHClaw.setPosition(hclaws);
        }
        if (high_claw_pos == 1.0) {
            HighClaw.setPosition(0.65);
        } else if (high_claw_pos == 0.0) {
            HighClaw.setPosition(0.35);
        }
    }

}