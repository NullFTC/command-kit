package dev.nullftc.choreolib.trajectory

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

interface TrajectorySample<Self : TrajectorySample<Self>> {
    val timestamp: Double
    val pose: Pose2D

    /**
     * Offsets the trajectory timestamp by a given double value
     */
    fun offsetBy(offset: Double): Self

    /**
     * Interpolates the future samples based off the current state.
     */
    fun interpolate(aheadState: Self, timestamp: Double): Self
}