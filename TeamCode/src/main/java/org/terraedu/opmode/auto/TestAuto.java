package org.terraedu.opmode.auto;

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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.GotoCommand;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetDepositLinkageCommand;
import org.terraedu.command.teleop.TriggerIntakeCommand;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Hang;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.Pose;

@Autonomous
public class TestAuto extends CommandOpMode {

    private double loopTime = 0;
    private final Robot robot = Robot.getInstance();

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Globals.AUTO = true;

        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new GotoCommand(robot, new Pose(10, 10, Math.toRadians(90)))
                )
        );
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
        loopTime = loop;
        telemetry.update();
    }
}

