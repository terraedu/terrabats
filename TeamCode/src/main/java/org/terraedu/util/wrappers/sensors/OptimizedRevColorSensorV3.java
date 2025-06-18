package org.terraedu.util.wrappers.sensors;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.terraedu.util.math.RGB;

public class OptimizedRevColorSensorV3 {
    private RevColorSensorV3 device;

    public OptimizedRevColorSensorV3(RevColorSensorV3 device) {
        this.device = device;
        device.enableLed(true);
    }

    private RGB getRGB() {
        NormalizedRGBA colors = device.getNormalizedColors();
        return new RGB(colors.red, colors.green, colors.blue);
    }

    public enum Color {
        YELLOW,
        RED,
        BLUE,
        NONE
    }

    public Color getColor() {
        RGB rgb = getRGB();
        double distance = getDistance();

        boolean isRed = rgb.getR().doubleValue() > rgb.getB().doubleValue()
                && rgb.getR().doubleValue() > rgb.getG().doubleValue();

        boolean isBlue = rgb.getB().doubleValue() > rgb.getR().doubleValue()
                && rgb.getB().doubleValue() > rgb.getG().doubleValue()
                && distance < 1.0;

        boolean isYellow = distance < 0.5 && !isRed && !isBlue;

        if (isRed) {
            return Color.RED;
        } else if (isBlue) {
            return Color.BLUE;
        } else if (isYellow) {
            return Color.YELLOW;
        } else {
            return Color.NONE;
        }
    }

    public double getDistance() {
        return device.getDistance(DistanceUnit.INCH);
    }
}
