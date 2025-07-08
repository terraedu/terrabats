package org.terraedu.opmode;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
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
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.command.teleop.TriggerIntakeCommand;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Hang;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.RobotMode;

//@TeleOp(name = "Lift Tuning")
public class LiftTuning extends CommandOpMode {

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
        //#region Command Registrar

//        gph1.getGamepadButton(GamepadKeys.Button.A).toggleWhenPressed(
//                new SetLiftCommand(robot.deposit, 500),
//                new SetLiftCommand(robot.deposit, 50)
//        );

        //#endregion

        //#endregion

//        schedule(new TriggerIntakeCommand(this::getIntakeSpeed));
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

        robot.liftMotors.forEach((it) -> it.setPower(getIntakeSpeed()));

        drive.set(driveVec, turn);

        robot.periodic();
        robot.write();
        robot.clearBulkCache();

        double loop = System.nanoTime();
        telemetry.addData("power ", robot.deposit.getPower());
        telemetry.addData("speewd ", getIntakeSpeed());
        telemetry.addData("target", robot.deposit.getTarget());
        telemetry.addData("pos", robot.deposit.getPosition());

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
}

