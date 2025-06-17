package org.terraedu.command.bot;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.terraedu.subsystem.Deposit;

public class DepositClawCommand extends InstantCommand {
    public DepositClawCommand(Deposit deposit, boolean closed) {
        super(
                () -> deposit.setClawClosed(closed)
        );
    }
}
