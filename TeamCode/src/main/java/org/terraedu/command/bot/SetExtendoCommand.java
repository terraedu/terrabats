package org.terraedu.command.bot;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.terraedu.subsystem.Intake;


public class SetExtendoCommand extends InstantCommand {
    public SetExtendoCommand(Intake intake, double target) {
        super(
                () -> intake.setTarget(target)
        );
    }
}
