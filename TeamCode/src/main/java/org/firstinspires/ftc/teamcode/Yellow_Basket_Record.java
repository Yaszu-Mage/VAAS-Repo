package org.firstinspires.ftc.teamcode;
import static java.lang.System.currentTimeMillis;

import android.os.Environment;
import com.qualcomm.hardware.lynx.LynxModule;
import java.io.File;
import java.util.ArrayList;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

@TeleOp(name = "M_Yellow_Basket_Record")
public class Yellow_Basket_Record extends LinearOpMode {

    String FILENAME = "20525autoyellowbasket";

    int recordingLength = 30;

    //List of each "Frame" of the recording | Each frame has multiple saved values that are needed to fully visualize it
    ArrayList<HashMap<String, Double>> recording = new ArrayList<>();
    private final HashMap<Servo, Long> cooldowns = new HashMap<>();// Cooldown storage
    private final long cooldownTime = 1000;
    public boolean low_claw_open = false;
    public boolean high_claw_open = false;

    final ElapsedTime runtime = new ElapsedTime();
    boolean isPlaying = false;
    int frameCounter = 0;
    int robotState = 0;

    //Variables for motor usage
    DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,LeftLift,RightLift;
    Servo RightHClaw, LeftHClaw, HighClaw, LeftSlide, RightSlide,Wrist,LowClaw;
    //Declaring variables used for motors

    @Override
    public void runOpMode() {

        //Increasing efficiency in getting data from the robot
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        //Attaching the variables declared with the physical motors by name or id
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
            LowClaw = hardwareMap.servo.get("LowClaw");
            Wrist = hardwareMap.servo.get("Wrist");
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            LeftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            RightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LeftLift.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        telemetry.addData("Status", "Waiting to Start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                //Before recording, gives driver a moment to get ready to record
                //Once the start button is pressed, recording will start
                if (gamepad1.start && robotState == 0) {
                    move_high_claw();
                    robotState = 1;
                    runtime.reset();
                    telemetry.addData("Status", "Recording");
                    telemetry.addData("Time until recording end", recordingLength - runtime.time() + "");
                }
                else if(robotState == 0){
                    telemetry.addData("Status", "Waiting to start recording");
                    telemetry.addData("Version", "1");
                }

                //The recording has started and inputs from the gamepad are being saved in a list
                else if(robotState == 1){
                    if(recordingLength - runtime.time() > 0){
                        telemetry.addData("Status", "Recording");
                        telemetry.addData("Time until recording end", recordingLength - runtime.time() + "");
                        HashMap<String, Double> values = robotMovement();
                        recording.add(values);
                    }else{
                        robotState = 2;
                    }
                }

                //PAUSE BEFORE REPLAYING RECORDING
                //Reset the robot position and samples

                //Press START to play the recording
                else if(robotState == 2){
                    telemetry.addData("Status", "Waiting to play Recording" + recording.size());
                    telemetry.addData("Time", runtime.time() + "");
                    if (gamepad1.start){
                        runtime.reset();
                        robotState = 3;
                        telemetry.addData("Status", "Playing Recording");
                        telemetry.update();
                        isPlaying = true;
                        playRecording(recording);
                    } else if (!gamepad1.start && gamepad2.y) {
                        move_high_claw();
                    }
                }

                //Play-back the recording(This is very accurate to what the autonomous will accomplish)
                //WARNING: I recommend replaying the recording(What was driven and what was replayed vary a lot!)

                //Press the X button to stop(The recording does not stop on its own)
                else if(robotState == 3){
                    if(gamepad1.x){
                        isPlaying = false;
                    }
                    if(isPlaying){
                        playRecording(recording);
                    }else{
                        robotState = 4;
                        telemetry.addData("Status", "Done Recording play-back");
                        telemetry.addData("Save to file", "Press start to save");
                        telemetry.update();
                    }
                }

                //Press START one last time to save the recording
                //After you see the confirmation, you may stop the program.
                else if(robotState == 4){
                    if(gamepad1.start){
                        telemetry.addData("Status", "Saving File");
                        boolean recordingIsSaved = false;
                        String path = String.format("%s/FIRST/" + FILENAME + ".fil",
                                Environment.getExternalStorageDirectory().getAbsolutePath());



                        telemetry.clearAll();
                        telemetry.addData("Status", saveRecording(recording, path));
                        telemetry.update();
                    }
                }

                telemetry.update();
            }
        }
    }

    //Writes the recording to file
    public String saveRecording(ArrayList<HashMap<String, Double>> recording, String path){
        String rv = "Save Complete";

        try {
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(recording);
            oos.close();
        }
        catch(IOException e){
            rv = e.toString();
        }

        return rv;
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
        double low_pos = values.getOrDefault("low_claw", 0.0);
        double slides = values.getOrDefault("Slides",0.0);
        double wrist = values.getOrDefault("wrist",0.0);
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
        LeftSlide.setPosition(slides);
        RightSlide.setPosition(slides - 0.15);
        Wrist.setPosition(wrist);
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
        if (low_pos == 1.0) {
            LowClaw.setPosition(0.65);
        } else if (high_claw_pos == 0.0) {
            LowClaw.setPosition(0.35);
        }
    }

    //Simple robot movement
    //Slowed to half speed so movements are more accurate
    private HashMap<String, Double> robotMovement() {
        frameCounter++;
        HashMap<String, Double> values = new HashMap<>();
        double highestValue;

        double forwardBackwardValue = gamepad1.left_stick_y; //Controls moving forward/backward
        double leftRightValue = gamepad1.left_stick_x * 1.1; //Controls strafing left/right       *the 1.1 multiplier is to counteract any imperfections during the strafing*
        double turningValue = gamepad1.right_stick_x; //Controls turning left/right
        forwardBackwardValue /= 2;
        leftRightValue /= 2;
        turningValue /= 2;
        if (gamepad2.y) {
            move_high_claw();
        }
        if (gamepad2.x) {
            LeftHClaw.setPosition(0.4);
            RightHClaw.setPosition(0.4);
            sleep(100);
            LeftSlide.setPosition(0.22);
            RightSlide.setPosition(0.22);
        }
        if (gamepad2.back) {
            LeftSlide.setPosition(gamepad2.right_trigger);
            RightSlide.setPosition(gamepad2.right_trigger - 0.15);
        } else {
            LeftSlide.setPosition(LeftSlide.getPosition());
            RightSlide.setPosition(RightSlide.getPosition());
        }
        if (gamepad2.dpad_up) {
            Wrist.setPosition(0.75);
        } else if (gamepad2.dpad_down) {
            Wrist.setPosition(0);
        }
        if (gamepad2.a) {
            move_low_claw();
        }
        if (gamepad2.dpad_right) {
            increase_height();
        } else if (gamepad2.dpad_left) {
            decrease_height();
        }
        if (gamepad1.right_trigger >= 0.1 && LeftLift.getCurrentPosition() <= 4800) {
            int New_Value = LeftLift.getCurrentPosition() + 150;
            LeftLift.setTargetPosition(New_Value);
            RightLift.setTargetPosition(New_Value);
            LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftLift.setPower(gamepad1.right_trigger);
            RightLift.setPower(gamepad1.right_trigger);
            telemetry.addLine("up");
            telemetry.addData("New Value", New_Value);
            telemetry.addData("Armliftpower", LeftLift.getPower());
        } else {
            if (!(gamepad1.left_trigger >= 0.1)) {
                LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                LeftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                RightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                RightLift.setTargetPosition(RightLift.getCurrentPosition());
                LeftLift.setTargetPosition(LeftLift.getCurrentPosition());
                telemetry.addData("BusyLeft", LeftLift.isBusy());
                telemetry.addData("BusyRight", RightLift.isBusy());
                LeftLift.setPower(0);
                RightLift.setPower(0);
                telemetry.addLine("off");
            }
        }
        if (gamepad1.left_trigger >= 0.1 && LeftLift.getCurrentPosition() >= 0) {
            int New_Value = LeftLift.getCurrentPosition() - 150;
            LeftLift.setTargetPosition(New_Value);
            RightLift.setTargetPosition(New_Value);
            LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftLift.setPower(gamepad1.left_trigger);
            RightLift.setPower(gamepad1.left_trigger);
            telemetry.addLine("down");
            telemetry.addData("New Value", New_Value);
        } else {
            if (!(gamepad1.right_trigger >= 0.1)) {
                LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                LeftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                RightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                RightLift.setTargetPosition(RightLift.getCurrentPosition());
                LeftLift.setTargetPosition(LeftLift.getCurrentPosition());
                telemetry.addData("BusyLeft", LeftLift.isBusy());
                telemetry.addData("BusyRight", RightLift.isBusy());
                LeftLift.setPower(0);
                RightLift.setPower(0);
                telemetry.addLine("off");
            }}
        if (gamepad2.dpad_right) {
            increase_height();
        } else if (gamepad2.dpad_left) {
            decrease_height();
        }
        double open = 0.0;
        if (high_claw_open) {
            open = 1.0;
        }else {
            open = 0.0;
        }
        double open_low = 0.0;
        if (low_claw_open) {
            open_low = 1.0;
        }else {
            open_low = 0.0;
        }
        values.put("low_claw", open_low);
        values.put("Slides",LeftSlide.getPosition());
        values.put("wrist",Wrist.getPosition());
        values.put("hclaws", LeftHClaw.getPosition());
        values.put("high_claw_pos", open);
        values.put("lift", (double) LeftLift.getTargetPosition() );
        values.put("lift_power", LeftLift.getPower());
        values.put("rotY", forwardBackwardValue);
        values.put("rotX", leftRightValue);
        values.put("rx", turningValue);
        values.put("time", runtime.time());

        //Makes sure power of each engine is not below 100% (Math cuts anything above 1.0 to 1.0, meaning you can lose values unless you change values)
        //This gets the highest possible outcome, and if it's over 1.0, it will lower all motor powers by the same ratio to make sure powers stay equal
        highestValue = Math.max(Math.abs(forwardBackwardValue) + Math.abs(leftRightValue) + Math.abs(turningValue), 1);

        //Calculates amount of power for each wheel to get the desired outcome
        //E.G. You pressed the left joystick forward and right, and the right joystick right, you strafe diagonally while at the same time turning right, creating a circular strafing motion.
        //E.G. You pressed the left joystick forward, and the right joystick left, you drive like a car and turn left
        if(highestValue >= 0.1){
            frontLeftMotor.setPower((-forwardBackwardValue + leftRightValue + turningValue) / highestValue);
            backLeftMotor.setPower((-forwardBackwardValue - leftRightValue + turningValue) / highestValue);
            frontRightMotor.setPower((-forwardBackwardValue - leftRightValue - turningValue) / highestValue);
            backRightMotor.setPower((-forwardBackwardValue + leftRightValue - turningValue) / highestValue);
        }

        return values;
    }

    public void move_high_claw() {
        sleep(500);
        if (cooldowns.containsKey(HighClaw)) {
            long lastUsed = cooldowns.get(HighClaw);
            long currentTime = currentTimeMillis();
            long timeLeft = cooldownTime - (currentTime - lastUsed);
            if (timeLeft > 0) {
                return;
            }
        }
        if (high_claw_open) {
            HighClaw.setPosition(0.35);
            cooldowns.put(HighClaw, currentTimeMillis());
            telemetry.addData("High Claw", HighClaw.getPosition());
            high_claw_open = false;
        } else if (!high_claw_open){
            HighClaw.setPosition(0.65);
            cooldowns.put(HighClaw, currentTimeMillis());
            telemetry.addData("High Claw", HighClaw.getPosition());
            high_claw_open = true;
        }
    }
    public void increase_height() {
        if (cooldowns.containsKey(LeftHClaw)) {
            long lastUsed = cooldowns.get(LeftHClaw);
            long currentTime = currentTimeMillis();
            long timeLeft = cooldownTime - (currentTime - lastUsed);
            if (timeLeft > 0) {
                return;
            }
        }
        double value = LeftHClaw.getPosition();
        if (value + 0.05 >= 1.05) {
            LeftHClaw.setPosition(0);
            RightHClaw.setPosition(0);
        } else {
            LeftHClaw.setPosition(value + 0.05);
            RightHClaw.setPosition(value + 0.05);
        }
    }
    public void decrease_height() {
        if (cooldowns.containsKey(LeftHClaw)) {
            long lastUsed = cooldowns.get(LeftHClaw);
            long currentTime = currentTimeMillis();
            long timeLeft = cooldownTime - (currentTime - lastUsed);
            if (timeLeft > 0) {
                return;
            }
        }
        double value = LeftHClaw.getPosition();
        if (value - 0.05 >= -0.05) {
            LeftHClaw.setPosition(0);
            RightHClaw.setPosition(0);
        } else {
            LeftHClaw.setPosition(value - 0.05);
            RightHClaw.setPosition(value - 0.05);
        }
    }
    public void move_low_claw() {
        sleep(500);
        if (cooldowns.containsKey(LowClaw)) {
            long lastUsed = cooldowns.get(LowClaw);
            long currentTime = currentTimeMillis();
            long timeLeft = cooldownTime - (currentTime - lastUsed);
            if (timeLeft > 0) {
                return;
            }
        }
        if (low_claw_open) {
            LowClaw.setPosition(0);
            cooldowns.put(HighClaw, currentTimeMillis());
            telemetry.addData("Low Claw", LowClaw.getPosition());
            low_claw_open = false;
        } else if (!low_claw_open){
            LowClaw.setPosition(0.5);
            cooldowns.put(HighClaw, currentTimeMillis());
            telemetry.addData("Low Claw", LowClaw.getPosition());
            low_claw_open = true;
        }

    }
}