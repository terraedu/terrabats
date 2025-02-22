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
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
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
import robotparts.hardware.Intake;

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
    private final Pose prePreloadPose = new Pose(-8, 0, 0);
    private final Pose preloadPose = new Pose(-10.7, -1, 0);

    private final Pose corner1 = new Pose(-4, 11, 0);
    private final Pose corner2 = new Pose(-22, 11, 0);

    private final Pose sample1 = new Pose(-21, 12, 0);
    private final Pose sample1In = new Pose(-3, 12, 0);
    private final Pose sample2 = new Pose(-15, 15.5, 0);
    private final Pose sample2In = new Pose(-4, 15.5, 0);
    private final Pose sample3 = new Pose(-21, 16, 0);
    private final Pose sample3In = new Pose(-4, 17, 0);

    private final Pose blahPose = new Pose(-11, -4, 0);

    private final Pose firstWall = new Pose(0, 7.8, 0);
    private final Pose firstWallGrab = new Pose(1.35, 7.8, 0);

    private final Pose specimen = new Pose(-11, 2, 0);

    private PathChain preload;
    private PathChain unload;
    private PathChain samples;

    private Path sample1Path;
    private Path sample2Path;
    private Path sample3Path;

    private Path blah;

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
        firstSpecimenPath.setLinearHeadingInterpolation(0, 0);

        // PATH CHAINS
        preload = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(preloadPose)))
                .setLinearHeadingInterpolation(0, 0)
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
                        OuttakeArmSub.INSTANCE.placeHigh(),
                        OuttakePivotSub.INSTANCE.placeHigh(),
                        OuttakeClawSub.INSTANCE.clawGrab(),

                        new FollowPath(preload)

                        ),
                new ParallelGroup(
                        new FollowPath(samples),
                        OuttakeClawSub.INSTANCE.clawRelease(),
                        LiftSub.INSTANCE.down(),

                        OuttakeArmSub.INSTANCE.specimenReady(),
                        OuttakePivotSub.INSTANCE.specimenReady(),
                        OuttakeClawSub.INSTANCE.specimenReady(),

                        IntakeArmSub.INSTANCE.specimenReady(),
                        IntakePivotSub.INSTANCE.yoink(),

                        IntakeLinkSub.INSTANCE.intakeSeek(),

                        IntakeClawSub.INSTANCE.specimenReady(),

                        IntakeTurretSub.INSTANCE.specimenReady(),

//                ),
////
                IntakeClawSub.INSTANCE.clawGrab(),
                IntakeArmSub.INSTANCE.transferSpecimen()
////                new FollowPath(firstSpecimenPath)
//                new ParallelGroup(
//                        OuttakeArmSub.INSTANCE.specimenReady(),
//                        OuttakeClawSub.INSTANCE.specimenReady(),
//                        OuttakePivotSub.INSTANCE.specimenReady(),
//                        IntakeArmSub.INSTANCE.specimenReady(),
//                        IntakePivotSub.INSTANCE.specimenReady(),
//                        IntakeClawSub.INSTANCE.clawRelease(),
//                        IntakeLinkSub.INSTANCE.tight()
//                ),
//                new Delay(2),
//                IntakeClawSub.INSTANCE.clawGrab(),
//                new Delay(0.1),
//                new ParallelGroup(
//                        IntakeArmSub.INSTANCE.yoinkSpecimen(),
//                        IntakePivotSub.INSTANCE.yoink()
//                ),
//                IntakeClawSub.INSTANCE.clawGRAB(),
//                IntakeLinkSub.INSTANCE.tight(),
//                new Delay(0.3),
//                new ParallelGroup(
//                        IntakeArmSub.INSTANCE.transferSpecimen(),
//                        IntakeLinkSub.INSTANCE.linkEnd(),
//                        IntakePivotSub.INSTANCE.transferSpecimen(),
//                        new Delay(0.5)
                )
        );
    }

    @Override
    public void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.setMaxPower(1);
        moveInit().invoke();
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        define().invoke();
    }
}