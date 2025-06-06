package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

import java.util.Timer;
import java.util.TimerTask;

@Autonomous(name = "20525 pedro pascal",group = "yahoo")
public class pedro_pascal_20525 extends OpMode {

    private DcMotor backRight, frontRight, frontLeft, backLeft,WinchElevator,RightLift,Tilt,LeftLift;
    private Servo LowClaw,RightHClaw,LeftHClaw,RightAscent,LeftAscent,LeftSlide,RightSlide,HighClaw,Wrist;
         private Telemetry telemetryA;

         public static double DISTANCE = 20;

         private boolean forward = true;

         private Follower follower;

         private Path forwards;
         private Path backwards;
    public boolean moving_lift = false;
         public int point = 0;
         public ElapsedTime timer = new ElapsedTime();
         public Timer schedule = new Timer();
         /**
          * This initializes the Follower and creates the forward and backward Paths. Additionally, this
          * initializes the FTC Dashboard telemetry.
          */


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
             backRight.setDirection(DcMotor.Direction.REVERSE);
             frontRight.setDirection(DcMotor.Direction.REVERSE);
             LeftLift.setDirection(DcMotor.Direction.REVERSE);
             WinchElevator.setDirection(DcMotor.Direction.REVERSE);
             LeftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
             RightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
             Tilt.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
             RightAscent.setDirection(Servo.Direction.REVERSE);
             WinchElevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
             LeftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
             RightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
             ((DcMotorEx) LeftLift).setVelocityPIDFCoefficients(1.17, 0.117, 0, 11.7);
             ((DcMotorEx) RightLift).setVelocityPIDFCoefficients(1.17, 0.117, 0, 11.7);
         }

    @Override
    public void init() {
        register_settings();
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
        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryA.addLine("auto time! please fasten your seatbelts");
        telemetryA.update();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(6.925, 48.157,0));
        follower.setMaxPower(0.8);
         }



         /**
          * This runs the OpMode, updating the Follower as well as printing out the debug statements to
          * the Telemetry, as well as the FTC Dashboard.
          */

        @Override
        public void loop() {
            follower.update();
            LeftSlide.setPosition(0);
            RightSlide.setPosition(0);
            if (point == 2 && RightLift.getCurrentPosition() <= 2000) {
                HighClaw.setPosition(0.3);
                LeftHClaw.setPosition(0);
                RightHClaw.setPosition(0);
            }else {
                LeftHClaw.setPosition(0.2);
                RightHClaw.setPosition(0.2);
                HighClaw.setPosition(0.65);
            }
            if (!follower.isBusy()) {
                if (point == 1) {

                    timer.reset();
                    timer.startTime();
                    if (timer.seconds() <= 2) {
                        point = 2;
                    }
                    while (timer.seconds() <= 2) {
                        follower.followPath(path0());
                    }


                } else if (point == 0) {
                    RightLift.setTargetPosition(2701);
                    LeftLift.setTargetPosition(2701);
                    RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    timer.reset();
                    timer.startTime();
                    while (timer.seconds() < 2) {
                        follower.followPath(path0());
                        RightLift.setPower(1);
                        LeftLift.setPower(1);
                    }
                    if (RightLift.getCurrentPosition() >= 2400) {
                        point = 1;
                    }
                } else if (point == 2) {
                    RightLift.setTargetPosition(0);
                    LeftLift.setTargetPosition(0);
                    telemetryA.addData("Baller", RightLift.getCurrentPosition());
                    RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    timer.reset();
                    timer.startTime();
                    while (timer.seconds() < 2) {
                        RightLift.setPower(1);
                        LeftLift.setPower(1);
                    }
                    if (RightLift.getCurrentPosition() <= 100) {
                        point = 3;
                    }
                } else if (point == 3) {

                    follower.followPath(path1());
                    point = 4;
                }


                telemetryA.addData("Point ", point);
                follower.telemetryDebug(telemetryA);
                telemetryA.update();

            }
        }

        public PathChain path() {
            PathBuilder builder = new PathBuilder();
            builder.addPath(
                    new BezierLine(
                            new Point(follower.getPose().getX(),follower.getPose().getY(),Point.CARTESIAN),
                            new Point(23.134, 79.475, Point.CARTESIAN)
                    )
            );
            return builder.build();
        }

         public PathChain path0() {
             PathBuilder builder = new PathBuilder();

             builder
                     .addPath(
                             // Line 1
                             new BezierCurve(
                                     new Point(1.2590164684858478, 53.19344142225921, Point.CARTESIAN),
                                     new Point(23.134, 79.475, Point.CARTESIAN),
                                     new Point(35.882, 78.531, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0));
             return builder.build();
         }
         public PathChain path1() {
             PathBuilder builder = new PathBuilder();

             builder
                     .addPath(
                             // Line 2
                             new BezierCurve(
                                     new Point(23.134, 79.475, Point.CARTESIAN),
                                     new Point(38.243, 30.689, Point.CARTESIAN),
                                     new Point(57.915, 39.030, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(180))
                     .addPath(
                             // Line 3
                             new BezierLine(
                                     new Point(57.915, 39.030, Point.CARTESIAN),
                                     new Point(57.285, 39.502, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 4
                             new BezierLine(
                                     new Point(57.285, 39.502, Point.CARTESIAN),
                                     new Point(57.600, 33.679, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 5
                             new BezierLine(
                                     new Point(57.600, 33.679, Point.CARTESIAN),
                                     new Point(21.089, 30.846, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 6
                             new BezierLine(
                                     new Point(21.089, 30.846, Point.CARTESIAN),
                                     new Point(54.295, 32.892, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 7
                             new BezierLine(
                                     new Point(54.295, 32.892, Point.CARTESIAN),
                                     new Point(57.285, 28.957, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 8
                             new BezierLine(
                                     new Point(57.285, 28.957, Point.CARTESIAN),
                                     new Point(19.200, 22.820, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 9
                             new BezierLine(
                                     new Point(19.200, 22.820, Point.CARTESIAN),
                                     new Point(62.007, 22.348, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 10
                             new BezierLine(
                                     new Point(62.007, 22.348, Point.CARTESIAN),
                                     new Point(61.849, 7.869, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 11
                             new BezierLine(
                                     new Point(61.849, 7.869, Point.CARTESIAN),
                                     new Point(17.311, 9.285, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0));
             return builder.build();
         }
}


