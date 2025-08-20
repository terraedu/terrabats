package org.terraedu.opmode.auto;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.FollowPointCommand;
import org.terraedu.subsystem.Intake;
import org.terraedu.util.system.Alliance;
import org.terraedu.util.system.Pose;

@Autonomous(name = "test4")
public class autotest4 extends CommandOpMode {

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

                        new FollowPointCommand(robot, new Pose(-50, -50, 0), 20, 20,  3),
                        new WaitCommand(5000),
                        new FollowPointCommand(robot, new Pose(-10, -10, 0), 20, 20,  3)




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

