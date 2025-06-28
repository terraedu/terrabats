package org.terraedu.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.constants.IntakePositions;
import org.terraedu.util.control.PDFController;
import org.terraedu.util.wrappers.WSubsystem;
import org.terraedu.util.wrappers.sensors.RevColorSensorV3;

import java.util.function.BooleanSupplier;

@Config
public class Intake extends WSubsystem {

    private DcMotorEx extension, intake;
    private Servo latch, linkage;


    public static double p = 0.0;

    private final PDFController controller = new PDFController(0.0, 0.0, 0);
    private final Motor.Encoder encoder;
    private RevColorSensorV3 color;

    double intakePower = 0.0;


    private boolean isReading = false;
    private boolean hasColor = false;

    public BooleanSupplier intakeSupplier = () -> hasColor;


    public Intake(Robot robot) {
        this.color = new RevColorSensorV3(robot.colorIntake);
        this.extension = robot.extendo;
        this.latch = robot.latch;
        this.linkage = robot.intakeLinkage;
        this.intake = robot.intakeMotor;
        this.encoder = robot.extendoEncoder;
        position = encoder.getPosition();
    }

    public enum IntakeState{
        INIT(IntakePositions.INIT_LINKAGE, IntakePositions.CLOSE_LATCH);


        final double linkPos;
        final double latchPos;

        IntakeState(double link, double latch) {
            this.linkPos = link;
            this.latchPos = latch;
        }
    }
    private double controlSignal;
    private double position;
    private double target = 0.0;



    public void setState(IntakeState state) {
        linkage.setPosition(state.linkPos);
//        latch.setPosition(state.latchPos);
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
            RevColorSensorV3.Color color = this.color.getColor();

            switch (Globals.ALLIANCE) {
                case RED -> hasColor = (
                        color == RevColorSensorV3.Color.RED ||
                                color == RevColorSensorV3.Color.YELLOW
                );
                case BLUE -> hasColor = (
                        color == RevColorSensorV3.Color.BLUE ||
                                color == RevColorSensorV3.Color.YELLOW
                );
            }
        }
    }

    @Override
    public void write() {
        if (extension.getPower() != controlSignal) {
            extension.setPower(controlSignal);
        }
        if (intake.getPower() != intakePower) {
            intake.setPower(intakePower);
        }
    }

    @Override
    public void reset() {

    }
}
