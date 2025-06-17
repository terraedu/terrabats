package org.terraedu.util;

import org.joml.Vector2d;

public class Pose extends Vector2d {
    double angle;

    public double getAngle() {
        return angle;
    }

    public Pose(double x, double y, double angle) {
        super(x, y);
        this.angle = angle;
    }
}
