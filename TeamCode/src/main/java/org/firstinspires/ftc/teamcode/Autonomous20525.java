package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/***********************************************************************
 *                                                                      *
 * OnbotJava Editor is still : beta! Please inform us of any bugs       |
 * on our discord channel! https://discord.gg/e7nVjMM                   *
 * Only BLOCKS code is submitted when in Arena                          *
 *                                                                      *
 ***********************************************************************/






@TeleOp(name = "M20525_Autonomous)")
public class Autonomous20525 extends LinearOpMode {

    EssentialMecanumRobot robot = new EssentialMecanumRobot(this);
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor frontLeft;
    DcMotor frontRight;
    double CPR = 384.5;
    boolean is_moving = false;
    int order = 0;
    boolean running = false;
    double circumference = 5.51181;
    //Wheels are 140mm, converted into inches
    double countPerInch = CPR / circumference;
    double inchestotravel = 0;
    double countToTravel = inchestotravel * countPerInch;
    // equals 69.7592986696

    @Override
    public void runOpMode() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Put initialization blocks here
        waitForStart();
        // Put run blocks here
        while (opModeIsActive()) {
            left(24);
            telemetry.addData("Order", order);
            telemetry.update();
        }
    }

    public void back(double inches) {
        while (running) {
        }
            running = true;
            inchestotravel = inches;
            countToTravel = inchestotravel * countPerInch;
            backLeft.setPower(0.5);
            backRight.setPower(0.5);
            frontLeft.setPower(0.5);
            frontRight.setPower(0.5);
            backLeft.setTargetPosition((int) countToTravel);
            backRight.setTargetPosition((int) -countToTravel);
            frontLeft.setTargetPosition((int) countToTravel);
            frontRight.setTargetPosition((int) -countToTravel);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
                telemetry.addLine("Busy");
            }
            running = false;
    }

    public void forward(double inches, int order_action) {
        while (running) {
        }
        running = true;
        inchestotravel = inches;
        countToTravel = inchestotravel * countPerInch;
        backLeft.setPower(0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backLeft.setTargetPosition((int) -countToTravel);
        backRight.setTargetPosition((int) countToTravel);
        frontLeft.setTargetPosition((int) -countToTravel);
        frontRight.setTargetPosition((int) countToTravel);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
            telemetry.addLine("Busy");
        }
        running = false;
    }

    public void left(double inches){
        while (running) {
        }
        running = true;
        inchestotravel = inches;
        countToTravel = inchestotravel * countPerInch;
        backLeft.setPower(0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backLeft.setTargetPosition((int) -countToTravel);
        backRight.setTargetPosition((int) -countToTravel);
        frontLeft.setTargetPosition((int) countToTravel);
        frontRight.setTargetPosition((int) countToTravel);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
            telemetry.addLine("Busy");
        }
        running = false;
    }

    public void right(double inches, int order_action){
        while (order_action != order) {
        }
        inchestotravel = inches;
        countToTravel = inchestotravel * countPerInch;
        telemetry.addData("Count to travel", countToTravel);
        backLeft.setPower(0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(-0.5);
        backLeft.setTargetPosition((int) countToTravel);
        backRight.setTargetPosition((int) countToTravel);
        frontLeft.setTargetPosition((int) -countToTravel);
        frontRight.setTargetPosition((int) -countToTravel);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        order++;
        telemetry.update();
    }

}
