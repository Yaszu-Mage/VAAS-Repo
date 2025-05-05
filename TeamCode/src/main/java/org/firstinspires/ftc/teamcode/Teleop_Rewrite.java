package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.yahoo_api.gamepad;
import org.firstinspires.ftc.teamcode.yahoo_api.Phone;

import java.util.HashMap;
import java.util.Map;

public class Teleop_Rewrite extends LinearOpMode {
    public boolean ry = false;
    private final HashMap<Servo, Long> cooldowns = new HashMap<>(); // Cooldown storage
    private final long cooldownTime = 1000;
    private DcMotor backRight, frontRight, frontLeft, backLeft,WinchElevator,RightLift,Tilt,LeftLift;
    private Servo LowClaw,RightHClaw,LeftHClaw,RightAscent,LeftAscent,LeftSlide,RightSlide,HighClaw,Wrist;
    private boolean low_claw, high_claw = false;
    int arise_state;
    float forward,strafe,turn;
    double denominator;
    gamepad player = new gamepad(gamepad1);
    gamepad player2 = new gamepad(gamepad2);

    public FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dash_telemetry = dashboard.getTelemetry();
    Phone phone = new Phone(telemetry,dash_telemetry);
    @Override
    public void runOpMode() throws InterruptedException {
        TelemetryPacket packet = new TelemetryPacket();
        register_settings();
        waitForStart();
        while (opModeIsActive()) {
            // Put loop blocks here.
            movement();
            arise();
            move_low_claw();
            baskets();
            high_claw_move();
            lift();
            slide();
            dashboard.sendTelemetryPacket(packet);
        }
    }

    public boolean register_motor(DcMotor motor, String motor_name) {
        try {
            motor = hardwareMap.get(DcMotor.class, motor_name);
        } finally {
            phone.print(motor.toString() + " ", motor.getPortNumber());
        }
        return true;
    }

    public void move_low_claw() {
        if (player2.a && !player2.b) {
        sleep(500);
        if (cooldowns.containsKey(LowClaw)) {
            long lastUsed = cooldowns.get(LowClaw);
            long currentTime = currentTimeMillis();
            long timeLeft = cooldownTime - (currentTime - lastUsed);
            if (timeLeft > 0) {
                return;
            }
        }
    }}
    public void arise(){
        if (player.start) {
            if (arise_state == 0) {
                Tilt.setTargetPosition(1200);
                Tilt.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Tilt.setPower(1);
                sleep(1000);
                LeftLift.setTargetPosition(3000);
                RightLift.setTargetPosition(3000);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftLift.setPower(1);
                RightLift.setPower(1);
                arise_state = 1;
                dash_telemetry.addLine("Ascent 1");
            } else if (arise_state == 1) {
                LeftLift.setTargetPosition(3000);
                RightLift.setTargetPosition(3000);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftLift.setPower(1);
                RightLift.setPower(1);

                LeftLift.setPower(1);
                RightLift.setPower(1);
                sleep(1000);
                arise_state = 2;
            } else if (arise_state == 2) {
                LeftHClaw.setPosition(1);
                RightHClaw.setPosition(1);
                LeftLift.setTargetPosition(0);
                RightLift.setTargetPosition(0);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                arise_state = 3;
            } else if (arise_state == 3) {
                LeftHClaw.setPosition(1);
                RightHClaw.setPosition(1);
                LeftLift.setTargetPosition(0);
                RightLift.setTargetPosition(0);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftLift.setPower(0);
                RightLift.setPower(0);
            }
        }
        phone.print("State: ", arise_state);
    }
    public void movement(){
        forward = player.left_stick.y;
        strafe = player.left_stick.x;
        turn = player.right_stick.x;
        double highestValue;

        double forwardBackwardValue = player.left_stick.y; //Controls moving forward/backward
        double leftRightValue = player.left_stick.x * 1.1; //Controls strafing left/right       *the 1.1 multiplier is to counteract any imperfections during the strafing*
        double turningValue = player.right_stick.x; //Controls turning left/right
        if (player.left_stick_button) {
            forwardBackwardValue /= 2;
            leftRightValue /= 2;
            turningValue /= 2;
        }
        highestValue = Math.max(Math.abs(forwardBackwardValue) + Math.abs(leftRightValue) + Math.abs(turningValue), 1);

        //Calculates amount of power for each wheel to get the desired outcome
        //E.G. You pressed the left joystick forward and right, and the right joystick right, you strafe diagonally while at the same time turning right, creating a circular strafing motion.
        //E.G. You pressed the left joystick forward, and the right joystick left, you drive like a car and turn left
        if(highestValue >= 0.3){
            frontLeft.setPower((forwardBackwardValue + -leftRightValue + -turningValue) / highestValue);
            backLeft.setPower((forwardBackwardValue - -leftRightValue + -turningValue) / highestValue);
            frontRight.setPower((forwardBackwardValue - -leftRightValue - -turningValue) / highestValue);
            backRight.setPower((forwardBackwardValue + -leftRightValue - -turningValue) / highestValue);
        }
    }
    public void register_settings() {
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        LeftLift = hardwareMap.get(DcMotor.class, "LeftLift");
        WinchElevator = hardwareMap.get(DcMotor.class, "WinchElevator");
        RightLift = hardwareMap.get(DcMotor.class, "RightLift");
        Tilt = hardwareMap.get(DcMotor.class, "Tilt");
        RightAscent = hardwareMap.get(Servo.class, "RightAscent");
        LowClaw = hardwareMap.get(Servo.class, "LowClaw");
        RightHClaw = hardwareMap.get(Servo.class, "RightHClaw");
        LeftHClaw = hardwareMap.get(Servo.class, "LeftHClaw");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        LeftAscent = hardwareMap.get(Servo.class, "LeftAscent");
        LeftSlide = hardwareMap.get(Servo.class, "LeftSlide");
        RightSlide = hardwareMap.get(Servo.class, "RightSlide");
        HighClaw = hardwareMap.get(Servo.class, "HighClaw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        // Put initialization blocks here.
        arise_state = 0;
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        LeftLift.setDirection(DcMotor.Direction.REVERSE);
        WinchElevator.setDirection(DcMotor.Direction.REVERSE);
        LeftLift.setTargetPosition(0);
        RightLift.setTargetPosition(0);
        LeftLift.setPower(1);
        RightLift.setPower(1);
        LeftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Tilt.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightAscent.setDirection(Servo.Direction.REVERSE);
        WinchElevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RightHClaw.setPosition(0);
        LeftHClaw.setPosition(0);
        LowClaw.setPosition(0);
        RightAscent.setPosition(0);
        ((DcMotorEx) LeftLift).setVelocityPIDFCoefficients(1.17, 0.117, 0, 11.7);
        ((DcMotorEx) RightLift).setVelocityPIDFCoefficients(1.17, 0.117, 0, 11.7);
    }

    public void baskets(){
        if (player.a) {
            // high basket
            ry = true;
            while (LeftLift.getCurrentPosition() < 2701) {
                LeftLift.setTargetPosition(2701);
                RightLift.setTargetPosition(2701);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftLift.setPower(1);
                RightLift.setPower(1);
            }
            ry = false;
        } else if (player.b) {

            // HIGH CHAMBER (specimen)
            ry = true;
            while (LeftLift.getCurrentPosition() < 716) {
                LeftLift.setTargetPosition(716);
                RightLift.setTargetPosition(716);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftLift.setPower(1);
                RightLift.setPower(1);
            } ry = false;}

        if (player.x) {
            // Reset
            ry = true;
            while (LeftLift.getCurrentPosition() > 0) {
                LeftLift.setTargetPosition(0);
                RightLift.setTargetPosition(0);
                LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftLift.setPower(1);
                RightLift.setPower(1);
            }
            ry = false;}
    }

    public void high_claw_move() {
        if (gamepad2.right_stick_y >= 0.1 && gamepad2.right_stick_button) {
            LeftHClaw.setPosition(gamepad2.right_stick_y);
            RightHClaw.setPosition(gamepad2.right_stick_y);
        } else if (gamepad2.right_stick_y <= -0.1) {
            double value = abs(gamepad2.right_stick_y);
            value = 1 - value;
            LeftHClaw.setPosition(value);
            RightHClaw.setPosition(value);
        }
    }

    public void lift() {
        // just going to use old controller here for simplicity
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
            if (arise_state == 0 && !(gamepad1.left_trigger >= 0.1) && !ry) {
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
            if (arise_state == 0 && !(gamepad1.right_trigger >= 0.1) && !ry) {
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
            }
        }
    }

        public void slide(){
            if (gamepad2.back) {
                LeftSlide.setPosition(gamepad2.right_trigger);
                RightSlide.setPosition(gamepad2.right_trigger - 0.15);
            }

            if (gamepad2.right_bumper) {
                LeftSlide.setPosition(0.35);
                RightSlide.setPosition(0.35);
            }
            if (gamepad2.left_bumper) {
                LeftHClaw.setPosition(0.15);
                RightHClaw.setPosition(0.15);
            }
        }
    }
