package dev.nullftc.choreolib.drive

import dev.nullftc.choreolib.kinematics.ChassisSpeeds
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

interface IFollower {
    val isBusy: Boolean
    val isFinished: Boolean

    fun reset(pose: Pose2D)

    fun update(currentPose: Pose2D, timeSeconds: Double): ChassisSpeeds
}