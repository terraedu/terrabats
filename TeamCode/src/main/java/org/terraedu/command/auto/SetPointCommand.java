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
import org.terraedu.util.system.Pose;
import org.terraedu.subsystem.PinpointLocalizer;
import org.terraedu.util.control.PIDFController;
import org.terraedu.util.interfaces.TerraDrive;

public class SetPointCommand extends CommandBase {
    private final TerraDrive drive;
    private final PinpointLocalizer localizer;
    private final Pose target;
    double powX;
    double powY;
    public double dist;
    public double distH;
    public double distX;
    public double distY;
    public double pathTime;
    private final Robot robot = Robot.getInstance();



    private ElapsedTime timer = new ElapsedTime();

    private final PIDFController xControl;
    private final PIDFController yControl;
    private final PIDFController xlControl;
    private final PIDFController ylControl;
    private final PIDFController hControl;

    public SetPointCommand(Robot robot, Pose position, double pathSeconds) {
        this.drive = robot.drive;
        this.localizer = robot.localizer;
        this.target = position;
        this.pathTime = pathSeconds;

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
    }

    @Override
    public void execute() {
        Pose current = getPose();

        xControl.setSetPoint(target.x);
        yControl.setSetPoint(target.y);
        xlControl.setSetPoint(target.x);
        ylControl.setSetPoint(target.y);
        hControl.setSetPoint(Math.toRadians(target.getAngle()));

        distX = target.x - current.x;
        distY = target.y - current.y;

        double distSq = pow(distX, 2) + pow(distY, 2);
        dist = sqrt(distSq);

        if (abs(dist) <= 30) {
            powX = xControl.calculate(current.x);
            powY = yControl.calculate(current.y);
        }else {
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
        drive.set(new Vector3f(0f,0f,0f), 0);
    }

    @Override
    public boolean isFinished() {
        return dist <= 0.2 && distH <= 0.5 || timer.seconds() > pathTime;
//        return false;

    }
}
