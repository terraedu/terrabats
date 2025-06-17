package org.terraedu.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.GotoCommand;
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

