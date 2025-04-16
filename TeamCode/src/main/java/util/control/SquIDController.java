package util.control;

public class SquIDController {
    double p;

    public SquIDController(double p) {

        this.p = p;
    }
    public void setPID(double p) {
        this.p = p;
    }
    public double calculate(double setpoint, double current) {
        return Math.sqrt(Math.abs((setpoint-current)*p))*Math.signum(setpoint-current);
    }
}
