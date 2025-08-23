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
    private Servo latch, linkage;


    public static double p = 0.045;
    private final Robot robot = Robot.getInstance();
    private final SquIDController controller = new SquIDController(p);
    private final Motor.Encoder encoder;
    private RevColorSensorV3 color;

    double intakePower = 0.0;


    private boolean isReading = false;
    private boolean hasColor = false;
    private boolean hasWrongColor = false;


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
        INIT(IntakePositions.INIT_LINKAGE),
        RETURN(IntakePositions.INIT_LINKAGE),
        HOVER(IntakePositions.DROP_LINKAGE),
        DROP(IntakePositions.DROPDA_LINKAGE),

        COLLECT(IntakePositions.DROP_LINKAGE),
        RELEASE(IntakePositions.INIT_LINKAGE);




        final double linkPos;

        IntakeState(double link) {
            this.linkPos = link;
        }
    }
    private double controlSignal;
    private double scaledSignal;
    private double scaledPower;
    private double position;
    private double target = 0.0;



    public void setState(IntakeState state) {
        linkage.setPosition(state.linkPos);
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
        scaledSignal = robot.scale(controlSignal);
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

        if (isReading) {
            RevColorSensorV3.Color color2 = this.color.getColor();

            switch (Robot.getInstance().alliance) {
                case RED -> hasWrongColor = (
                        color2 == RevColorSensorV3.Color.BLUE || color2 == RevColorSensorV3.Color.YELLOW
                );
                case BLUE-> hasWrongColor = (
                        color2 == RevColorSensorV3.Color.RED || color2 == RevColorSensorV3.Color.YELLOW
                );
                case REDY -> hasWrongColor = (
                        color2 == RevColorSensorV3.Color.BLUE
                );
                case BLUEY -> hasWrongColor = (
                        color2 == RevColorSensorV3.Color.RED
                );
            }
        } else {
            hasWrongColor = false;
        }
    }

    @Override
    public void write() {

        scaledPower = robot.scale(intakePower);

        if (extension.getPower() != scaledSignal) {
            extension.setPower(scaledSignal);
        }
        if (intake.getPower() != scaledPower) {
            intake.setPower(scaledPower);
        }

        if (hasColor && isReading && !hasWrongColor) {
            latch.setPosition(IntakePositions.CLOSE_LATCH);
        }else if(isReading && !hasWrongColor){
            latch.setPosition(IntakePositions.CLOSE_LATCH);
        }   else if(hasWrongColor){
        latch.setPosition(IntakePositions.OPEN_LATCH);
         }else {
            Robot.getInstance();
            latch.setPosition(IntakePositions.OPEN_LATCH);

        }
    }

    @Override
    public void reset() {

    }
}
