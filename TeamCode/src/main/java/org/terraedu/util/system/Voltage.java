package org.terraedu.util.system;

import org.terraedu.Robot;

public class Voltage {

    double currentVoltage;
    double idealVoltage;
    double power;

    public double getVoltage() {
        currentVoltage = Robot.getInstance().voltage;

        return currentVoltage;
    }

    public double getIdealVoltage() {
        idealVoltage = Robot.getInstance().idealVoltage;

        return idealVoltage;
    }

    public double scale(double input) {

        this.power = input;

        double output = (input * currentVoltage) / idealVoltage;

        return output;

    }

}
