package auton.AUTOPEDROOO;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;
import com.rowanmcalpin.nextftc.pedro.FollowPath;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import robotparts.autoSubsystems.LiftSub;
import robotparts.autoSubsystems.OuttakeArmSub;
import robotparts.autoSubsystems.OuttakeClawSub;
import robotparts.autoSubsystems.OuttakePivotSub;

@Autonomous(name = "5+0", group = "auto")
public class FiveSpec extends PedroOpMode {
    public FiveSpec() {
        super(LiftSub.INSTANCE, OuttakeArmSub.INSTANCE, OuttakePivotSub.INSTANCE, OuttakeClawSub.INSTANCE);
    }

    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));
    private final Pose finishPose = new Pose(-3, 2, Math.toRadians(40));

    private PathChain move;

    public void buildPaths() {
        move = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(finishPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), finishPose.getHeading())
                .build();
    }

    public Command preload() {
//        return new SequentialGroup(
//                new ParallelGroup(
//                        new FollowPath(move),
//                        LiftSub.INSTANCE.specimen(),
//                        OuttakeClawSub.INSTANCE.clawGrab(),
//                        OuttakeArmSub.INSTANCE.upSpecimen(),
//                        OuttakePivotSub.INSTANCE.upSpecimen()
//                ),
//                new Delay(0.5),
//                new ParallelGroup(
//                        LiftSub.INSTANCE.down(),
//                        OuttakeClawSub.INSTANCE.clawRelease(),
//                        OuttakeArmSub.INSTANCE.moveInit(),
//                        OuttakePivotSub.INSTANCE.moveInit()
//                )
//        );
        return new SequentialGroup(
                new FollowPath(move)
        );
    }

    @Override
    public void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        buildPaths();
    }

    @Override
    public void onStartButtonPressed() {
        preload().invoke();
    }
}