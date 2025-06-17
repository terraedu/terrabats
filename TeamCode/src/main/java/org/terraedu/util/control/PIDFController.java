package org.terraedu.util.control;

/**
 * This is a PID controller (https://en.wikipedia.org/wiki/PID_controller)
 * for your robot. Internally, it performs all the calculations for you.
 * You need to tune your values to the appropriate amounts in order
 * to properly utilize these calculations.
 * <p>
 * The equation we will use is:
 * u(t) = kP * e(t) + kI * int(0,t)[e(t')dt'] + kD * e'(t) + kF
 * where e(t) = r(t) - y(t) and r(t) is the setpoint and y(t) is the
 * measured value. If we consider e(t) the positional error, then
 * int(0,t)[e(t')dt'] is the total error and e'(t) is the velocity error.
 * @author FTCLib
 */
public class PIDFController {

    private double kP, kI, kD, kF;
    private double setPoint;
    private double measuredValue;
    private double minIntegral, maxIntegral;

    private double errorVal_p;
    private double errorVal_v;

    private double totalError;
    private double prevErrorVal;

    private double errorTolerance_p = 0.05;
    private double errorTolerance_v = Double.POSITIVE_INFINITY;

    private double lastTimeStamp;
    private double period;

    /**
     * The base constructor for the PIDF controller
     */
    public PIDFController(final double kp, final double ki, final double kd, final double kf) {
        this(kp, ki, kd, kf, 0, 0);
    }

    /**
     * This is the full constructor for the PIDF controller. Our PIDF controller
     * includes a feed-forward value which is useful for fighting friction and gravity.
     * Our errorVal represents the return of e(t) and prevErrorVal is the previous error.
     *
     * @param sp The setpoint of the pid control loop.
     * @param pv The measured value of he pid control loop. We want sp = pv, or to the degree
     *           such that sp - pv, or e(t) < tolerance.
     */
    public PIDFController(final double kp, final double ki, final double kd, final double kf, final double sp, final double pv) {
        this.kP = kp;
        this.kI = ki;
        this.kD = kd;
        this.kF = kf;

        this.setPoint = sp;
        this.measuredValue = pv;

        this.minIntegral = -1.0;
        this.maxIntegral = 1.0;

        this.lastTimeStamp = 0;
        this.period = 0;

        this.errorVal_p = this.setPoint - this.measuredValue;
        this.reset();
    }

    public void reset() {
        this.totalError = 0;
        this.prevErrorVal = 0;
        this.lastTimeStamp = 0;
    }

    /**
     * Sets the error which is considered tolerable for use with {@link #atSetPoint()}.
     *
     * @param positionTolerance Position error which is tolerable.
     */
    public void setTolerance(final double positionTolerance) {
        this.setTolerance(positionTolerance, Double.POSITIVE_INFINITY);
    }

    /**
     * Sets the error which is considered tolerable for use with {@link #atSetPoint()}.
     *
     * @param positionTolerance Position error which is tolerable.
     * @param velocityTolerance Velocity error which is tolerable.
     */
    public void setTolerance(final double positionTolerance, final double velocityTolerance) {
        this.errorTolerance_p = positionTolerance;
        this.errorTolerance_v = velocityTolerance;
    }

    /**
     * Returns the current setpoint of the PIDFController.
     *
     * @return The current setpoint.
     */
    public double getSetPoint() {
        return this.setPoint;
    }

    /**
     * Sets the setpoint for the PIDFController
     *
     * @param sp The desired setpoint.
     */
    public void setSetPoint(final double sp) {
        this.setPoint = sp;
        this.errorVal_p = this.setPoint - this.measuredValue;
        this.errorVal_v = (this.errorVal_p - this.prevErrorVal) / this.period;
    }

    /**
     * Sets the setpoint for the PIDFController
     *
     * @param sp The desired setpoint.
     */
    public void setSetPointAngleWrap(final double sp) {
        this.setPoint = sp;
        this.errorVal_p = angleWrap(this.setPoint - this.measuredValue);
        this.errorVal_v = (this.errorVal_p - this.prevErrorVal) / this.period;
    }

    /**
     * Returns true if the error is within the percentage of the total input range, determined by
     * {@link #setTolerance}.
     *
     * @return Whether the error is within the acceptable bounds.
     */
    public boolean atSetPoint() {
        return Math.abs(this.errorVal_p) < this.errorTolerance_p
                && Math.abs(this.errorVal_v) < this.errorTolerance_v;
    }

    /**
     * @return the PIDF coefficients
     */
    public double[] getCoefficients() {
        return new double[]{this.kP, this.kI, this.kD, this.kF};
    }

    /**
     * @return the positional error e(t)
     */
    public double getPositionError() {
        return this.errorVal_p;
    }

    /**
     * @return the tolerances of the controller
     */
    public double[] getTolerance() {
        return new double[]{this.errorTolerance_p, this.errorTolerance_v};
    }

    /**
     * @return the velocity error e'(t)
     */
    public double getVelocityError() {
        return this.errorVal_v;
    }

    /**
     * Calculates the next output of the PIDF controller.
     *
     * @return the next output using the current measured value via
     * {@link #calculate(double)}.
     */
    public double calculate() {
        return this.calculate(this.measuredValue);
    }

    /**
     * Calculates the next output of the PIDF controller.
     *
     * @param pv The given measured value.
     * @param sp The given setpoint.
     * @return the next output using the given measurd value via
     * {@link #calculate(double)}.
     */
    public double calculate(final double pv, final double sp) {
        // set the setpoint to the provided value
        this.setSetPoint(sp);
        return this.calculate(pv);
    }

    public static double angleWrap(double radians) {
        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }

        return radians;
    }

    /**
     * Calculates the control value, u(t).
     *
     * @param pv The current measurement of the process variable.
     * @return the value produced by u(t).
     */
    public double calculate(final double pv) {
        this.prevErrorVal = this.errorVal_p;

        final double currentTimeStamp = (double) System.nanoTime() / 1E9;
        if (this.lastTimeStamp == 0) this.lastTimeStamp = currentTimeStamp;
        this.period = currentTimeStamp - this.lastTimeStamp;
        this.lastTimeStamp = currentTimeStamp;

        if (this.measuredValue == pv) {
            this.errorVal_p = this.setPoint - this.measuredValue;
        } else {
            this.errorVal_p = this.setPoint - pv;
            this.measuredValue = pv;
        }

        if (Math.abs(this.period) > 1E-6) {
            this.errorVal_v = (this.errorVal_p - this.prevErrorVal) / this.period;
        } else {
            this.errorVal_v = 0;
        }

        /*
        if total error is the integral from 0 to t of e(t')dt', and
        e(t) = sp - pv, then the total error, E(t), equals sp*t - pv*t.
         */
        this.totalError += this.period * (this.setPoint - this.measuredValue);
        this.totalError = this.totalError < this.minIntegral ? this.minIntegral : Math.min(this.maxIntegral, this.totalError);

        // returns u(t)
        return this.kP * this.errorVal_p + this.kI * this.totalError + this.kD * this.errorVal_v + this.kF * this.setPoint;
    }

    /**
     * Calculates the control value, u(t).
     *
     * @param pv The current measurement of the process variable.
     * @return the value produced by u(t).
     */
    public double calculateAngleWrap(final double pv) {
        this.prevErrorVal = this.errorVal_p;

        final double currentTimeStamp = (double) System.nanoTime() / 1E9;
        if (this.lastTimeStamp == 0) this.lastTimeStamp = currentTimeStamp;
        this.period = currentTimeStamp - this.lastTimeStamp;
        this.lastTimeStamp = currentTimeStamp;

        if (this.measuredValue == pv) {
            this.errorVal_p = angleWrap(this.setPoint - this.measuredValue);
        } else {
            this.errorVal_p = angleWrap(this.setPoint - pv);
            this.measuredValue = pv;
        }

        if (Math.abs(this.period) > 1E-6) {
            this.errorVal_v = (this.errorVal_p - this.prevErrorVal) / this.period;
        } else {
            this.errorVal_v = 0;
        }

        /*
        if total error is the integral from 0 to t of e(t')dt', and
        e(t) = sp - pv, then the total error, E(t), equals sp*t - pv*t.
         */
        this.totalError += this.period * angleWrap(this.setPoint - this.measuredValue);
        this.totalError = this.totalError < this.minIntegral ? this.minIntegral : Math.min(this.maxIntegral, this.totalError);

        // returns u(t)
        return this.kP * this.errorVal_p + this.kI * this.totalError + this.kD * this.errorVal_v + this.kF * this.setPoint;
    }

    public void setPIDF(final double kp, final double ki, final double kd, final double kf) {
        this.kP = kp;
        this.kI = ki;
        this.kD = kd;
        this.kF = kf;
    }

    public void setIntegrationBounds(final double integralMin, final double integralMax) {
        this.minIntegral = integralMin;
        this.maxIntegral = integralMax;
    }

    public void clearTotalError() {
        this.totalError = 0;
    }

    public void setP(final double kp) {
        this.kP = kp;
    }

    public void setI(final double ki) {
        this.kI = ki;
    }

    public void setD(final double kd) {
        this.kD = kd;
    }

    public void setF(final double kf) {
        this.kF = kf;
    }

    public double getP() {
        return this.kP;
    }

    public double getI() {
        return this.kI;
    }

    public double getD() {
        return this.kD;
    }

    public double getF() {
        return this.kF;
    }

    public double getPeriod() {
        return this.period;
    }

}