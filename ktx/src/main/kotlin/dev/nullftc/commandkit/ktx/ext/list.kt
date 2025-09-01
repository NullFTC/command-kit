package dev.nullftc.commandkit.ktx.ext

import dev.nullftc.commandkit.command.Command
import dev.nullftc.commandkit.command.RunnableCommand

@DslMarker
annotation class CommandDsl

@CommandDsl
class CommandListBuilder {
    val commands = mutableListOf<Command>()

    fun add(command: Command) {
        commands.add(command)
    }

    fun run(action: () -> Unit) {
        commands.add(RunnableCommand(action) { true })
    }

    fun proxy(command: Command) {
        commands.add(proxy { command })
    }
}