package wrappers.positional;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.HashMap;

public class PServo implements Servo {
    private Servo servo;
    private double offset = 0.0;

    HashMap<String, Double> positions = new HashMap<>();

    public PServo(Servo servo) {
        this.servo = servo;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getOffset() {
        return this.offset;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }


    @Override
    public void close() {

    }

    @Override
    public ServoController getController() {
        return servo.getController();
    }

    @Override
    public int getPortNumber() {
        return servo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        this.servo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return this.servo.getDirection();
    }

    public void addPosition(String name, double position) {
        positions.put(name, position);
    }

    public void setPosition(String name) {
        double position = positions.get(name);
        servo.setPosition(position);
    }

    @Override
    public void setPosition(double position) {
        this.servo.setPosition(position - offset);
    }

    @Override
    public double getPosition() {
        return this.servo.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        this.servo.scaleRange(min, max);
    }
}

