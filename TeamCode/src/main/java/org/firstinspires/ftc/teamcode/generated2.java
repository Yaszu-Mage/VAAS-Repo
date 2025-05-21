package org.firstinspires.ftc.teamcode;

import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.Point;

public class generated2 {
    public PathBuilder builder;

    public generated2() {
        PathBuilder builder = new PathBuilder();

        builder
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(0.313, 47.375, Point.CARTESIAN),
                                new Point(40.651, 77.238, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0));
    }
}
