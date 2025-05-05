package org.firstinspires.ftc.teamcode.micah;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "Recode (Blocks to Java)")
public class Recode extends LinearOpMode {

    private DcMotor backRight;
    private DcMotor frontRight;
    private DcMotor armLiftLeft;
    private Servo intakeWrist;
    private DcMotor armLiftRight;
    private DcMotor extendIntake;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private CRServo rollerIntakeLeft;
    private CRServo rollerIntakeRight;
    public int integral_cycle = 0;
    /**
     * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
     * Comment Blocks show where to place Initialization code (runs once, after touching the
     * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
     * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
     * Stopped).
     */
    @Override
    public void runOpMode() {
        float forward;
        float strafe;
        float turn;
        double denominator;

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        armLiftLeft = hardwareMap.get(DcMotor.class, "armLiftLeft");
        intakeWrist = hardwareMap.get(Servo.class, "intakeWrist");
        armLiftRight = hardwareMap.get(DcMotor.class, "armLiftRight");
        extendIntake = hardwareMap.get(DcMotor.class, "extendIntake");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        rollerIntakeLeft = hardwareMap.get(CRServo.class, "rollerIntakeLeft");
        rollerIntakeRight = hardwareMap.get(CRServo.class, "rollerIntakeRight");

        // Put initialization blocks here.
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        armLiftLeft.setDirection(DcMotor.Direction.REVERSE);
        intakeWrist.setPosition(0.45);
        armLiftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLiftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendIntake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ((DcMotorEx) armLiftLeft).setVelocityPIDFCoefficients(1.17, 0.117, 0, 11.7);
        ((DcMotorEx) armLiftRight).setVelocityPIDFCoefficients(1.17, 0.117, 0, 11.7);
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                forward = gamepad1.left_stick_y;
                strafe = gamepad1.left_stick_x;
                turn = gamepad1.right_stick_x;
                if (gamepad1.dpad_down) {
                    forward = forward / 2;
                    strafe = strafe / 2;
                    turn = turn / 2;
                }
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(1, Math.abs(forward) + strafe + turn));
                frontLeft.setPower((forward - (strafe + turn)) / denominator);
                backLeft.setPower((forward + (strafe - turn)) / denominator);
                frontRight.setPower((forward - (strafe + turn)) / denominator);
                backRight.setPower((forward + (strafe - turn)) / denominator);
                if (gamepad1.a) {
                    integral_cycle = integral_cycle + 1;
                    if (integral_cycle + 1 > 2){
                        integral_cycle = 0;
                    }
                    switch (integral_cycle) {
                        case 0:
                            // Pick up Samples Height
                            armLiftLeft.setTargetPosition(10);
                            armLiftRight.setTargetPosition(10);
                            armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftLeft.setPower(1);
                            armLiftRight.setPower(1);
                            extendIntake.setTargetPosition(1200);
                            extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            extendIntake.setPower(1);
                            intakeWrist.setPosition(0.75);
                        case 1:
                            // Submersible distance Extension
                            extendIntake.setTargetPosition(1440);
                            extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            extendIntake.setPower(1);
                            intakeWrist.setPosition(0.75);
                            armLiftLeft.setTargetPosition(10);
                            armLiftRight.setTargetPosition(10);
                            armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftLeft.setPower(1);
                            armLiftRight.setPower(1);
                        case 2:
                            // Deposit Samples Height
                            armLiftLeft.setTargetPosition(3500);
                            armLiftRight.setTargetPosition(3500);
                            armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftLeft.setPower(1);
                            armLiftRight.setPower(1);
                            extendIntake.setTargetPosition(140);
                            extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            extendIntake.setPower(1);
                            intakeWrist.setPosition(1);

                    }
                }
                if (gamepad1.b) {
                    integral_cycle = integral_cycle - 1;
                    if (integral_cycle - 1 < 0){
                        integral_cycle = 2;
                    }
                    switch (integral_cycle) {
                        case 0:
                            // Pick up Samples Height
                            armLiftLeft.setTargetPosition(10);
                            armLiftRight.setTargetPosition(10);
                            armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftLeft.setPower(1);
                            armLiftRight.setPower(1);
                            extendIntake.setTargetPosition(1200);
                            extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            extendIntake.setPower(1);
                            intakeWrist.setPosition(0.75);
                        case 1:
                            // Submersible distance Extension
                            extendIntake.setTargetPosition(1440);
                            extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            extendIntake.setPower(1);
                            intakeWrist.setPosition(0.75);
                            armLiftLeft.setTargetPosition(10);
                            armLiftRight.setTargetPosition(10);
                            armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftLeft.setPower(1);
                            armLiftRight.setPower(1);
                        case 2:
                            // Deposit Samples Height
                            armLiftLeft.setTargetPosition(3500);
                            armLiftRight.setTargetPosition(3500);
                            armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            armLiftLeft.setPower(1);
                            armLiftRight.setPower(1);
                            extendIntake.setTargetPosition(140);
                            extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            extendIntake.setPower(1);
                            intakeWrist.setPosition(1);

                    }

                }
                if (gamepad1.x) {
                    // Reset Arm to starting position
                    extendIntake.setTargetPosition(0);
                    armLiftLeft.setTargetPosition(0);
                    armLiftRight.setTargetPosition(0);
                    extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armLiftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armLiftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armLiftLeft.setPower(0.5);
                    armLiftRight.setPower(0.5);
                    extendIntake.setPower(0.5);
                }
                if (gamepad1.y) {
                        //  Deposit distance Extension
                        extendIntake.setTargetPosition(700);
                        extendIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        extendIntake.setPower(1);
                }
                if (gamepad1.left_bumper) {
                    // Roller Intake
                    rollerIntakeLeft.setPower(-1);
                    rollerIntakeRight.setPower(1);
                } else if (gamepad1.right_bumper) {
                    // Roller Outtake
                    rollerIntakeLeft.setPower(1);
                    rollerIntakeRight.setPower(-1);
                } else {
                    rollerIntakeLeft.setPower(0);
                    rollerIntakeRight.setPower(0);
                }
                if (gamepad1.dpad_left) {
                    intakeWrist.setPosition(0.45);
                } else if (gamepad1.dpad_right) {
                    intakeWrist.setPosition(0.75);
                }
                telemetry.update();
                telemetry.addData("Current Position Left Arm", armLiftLeft.getCurrentPosition());
                telemetry.addData("Current Position Right Arm", armLiftRight.getCurrentPosition());
                telemetry.addData("Current Extension Position", extendIntake.getCurrentPosition());
            }
        }
    }
}