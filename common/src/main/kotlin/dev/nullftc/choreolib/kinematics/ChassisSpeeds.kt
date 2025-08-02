package dev.nullftc.choreolib.kinematics

import org.joml.Vector2d
import kotlin.math.cos
import kotlin.math.sin

class ChassisSpeeds(
    val linearVelocity: Vector2d,
    val angularVelocity: Double
) {
    constructor(vxMetersPerSecond: Double, vyMetersPerSecond: Double, omegaRadiansPerSecond: Double) :
        this(Vector2d(vxMetersPerSecond, vyMetersPerSecond), omegaRadiansPerSecond)

    val vx: Double
        get() = linearVelocity.x

    val vy: Double
        get() = linearVelocity.y

    val omega: Double
        get() = angularVelocity

    fun scaled(factor: Double): ChassisSpeeds = ChassisSpeeds(
        Vector2d(linearVelocity).mul(factor), angularVelocity * factor
    )

    companion object {
        fun fromFieldRelative(
            fieldVelocity: Vector2d,
            fieldAngularVelocity: Double,
            robotHeadingRadians: Double
        ): ChassisSpeeds {
            val cos = cos(robotHeadingRadians)
            val sin = sin(robotHeadingRadians)
            val vx = fieldVelocity.x * cos + fieldVelocity.y * sin
            val vy = -fieldVelocity.x * sin + fieldVelocity.y * cos
            return ChassisSpeeds(vx, vy, fieldAngularVelocity)
        }
    }
}