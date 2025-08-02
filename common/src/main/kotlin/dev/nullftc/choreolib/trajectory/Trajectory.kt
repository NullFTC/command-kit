package dev.nullftc.choreolib.trajectory

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.joml.Vector2d

/**
 * A Choreo generated trajectory featuring events, splits, and samples.
 */
class Trajectory<SampleType : TrajectorySample<SampleType>>(
    val name: String,
    val samples: List<SampleType>,
    val splits: List<Int>,
    val events: List<EventMarker>
) {
    /**
     * Getters for the first/last samples in a given trajectory
     */

    fun getInitialSample(): SampleType? = samples.firstOrNull()
    fun getFinalSample(): SampleType? = samples.lastOrNull()

    /**
     * The total time of the trajectory in seconds.
     */
    val totalTime: Double
        get() = getFinalSample()?.timestamp ?: 0.0

    /**
     * Get a sample at a given timestamp.
     */
    fun sampleAt(timestamp: Double): SampleType? {
        return when {
            samples.isEmpty() -> null
            samples.size == 1 -> samples[0]
            else -> sampleInterval(timestamp)
        }
    }

    private fun sampleInterval(timestamp: Double): SampleType {
        if (timestamp < samples.first().timestamp) return getInitialSample()!!
        if (timestamp >= totalTime) return getFinalSample()!!

        var low = 0
        var high = samples.lastIndex

        while (low != high) {
            val mid = (low + high) / 2
            if (samples[mid].timestamp < timestamp) {
                low = mid + 1
            } else {
                high = mid
            }
        }

        if (low == 0) {
            return samples[low]
        }

        val behind = samples[low - 1]
        val ahead = samples[low]

        return if ((ahead.timestamp - behind.timestamp) < 1E-6) ahead else behind.interpolate(ahead, timestamp)
    }

    fun getInitialPose(): Pose2D? = getInitialSample()?.pose
    fun getFinalPose(): Pose2D? = getFinalSample()?.pose

    fun getPoses(): Array<Pose2D> = samples.map { it.pose }.toTypedArray()

    /**
     * Get the events for a given trajectory
     *
     * @param e The name of the event
     */
    fun getEvents(e: String) = events.filter { it.event == e }

    fun getSplit(splitIndex: Int): Trajectory<SampleType>? {
        if (splitIndex !in splits.indices) return null

        val start = splits[splitIndex]
        val end = if (splitIndex + 1 < splits.size) splits[splitIndex + 1] + 1 else samples.size
        val sublist = samples.subList(start, end)
        val startTime = sublist.first().timestamp
        val endTime = sublist.last().timestamp

        val offsetSamples = sublist.map { it.offsetBy(-startTime) }
        val offsetEvents = events
            .filter {
                it.timestamp in startTime..endTime
            }
            .map { it.offsetBy(-startTime) }

        return Trajectory("$name[$splitIndex]", offsetSamples, emptyList(), offsetEvents)
    }

    fun getClosestSample(pose: Pose2D): SampleType? {
        val x = pose.getX(DistanceUnit.INCH)
        val y = pose.getY(DistanceUnit.INCH)

        val robotPose = Vector2d(x, y)

        var minDistSquared = Double.MAX_VALUE
        var minSample: SampleType? = null

        for (i in 0 until samples.size - 1) {
            val startPose = samples[i].pose
            val endPose = samples[i + 1].pose
            val start = Vector2d(startPose.getX(DistanceUnit.INCH), startPose.getY(DistanceUnit.INCH))
            val end = Vector2d(endPose.getX(DistanceUnit.INCH), endPose.getY(DistanceUnit.INCH))
            val dir = end.sub(start, Vector2d()).normalize()
            val length = end.distance(start)

            val diff = robotPose.sub(start, Vector2d())
            var proj = diff.dot(dir)
            proj = proj.coerceIn(0.0, length)
            val point = start.fma(proj, dir, Vector2d())

            val distSq = point.distanceSquared(robotPose)
            if (distSq < minDistSquared) {
                minDistSquared = distSq
                val startSample = samples[i]
                val endSample = samples[i + 1]
                val timestamp = startSample.timestamp + (endSample.timestamp - startSample.timestamp) * proj / length
                minSample = startSample.interpolate(endSample, timestamp)
            }
        }

        return minSample
    }

    override fun equals(other: Any?): Boolean {
        return other is Trajectory<*> &&
                name == other.name &&
                samples == other.samples &&
                splits == other.splits &&
                events == other.events
    }
}
