package org.terraedu.command.bot;

import com.arcrobotics.ftclib.command.InstantCommand;


import org.terraedu.subsystem.Intake;

public class SetIntakeCommand extends InstantCommand {
    public SetIntakeCommand(Intake intake, Intake.IntakeState state) {
        super(
                () -> intake.setState(state)
        );
    }
}
