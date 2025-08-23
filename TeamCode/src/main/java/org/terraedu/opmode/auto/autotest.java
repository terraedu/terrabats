package org.terraedu.opmode.auto;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.FollowPointCommand;
import org.terraedu.command.auto.FollowWayPointCommand;
import org.terraedu.command.auto.SetPointCommand;
import org.terraedu.subsystem.Intake;
import org.terraedu.util.system.Alliance;
import org.terraedu.util.system.Pose;

@Autonomous(name = "test")
public class autotest extends CommandOpMode {

    private double loopTime = 0;
    private final Robot robot = Robot.getInstance();

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        Globals.AUTO = true;

        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();
        robot.intake.setState(Intake.IntakeState.INIT);


        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(

                        new FollowWayPointCommand(robot, new Pose(-20, 35, 0),10,3),
//                        new WaitCommand(5000),

                        new FollowPointCommand(robot, new Pose(-50, 50, 0),20,20, 3),
//                        new WaitCommand(2000),
                        new FollowWayPointCommand(robot, new Pose(-40, 40, 180),15,3),
                        new FollowPointCommand(robot, new Pose(0, 0, 180), 20, 20, 3)




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
        telemetry.addData("X", robot.localizer.getCurrX());
        telemetry.addData("Y", robot.localizer.getCurrY());
        telemetry.addData("Heading", robot.localizer.getCurrHeading());


        loopTime = loop;
        telemetry.update();
    }
}

