package org.terraedu.command.auto;

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

    private ElapsedTime timer = new ElapsedTime();

    private final PIDFController xControl;
    private final PIDFController yControl;
    private final PIDFController hControl;

    public GotoCommand(Robot robot, Pose pose) {
        this.drive = robot.drive;
        this.localizer = robot.localizer;
        this.target = pose;

        xControl = new PIDFController(GotoConfig.xPID.p, GotoConfig.xPID.i, GotoConfig.xPID.d, GotoConfig.xPID.f);
        yControl = new PIDFController(GotoConfig.yPID.p, GotoConfig.yPID.i, GotoConfig.yPID.d, GotoConfig.xPID.f);
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
        hControl.setSetPoint(target.getAngle());

        double x = xControl.calculateAngleWrap(current.x);
        double y = yControl.calculateAngleWrap(current.y);
        double heading = hControl.calculateAngleWrap(current.getAngle());

        double currentHeading = localizer.getPose().getHeading(AngleUnit.RADIANS);

        drive.setField(new Vector3f((float) x, (float) y, 0f), heading, (float) currentHeading);
    }

    @Override
    public void end(boolean interrupted) {
        drive.set(new Vector3f(0f,0f,0f), 0);
    }

    @Override
    public boolean isFinished() {
        return getPose().distance(target) > 2.0 || timer.seconds() > 3.5;
    }
}
