package dev.nullftc.choreolib.sample

import dev.nullftc.choreolib.trajectory.TrajectorySample
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

class MecanumSample: TrajectorySample<MecanumSample> {
    override val timestamp: Double
        get() = TODO("Not yet implemented")
    override val pose: Pose2D
        get() = TODO("Not yet implemented")

    override fun offsetBy(offset: Double): MecanumSample {
        TODO("Not yet implemented")
    }

    override fun interpolate(
        aheadState: MecanumSample,
        timestamp: Double
    ): MecanumSample {
        TODO("Not yet implemented")
    }
}