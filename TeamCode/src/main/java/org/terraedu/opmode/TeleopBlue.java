package org.terraedu.opmode;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Robot;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetDepositLinkageCommand;
import org.terraedu.command.teleop.TriggerIntakeCommand;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Hang;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;

@TeleOp(name = "Blue")
public class TeleopBlue extends CommandOpMode {

    private ElapsedTime timer;
    private double loopTime = 0;

    private final Robot robot = Robot.getInstance();

    GamepadEx driver;
    GamepadEx gunner;

    private MecanumDrive drive;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();

        driver = new GamepadEx(gamepad1);
        gunner = new GamepadEx(gamepad2);

        drive = robot.drive;

        //#region Command Registrar

        driver.getGamepadButton(GamepadKeys.Button.X).whenPressed(new ParallelCommandGroup(
                new SetArmCommand(robot.deposit, Deposit.FourBarState.INIT),
                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT)
        ));

        driver.getGamepadButton(GamepadKeys.Button.A).whenPressed(new InstantCommand(
                () -> robot.intake.setState(Intake.IntakeState.HOVER)
        ));

        //#region Climb

        Trigger climbTrigger = new Trigger(this::isClimbTime);

        climbTrigger.whileActiveOnce(new SequentialCommandGroup(
                new InstantCommand(
                        () -> {
                            robot.hang.setState(Hang.HangState.OUT);
                        }
                ),
                //TODO(find out how long to make this)
                new WaitCommand(200),
                new InstantCommand(
                        () -> {
                            robot.hang.setState(Hang.HangState.STATIONARY);
                        }
                )
        ));

        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenHeld(
                new InstantCommand(() -> {
                    robot.hang.setState(Hang.HangState.OUT);
                })
        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenHeld(
                new InstantCommand(() -> {
                    robot.hang.setState(Hang.HangState.IN);
                })
        );

        //#endregion

        //#endregion

        schedule(new TriggerIntakeCommand(this::getIntakeSpeed));
    }

    @Override
    public void run() {
        super.run();

        if (timer == null) {
            timer = new ElapsedTime();
        }

        robot.read();


        double turn = joystickScalar(gamepad1.right_stick_x, 0.01);

        Vector3f driveVec = new Vector3f(
                (float) joystickScalar(gamepad1.left_stick_y, 0.001),
                (float) joystickScalar(gamepad1.left_stick_x, 0.001),
                0f
        );

        drive.set(driveVec, turn);

        robot.periodic();
        robot.write();
        robot.clearBulkCache();

        double loop = System.nanoTime();
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

    public boolean isClimbTime() {
        return timer.seconds() >= 90;
    }

    private double getIntakeSpeed() {
        return driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
    }
}

