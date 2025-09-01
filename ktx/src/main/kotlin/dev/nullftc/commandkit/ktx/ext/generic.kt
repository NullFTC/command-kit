package dev.nullftc.commandkit.ktx.ext

import dev.nullftc.commandkit.command.Command
import dev.nullftc.commandkit.command.ConditionalCommand
import dev.nullftc.commandkit.command.ProxyCommand
import dev.nullftc.commandkit.command.RepeatCommand
import dev.nullftc.commandkit.command.RunnableCommand
import dev.nullftc.commandkit.command.WaitCommand

fun run(action: () -> Unit) = RunnableCommand(action) { true }

fun wait(seconds: Double) = WaitCommand((seconds * 1000).toLong())

fun proxy(commandSupplier: () -> Command) = ProxyCommand(commandSupplier)

fun repeat(count: Int, command: Command) = RepeatCommand(command, count)

fun proxy(command: Command) = proxy { command }

fun conditional(condition: Boolean, then: Command, orElse: Command): ConditionalCommand {
    return ConditionalCommand(then, orElse) { condition }
}

