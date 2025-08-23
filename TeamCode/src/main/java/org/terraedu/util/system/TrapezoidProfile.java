package org.terraedu.util.system;

public class TrapezoidProfile {
    private double vMax, aMax, totalDist;
    private double tAccel, tCruise, tDecel;

    public TrapezoidProfile(double totalDistance, double vMax, double aMax) {
        this.totalDist = totalDistance;
        this.vMax = vMax;
        this.aMax = aMax;

        tAccel = vMax / aMax;
        double sAccel = 0.5 * aMax * tAccel * tAccel;
        double sDecel = sAccel;
        double sCruise = totalDist - sAccel - sDecel;
        if (sCruise < 0) {
            tAccel = Math.sqrt(totalDist / aMax);
            tCruise = 0;
            tDecel = tAccel;
        } else {
            tCruise = sCruise / vMax;
            tDecel = tAccel;
        }
    }

    public double getTotalTime() {
        return tAccel + tCruise + tDecel;
    }

    public double getVelocity(double time) {
        if (time < tAccel) return aMax * time;
        else if (time < tAccel + tCruise) return vMax;
        else if (time < tAccel + tCruise + tDecel) return vMax - aMax*(time - tAccel - tCruise);
        else return 0;
    }
}
