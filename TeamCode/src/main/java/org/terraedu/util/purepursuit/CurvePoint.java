package org.terraedu.util.purepursuit;

import org.opencv.core.Point;

public class CurvePoint {

    public double x;
    public double y;
    public double speed;
    public double headingSpeed;
    public double followDist;
    public double pointLen;
    public double slowdownRad;
    public double slowdownHeadingAmount;

    public CurvePoint(double x, double y, double speed, double headingSpeed, double followDist, double slowdownRad, double slowdownHeadingAmount) {

        this.x = x;
        this.y = y;
        this.speed = speed;
        this.headingSpeed = headingSpeed;
        this.followDist = followDist;
        this.slowdownRad = slowdownRad;
        this.slowdownHeadingAmount = slowdownHeadingAmount;

    }


    public CurvePoint(CurvePoint thisPoint) {

        x = thisPoint.x;
        y = thisPoint.y;
        speed = thisPoint.speed;
        headingSpeed = thisPoint.headingSpeed;
        followDist = thisPoint.followDist;
        slowdownRad = thisPoint.slowdownRad;
        slowdownHeadingAmount = thisPoint.slowdownHeadingAmount;
        pointLen = thisPoint.pointLen;

    }

    public Point toPoint() {
        return new Point(x, y);
    }


    public void setPoint(Point point) {
        x = point.x;
        y = point.y;
    }


}
