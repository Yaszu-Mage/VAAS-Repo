package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.yahoo_api.Phone;

@Autonomous(name = "M-AUTO")
public class M_Autotest2 extends LinearOpMode {
    public FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dash_telemetry = dashboard.getTelemetry();
    Phone phone = new Phone(telemetry,dash_telemetry);
    DcMotor backRight, frontRight, backLeft, frontLeft,LeftLift,RightLift;
    Servo HighClaw;
    EssentialMecanumRobot robot = new EssentialMecanumRobot(this);
    int state = 0;
    public ElapsedTime holdtimer = new ElapsedTime();
    boolean running = false;
    @Override
    public void runOpMode() throws InterruptedException {
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
            LeftLift.setDirection(DcMotor.Direction.REVERSE);
            HighClaw = hardwareMap.servo.get("HighClaw");
        }
        waitForStart();
        while (opModeIsActive()) {
            if (state == 0) {
                holdtimer.reset();
                robot.drive(20, 1, 0.1);
                robot.strafe(20, -1, 0.1);
                robot.drive(7, 1, 1);
                run_to_position(2800, LeftLift, 1);
                run_to_position(2800, RightLift, 1);
                state = state + 1;
            }
                else if (state == 1) {
                if (holdtimer.time() > 2) {
                    run_to_position(2000,LeftLift,-1);
                    run_to_position(2000,RightLift,-1);
                    holdtimer.reset();
                    state = state + 1;
                }
                } else if (state == 2) {
                if (holdtimer.time() > 0.5) {
                    run_to_position(2000,LeftLift,-1);
                    run_to_position(2000,RightLift,-1);
                    state = state + 1;
                }
            } else if (state == 3) {
                run_to_position(0,LeftLift,-1);
                run_to_position(0,RightLift,-1);
                state = state + 1;
            }
                HighClaw.setPosition(0.3);
                robot.drive(20,-1,0.1);
                robot.strafe(36,1,0.1);
                robot.drive(36,1,0.1);
                robot.strafe(12,1,0.1);
                robot.drive(36,-1,0.1);
                robot.drive(36,1,0.1);
                robot.strafe(6,1,0.1);
                robot.drive(36,-1,0.1);
                robot.drive(36,1,0.1);
                robot.strafe(6,1,0.1);
                robot.drive(36,-1,0.1);
                robot.drive(36,1,0.1);
                state += 1;
            }
            phone.print("drive distance",robot.driveDistance);
        }

    public void run_to_position(int position, DcMotor motor, int power){
        motor.setTargetPosition(position);
        telemetry.addData("Position", motor.getCurrentPosition());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }
}
