package org.terraedu.util.control.pathing;

public class BezierFollower {

    private BezierCurve curve;
    private double totalDist;
    private int steps = 200;

    public BezierFollower(double[] start, double[] end){

        double dx = end[0] - start[0];
        double dy = end[1] - start[1];

        double magnitude = Math.hypot(dx, dy);
        double offset = 0.3 * magnitude;

        double cx = (start[0] + end[0]) / 2.0 - dy / magnitude * offset;
        double cy = (start[1] + end[1]) / 2.0 + dx / magnitude * offset;

        curve = new BezierCurve(
                new double[]{start[0], cx, end[0]},
                new double[]{start[1], cy, end[1]}
        );

        totalDist = approximateLength();
    }

    private double approximateLength(){
        double dist = 0;
        double prev[] = curve.getPoint(0);

        for (int i = 1; i <= steps; i++){
            double[] p = curve.getPoint((double)i / steps);
            dist += Math.hypot(p[0] - prev[0], p[1] - prev[1]);
            prev = p;
        }
        return dist;
    }

    public double[] getTarget(double distTravel){

        double t = distTravel / totalDist;
        t = Math.min(Math.max(t,0), 1);
        return curve.getPoint(t);

    }

    public double getTargetHeading(double distTravel){

        double t = distTravel / totalDist;
        t = Math.min(Math.max(t,0), 1);
        return curve.getHeading(t);

    }

    public double getTotalDist(){
        return totalDist;
    }
}
