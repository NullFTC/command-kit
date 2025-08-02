package dev.nullftc.choreolib.sample

import dev.nullftc.choreolib.trajectory.TrajectorySample
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

/**
 * A single swerve robot sample in a Trajectory.
 */
class SwerveSample(
    val t: Double,
    val x: Double,
    val y: Double,
    val heading: Double,
    val vx: Double,
    val vy: Double,
    val omega: Double,
    val ax: Double,
    val ay: Double,
    val alpha: Double,
    private val fx: DoubleArray?,
    private val fy: DoubleArray?
) : TrajectorySample<SwerveSample> {
    fun moduleForcesX(): DoubleArray =
        if (fx == null || fx.size != 4) EMPTY_MODULE_FORCES else fx

    fun moduleForcesY(): DoubleArray =
        if (fy == null || fy.size != 4) EMPTY_MODULE_FORCES else fy

    override val timestamp: Double
        get() = t

    override val pose: Pose2D
        get() = Pose2D(DistanceUnit.METER, x, y, AngleUnit.RADIANS, heading)

    override fun interpolate(aheadState: SwerveSample, timestamp: Double): SwerveSample {
        val scale = (timestamp - t) / (aheadState.t - t)

        fun interpolateArray(start: DoubleArray?, end: DoubleArray?): DoubleArray {
            val startArr = start ?: EMPTY_MODULE_FORCES
            val endArr = end ?: EMPTY_MODULE_FORCES
            return DoubleArray(4) { i -> startArr[i] + (endArr[i] - startArr[i]) * scale }
        }

        return SwerveSample(
            timestamp,
            x + (aheadState.x - x) * scale,
            y + (aheadState.y - y) * scale,
            heading + (aheadState.heading - heading) * scale,
            vx + (aheadState.vx - vx) * scale,
            vy + (aheadState.vy - vy) * scale,
            omega + (aheadState.omega - omega) * scale,
            ax + (aheadState.ax - ax) * scale,
            ay + (aheadState.ay - ay) * scale,
            alpha + (aheadState.alpha - alpha) * scale,
            interpolateArray(fx, aheadState.fx),
            interpolateArray(fy, aheadState.fy)
        )
    }



    override fun offsetBy(timestampOffset: Double): SwerveSample = SwerveSample(
            t + timestampOffset,
            x, y, heading,
            vx, vy, omega,
            ax, ay, alpha,
            moduleForcesX(),
            moduleForcesY()
    )

    companion object {
        private val EMPTY_MODULE_FORCES = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
    }
}