package dev.nullftc.choreolib.drive

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.joml.Vector2d

interface ILocalizer {
    var pose: Pose2D
    var angle: Double
    val velocity: Vector2d

    fun update()
}