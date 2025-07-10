package org.terraedu.subsystem;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.terraedu.util.control.GoBildaPinpointDriver;
import org.terraedu.util.wrappers.WSubsystem;

public class PinpointLocalizer extends WSubsystem {
    public GoBildaPinpointDriver pinpoint;

    Pose2D lastPose = new Pose2D(DistanceUnit.INCH, 0.0, 0.0, AngleUnit.RADIANS, 0.0);


    public PinpointLocalizer(GoBildaPinpointDriver pinpoint) {
        this.pinpoint = pinpoint;
    }

    public void setDirection(GoBildaPinpointDriver.EncoderDirection x, GoBildaPinpointDriver.EncoderDirection y){
        pinpoint.setEncoderDirections(x,y);
    }

    public void setResolution(GoBildaPinpointDriver.GoBildaOdometryPods resolution){
        pinpoint.setEncoderResolution(resolution);
    }

    public void setCustomResolution(double resolution){
        pinpoint.setEncoderResolution(resolution);
    }

    public double getCurrX(){
        return lastPose.getX(DistanceUnit.INCH);
    }

    public double getCurrY(){
        return lastPose.getY(DistanceUnit.INCH);

    }

    public double getCurrHeading(){
        return lastPose.getHeading(AngleUnit.DEGREES);

    }


    public void setOffsets(double x, double y){
        pinpoint.setOffsets(x,y);
    }

    public Pose2D getPose() {
        return lastPose;
    }


    public Pose2D getCurrent() {
        return pinpoint.getPosition();
    }

    public boolean isReady() {
        return pinpoint.getDeviceStatus().equals(GoBildaPinpointDriver.DeviceStatus.READY);
    }

    @Override
    public void periodic() {}

    @Override
    public void read() {
        pinpoint.update();
        if (!(Double.isNaN(pinpoint.getPosX()) || Double.isNaN(pinpoint.getPosY()) || Double.isNaN(pinpoint.getHeading()))) {
            lastPose = new Pose2D(
                    DistanceUnit.MM,
                    pinpoint.getPosX(),
                    pinpoint.getPosY(),
                    AngleUnit.RADIANS,
                    normalize(pinpoint.getHeading())
            );
        }
    }

    @Override
    public void write() {}

    @Override
    public void reset() {
        pinpoint.resetPosAndIMU();
        lastPose = new Pose2D(DistanceUnit.INCH, 0.0, 0.0, AngleUnit.RADIANS, 0.0);
    }

    private double normalize(double angle) {
        double pi2 = 2 * Math.PI;
        double angleMod = (angle % pi2 + pi2) % pi2;
        if (angleMod > Math.PI) angleMod -= pi2;
        return angleMod;
    }
}
