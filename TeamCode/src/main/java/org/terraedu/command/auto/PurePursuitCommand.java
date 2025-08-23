//package org.terraedu.command.auto;
//
//import com.arcrobotics.ftclib.command.CommandBase;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.joml.Vector3f;
//import org.opencv.core.Point;
//import org.terraedu.Robot;
//import org.terraedu.subsystem.PinpointLocalizer;
//import org.terraedu.util.interfaces.TerraDrive;
//import org.terraedu.util.system.Pose;
//import org.terraedu.util.system.TrapezoidProfile;
//
//import java.util.List;
//
//public class PurePursuitCommand extends CommandBase {
//    private final TerraDrive drive;
//    private final PinpointLocalizer localizer;
//    private final Robot robot = Robot.getInstance();
//
//    private final List<Point> path;
//    private TrapezoidProfile driveProfile;
//    private TrapezoidProfile headingProfile;
//
//    private final ElapsedTime timer = new ElapsedTime();
//    private int moveSegment = 1;
//    private int headingSegment = 1;
//
//    public PurePursuitCommand(Robot robot, List<Point> path, TrapezoidProfile headingProfile) {
//        this.drive = robot.drive;
//        this.localizer = robot.localizer;
//        this.path = path;
//        this.headingProfile = headingProfile;
//
//
//    }
//
//    private Point getIntersectionCircleSegment(Point circleCenter, double radius, Point segStart, Point segEnd) {
//        double dx = segEnd.x - segStart.x;
//        double dy = segEnd.y - segStart.y;
//        double distance = Math.hypot(dx, dy);
//
//
//        double fx = segStart.x - circleCenter.x;
//        double fy = segStart.y - circleCenter.y;
//
//        double a = dx*dx + dy*dy;
//        double b = 2*(fx*dx + fy*dy);
//        double c = fx*fx + fy*fy - radius*radius;
//
//        double disc = b*b - 4*a*c;
//        if (disc < 0) return null;
//
//        double sqrtDisc = Math.sqrt(disc);
//        double t1 = (-b - sqrtDisc)/(2*a);
//        double t2 = (-b + sqrtDisc)/(2*a);
//
//        double t = Double.NaN;
//        if (0 <= t1 && t1 <= 1) t = t1;
//        else if (0 <= t2 && t2 <= 1) t = t2;
//
//        if (Double.isNaN(t)) return null;
//        return new Point(segStart.x + t*dx, segStart.y + t*dy);
//    }
//
//    private Point getLookahead(Point current, double lookaheadDist, boolean headingCircle) {
//        int segment = headingCircle ? headingSegment : moveSegment;
//
//        while (segment < path.size()) {
//            Point start = path.get(segment-1);
//            Point end = path.get(segment);
//
//            Point inter = getIntersectionCircleSegment(current, lookaheadDist, start, end);
//            if (inter != null) return inter;
//
//            segment++;
//            if (headingCircle) headingSegment = segment;
//            else moveSegment = segment;
//        }
//        return path.get(path.size()-1);
//    }
//
//    Pose getPose() {
//        return new Pose(
//                localizer.getPose().getX(DistanceUnit.INCH),
//                localizer.getPose().getY(DistanceUnit.INCH),
//                localizer.getPose().getHeading(AngleUnit.RADIANS)
//        );
//    }
//
//    Point getPoint() {
//        return new Point(
//                localizer.getPose().getX(DistanceUnit.INCH),
//                localizer.getPose().getY(DistanceUnit.INCH)
//        );
//    }
//
//    @Override
//    public void initialize() { timer.reset(); }
//
//    @Override
//    public void execute() {
//
//        path.get
//
//        driveProfile = new TrapezoidProfile(0,0,0);
//        headingProfile = new TrapezoidProfile(0,0,0);
//
//        Point current = getPoint();
//        Pose currPose = getPose();
//
//
//        double t = timer.seconds();
//
//        double driveRadius = driveProfile.getVelocity(t);
//        double headingRadius = headingProfile.getVelocity(t);
//
//        Point driveTarget = getLookahead(current, driveRadius, false);
//        Point headingTarget = getLookahead(current, headingRadius, true);
//
//        double vx = driveTarget.x - current.x;
//        double vy = driveTarget.y - current.y;
//        double mag = Math.hypot(vx, vy);
//        if (mag > 0) { vx = vx / mag * driveRadius; vy = vy / mag * driveRadius; }
//
//        double desiredHeading = Math.atan2(headingTarget.y - current.y, headingTarget.x - current.x);
//        double omega = desiredHeading - currPose.getAngle();
//
//        // rotate to robot frame
//        double headingRad = currPose.getAngle();
//        double x_rotated = vx * Math.cos(-headingRad) - vy * Math.sin(-headingRad);
//        double y_rotated = vx * Math.sin(-headingRad) + vy * Math.cos(-headingRad);
//
//        drive.set(new Vector3f((float) robot.scale(x_rotated), (float) robot.scale(y_rotated),0f),robot.scale(omega));
//    }
//
//    @Override
//    public void end(boolean interrupted) { drive.set(new Vector3f(0f,0f,0f), 0f); }
//
//    @Override
//    public boolean isFinished() {
//        Point current = getPoint();
//        Point last = path.get(path.size() - 1);
//        double dist = Math.hypot(last.x - current.x, last.y - current.y);
//        return dist < 0.2 || timer.seconds() > 30;
//    }
//}
