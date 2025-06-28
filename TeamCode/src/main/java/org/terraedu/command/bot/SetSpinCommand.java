package org.terraedu.command.bot;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.terraedu.subsystem.Intake;

public class SetSpinCommand extends InstantCommand {
    public SetSpinCommand(Intake intake, double power) {
        super(
                () -> intake.setPower(power)
        );
    }
}
