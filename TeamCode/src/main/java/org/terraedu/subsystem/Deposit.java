package org.terraedu.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.terraedu.Robot;
import org.terraedu.constants.DepositPositions;
import org.terraedu.util.control.SquIDController;
import org.terraedu.util.wrappers.WSubsystem;

import java.util.Set;

@Config
public class Deposit extends WSubsystem {
    private Set<DcMotorEx> motors;
    private Servo pivot, linkage, armLeft, armRight;
    private Claw claw;
    public static double p = 0.14;
    public static double ff = 0.015;

    private final SquIDController controller = new SquIDController(p);
    private final Motor.Encoder encoder;

    private double lastPower = 0;

    public enum DepositState {
        INIT,
        GRAB,
        SAMPLE,
        RESET,
        NEUTRAL,
        SPEC_HIGH,
        SPEC_LOW,
        SPEC_GRAB
    }

    public enum FourBarState {
        SPECI(DepositPositions.SPECI_ARM, DepositPositions.INIT_PIVOT),
        INIT(DepositPositions.INIT_ARM, DepositPositions.INIT_PIVOT),
        TRANSFER(DepositPositions.ARM_TRANSFER, DepositPositions.INIT_PIVOT),
        PLACE(DepositPositions.PLACE_ARM, DepositPositions.PLACE_PIVOT),
        SPECIPLACE(DepositPositions.INIT_ARM, DepositPositions.SPECI_PIVOT);


        final double armPos;
        final double pivot;

        FourBarState(double arm, double pivot) {
            this.armPos = arm;
            this.pivot = pivot;
        }
    }



    public double getTarget() {
        return target;
    }

    public double getPosition() {
        return position;
    }

    public double getPower() {
        return controlSignal;
    }

    public enum LinkageState {
        INIT(DepositPositions.INIT_LINKAGE),
        PLACE(DepositPositions.PLACE_LINKAGE);

        final double position;
        LinkageState(double pos) {
            position = pos;
        }
    }

    private DepositState currentState = DepositState.INIT;

    private double controlSignal;
    private double position;
    private double target = 0.0;

    public Deposit(Robot robot) {
        this.motors = robot.liftMotors;
        this.claw = new Claw(robot.claw);
        this.pivot = robot.pivot;
        this.linkage = robot.outtakeLinkage;
        this.armLeft = robot.armLeft;
        this.armRight = robot.armRight;
        this.encoder = robot.liftEncoder;
    }

    private LinkageState linkageState;

    public void setState(DepositState state) {
        this.currentState = state;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setState(FourBarState state) {
        armLeft.setPosition(state.armPos);
        armRight.setPosition(state.armPos);
        pivot.setPosition(state.pivot);
    }

    public void setClawClosed(boolean closed) {
        this.claw.setClawState(closed);
    }

    public void setSampleClosed(boolean closed) {
        this.claw.setSampleState(closed);
    }

    public void setLinkage(LinkageState state) {
        this.linkageState = state;
        linkage.setPosition(state.position);
    }

    public LinkageState getLinkageState() {
        return linkageState;
    }

    public void setReading(boolean reading) {
        this.claw.setReading(reading);
    }

    @Override
    public void periodic() {
        controller.setP(p);
        controlSignal = (controller.calculate(position, target)) + ff;
    }

    @Override
    public void read() {
        position = encoder.getPosition();
        claw.read();
    }

    @Override
    public void write() {
        motors.forEach((s) -> {
            if (lastPower != controlSignal) {
                s.setPower(controlSignal);
                lastPower = controlSignal;
            }
        });
        claw.write();
    }

    @Override
    public void reset() {

    }
}
