package org.terraedu.command.teleop;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.terraedu.Robot;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.command.bot.SetSpinCommand;
import org.terraedu.subsystem.Intake;

public class SetExampleCommand extends SequentialCommandGroup {

    private final Robot robot = Robot.getInstance();

    public SetExampleCommand() {
        addCommands(
                new InstantCommand(() -> robot.intake.setReading(false)),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetLiftCommand(robot.deposit, 600)

        );
    }

}
