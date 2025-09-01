package dev.nullftc.commandkit.ktx.ext

import com.qualcomm.robotcore.util.ElapsedTime
import dev.nullftc.commandkit.command.RunnableCommand
import dev.nullftc.commandkit.command.Command

fun instant(block: () -> Unit): Command {
    return RunnableCommand(block) { true }
}

fun delayed(seconds: Double, block: ((deltaTime: Double, elapsed: Double) -> Unit)? = null): Command {
    val timer = ElapsedTime()
    var lastTime = 0.0

    return RunnableCommand(
        {
            val now = timer.seconds()
            val delta = now - lastTime
            lastTime = now
            block?.invoke(delta, now)
        },
        { timer.seconds() >= seconds }
    )
}
