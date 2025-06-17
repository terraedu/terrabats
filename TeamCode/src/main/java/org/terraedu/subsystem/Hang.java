package org.terraedu.subsystem;

import com.qualcomm.robotcore.hardware.CRServo;

import org.terraedu.Robot;
import org.terraedu.util.wrappers.WSubsystem;

public class Hang extends WSubsystem {
    public CRServo hangLeft;
    public CRServo hangRight;

    public enum HangState {
        STATIONARY(0),
        OUT(1),
        IN(-1);

        double power;

        HangState(double power) {
            this.power = power;
        }

        public double get() {
            return power;
        }
    }

    private double power = 0.0;

    public HangState currentState;

    public Hang(Robot robot) {
        hangLeft = robot.hangServoLeft;
        hangRight = robot.hangServoRight;
        setState(HangState.STATIONARY);
    }

    public void setState(HangState state) {
        this.currentState = state;
        this.power = state.get();
    }

    @Override
    public void periodic() {}

    @Override
    public void read() {}

    @Override
    public void write() {
        hangLeft.setPower(power);
        hangRight.setPower(power);
    }

    @Override
    public void reset() {}
}
