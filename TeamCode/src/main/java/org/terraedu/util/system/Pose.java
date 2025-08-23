package org.terraedu.util.system;

import org.joml.Vector2d;

public class Pose extends Vector2d {
    double heading;

    public double getAngle() {
        return heading;
    }

    public Pose(double x, double y, double heading) {
        super(x, y);
        this.heading = heading;
    }
}
