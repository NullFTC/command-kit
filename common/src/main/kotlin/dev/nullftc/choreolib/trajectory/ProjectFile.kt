package dev.nullftc.choreolib.trajectory

class ProjectFile(
    val name: String,
    val version: String,
    val type: String,
    val config: Config,
    val generationFeatures: List<String>
) {
    /**
     * A representation of an expression. An equation and its value.
     */
    data class Expression(
        val exp: String,
        val `val`: Double
    )

    /**
     * An xy pair of expressions.
     */
    data class XYExpression(
        val x: Expression,
        val y: Expression
    )

    /**
     * A collection of expressions representing the distance of the bumpers from the center of the robot.
     */
    data class Bumpers(
        val front: Expression,
        val back: Expression,
        val side: Expression
    )

    /**
     * The user configuration of the project.
     */
    data class Config(
        val frontLeft: XYExpression,
        val backLeft: XYExpression,
        val mass: Expression,
        val inertia: Expression,
        val gearing: Expression,
        val wheelRadius: Expression,
        val vmax: Expression,
        val tmax: Expression,
        val cof: Expression,
        val bumper: Bumpers,
        val differentialTrackWidth: Expression
    )
}