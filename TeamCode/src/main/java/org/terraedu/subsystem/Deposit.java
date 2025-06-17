package org.terraedu.subsystem;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.terraedu.Robot;
import org.terraedu.constants.DepositPositions;
import org.terraedu.util.control.PDFController;
import org.terraedu.util.wrappers.WSubsystem;

import java.util.Set;

public class Deposit extends WSubsystem {
    private Set<DcMotorEx> motors;
    private Servo claw, pivot, linkage, armLeft, armRight;
    private final PDFController controller = new PDFController(0.0000, 0.0000, -0.15);
    private final Motor.Encoder encoder;

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
        INIT(DepositPositions.ARM_INIT, DepositPositions.PIVOT_INIT),
        TRANSFER(DepositPositions.ARM_TRANSFER, DepositPositions.PIVOT_INIT),
        PLACE(DepositPositions.ARM_PLACE, DepositPositions.PIVOT_PLACE);

        final double armPos;
        final double pivot;

        FourBarState(DepositPositions arm, DepositPositions pivot) {
            this.armPos = arm.get();
            this.pivot = pivot.get();
        }
    }

    public enum LinkageState {
        INIT(DepositPositions.INIT_LINKAGE),
        PLACE(DepositPositions.LINKAGE_PLACE);

        final double position;
        LinkageState(DepositPositions pos) {
            position = pos.get();
        }
    }

    private DepositState currentState = DepositState.INIT;

    private double controlSignal;
    private double position;
    private double target = 0.0;

    private boolean clawClosed = false;

    public Deposit(Robot robot) {
        this.motors = robot.liftMotors;
        this.claw = robot.claw;
        this.pivot = robot.pivot;
        this.linkage = robot.outtakeLinkage;
        this.armLeft = robot.armLeft;
        this.armRight = robot.armRight;
        this.encoder = robot.liftEncoder;
    }

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
        this.clawClosed = closed;
    }

    public void setLinkage(LinkageState state) {
        linkage.setPosition(state.position);
    }

    @Override
    public void periodic() {
        controlSignal = -(controller.calculate(position, target));
    }

    @Override
    public void read() {
        position = encoder.getPosition();
    }

    @Override
    public void write() {
        for (DcMotorEx motor : motors) {
            if (motor.getPower() != controlSignal) {
                motor.setPower(controlSignal);
            }
        }

        if (clawClosed) {
            claw.setPosition(DepositPositions.CLAW_GRAB.get());
        } else {
            claw.setPosition(DepositPositions.CLAW_GRAB.get());
        }
    }

    @Override
    public void reset() {

    }
}
