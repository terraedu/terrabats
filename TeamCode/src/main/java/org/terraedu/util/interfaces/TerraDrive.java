package org.terraedu.util.interfaces;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface TerraDrive {
    void set(Vector3f movement, double turnSpeed);

    default void setField(Vector3f movement, double turnSpeed, float angle) {
        Matrix4f identity = new Matrix4f().identity();
        identity.rotate(angle, 1, 1, 0);

        Vector3f movementVector = identity.transformPosition(movement);

        set(movementVector, turnSpeed);
    }

    default void drive(Pose2D movement, double turnSpeed) {
        Vector3f movementVector = new Vector3f((float) movement.getX(DistanceUnit.INCH), (float) movement.getY(DistanceUnit.INCH), 0);
        set(movementVector, turnSpeed);
    }

    default void driveField(Pose2D movement, double turnSpeed, float angle) {
        Vector3f movementVector = new Vector3f((float) movement.getX(DistanceUnit.INCH), (float) movement.getY(DistanceUnit.INCH), 0);
        setField(movementVector, turnSpeed, angle);
    }
}
