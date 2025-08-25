package org.terraedu.util.control.pathing;

public class BezierCurve {

    private double[] px;
    private double[] py;

    public BezierCurve(double[] px, double[] py){
        this.px = px;
        this.py = py;
    }

    public double[] getPoint(double t){

        double x = (1 - t) * (1 - t) * px[0] + 2 * (1 - t) * t * px[1] + t * t * px[2];
        double y = (1 - t) * (1 - t) * py[0] + 2 * (1 - t) * t * py[1] + t * t * py[2];

        return new double[]{x,y};

    }

    public double[] getDerivative(double t){

        double dx = 2 * (1 - t) * (px[1] - px[0]) + 2 * t * (px[2] - px[1]);
        double dy = 2 * (1 - t) * (py[1] - py[0]) + 2 * t * (py[2] - py[1]);

        return new double[] {dx, dy};

    }

    public double getHeading(double t){
        double[] d = getDerivative(t);
        return Math.atan2(d[1], d[0]);
    }

}
