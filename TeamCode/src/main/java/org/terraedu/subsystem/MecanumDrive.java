package org.terraedu.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.joml.Vector3f;
import org.terraedu.util.interfaces.TerraDrive;
import org.terraedu.util.wrappers.WSubsystem;

public class MecanumDrive extends WSubsystem implements TerraDrive {
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    public double speedCoeff;
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

    public void setCap(double cap){
        this.speedCoeff = cap;
    }

    @Override
    public void set(@NonNull Vector3f movement, double turnSpeed) {
        double strafe = -movement.y;
        double forward = -movement.x;

        double r = Math.hypot(-strafe, forward);
        double robotAngle = Math.atan2(forward, -strafe) + Math.PI / 4;

        double flP = r * Math.cos(robotAngle) - turnSpeed;
        double frP = r * Math.sin(robotAngle) + turnSpeed;
        double blP = r * Math.sin(robotAngle) - turnSpeed;
        double brP = r * Math.cos(robotAngle) + turnSpeed;

        frontLeft.setPower(flP * speedCoeff);
        frontRight.setPower(frP * speedCoeff);
        backLeft.setPower(blP * speedCoeff);
        backRight.setPower(brP * speedCoeff);
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