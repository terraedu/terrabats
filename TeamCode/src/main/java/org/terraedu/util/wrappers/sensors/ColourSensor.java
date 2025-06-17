package org.terraedu.util.wrappers.sensors;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColourSensor {

    private ColorRangeSensor colourSensor;
    public int getRed() {
        return colourSensor.red();
    }

    public int getGreen() {
        return colourSensor.green();
    }

    public int getBlue() {
        return colourSensor.blue();
    }

    public double getDistance() {
        return colourSensor.getDistance(DistanceUnit.CM);
    }

}
