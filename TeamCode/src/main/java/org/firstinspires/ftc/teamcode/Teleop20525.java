package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;

import com.acmerobotics.dashboard.DashboardCore;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.yahoo_api.Phone;
import org.firstinspires.ftc.teamcode.yahoo_api.YahAPI;

import java.util.HashMap;

@TeleOp(name = "M20525_Teleop",group = "yahoo")
public class Teleop20525 extends LinearOpMode {
    private final HashMap<Servo, Long> cooldowns = new HashMap<>(); // Cooldown storage
    private final long cooldownTime = 1000;

    private DcMotor backRight;
    YahAPI yahAPI = new YahAPI(gamepad1);
    
    private boolean ry = false;
    private boolean low_claw_open = false;
    private boolean high_claw_open = false;
    public FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dash_telemetry = dashboard.getTelemetry();
    private DcMotor frontRight;
    private DcMotor LeftLift;
    private DcMotor WinchElevator;
    private DcMotor RightLift;
    private DcMotor Tilt;
    private Servo RightAscent;
    private Servo LowClaw;
    private Servo RightHClaw;
    private Servo LeftHClaw;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private Servo LeftAscent;
    private Servo LeftSlide;
    private Servo RightSlide;
    private Servo HighClaw;
    private boolean can_move = true;
    private Servo Wrist;
    Phone phone = new Phone(telemetry,dash_telemetry);

    /**
     * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
     * Comment Blocks show where to place Initialization code (runs once, after touching the
     * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
     * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
     * Stopped).
     */
    @Override
    public void runOpMode() {
        int new_zero = 0;
        int move_point = 0;
        int arise_state;
        float forward;
        float strafe;
        float turn;
        double denominator;
        TelemetryPacket packet = new TelemetryPacket();
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
        new_zero = LeftLift.getCurrentPosition();
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
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                phone.print("x",LeftLift.getCurrentPosition());
                forward = gamepad1.left_stick_y;
                strafe = gamepad1.left_stick_x;
                turn = gamepad1.right_stick_x;
                double highestValue;

                double forwardBackwardValue = gamepad1.left_stick_y; //Controls moving forward/backward
                double leftRightValue = gamepad1.left_stick_x * 1.1; //Controls strafing left/right       *the 1.1 multiplier is to counteract any imperfections during the strafing*
                double turningValue = gamepad1.right_stick_x; //Controls turning left/right
                if (gamepad1.left_stick_button) {
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
                //triggers range from 0 --> 1

                if (gamepad1.start) {
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
                        phone.add_line("Ascent 1");
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
                    phone.print("State", arise_state);
                }

                if (gamepad2.a && !gamepad2.b) {
                    move_low_claw();
                }

                if (gamepad1.a) {
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
                } else if (gamepad1.b) {

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

                if (gamepad1.x) {
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
                if (gamepad2.right_stick_y >= 0.1 && gamepad2.right_stick_button) {
                    LeftHClaw.setPosition(gamepad2.right_stick_y);
                    RightHClaw.setPosition(gamepad2.right_stick_y);
                } else if (gamepad2.right_stick_y <= -0.1) {
                    double value = abs(gamepad2.right_stick_y);
                    value = 1 - value;
                    LeftHClaw.setPosition(value);
                    RightHClaw.setPosition(value);
                }
                if (gamepad1.right_trigger >= 0.1 && LeftLift.getCurrentPosition() <= 4800) {
                    int New_Value = LeftLift.getCurrentPosition() + 150;
                    LeftLift.setTargetPosition(New_Value);
                    RightLift.setTargetPosition(New_Value);
                    LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    LeftLift.setPower(gamepad1.right_trigger);
                    RightLift.setPower(gamepad1.right_trigger);
                    phone.addLine("up");
                    phone.print("New Value", New_Value);
                    phone.print("Armliftpower", LeftLift.getPower());
                } else {
                    if (arise_state == 0 && !(gamepad1.left_trigger >= 0.1) && !ry) {
                        LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        LeftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        RightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        RightLift.setTargetPosition(RightLift.getCurrentPosition());
                        LeftLift.setTargetPosition(LeftLift.getCurrentPosition());
                        phone.print("BusyLeft", LeftLift.isBusy());
                        phone.print("BusyRight", RightLift.isBusy());
                        LeftLift.setPower(0);
                        RightLift.setPower(0);
                        phone.addLine("off");
                    }
                }
                if (gamepad1.left_trigger >= 0.1 && LeftLift.getCurrentPosition() >= 0) {
                    int New_Value = LeftLift.getCurrentPosition() - 150;
                    can_move = false;
                    LeftLift.setTargetPosition(New_Value);
                    RightLift.setTargetPosition(New_Value);
                    LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    LeftLift.setPower(gamepad1.left_trigger);
                    RightLift.setPower(gamepad1.left_trigger);
                    phone.addLine("down");
                    phone.print("New Value", New_Value);
                } else {
                    if (arise_state == 0 && !(gamepad1.right_trigger >= 0.1) && !ry) {
                        LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        LeftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        RightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        RightLift.setTargetPosition(RightLift.getCurrentPosition());
                        LeftLift.setTargetPosition(LeftLift.getCurrentPosition());
                        phone.print("BusyLeft", LeftLift.isBusy());
                        phone.print("BusyRight", RightLift.isBusy());
                        LeftLift.setPower(0);
                        RightLift.setPower(0);
                    }
                }
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
                // deprecated
                /*if (gamepad2.right_stick_x <= -0.1 || gamepad2.right_stick_x >= 0.1) {
                    LeftSlide.setPosition(gamepad2.right_stick_y);
                    RightSlide.setPosition(gamepad2.right_stick_y);
                }*/
                //770
                if (gamepad2.x) {
                    LeftHClaw.setPosition(0.4);
                    RightHClaw.setPosition(0.4);
                    sleep(100);
                    LeftSlide.setPosition(0.22);
                    RightSlide.setPosition(0.22);
                }
                if (gamepad2.b && !gamepad2.a) {
                    LeftHClaw.setPosition(0.5);
                    RightHClaw.setPosition(0.5);
                }
                if (gamepad2.y) {
                    move_high_claw();
                }
                // Overlaps controls, need to come up with a control scheme
                /*if (gamepad2.y) {
                    LeftHClaw.setPosition(1);
                    RightHClaw.setPosition(1);
                }*/
                //Deprecated? I might just keep it and see how it goes
                if (gamepad2.dpad_up) {
                    Wrist.setPosition(0.75);
                } else if (gamepad2.dpad_down) {
                    Wrist.setPosition(0);
                }

                if (gamepad2.dpad_right) {
                    increase_height();
                } else if (gamepad2.dpad_left) {
                    decrease_height();
                }
                dashboard.sendTelemetryPacket(packet);
                phone.print("Left Lift", LeftLift.getCurrentPosition());
                phone.print("Right Lift", RightLift.getCurrentPosition());
                phone.print("Current Position Winch Elevator", WinchElevator.getCurrentPosition());
                phone.print("Current Position Left Arm", LeftLift.getCurrentPosition());
                phone.print("Current Position Witch", WinchElevator.getCurrentPosition());
                phone.print("Current Position Right Arm", RightLift.getCurrentPosition());
                phone.print("Current Left Slide Position", LeftSlide.getPosition());
                phone.print("Current Right Slide Position", RightSlide.getPosition());
                phone.print("Current position tilt", Tilt.getCurrentPosition());
                telemetry.update();
                dash_telemetry.update();
                dash_telemetry.update();
            }
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
        phone.print("Low Claw", LowClaw.getPosition());
        low_claw_open = false;
    } else if (!low_claw_open){
        LowClaw.setPosition(0.5);
        cooldowns.put(HighClaw, currentTimeMillis());
        phone.print("Low Claw", LowClaw.getPosition());
        low_claw_open = true;
    }

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
            HighClaw.setPosition(0.3);
            cooldowns.put(HighClaw, currentTimeMillis());
            phone.print("High Claw", HighClaw.getPosition());
            high_claw_open = false;
        } else if (!high_claw_open){
            HighClaw.setPosition(0.65);
            cooldowns.put(HighClaw, currentTimeMillis());
            phone.print("High Claw", HighClaw.getPosition());
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
    public void run_up () {
        LeftLift.setTargetPosition(1850);
        RightLift.setTargetPosition(1850);
        LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftLift.setPower(1);
        RightLift.setPower(1);
    }



}

