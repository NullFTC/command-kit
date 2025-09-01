package dev.nullftc.commandkit.ktx.ext

import com.qualcomm.robotcore.util.ElapsedTime
import dev.nullftc.commandkit.command.Command
import dev.nullftc.commandkit.command.RunnableCommand
import java.util.function.BooleanSupplier

fun idler(condition: BooleanSupplier, block: ((deltaTime: Double, elapsed: Double) -> Unit)? = null): Command {
    val timer = ElapsedTime()
    var lastTime = 0.0

    return RunnableCommand(
        Runnable {
            val now = timer.seconds()
            val delta = now - lastTime
            lastTime = now
            block?.invoke(delta, now)
        },
        condition
    )
}

fun forever(block: (deltaTime: Double, elapsed: Double) -> Unit): Command {
    val timer = ElapsedTime()
    var lastTime = 0.0

    return RunnableCommand(
        {
            val now = timer.seconds()
            val delta = now - lastTime
            lastTime = now
            block(delta, now)
        },
        { false }
    )
}
