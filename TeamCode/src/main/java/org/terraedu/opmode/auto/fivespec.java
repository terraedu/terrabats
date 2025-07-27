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
import org.terraedu.command.auto.WayPointCommand;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetDepositLinkageCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.command.bot.SetSpinCommand;
import org.terraedu.constants.DepositPositions;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.util.Alliance;
import org.terraedu.util.Pose;

@Autonomous(name = "5 spec or bust")
public class fivespec extends CommandOpMode {

    private double loopTime = 0;
    private final Robot robot = Robot.getInstance();

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();


        Globals.AUTO = true;

        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();
        robot.hardwarereset();

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                                new InstantCommand(() -> robot.deposit.setState(Deposit.FourBarState.SPECIPLACE)),
                                new InstantCommand(() -> robot.intake.setState(Intake.IntakeState.INIT)),
                                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT)

                        ),
                        new ParallelCommandGroup(

                                new SetPointCommand(robot, new Pose(-2, 15, Math.toRadians(0)), .5),
                                new SetLiftCommand(robot.deposit, 475)

                        )
                        , new SequentialCommandGroup(
                                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                        new WaitCommand(550),
                        new SetPointCommand(robot, new Pose(0, 29, Math.toRadians(0)), .5),
                        new InstantCommand(() ->robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                        new SetPointCommand(robot, new Pose(0, 15, Math.toRadians(0)), .25)

                ), new ParallelCommandGroup(
                        new SetPointCommand(robot, new Pose(-30, 7, Math.toRadians(0)), 1),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetLiftCommand(robot.deposit, 0),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI)


                ), new SequentialCommandGroup(

                        new WaitCommand(300),
                        new SetPointCommand(robot, new Pose(-30, -0.5, Math.toRadians(0)), .5),
                        new SetLiftCommand(robot.deposit, 0),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                        new WaitCommand(75),
                        new SetLiftCommand(robot.deposit, 475),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE),
                        new SetPointCommand(robot, new Pose(2, 25, Math.toRadians(0)), 1)



                ), new SequentialCommandGroup(
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                        new WaitCommand(250),
                        new SetPointCommand(robot, new Pose(2, 29, Math.toRadians(0)), .5),
                        new WaitCommand(100),

                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT))



                ), new SequentialCommandGroup(
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetPointCommand(robot, new Pose(5, 10, Math.toRadians(0)), .5)

                ), new SequentialCommandGroup(
//                        new SetPointCommand(robot, new Pose(-30, 10, Math.toRadians(0)), 1).alongWith(new SetLiftCommand(robot.deposit, 20)),
                        new SetPointCommand(robot, new Pose(-30, 10, Math.toRadians(0)), 1.5).alongWith(new SetLiftCommand(robot.deposit, 0)),
                        new SetPointCommand(robot, new Pose(-30, 48, Math.toRadians(0)), 1),
                        new SetPointCommand(robot, new Pose(-42, 50, Math.toRadians(0)), .5),
                        new SetPointCommand(robot, new Pose(-32, 8, Math.toRadians(0)), 1),
                        new SetPointCommand(robot, new Pose(-40, 50, Math.toRadians(0)), 1),
                        new SetPointCommand(robot, new Pose(-51, 50, Math.toRadians(0)), .5),
                        new SetPointCommand(robot, new Pose(-42, 8, Math.toRadians(0)), 1),
                        new SetPointCommand(robot, new Pose(-50, 50, Math.toRadians(0)), 1),
                        new SetPointCommand(robot, new Pose(-58, 50, Math.toRadians(0)), .5),
                        new SetPointCommand(robot, new Pose(-57, 7, Math.toRadians(0)), 1),
        new SetPointCommand(robot, new Pose(-56, 12, Math.toRadians(0)), .5)















//
                ), new ParallelCommandGroup(
                        new SetPointCommand(robot, new Pose(-30, 12, Math.toRadians(0)), 1),

                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetLiftCommand(robot.deposit, 0),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI)
                ), new SequentialCommandGroup(
                        new WaitCommand(300),

                        new SetPointCommand(robot, new Pose(-30, 0, Math.toRadians(0)), .5),
                        new WaitCommand(100),

                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                        new SetLiftCommand(robot.deposit, 475),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE).alongWith(new SetPointCommand(robot, new Pose(4, 22, Math.toRadians(0)), 1)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE)

                ), new SequentialCommandGroup(
//                        new SetPointCommand(robot, new Pose(4, 15, Math.toRadians(0)), 1),
                        new SetPointCommand(robot, new Pose(4, 29, Math.toRadians(0)), .5),
        new WaitCommand(100),

                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT))


                ), new SequentialCommandGroup(
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetPointCommand(robot, new Pose(5, 15, Math.toRadians(0)), .5)

                ), new ParallelCommandGroup(
                        new SetPointCommand(robot, new Pose(-30, 7, Math.toRadians(0)), 1),

                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetLiftCommand(robot.deposit, 0),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI)
                ), new SequentialCommandGroup(
                        new WaitCommand(300),

                        new SetPointCommand(robot, new Pose(-30, 0, Math.toRadians(0)), .5),
                        new SetLiftCommand(robot.deposit, 0),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                        new WaitCommand(75),
                        new SetLiftCommand(robot.deposit, 470),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE).alongWith(new SetPointCommand(robot, new Pose(6, 22, Math.toRadians(0)), 1)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE)

                ), new SequentialCommandGroup(
                        new SetLiftCommand(robot.deposit, 470),//.alongWith(new SetPointCommand(robot, new Pose(6, 15, Math.toRadians(0)), .5)),
                        new SetPointCommand(robot, new Pose(6, 29, Math.toRadians(0)), .5),
        new WaitCommand(100),

                new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT))



                ), new SequentialCommandGroup(
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetPointCommand(robot, new Pose(5, 15, Math.toRadians(0)), .5)

                ), new ParallelCommandGroup(
                        new SetPointCommand(robot, new Pose(-30, 7, Math.toRadians(0)), 1),

                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetLiftCommand(robot.deposit, 0),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI)
                ), new SequentialCommandGroup(
                        new WaitCommand(300),

                        new SetPointCommand(robot, new Pose(-30, 0, Math.toRadians(0)), .5),
                        new SetLiftCommand(robot.deposit, 0),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_GRAB)),
                        new WaitCommand(75),
                        new SetLiftCommand(robot.deposit, 470),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE).alongWith(new SetPointCommand(robot, new Pose(12, 22, Math.toRadians(0)), 1)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE)

                ), new SequentialCommandGroup(

        new SetLiftCommand(robot.deposit, 470),//.alongWith(new SetPointCommand(robot, new Pose(12, 15, Math.toRadians(0)), .5)),

                        new SetPointCommand(robot, new Pose(12, 29, Math.toRadians(0)), .5),
                        new WaitCommand(100),

                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT))



                ), new SequentialCommandGroup(
                        new InstantCommand(() -> robot.claw.setPosition(DepositPositions.CLAW_INIT)),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetPointCommand(robot, new Pose(5, 15, Math.toRadians(0)), .5)

                ), new ParallelCommandGroup(
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new SetLiftCommand(robot.deposit, 0),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.INIT),
                        new SetPointCommand(robot, new Pose(-40, 10, Math.toRadians(0)), 2)

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

