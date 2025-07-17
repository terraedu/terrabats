package org.terraedu.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.terraedu.Robot;
import org.terraedu.constants.IntakePositions;
import org.terraedu.util.control.SquIDController;
import org.terraedu.util.wrappers.WSubsystem;
import org.terraedu.util.wrappers.sensors.RevColorSensorV3;

import java.util.function.BooleanSupplier;

@Config
public class Intake extends WSubsystem {

    private ElapsedTime time;
    private DcMotorEx extension, intake;
    private Servo armLeft, armRight,claw,turret;


    public static double p = 0.065;

    private final SquIDController controller = new SquIDController(p);
    private final Motor.Encoder encoder;
    private RevColorSensorV3 color;

    double intakePower = 0.0;


    private boolean isReading = false;
    private boolean hasColor = false;

    public BooleanSupplier intakeSupplier = () -> hasColor;


    public Intake(Robot robot) {
        this.extension = robot.extendo;
        this.turret = robot.turret;
        this.claw = robot.iclaw;
        this.armLeft = robot.intakeArmLeft;
        this.armRight = robot.intakeArmRight;
        this.encoder = robot.extendoEncoder;
        position = encoder.getPosition();
    }

    public enum IntakeState{
        INIT(IntakePositions.INIT_ARM, IntakePositions.INIT_TURRET, IntakePositions.OPEN_CLAW),
        GRAB(IntakePositions.INTAKE_ARM, IntakePositions.INIT_TURRET, IntakePositions.CLOSE_CLAW),
        HOVER(IntakePositions.GRABPOS_ARM, IntakePositions.INIT_TURRET, IntakePositions.OPEN_CLAW),
        RELEASE(IntakePositions.INIT_ARM, IntakePositions.INIT_TURRET, IntakePositions.OPEN_CLAW);




        final double armPos;
        final double turret;
        final double claw;

        IntakeState(double armPos, double turret, double claw) {

            this.armPos = armPos;
            this.turret = turret;
            this.claw = claw;
        }
    }
    private double controlSignal;
    private double position;
    private double target = 0.0;



    public void setState(IntakeState state) {
        armRight.setPosition(state.armPos);
        armLeft.setPosition(state.armPos + 0.02);
        turret.setPosition(state.turret);
        claw.setPosition(state.claw);
    }

    public BooleanSupplier getSupplier() {
        return intakeSupplier;
    }

    public void setReading(boolean isReading) {
        this.isReading = isReading;
    }

    public double getTarget() {
        return target;
    }

    public double getPosition() {
        return position;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setPower(double power) {
        this.intakePower = power;
    }

    public boolean getReading(){
        return hasColor;
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

            switch (Robot.getInstance().alliance) {
                case RED -> hasColor = (
                        color == RevColorSensorV3.Color.RED
                );
                case BLUE -> hasColor = (
                        color == RevColorSensorV3.Color.BLUE
                );
                case REDY -> hasColor = (
                        color == RevColorSensorV3.Color.RED ||
                                color == RevColorSensorV3.Color.YELLOW
                );
                case BLUEY -> hasColor = (
                        color == RevColorSensorV3.Color.BLUE ||
                                color == RevColorSensorV3.Color.YELLOW
                );
            }
        }else{
            hasColor = true;
        }
    }

    @Override
    public void write() {

        if (extension.getPower() != controlSignal) {
            extension.setPower(controlSignal);
        }

    }

    @Override
    public void reset() {

    }
}
