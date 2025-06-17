package org.terraedu.command.teleop;

import com.arcrobotics.ftclib.command.CommandBase;

import org.terraedu.Robot;

import java.util.function.DoubleSupplier;

public class TriggerIntakeCommand extends CommandBase {
    private DoubleSupplier powerProvider;

    public TriggerIntakeCommand(DoubleSupplier powerProvider) {
        this.powerProvider = powerProvider;
    }

    @Override
    public void execute() {
        Robot.getInstance().intake.setPower(powerProvider.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}