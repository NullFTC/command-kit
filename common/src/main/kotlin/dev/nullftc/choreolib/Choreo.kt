package dev.nullftc.choreolib

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import dev.nullftc.choreolib.sample.DifferentialSample
import dev.nullftc.choreolib.sample.SwerveSample
import dev.nullftc.choreolib.trajectory.EventMarker
import dev.nullftc.choreolib.trajectory.ProjectFile
import dev.nullftc.choreolib.trajectory.Trajectory
import dev.nullftc.choreolib.trajectory.TrajectorySample
import dev.nullftc.choreolib.util.gson
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

class Choreo {
    private val TRAJECTORY_FILE_EXTENSION = ".traj"
    private val SPEC_VERSION = "v2025.0.0"

    private var lazyProjectFile: ProjectFile? = null

    /**
     * Only to be used when testing.
     */
    internal fun setChoreoDir(directory: File) {
        CHOREO_DIR = directory
        lazyProjectFile = null
    }

    /**
     * Gets the project file from the deploy directory. Choreolib expects a .chor file to be placed in
     * src/main/deploy/choreo.
     *
     * The result is cached after the first call.
     */
    fun getProjectFile(): ProjectFile {
        lazyProjectFile?.let { return it }

        val projectFiles = CHOREO_DIR.listFiles { _, name -> name.endsWith(".chor") }
            ?: throw RuntimeException("Could not find Choreo directory: $CHOREO_DIR")

        when {
            projectFiles.isEmpty() -> throw RuntimeException("Could not find project file in deploy directory")
            projectFiles.size > 1 -> throw RuntimeException("Found multiple project files in deploy directory")
        }

        val file = projectFiles[0]
        val contents = FileReader(file).use { reader ->
            reader.readText()
        }

        val json = try {
            gson.fromJson(contents, JsonObject::class.java)
        } catch (e: JsonSyntaxException) {
            throw RuntimeException("Could not parse the project file: ", e)
        }

        val version = json.get("version").asString
        if (version != SPEC_VERSION) {
            throw RuntimeException(".chor project file: Wrong version $version. Expected $SPEC_VERSION")
        }

        lazyProjectFile = gson.fromJson(contents, ProjectFile::class.java)
        return lazyProjectFile!!
    }

    /**
     * This interface exists as a type alias. A TrajectoryLogger has a signature of (Trajectory, Boolean) -> Unit,
     * where the function consumes a trajectory and a boolean indicating whether the trajectory is starting or finishing.
     */
    fun interface TrajectoryLogger<SampleType : TrajectorySample<SampleType>> : (Trajectory<SampleType>, Boolean) -> Unit

    @Suppress("UNCHECKED_CAST")
    fun <SampleType : TrajectorySample<SampleType>> loadTrajectory(trajectoryNameRaw: String): Optional<Trajectory<SampleType>> {
        var trajectoryName = trajectoryNameRaw
        if (trajectoryName.endsWith(TRAJECTORY_FILE_EXTENSION)) {
            trajectoryName = trajectoryName.removeSuffix(TRAJECTORY_FILE_EXTENSION)
        }
        val trajectoryFile = File(CHOREO_DIR, "$trajectoryName$TRAJECTORY_FILE_EXTENSION")

        LOGGER.info("Loading trajectory {}", trajectoryFile.absolutePath)

        return try {
            val content = FileReader(trajectoryFile).use { it.readText() }
            val trajectory = loadTrajectoryString(content, getProjectFile()) as Trajectory<SampleType>
            Optional.of(trajectory)
        } catch (ex: FileNotFoundException) {
            LOGGER.error("Could not find trajectory file: $trajectoryFile", ex)
            Optional.empty()
        } catch (ex: JsonSyntaxException) {
            LOGGER.error("Could not parse trajectory file: $trajectoryFile", ex)
            Optional.empty()
        } catch (ex: Exception) {
            LOGGER.error("Error loading trajectory file: ${ex.message}", ex)
            Optional.empty()
        }
    }

    /**
     * Load a trajectory from a JSON string.
     */
    private fun loadTrajectoryString(trajectoryJsonString: String, projectFile: ProjectFile): Trajectory<out TrajectorySample<*>> {
        val wholeTrajectory = gson.fromJson(trajectoryJsonString, JsonObject::class.java)

        val name = wholeTrajectory.get("name").asString
        val version = wholeTrajectory.get("version").asString
        if (version != SPEC_VERSION) {
            throw RuntimeException("$name.traj: Wrong version: $version. Expected $SPEC_VERSION")
        }

        val unfilteredEvents = gson.fromJson(wholeTrajectory.get("events"), Array<EventMarker>::class.java)
            .filter { it.timestamp >= 0 && it.event.isNotEmpty() }
        val events = unfilteredEvents.toTypedArray()

        val trajectoryObj = wholeTrajectory.getAsJsonObject("trajectory")
        var splits = gson.fromJson(trajectoryObj.get("splits"), Array<Int>::class.java)
        if (splits.isEmpty()) {
            splits = arrayOf(0) + splits
        }

        return when (projectFile.type) {
            "Swerve" -> {
                val samples = gson.fromJson(trajectoryObj["samples"], Array<SwerveSample>::class.java)
                Trajectory(name, samples.toList(), splits.toList(), events.toList())
            }
            "Differential" -> {
                val samples = gson.fromJson(trajectoryObj["samples"], Array<DifferentialSample>::class.java)
                Trajectory(name, samples.toList(), splits.toList(), events.toList())
            }
            else -> throw IllegalArgumentException("Unsupported project type: ${projectFile.type}")
        }
    }

    /** Utility class for caching loaded trajectories */
    class TrajectoryCache(
        private val cache: MutableMap<String, Trajectory<*>> = ConcurrentHashMap()
    ) {

        /**
         * Load a trajectory, caching it for reuse.
         */
        fun loadTrajectory(trajectoryName: String): Optional<Trajectory<*>> {
            cache[trajectoryName]?.let { return Optional.of(it) }

            // You need to implement this function to actually load from disk or wherever.
            val loaded = loadTrajectoryFromSource(trajectoryName)
            loaded.ifPresent { cache[trajectoryName] = it }
            return loaded
        }

        fun loadTrajectory(trajectoryName: String, splitIndex: Int): Optional<Trajectory<*>> {
            val key = "$trajectoryName.:.$splitIndex"
            cache[key]?.let { return Optional.of(it) }

            val fullTrajectory = cache[trajectoryName]
                ?: loadTrajectory(trajectoryName).orElse(null)
                ?: return Optional.empty()

            val split = fullTrajectory.getSplit(splitIndex)
            return split?.let {
                cache[key] = it
                Optional.of(it)
            } ?: Optional.empty()
        }


        /** Clear the cache */
        fun clear() = cache.clear()

        /** This should be implemented or replaced with actual load logic */
        private fun loadTrajectoryFromSource(name: String): Optional<Trajectory<*>> {
            // Replace with actual loading logic
            return Optional.empty()
        }
    }

    companion object {
        @JvmField
        val INSTANCE = Choreo()

        val LOGGER = LoggerFactory.getLogger("Choreo")
        var CHOREO_DIR = File(AppUtil.FIRST_FOLDER, "/choreo/")
    }
}