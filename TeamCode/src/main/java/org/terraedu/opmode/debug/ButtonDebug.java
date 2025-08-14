package org.terraedu.opmode.debug;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Robot;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetDepositLinkageCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.command.bot.SetSpinCommand;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Modes;
import org.terraedu.util.Modes.PlaceMode;
import org.terraedu.util.Modes.RobotMode;
import org.terraedu.util.system.Alliance;

//@TeleOp(name = "DeButton")
public class ButtonDebug extends CommandOpMode {

    private ElapsedTime timer;
    private double loopTime = 0;
    public PlaceMode deposit;
    public RobotMode status;
    private final Robot robot = Robot.getInstance();

    GamepadEx gph1;
    GamepadEx gph2;

    private MecanumDrive drive;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        deposit = PlaceMode.SPECIMEN;
        status = RobotMode.DRIVING;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();

        gph1 = new GamepadEx(gamepad1);
        gph2 = new GamepadEx(gamepad2);

        drive = robot.drive;
        robot.deposit.setState(Deposit.FourBarState.INIT);
        robot.deposit.setLinkage(Deposit.LinkageState.INIT);
        robot.intake.setState(Intake.IntakeState.INIT);


        //#region Command Registrar


        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).and(new Trigger(() -> status == RobotMode.DRIVING)).whenActive(

                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                        new InstantCommand(() -> deposit = PlaceMode.SAMPLE),
                        new WaitCommand(500),
                        new InstantCommand(() -> status = RobotMode.PLACING)


                ));

        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).and(new Trigger(() -> status == RobotMode.PLACING)).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                        new InstantCommand(() -> deposit = PlaceMode.SAMPLE),
                        new WaitCommand(500),
                        new InstantCommand(() -> status = RobotMode.DRIVING)


                ));

        //#endregion
    }

    @Override
    public void run() {
        super.run();

        if (timer == null) {
            timer = new ElapsedTime();
        }

        robot.read();


        double turn = joystickScalar(-gamepad1.left_stick_x, 0.01);

        Vector3f driveVec = new Vector3f(
                (float) joystickScalar(-gamepad1.right_stick_x, 0.001),
                (float) joystickScalar(gamepad1.right_stick_y, 0.001),
                0f
        );

        drive.set(driveVec, turn);
        robot.periodic();
        robot.write();
        robot.clearBulkCache();

        double loop = System.nanoTime();
//        telemetry.addData("serov", robot.intakeLinkage.getPosition());
//        telemetry.addData("target", robot.intake.getTarget());
//        telemetry.addData("pos", robot.intake.getPosition());
        telemetry.addData("mode ", status);
        telemetry.addData("reading", robot.intake.getReading());

        telemetry.addData("hz ", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }

    private double joystickScalar(double num, double min) {
        return joystickScalar(num, min, 0.66, 4);
    }

    private double joystickScalar(double n, double m, double l, double a) {
        return Math.signum(n) * m
                + (1 - m) *
                (Math.abs(n) > l ?
                        Math.pow(Math.abs(n), Math.log(l / a) / Math.log(l)) * Math.signum(n) :
                        n / a);
    }

    private Command toSpecimenMode() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> status = RobotMode.PLACING),
                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                new WaitCommand(250),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                new SetLiftCommand(robot.deposit, 0),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI)
        );
    }


    private Command toggleSpecimenLinkage() {
        return new ConditionalCommand(

                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                new SequentialCommandGroup(
                        new InstantCommand(() -> status = RobotMode.PLACING),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT)
                ),
                () -> status == RobotMode.PLACING && robot.deposit.getLinkageState() == Deposit.LinkageState.INIT
        );
    }

    private Command toDrivingFromSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> status = RobotMode.DRIVING),
                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                new WaitCommand(250),
                new SetLiftCommand(robot.deposit, 530),
                new WaitCommand(250),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE)
        );
    }

    private Command sampleTransfer() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> status = RobotMode.PLACING),
                new SetExtendoCommand(robot.intake, 100),
                new WaitCommand(150),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.TRANSFER),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.RELEASE),
                new WaitCommand(150),
                new SetSpinCommand(robot.intake, 1),
                new SetExtendoCommand(robot.intake, 40),
                new WaitCommand(300),
                new InstantCommand(() -> robot.deposit.setSampleClosed(true)),
                new SetSpinCommand(robot.intake, 0),
                new SetExtendoCommand(robot.intake, 135),
                new WaitCommand(150),
                new SetLiftCommand(robot.deposit, 630),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.PLACE),
                new WaitCommand(300),
                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE)


        );
    }

    private Command samplePlace() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setSampleClosed(false)),
                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                new WaitCommand(300),
                new InstantCommand(() -> status = RobotMode.DRIVING),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                new WaitCommand(300),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.INIT),
                new WaitCommand(300),
                new SetLiftCommand(robot.deposit, 0),
                new WaitCommand(200),
                new SetExtendoCommand(robot.intake, 0)

        );
    }

    private Command startAutoIntake() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> status = RobotMode.INTAKING),
                new InstantCommand(() -> robot.intake.setReading(true)),
                new SetExtendoCommand(robot.intake, 475),// Max is 500
                new WaitCommand(500),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
                new SetSpinCommand(robot.intake, 1),
                new WaitUntilCommand(robot.intake.getSupplier()),
                new SetSpinCommand(robot.intake, 0),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetExtendoCommand(robot.intake, 0),
                new SetSpinCommand(robot.intake, -.35),
                new WaitCommand(150),
                new SetSpinCommand(robot.intake, 0)


        );
    }

    private Command cancelIntake() {
        return new SequentialCommandGroup(
                new SetSpinCommand(robot.intake, 0),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetExtendoCommand(robot.intake, 0),
                new InstantCommand(() -> status = RobotMode.DRIVING)
        );
    }

    private Command manualEject() {
        return new SequentialCommandGroup(
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetExtendoCommand(robot.intake, 450),
                new SetSpinCommand(robot.intake, -1),
                new WaitCommand(750),
                new SetSpinCommand(robot.intake, 0),
                new SetExtendoCommand(robot.intake, 0),
                new InstantCommand(() -> status = RobotMode.DRIVING)

        );
    }

}

