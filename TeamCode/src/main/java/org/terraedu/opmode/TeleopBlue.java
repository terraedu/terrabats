package org.terraedu.opmode;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Robot;
import org.terraedu.command.auto.GotoCommand;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetDepositLinkageCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.command.bot.SetSpinCommand;
import org.terraedu.command.teleop.TriggerIntakeCommand;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Hang;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.RobotMode;

@TeleOp(name = "Blue")
public class TeleopBlue extends CommandOpMode {

    private ElapsedTime timer;
    private double loopTime = 0;
    public RobotMode status;
    private final Robot robot = Robot.getInstance();

    GamepadEx gph1;
    GamepadEx gph2;

    private MecanumDrive drive;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();


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

        // Y Button – Toggle Specimen Mode
        gph1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new ConditionalCommand(
                        toSpecimenMode(),
                        toDrivingFromSpecimen(),
                        () -> status == RobotMode.DRIVING
                )
        );

        // DPAD UP – Toggle Linkage between INIT and PLACE
        gph1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(toggleSpecimenLinkage());

        // Right Trigger – Intake Logic (Auto-Intake If Driving)
        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).whenActive(
                new ConditionalCommand(
                        startAutoIntake(),
                        cancelIntake(),
                        () -> status == RobotMode.DRIVING
                )
        );

        // Left Trigger – Manual Eject
        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1).whenActive(
                manualEject()
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenHeld(
                new InstantCommand(() -> {
                    robot.hang.setState(Hang.HangState.IN);
                })
        );

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
//        telemetry.addData("power ", robot.liftLeft.getPower());
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

//    public boolean isClimbTime() {
//        return timer.seconds() >= 90;
//    }

    private double getIntakeSpeed() {
        return gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
    }

    private Command toSpecimenMode() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                new WaitCommand(250),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                new SetLiftCommand(robot.deposit, 0),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI),
                new InstantCommand(() -> status = RobotMode.SPECIMEN)
        );
    }

    private Command toggleSpecimenLinkage() {
        return new ConditionalCommand(
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                new SequentialCommandGroup(
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new InstantCommand(() -> status = RobotMode.SPECIMEN)
                ),
                () -> status == RobotMode.SPECIMEN && robot.deposit.getLinkageState() == Deposit.LinkageState.INIT
        );
    }
    private Command toDrivingFromSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                new WaitCommand(250),
                new SetLiftCommand(robot.deposit, 530),
                new WaitCommand(250),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE),
                new WaitCommand(250),
                new InstantCommand(() -> status = RobotMode.DRIVING)
        );
    }

    private Command startAutoIntake() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.setReading(true)),
                new SetExtendoCommand(robot.intake, 450), // Max is 500
                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
                new SetSpinCommand(robot.intake, 1),
                new InstantCommand(() -> status = RobotMode.INTAKING),
                new WaitUntilCommand(robot.intake.intakeSupplier),
                new SetSpinCommand(robot.intake, 0),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetExtendoCommand(robot.intake, 0),
                new InstantCommand(() -> status = RobotMode.DRIVING)
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
                new SetExtendoCommand(robot.intake, 0)
        );
    }

}

