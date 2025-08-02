package dev.nullftc.choreolib.sample

import dev.nullftc.choreolib.trajectory.TrajectorySample
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

class DifferentialSample: TrajectorySample<DifferentialSample> {
    override val timestamp: Double
        get() = TODO("Not yet implemented")
    override val pose: Pose2D
        get() = TODO("Not yet implemented")

    override fun offsetBy(offset: Double): DifferentialSample {
        TODO("Not yet implemented")
    }

    override fun interpolate(
        aheadState: DifferentialSample,
        timestamp: Double
    ): DifferentialSample {
        TODO("Not yet implemented")
    }
}