package org.terraedu.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.joml.Vector3f;
import org.terraedu.util.interfaces.TerraDrive;
import org.terraedu.util.wrappers.WSubsystem;

public class MecanumDrive extends WSubsystem implements TerraDrive {
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;

    public MecanumDrive(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx backLeft, DcMotorEx backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        this.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void set(Vector3f movement, double turnSpeed) {
        double strafe = -movement.y;
        double forward = -movement.x;

        double r = Math.hypot(-strafe, forward);
        double robotAngle = Math.atan2(forward, -strafe) + Math.PI / 4;

        double flP = r * Math.cos(robotAngle) + turnSpeed;
        double frP = r * Math.sin(robotAngle) - turnSpeed;
        double blP = r * Math.sin(robotAngle) + turnSpeed;
        double brP = r * Math.cos(robotAngle) - turnSpeed;

        frontLeft.setPower(flP);
        frontRight.setPower(frP);
        backLeft.setPower(blP);
        backRight.setPower(brP);
    }

    @Override
    public void periodic() {}

    @Override
    public void read() {}

    @Override
    public void write() {}

    @Override
    public void reset() {}
}