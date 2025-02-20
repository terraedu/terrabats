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

@Autonomous(name = "0+4", group = "auto")
public class FourSample extends PedroOpMode {
    public FourSample() {
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
    private final Pose finishPose = new Pose(-6.3, 0.8, Math.toRadians(28));
    private final Pose sample1 = new Pose(-2.4, 4, Math.toRadians(-150));

    private PathChain move;
    private PathChain sample1path;

    public void buildPaths() {
        move = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(finishPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), finishPose.getHeading())
                .build();
        sample1path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(finishPose), new Point(sample1)))
                .setLinearHeadingInterpolation(finishPose.getHeading(), sample1.getHeading())
                .build();
    }

    public Command preload() {
        return new SequentialGroup(
                new ParallelGroup(
                        OuttakeClawSub.INSTANCE.clawGrab(),
                        new FollowPath(move),
                        LiftSub.INSTANCE.placeHigh(),
                        OuttakeArmSub.INSTANCE.placeHigh(),
                        OuttakePivotSub.INSTANCE.prePlaceHigh()
                ),
                OuttakePivotSub.INSTANCE.placeHigh(),
                OuttakeClawSub.INSTANCE.clawRelease(),
                new Delay(0.1),
                OuttakePivotSub.INSTANCE.prePlaceHigh(),
                new ParallelGroup(
                        LiftSub.INSTANCE.down(),
                        new FollowPath(sample1path),
                        IntakeArmSub.INSTANCE.stageTransfer(),
                        IntakePivotSub.INSTANCE.stageTransfer(),
                        IntakeClawSub.INSTANCE.stageTransfer(),
                        IntakeTurretSub.INSTANCE.turretReset()
                ),
                ExtendoSub.INSTANCE.extend1500(),
                new ParallelGroup(
                        IntakeLinkSub.INSTANCE.linkEnd(),
                        IntakeArmSub.INSTANCE.intakeSeek(),
                        IntakePivotSub.INSTANCE.intakeSeek(),
                        IntakeTurretSub.INSTANCE.intakeSeek(),
                        IntakeClawSub.INSTANCE.intakeSeek(),
                        IntakeLinkSub.INSTANCE.intakeSeek()
                )
        );
    }

    @Override
    public void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.setMaxPower(0.65);
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        preload().invoke();
    }
}