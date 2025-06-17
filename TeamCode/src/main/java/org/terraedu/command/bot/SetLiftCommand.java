package org.terraedu.command.bot;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.terraedu.subsystem.Deposit;

public class SetLiftCommand extends InstantCommand {
    public SetLiftCommand(Deposit deposit, double target) {
        super(
                () -> deposit.setTarget(target)
        );
    }
}
