package dev.nullftc.choreolib.drive

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.joml.Vector2d
import org.joml.Vector3d

interface IDrivetrain {
    fun set(movement: Vector2d, turnSpeed: Double);

    fun setField(movement: Vector2d, turnSpeed: Double, angle: Double) {
        val rotatedMove = Vector3d(movement.x, 0.0, movement.y).rotateY(angle)
        set(Vector2d(rotatedMove.x, rotatedMove.z), turnSpeed)
    }

    fun drive(movement: Pose2D, turnSpeed: Double) {
        val moveVec = Vector2d(
            movement.getX(DistanceUnit.INCH),
            movement.getY(DistanceUnit.INCH)
        )
        set(moveVec, turnSpeed)
    }

    fun driveField(movement: Pose2D, turnSpeed: Double, angle: Double) {
        val moveVec = Vector2d(
            movement.getX(DistanceUnit.INCH),
            movement.getY(DistanceUnit.INCH)
        )
        setField(moveVec, turnSpeed, angle)
    }
}