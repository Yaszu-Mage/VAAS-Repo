package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LConstants {
    static {
        TwoWheelConstants.forwardTicksToInches = -0.00202816594;
        TwoWheelConstants.strafeTicksToInches = -0.0020734144838936343;
        TwoWheelConstants.forwardY = 8; //default -1

        TwoWheelConstants.strafeX = 4;//default -2.5
        TwoWheelConstants.forwardEncoder_HardwareMapName = "backLeft";
        TwoWheelConstants.strafeEncoder_HardwareMapName = "frontLeft";
        TwoWheelConstants.forwardEncoderDirection = Encoder.REVERSE;
        TwoWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
        TwoWheelConstants.IMU_HardwareMapName = "imu";
        TwoWheelConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD, RevHubOrientationOnRobot.UsbFacingDirection.LEFT);
    }
}




