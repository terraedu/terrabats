package opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

import subsystem.Drive;
import util.purepursuit.PurePursuit;
import util.purepursuit.PurePursuitUtil;

@Autonomous
public class test extends LinearOpMode {

    enum PathState {
        ONE,
        TWO
    }

    enum ActionState {
        ACTION_1,
        ACTION_2,
    }

    Drive drive;
    PurePursuit purePursuit;
    ArrayList<Pose2d> path1;
    ArrayList<Pose2d> path2;
    boolean timeToggle = true;
    double TimeStamp = 0;
    ElapsedTime timer2 = new ElapsedTime();
    double previousTime = 0;
    double currentTime = 0;
    ElapsedTime timer = new ElapsedTime();
    public static double movePower = 0.5;
    double headingOffset = 0;
    public static double moveRadius = 5;
    public static double headingRadius = 5;
    public static double dist = 30;
    PathState pathState = PathState.ONE;

    ActionState actionState = ActionState.ACTION_1;
    ElapsedTime timerArm = new ElapsedTime();
    private Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

    @Override
    public void runOpMode() throws InterruptedException {

        drive = new Drive(hardwareMap, startPose, timer);


        purePursuit = new PurePursuit(drive, moveRadius, headingRadius, movePower);

        path1 = new ArrayList<>();
        path1.add(startPose);


        path2 = new ArrayList<>();
        path2.add(new Pose2d(0, 20));
        path2.add(new Pose2d(20, 20));
        path2.add(new Pose2d(20, 0));
        path2.add(new Pose2d(0, 0));
        PurePursuitUtil.updateMoveSegment(1);
        PurePursuitUtil.updateHeadingSegment(1);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while (opModeIsActive()) {


            PurePursuitUtil.setPathLength(purePursuit.getPathLength());
            purePursuit.followPath(false, path2, 0);


            previousTime = currentTime;
            currentTime = timer2.nanoseconds() / 1000000000.0;
            telemetry.addData("hz ", 1 / (currentTime - previousTime));
            telemetry.addData("pose x", drive.getX());
            telemetry.addData("pose y", drive.getY());
            telemetry.addData("pose r", drive.getR());
            telemetry.addData("pushed", 1);
            telemetry.addData("timer", timer2.milliseconds() - TimeStamp);
            telemetry.addData("state", pathState);
            telemetry.addData("path length", purePursuit.getPathLength());
            telemetry.addData("move segment ", PurePursuitUtil.getMoveSegment());
            telemetry.addData("heading segment ", PurePursuitUtil.getHeadingSegment());
            telemetry.update();


        }


    }
}