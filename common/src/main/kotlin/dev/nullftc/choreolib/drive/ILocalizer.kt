package dev.nullftc.choreolib.drive

import dev.nullftc.wpimath.geometry.Twist2d
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.joml.Vector2d

interface ILocalizer {
    var pose: Pose2D
    var angle: Double
    val velocity: Vector2d

    fun update()
    fun toTwist2d(start: Pose2D, end: Pose2D): Twist2d
}