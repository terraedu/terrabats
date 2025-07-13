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
import org.terraedu.util.Pose;
import org.terraedu.subsystem.PinpointLocalizer;
import org.terraedu.util.control.PIDFController;
import org.terraedu.util.interfaces.TerraDrive;

public class GotoCommand extends CommandBase {
    private final TerraDrive drive;
    private final PinpointLocalizer localizer;
    private final Pose target;
    double powX;
    double powY;
    public double dist;
    public double distH;
    public double distX;
    public double distY;



    private ElapsedTime timer = new ElapsedTime();

    private final PIDFController xControl;
    private final PIDFController yControl;
    private final PIDFController xlControl;
    private final PIDFController ylControl;
    private final PIDFController hControl;

    public GotoCommand(Robot robot, Pose pose) {
        this.drive = robot.drive;
        this.localizer = robot.localizer;
        this.target = pose;

        xControl = new PIDFController(GotoConfig.xPID.p, GotoConfig.xPID.i, GotoConfig.xPID.d, GotoConfig.xPID.f);
        yControl = new PIDFController(GotoConfig.yPID.p, GotoConfig.yPID.i, GotoConfig.yPID.d, GotoConfig.xPID.f);
        xlControl = new PIDFController(GotoConfig.xlPID.p, GotoConfig.xlPID.i, GotoConfig.xlPID.d, GotoConfig.xlPID.f);
        ylControl = new PIDFController(GotoConfig.ylPID.p, GotoConfig.ylPID.i, GotoConfig.ylPID.d, GotoConfig.xlPID.f);
        hControl = new PIDFController(GotoConfig.hPID.p, GotoConfig.hPID.i, GotoConfig.hPID.d, GotoConfig.xPID.f);
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
//        timer.reset();
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
        hControl.setSetPoint(target.getAngle());

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

        double heading = (hControl.calculateAngleWrap(current.getAngle()))*0;
        double currentHeading = localizer.getPose().getHeading(AngleUnit.RADIANS);

        distH = target.getAngle() - currentHeading;

        drive.setField(new Vector3f((float) x, (float) y, 0f), heading, (float) currentHeading);
    }

    @Override
    public void end(boolean interrupted) {
        drive.set(new Vector3f(0f,0f,0f), 0);
    }

    public boolean getFinished(){
        return isFinished();
    }

    @Override
    public boolean isFinished() {
        return dist <= 0.2 && distH <= 0.2;
//        return false;
//        if(dist > 0.2){
//            timer.reset();
//        }
//        return timer.seconds()>0.2;
//        return getPose().distance(target) > 2.0;
    }
}
