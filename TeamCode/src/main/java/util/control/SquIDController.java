package util.control;

import static mathutil.MathFunctions.angleWrap;

public class SquIDController {
    double p;
    double hError;

    public SquIDController() {

    }
    public void setPID(double p) {
        this.p = p;
    }
    public double calculate(double setpoint, double current) {
        return Math.sqrt(Math.abs((setpoint-current)*p))*Math.signum(setpoint-current);



    }

    public double calculateH(double setpoint, double current) {

       hError = Math.toDegrees(angleWrap(Math.toRadians(setpoint - current)));
        return Math.sqrt(Math.abs((hError)*p))*Math.signum(hError);



    }
}
