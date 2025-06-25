package org.terraedu.subsystem;

import com.qualcomm.robotcore.hardware.Servo;

import org.terraedu.Globals;
import org.terraedu.constants.DepositPositions;
import org.terraedu.util.wrappers.WSubsystem;
import org.terraedu.util.wrappers.sensors.RevColorSensorV3;

import java.util.function.BooleanSupplier;

public class Claw extends WSubsystem {
    private Servo servo;
    private RevColorSensorV3 color;

    private boolean isClawOpen = false;

    private boolean isReading = false;
    private boolean hasColor = false;

    public BooleanSupplier clawSupplier = () -> hasColor;

    public Claw(Servo servo) {
//        this.color = color;
        this.servo = servo;
    }

    public BooleanSupplier getSupplier() {
        return clawSupplier;
    }

    public void setClawState(boolean opened) {
        isClawOpen = opened;
    }

    public void setReading(boolean isReading) {
        this.isReading = isReading;
    }

    @Override
    public void periodic() {
    }

    @Override
    public void read() {
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
        if (isClawOpen) {
            servo.setPosition(DepositPositions.CLAW_GRAB.get());
        } else {
            servo.setPosition(DepositPositions.CLAW_INIT.get());
        }
    }

    @Override
    public void reset() {

    }
}
