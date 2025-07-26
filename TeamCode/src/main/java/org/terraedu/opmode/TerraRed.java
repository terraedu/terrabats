package org.terraedu.opmode;


import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.constants.DepositPositions;
import org.terraedu.constants.IntakePositions;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.LinkageMode;
import org.terraedu.util.PlaceMode;
import org.terraedu.util.RobotMode;

import java.util.Set;

@TeleOp(name = "OpMode")
public class TerraRed extends CommandOpMode {

    private ElapsedTime timer;
    private double loopTime = 0;


    public PlaceMode deposit;
    public LinkageMode link;
    public RobotMode status;
    private final Robot robot = Robot.getInstance();

    GamepadEx gph1;
    GamepadEx gph2;


    private MecanumDrive drive;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        Globals.AUTO = false;

        deposit = PlaceMode.SPECIMEN;
        status = RobotMode.DRIVING;
        link = LinkageMode.IN;

        robot.init(hardwareMap, telemetry, Alliance.RED);
        robot.reset();

        gph1 = new GamepadEx(gamepad1);
        gph2 = new GamepadEx(gamepad2);

        drive = robot.drive;
        robot.deposit.setState(Deposit.OuttakeState.START);
        robot.intake.setState(Intake.IntakeState.INIT);
        robot.deposit.setClawClosed(false);
        new SetExtendoCommand(robot.intake, 0);
        robot.turret.setPosition(IntakePositions.INIT_TURRET);
        robot.drive.setCap(0.8);
        //#region Command Registrar

        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).whenActive(
                new SequentialCommandGroup(
                new InstantCommand(()->         robot.drive.setCap(.3)),
                intakeSequence()
                )
        );

        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1).whenActive(
                new SequentialCommandGroup(
                sampleDepositSequence(),
                        new InstantCommand(()->         robot.drive.setCap(1))

                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(()->         robot.drive.setCap(1)),
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.INT_READY),
                        new WaitCommand(250),
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.GRAB),
                        new WaitCommand(250),
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET)),
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.TRANSFER),
                        new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                        new SetExtendoCommand(robot.intake, 10),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                        new WaitCommand(350),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.iclaw.setPosition(IntakePositions.OPEN_CLAW))
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(()->         robot.drive.setCap(.8)),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                        new SetExtendoCommand(robot.intake, 0),
                        new SetLiftCommand(robot.deposit, 1750),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE)

                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.X).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(()->         robot.drive.setCap(.6)),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
                        new WaitCommand(250),
                        new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                        new WaitCommand(350),
                        new InstantCommand(() -> robot.iclaw.setPosition(IntakePositions.OPEN_CLAW)),
                        new WaitCommand(150),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                        new SetExtendoCommand(robot.intake, 0),
                        new SetLiftCommand(robot.deposit, 610),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE)

                )
        );


        gph1.getGamepadButton(GamepadKeys.Button.Y).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(()-> robot.intake.addTarget(50))
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.A).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(()-> robot.intake.addTarget(-50))
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.RIGHT_TURRET))
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET))
                )
        );
        gph1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.VERT_TURRET))
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.LEFT_TURRET))
                )
        );



    }

    @Override
    public void run() {
        super.run();

        if (timer == null) {
            timer = new ElapsedTime();
        }

        robot.read();


        double turn = joystickScalar(gamepad1.left_stick_x, 0.01);

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
        telemetry.addData("intake", robot.intake.getPosition());
        telemetry.addData("deposit", robot.deposit.getPosition());

        telemetry.addData("mode ", robot.deposit.getDynamic());
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

    private Command intakeSequence() {
        return new SequentialCommandGroup(
                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                new SetExtendoCommand(robot.intake, 250),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER)
        );
    }

    private Command sampleDepositSequence () {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                new WaitCommand(450),
                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                new SetLiftCommand(robot.deposit, 15)
        );
    }

}

