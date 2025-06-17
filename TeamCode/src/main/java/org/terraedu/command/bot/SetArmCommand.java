package org.terraedu.command.bot;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.terraedu.subsystem.Deposit;

public class SetArmCommand extends InstantCommand {
    public SetArmCommand(Deposit deposit, Deposit.FourBarState state) {
        super(
                () -> deposit.setState(state)
        );
    }
}
