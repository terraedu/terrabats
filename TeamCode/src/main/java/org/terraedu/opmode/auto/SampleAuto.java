package org.terraedu.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.SetPointCommand;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.constants.DepositPositions;
import org.terraedu.constants.IntakePositions;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.util.Alliance;
import org.terraedu.util.Pose;

import java.util.Set;

@Autonomous(name = "Sample Auto")
public class SampleAuto extends CommandOpMode {

    private double loopTime = 0;
    private final Robot robot = Robot.getInstance();

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Globals.AUTO = true;
        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();
        robot.drive.setCap(1);

        CommandScheduler.getInstance().schedule(

                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                                new InstantCommand(() -> robot.deposit.setState(Deposit.OuttakeState.START)),
                                new InstantCommand(() -> robot.intake.setState(Intake.IntakeState.INIT))

                        ),
                        new SequentialCommandGroup(
                                new SetLiftCommand(robot.deposit, 1750)
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(100),
                                new SetPointCommand(robot, new Pose(6, -10, Math.toRadians(15)), 1.25),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE)
                        ),
                        new SequentialCommandGroup(
                                new SetPointCommand(robot, new Pose(5, -14, Math.toRadians(30)), 0.5),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                                new WaitCommand(350)
                        ),
                        new SequentialCommandGroup(
                                new SetPointCommand(robot, new Pose(17, -8.5, Math.toRadians(90)), 1.5),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetLiftCommand(robot.deposit, 0)
                        ),
                        new SequentialCommandGroup(
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetExtendoCommand(robot.intake, 205),
                                new WaitCommand(250),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
                                new WaitCommand(250),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.INT_READY),
                                new WaitCommand(350),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.GRAB),
                                new WaitCommand(250),
                                new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET)),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.TRANSFER),
                                new SetExtendoCommand(robot.intake, 10),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new WaitCommand(350),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
                                new WaitCommand(100),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                                new WaitCommand(250),
                                new InstantCommand(() -> robot.iclaw.setPosition(IntakePositions.OPEN_CLAW))
                        ),
                        new SequentialCommandGroup(
                                new SetLiftCommand(robot.deposit, 1750),
                                new SetPointCommand(robot, new Pose(5, -14, Math.toRadians(30)), 1.5),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE),
                                new WaitCommand(750),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT))

                        ),
                        new SequentialCommandGroup(
                                new SetPointCommand(robot, new Pose(17, -18.75, Math.toRadians(90)), 1.5),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetLiftCommand(robot.deposit, 0)
                        ),
                        new SequentialCommandGroup(
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetExtendoCommand(robot.intake, 205),
                                new WaitCommand(250),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
                                new WaitCommand(250),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.INT_READY),
                                new WaitCommand(350),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.GRAB),
                                new WaitCommand(250),
                                new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET)),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.TRANSFER),
                                new SetExtendoCommand(robot.intake, 10),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new WaitCommand(350),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
                                new WaitCommand(100),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                                new WaitCommand(250),
                                new InstantCommand(() -> robot.iclaw.setPosition(IntakePositions.OPEN_CLAW))
                        ),
                        new SequentialCommandGroup(
                                new SetLiftCommand(robot.deposit, 1750),
                                new SetPointCommand(robot, new Pose(5, -14, Math.toRadians(30)), 1.5),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE),
                                new WaitCommand(750),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                                new WaitCommand(250)

                        ),
                        new SequentialCommandGroup(
                                new SetPointCommand(robot, new Pose(17, -17, Math.toRadians(119)), 1.5),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetLiftCommand(robot.deposit, 0)
                        ),
                        new SequentialCommandGroup(
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetExtendoCommand(robot.intake, 250),
                                new WaitCommand(250),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
                                new InstantCommand(() -> robot.turret.setPosition(IntakePositions.RIGHT_TURRET)),
                                new WaitCommand(250),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.INT_READY),
                                new WaitCommand(350),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.GRAB),
                                new WaitCommand(250),
                                new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET)),
                                new SetIntakeCommand(robot.intake, Intake.IntakeState.TRANSFER),
                                new SetExtendoCommand(robot.intake, 10),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new WaitCommand(350),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
                                new WaitCommand(100),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                                new WaitCommand(250),
                                new InstantCommand(() -> robot.iclaw.setPosition(IntakePositions.OPEN_CLAW))
                        ),
                        new SequentialCommandGroup(
                                new SetLiftCommand(robot.deposit, 1750),
                                new SetPointCommand(robot, new Pose(5, -14, Math.toRadians(30)), 1.75),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE),
                                new WaitCommand(750),
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                                new WaitCommand(350)

                        ),
                        new SequentialCommandGroup(
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                                new SetLiftCommand(robot.deposit, 0),
                                new SetPointCommand(robot, new Pose(53, -18, Math.toRadians(180)), 2.5),
                                new SetPointCommand(robot, new Pose(55, 13, Math.toRadians(180)),2.5),
                                new SetPointCommand(robot, new Pose(55, 16, Math.toRadians(180)),1.5),
                                new WaitCommand(500),
                                new SetArmCommand(robot.deposit, Deposit.OuttakeState.PARK)
                        )
                ));

    }

    @Override
    public void run() {
        super.run();
        robot.read();
        robot.periodic();
        robot.write();
        robot.clearBulkCache();

        double loop = System.nanoTime();
        telemetry.addData("hz ", 1000000000 / (loop - loopTime));
        telemetry.addData("X", robot.localizer.getCurrX());
        telemetry.addData("Y", robot.localizer.getCurrY());
        telemetry.addData("Heading", robot.localizer.getCurrHeading());


        loopTime = loop;
        telemetry.update();
    }
}

