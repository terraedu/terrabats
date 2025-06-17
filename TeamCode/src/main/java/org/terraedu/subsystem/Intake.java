package org.terraedu.subsystem;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.terraedu.Robot;
import org.terraedu.util.control.PDFController;
import org.terraedu.util.wrappers.WSubsystem;

public class Intake extends WSubsystem {

    private DcMotorEx extension, intakeMotor;

    private final PDFController controller = new PDFController(0.0, 0.0, 0);
    private final Motor.Encoder encoder;

    double intakePower = 0.0;

    public enum IntakeState {
        INIT,
        GRAB,
        HOVER;
    }

    public Intake(Robot robot) {
        this.extension = robot.extendo;
        this.encoder = robot.extendoEncoder;
        position = encoder.getPosition();
    }

    IntakeState currentState;

    private double controlSignal;
    private double position;
    private double target = 0.0;

    public void setState(IntakeState state) {
        this.currentState = state;

        switch (state) {
            case HOVER:
                break;
            case GRAB:
                break;
            case INIT:
                break;
        }
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setPower(double power) {
        this.intakePower = power;
    }

    @Override
    public void periodic() {
        controlSignal = controller.calculate(position, target);
    }

    @Override
    public void read() {
        position = encoder.getPosition();
    }

    @Override
    public void write() {
        if (extension.getPower() != controlSignal) {
            extension.setPower(controlSignal);
        }
        if (intakeMotor.getPower() != intakePower) {
            intakeMotor.setPower(intakePower);
        }
    }

    @Override
    public void reset() {

    }
}
