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
        follower.setStartingPose(new Pose(0.3127035830618892,47.37459283387623,0));


    }



         /**
          * This runs the OpMode, updating the Follower as well as printing out the debug statements to
          * the Telemetry, as well as the FTC Dashboard.
          */
        public boolean liftup = true;
        public boolean liftdown = false;

        @Override
        public void loop() {
            follower.update();
            LeftSlide.setPosition(0);
            RightSlide.setPosition(0);
            if (!follower.isBusy()) {
                if (point == 0) {
                    timer.reset();
                    timer.startTime();
                    if (timer.seconds() <= 2) {
                        follower.breakFollowing();
                        point = 1;
                    }
                    while (timer.seconds() <= 2) {
                        follower.followPath(path0());
                    }

                } else if (point == 1) {
                    follower.followPath(path0());
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
                        point = 2;
                    }
                } else if (point == 2) {
                    RightLift.setTargetPosition(0);
                    LeftLift.setTargetPosition(0);
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



         public PathChain path0() {
             PathBuilder builder = new PathBuilder();

             builder
                     .addPath(
                             // Line 1
                             new BezierLine(
                                     new Point(0.313, 47.375, Point.CARTESIAN),
                                     new Point(33.651, 75.238, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0));
             return  builder.build();
         }
         public PathChain path1() {
             PathBuilder builder = new PathBuilder();

             builder
                     .addPath(
                             // Line 1
                             new BezierLine(
                                     new Point(0.313, 47.375, Point.CARTESIAN),
                                     new Point(27.362, 80.678, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 2
                             new BezierLine(
                                     new Point(27.362, 80.678, Point.CARTESIAN),
                                     new Point(28.456, 47.687, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 3
                             new BezierLine(
                                     new Point(28.456, 47.687, Point.CARTESIAN),
                                     new Point(58.476, 38.775, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 4
                             new BezierLine(
                                     new Point(58.476, 38.775, Point.CARTESIAN),
                                     new Point(57.850, 28.769, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 5
                             new BezierLine(
                                     new Point(57.850, 28.769, Point.CARTESIAN),
                                     new Point(8.287, 22.827, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 6
                             new BezierLine(
                                     new Point(8.287, 22.827, Point.CARTESIAN),
                                     new Point(57.537, 24.078, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 7
                             new BezierLine(
                                     new Point(57.537, 24.078, Point.CARTESIAN),
                                     new Point(57.537, 12.508, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 8
                             new BezierLine(
                                     new Point(57.537, 12.508, Point.CARTESIAN),
                                     new Point(7.349, 11.414, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 9
                             new BezierLine(
                                     new Point(7.349, 11.414, Point.CARTESIAN),
                                     new Point(58.319, 9.850, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 10
                             new BezierLine(
                                     new Point(58.319, 9.850, Point.CARTESIAN),
                                     new Point(58.319, 3.127, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 11
                             new BezierLine(
                                     new Point(58.319, 3.127, Point.CARTESIAN),
                                     new Point(4.847, 2.345, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0))
                     .addPath(
                             // Line 12
                             new BezierLine(
                                     new Point(4.847, 2.345, Point.CARTESIAN),
                                     new Point(26.423, 12.039, Point.CARTESIAN)
                             )
                     )
                     .setConstantHeadingInterpolation(Math.toRadians(0));
         return builder.build();
         }
}


