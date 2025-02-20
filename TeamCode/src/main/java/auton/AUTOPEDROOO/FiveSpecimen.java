package auton.AUTOPEDROOO;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.pedro.FollowPath;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import robotparts.autoSubsystems.ExtendoSub;
import robotparts.autoSubsystems.IntakeArmSub;
import robotparts.autoSubsystems.IntakeClawSub;
import robotparts.autoSubsystems.IntakeLinkSub;
import robotparts.autoSubsystems.IntakePivotSub;
import robotparts.autoSubsystems.IntakeTurretSub;
import robotparts.autoSubsystems.LiftSub;
import robotparts.autoSubsystems.OuttakeArmSub;
import robotparts.autoSubsystems.OuttakeClawSub;
import robotparts.autoSubsystems.OuttakePivotSub;

@Autonomous(name = "5+0", group = "auto")
public class FiveSpecimen extends PedroOpMode {
    public FiveSpecimen() {
        super(LiftSub.INSTANCE,
                ExtendoSub.INSTANCE,
                OuttakeArmSub.INSTANCE,
                OuttakePivotSub.INSTANCE,
                OuttakeClawSub.INSTANCE,
                IntakeArmSub.INSTANCE,
                IntakePivotSub.INSTANCE,
                IntakeTurretSub.INSTANCE,
                IntakeClawSub.INSTANCE,
                IntakeTurretSub.INSTANCE);
    }

    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));
    private final Pose finishPose = new Pose(3, 0, Math.toRadians(90));
    private final Pose sample1 = new Pose(3, 3, Math.toRadians(180));
    private final Pose sample2 = new Pose(0, 3, Math.toRadians(270));
    private final Pose sample3 = new Pose(0, 0, Math.toRadians(0));

    private PathChain move;
    private PathChain sample1path;
    private PathChain sample2path;
    private PathChain sample3path;
    private PathChain path;

    public void buildPaths() {
        move = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(finishPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), finishPose.getHeading())
                .build();
        sample1path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(finishPose), new Point(sample1)))
                .setLinearHeadingInterpolation(finishPose.getHeading(), sample1.getHeading())
                .build();
        sample2path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(sample1), new Point(sample2)))
                .setLinearHeadingInterpolation(sample1.getHeading(), sample2.getHeading())
                .build();
        sample3path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(sample2), new Point(sample3)))
                .setLinearHeadingInterpolation(sample2.getHeading(), sample3.getHeading())
                .build();
        path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(sample3), new Point(startPose)))
                .setLinearHeadingInterpolation(sample3.getHeading(), startPose.getHeading())
                .build();
    }

    public Command preload() {
        return new SequentialGroup(
                new FollowPath(move),
                new FollowPath(sample1path),
                new FollowPath(sample2path),
                new FollowPath(sample3path),
                new FollowPath(path)
        );
    }

    @Override
    public void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.setMaxPower(1);
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        preload().invoke();
    }
}