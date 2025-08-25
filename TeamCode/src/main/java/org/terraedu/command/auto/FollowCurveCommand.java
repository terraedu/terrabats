package org.terraedu.command.auto;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.joml.Vector3f;
import org.terraedu.Robot;
import org.terraedu.subsystem.PinpointLocalizer;
import org.terraedu.util.control.PIDFController;
import org.terraedu.util.control.pathing.BezierFollower;
import org.terraedu.util.interfaces.TerraDrive;
import org.terraedu.util.system.Pose;

public class FollowCurveCommand extends CommandBase {
    private final TerraDrive drive;
    private final PinpointLocalizer localizer;
    private final Pose target;

    private final Robot robot = Robot.getInstance();

    double powX;
    double powY;

    public double dist;
    public double distH;
    public double distX;
    public double distY;

    public double pathTime;

    private final BezierFollower follower;


    private ElapsedTime timer = new ElapsedTime();

    public double distTravel;
    public double[] prevPoint;
    public double[] robotPos;
    public double[] targetArc;
    public double targetHeading;

    private final PIDFController xControl;
    private final PIDFController yControl;
    private final PIDFController xlControl;
    private final PIDFController ylControl;
    private final PIDFController hControl;

    public FollowCurveCommand(Robot robot, Pose position, double curveFactor, double pathSeconds) {
        this.drive = robot.drive;
        this.localizer = robot.localizer;
        this.target = position;
        this.pathTime = pathSeconds;

        double startX = localizer.getCurrX();
        double startY = localizer.getCurrY();
        double endX = target.x;
        double endY = target.y;

        double factor = curveFactor;
        double dx = endX - startX;
        double dy = endY - startY;
        double mag = Math.hypot(dx, dy);
        double offset = factor * mag; // higher bends more away from the straight lines

        double ctrlX = (startX + endX) / 2.0 - dy / mag * offset;
        double ctrlY = (startY + endY) / 2.0 + dx / mag * offset;

        this.follower = new BezierFollower(
                new double[]{startX, ctrlX, endX},
                new double[]{startY, ctrlY, endY}
        );

        xControl = new PIDFController(AutoConfig.xPID.p, AutoConfig.xPID.i, AutoConfig.xPID.d, AutoConfig.xPID.f);
        yControl = new PIDFController(AutoConfig.yPID.p, AutoConfig.yPID.i, AutoConfig.yPID.d, AutoConfig.xPID.f);
        xlControl = new PIDFController(AutoConfig.xlPID.p, AutoConfig.xlPID.i, AutoConfig.xlPID.d, AutoConfig.xlPID.f);
        ylControl = new PIDFController(AutoConfig.ylPID.p, AutoConfig.ylPID.i, AutoConfig.ylPID.d, AutoConfig.ylPID.f);
        hControl = new PIDFController(AutoConfig.hPID.p, AutoConfig.hPID.i, AutoConfig.hPID.d, AutoConfig.xPID.f);
    }

    Pose getPose() {
        return new Pose(
                localizer.getPose().getX(DistanceUnit.INCH),
                localizer.getPose().getY(DistanceUnit.INCH),
                localizer.getPose().getHeading(AngleUnit.RADIANS)
        );
    }

    @Override
    public void initialize() {
        timer.reset();
        xControl.reset();
        yControl.reset();
        hControl.reset();
        distTravel = 0;
        Pose curr = getPose();
        prevPoint = new double[]{curr.x, curr.y};
    }

    @Override
    public void execute() {

        Pose current = getPose();

        robotPos = new double[]{current.x, current.y};
        distTravel += Math.hypot(robotPos[0] - prevPoint[0], robotPos[1] - prevPoint[1]);
        prevPoint = robotPos;

        distX = target.x - current.x;
        distY = target.y - current.y;


        targetArc = follower.getTarget(distTravel);
        targetHeading = follower.getTargetHeading(distTravel);


        double distSq = pow(distX, 2) + pow(distY, 2);
        dist = sqrt(distSq);

        xControl.setSetPoint(targetArc[0]);
        yControl.setSetPoint(targetArc[1]);
        hControl.setSetPoint(targetHeading);

        if (abs(dist) <= 30) {
            powX = xControl.calculate(current.x);
            powY = yControl.calculate(current.y);
        } else {
            powX = xlControl.calculate(current.x);
            powY = ylControl.calculate(current.y);
        }



        double x = powX;
        double y = -powY;


        double heading = robot.scale(hControl.calculateAngleWrap(current.getAngle()));
        double currentHeading = localizer.getPose().getHeading(AngleUnit.RADIANS);
        double x_rotated = robot.scale(x * Math.cos(-currentHeading) - y * Math.sin(-currentHeading));
        double y_rotated = robot.scale(x * Math.sin(-currentHeading) + y * Math.cos(-currentHeading));
        distH = Math.toRadians(target.getAngle()) - currentHeading;

        drive.set(new Vector3f((float) x_rotated, (float) y_rotated, 0f), heading);
    }

    @Override
    public void end(boolean interrupted) {
        drive.set(new Vector3f(0f, 0f, 0f), 0);
    }

    @Override
    public boolean isFinished() {
        return (dist <= 0.2 && distH <= 0.5) || timer.seconds() > pathTime;

    }
}
