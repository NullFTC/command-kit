package dev.nullftc.choreolib.drive

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

interface ILocalizer {
    var pose: Pose2D
    var angle: Double
}