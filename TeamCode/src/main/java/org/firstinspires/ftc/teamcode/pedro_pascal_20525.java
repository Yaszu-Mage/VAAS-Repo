package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
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
         public int point = 0;
         public GeneratedPath path = new GeneratedPath();
         public generated2 path0 = new generated2();
         public ElapsedTime timer = new ElapsedTime();
         /**
          * This initializes the Follower and creates the forward and backward Paths. Additionally, this
          * initializes the FTC Dashboard telemetry.
          */
         @Override
         public void init() {
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
         }

         /**
          * This runs the OpMode, updating the Follower as well as printing out the debug statements to
          * the Telemetry, as well as the FTC Dashboard.
          */
         @Override
         public void loop() {
             follower.update();
             if (!follower.isBusy()) {
                 if (point == 0) {
                     follower.followPath(path0.builder.build());
                     RightLift.setTargetPosition(2701);
                     LeftLift.setTargetPosition(2701);
                     RightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                     LeftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                     RightLift.setPower(1);
                     LeftLift.setPower(1);
                     timer.reset();
                     while (timer.seconds() > 3) {
                         telemetryA.addData("Waiting ",3 - timer.seconds());
                     }
                     RightLift.setTargetPosition(0);
                     LeftLift.setTargetPosition(0);
                     RightLift.setPower(-1);
                     LeftLift.setPower(-1);
                     timer.reset();
                     while (timer.seconds() > 1) {
                         telemetryA.addData("Waiting ",1 - timer.seconds());
                     }
                     point = 1;
                 } else if (point == 1) {
                     if (RightLift.getCurrentPosition() >= 100) {
                         RightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                         LeftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                     }
                     follower.followPath(path.builder.build());
                     point = 2;
                 }
             }
             telemetryA.addData("going forward", forward);
             follower.telemetryDebug(telemetryA);
         }
}


