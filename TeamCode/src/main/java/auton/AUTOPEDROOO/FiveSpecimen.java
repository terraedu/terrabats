package auton.AUTOPEDROOO;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
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
                IntakeLinkSub.INSTANCE);
    }

    private final Pose startPose = new Pose(0, 0, 0);
    private final Pose prePreloadPose = new Pose(-4, 0, 0);
    private final Pose preloadPose = new Pose(-10.3, 0, 0);

    private final Pose corner1 = new Pose(-4, 10, 0);
    private final Pose corner2 = new Pose(-22, 11, 0);

    private final Pose sample1 = new Pose(-21, 12, 0);
    private final Pose sample1In = new Pose(-4, 12, 0);
    private final Pose sample2 = new Pose(-19, 15, 0);
    private final Pose sample2In = new Pose(-4, 15, 0);
    private final Pose sample3 = new Pose(-19, 16, 0);
    private final Pose sample3In = new Pose(-4, 16, 0);

    private final Pose firstWall = new Pose(-0.6, 7.8, 0);
    private final Pose firstWallGrab = new Pose(-0.6, 7.8, 0);

    private final Pose specimen = new Pose(-10, 0, 30);

    private PathChain preload;
    private PathChain samples;

    private Path sample1Path;
    private Path sample2Path;
    private Path sample3Path;

    private Path firstWallPath;
    private Path firstWallGrabPath;

    private Path firstSpecimenPath;

    public void buildPaths() {
        // PATHS
        sample1Path = new Path(new BezierCurve(preloadPose, prePreloadPose, corner1, corner2, sample1, sample1In));
        sample1Path.setLinearHeadingInterpolation(0, 0);

        sample2Path = new Path(new BezierCurve(sample1In, sample1, sample2, sample2In));
        sample2Path.setLinearHeadingInterpolation(0, 0);

        sample3Path = new Path(new BezierCurve(sample2In, sample2, sample3, sample3In));
        sample3Path.setLinearHeadingInterpolation(0, 0);

        firstWallPath = new Path(new BezierCurve(sample3In, firstWall));
        firstWallPath.setLinearHeadingInterpolation(0, 0);

        firstWallGrabPath = new Path(new BezierLine(firstWall, firstWallGrab));
        firstWallGrabPath.setLinearHeadingInterpolation(0, 0);

        firstSpecimenPath = new Path(new BezierCurve(firstWallGrab, specimen));
        firstWallGrabPath.setLinearHeadingInterpolation(firstWallGrab.getHeading(), specimen.getHeading());

        // PATH CHAINS
        preload = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(prePreloadPose)))
                .setLinearHeadingInterpolation(0, 0)
                .addPath(new BezierLine(new Point(prePreloadPose), new Point(preloadPose)))
                .setLinearHeadingInterpolation(0, 0)
                .setPathEndTimeoutConstraint(0)
                .build();

        samples = follower.pathBuilder()
                .addPath(sample1Path)
                .setLinearHeadingInterpolation(0, 0)
                .addPath(sample2Path)
                .setLinearHeadingInterpolation(0, 0)
                .addPath(sample3Path)
                .setLinearHeadingInterpolation(0, 0)
                .addPath(firstWallPath)
                .setLinearHeadingInterpolation(0, 0)
                .build();
    }

    public Command moveInit() {
        return new ParallelGroup(
                IntakeArmSub.INSTANCE.moveInit(),
                IntakePivotSub.INSTANCE.moveInit(),
                IntakeTurretSub.INSTANCE.moveInit(),
                IntakeClawSub.INSTANCE.moveInit(),
                IntakeLinkSub.INSTANCE.moveInit(),

                OuttakeArmSub.INSTANCE.moveInit(),
                OuttakePivotSub.INSTANCE.moveInit(),
                OuttakeClawSub.INSTANCE.moveInit()
        );
    }

    public Command define() {
        return new SequentialGroup(
                new ParallelGroup(
                        OuttakeClawSub.INSTANCE.clawGrab(),
                        LiftSub.INSTANCE.specimen(),
                        OuttakeArmSub.INSTANCE.upSpecimen(),
                        OuttakePivotSub.INSTANCE.upSpecimen(),
                        OuttakeClawSub.INSTANCE.clawGrab(),
                        new FollowPath(preload)
                ),
                OuttakeClawSub.INSTANCE.clawRelease(),
                new ParallelGroup(
                        LiftSub.INSTANCE.down(),
                        OuttakeArmSub.INSTANCE.moveInit(),
                        OuttakePivotSub.INSTANCE.moveInit()
                ),
                new ParallelGroup(
                        new FollowPath(samples),
                        IntakePivotSub.INSTANCE.specimenReady(),
                        IntakeArmSub.INSTANCE.specimenReady(),
                        IntakeTurretSub.INSTANCE.specimenReady(),
                        IntakeClawSub.INSTANCE.specimenReady(),
                        IntakeLinkSub.INSTANCE.specimenReady(),

                        OuttakeArmSub.INSTANCE.specimenReady(),
                        OuttakePivotSub.INSTANCE.specimenReady(),
                        OuttakeClawSub.INSTANCE.specimenReady()
                ),
                new FollowPath(firstWallGrabPath),
                IntakeClawSub.INSTANCE.clawGRAB(),
                new Delay(0.1),
                new ParallelGroup(
                        IntakeArmSub.INSTANCE.yoinkSpecimen(),
                        IntakeLinkSub.INSTANCE.yoinkSpecimen(),
                        IntakeClawSub.INSTANCE.yoinkSpecimen()
                ),
                new Delay(0.1),
                new ParallelGroup(
                        IntakeArmSub.INSTANCE.transferSpecimen(),
                        IntakePivotSub.INSTANCE.transferSpecimen(),
                        IntakeLinkSub.INSTANCE.transferSpecimen()
                ),
                new Delay(0.05),
                OuttakeClawSub.INSTANCE.clawGrab(),
                new Delay(0.05),
                OuttakeClawSub.INSTANCE.clawRelease(),
                new Delay(0.05),
                OuttakeClawSub.INSTANCE.clawGrab(),
                new Delay(0.2),
                new FollowPath(firstSpecimenPath)
        );
    }

    @Override
    public void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.setMaxPower(2);
        moveInit().invoke();
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        define().invoke();
    }
}