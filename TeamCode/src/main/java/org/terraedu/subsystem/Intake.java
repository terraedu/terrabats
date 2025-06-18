package org.terraedu.subsystem;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.util.control.PDFController;
import org.terraedu.util.wrappers.WSubsystem;
import org.terraedu.util.wrappers.sensors.OptimizedRevColorSensorV3;

import java.util.function.BooleanSupplier;

public class Intake extends WSubsystem {

    private DcMotorEx extension, intakeMotor;

    private final PDFController controller = new PDFController(0.0, 0.0, 0);
    private final Motor.Encoder encoder;
    private OptimizedRevColorSensorV3 color;

    double intakePower = 0.0;

    public enum IntakeState {
        INIT,
        GRAB,
        HOVER;
    }

    private boolean isReading = false;
    private boolean hasColor = false;

    public BooleanSupplier intakeSupplier = () -> hasColor;


    public Intake(Robot robot) {
        this.color = new OptimizedRevColorSensorV3(robot.colorIntake);
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

    public BooleanSupplier getSupplier() {
        return intakeSupplier;
    }

    public void setReading(boolean isReading) {
        this.isReading = isReading;
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

        if (isReading) {
            OptimizedRevColorSensorV3.Color color = this.color.getColor();

            switch (Globals.ALLIANCE) {
                case RED -> hasColor = (
                        color == OptimizedRevColorSensorV3.Color.RED ||
                                color == OptimizedRevColorSensorV3.Color.YELLOW
                );
                case BLUE -> hasColor = (
                        color == OptimizedRevColorSensorV3.Color.BLUE ||
                                color == OptimizedRevColorSensorV3.Color.YELLOW
                );
            }
        }
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
