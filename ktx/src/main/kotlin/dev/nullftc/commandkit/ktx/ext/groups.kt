package dev.nullftc.commandkit.ktx.ext

import dev.nullftc.commandkit.command.Command
import dev.nullftc.commandkit.command.DeadlineCommandGroup
import dev.nullftc.commandkit.command.ParallelCommandGroup
import dev.nullftc.commandkit.command.ParallelRaceGroup

fun parallel(block: CommandListBuilder.() -> Unit): ParallelCommandGroup {
    val builder = CommandListBuilder()
    builder.block()
    return ParallelCommandGroup(*builder.commands.toTypedArray())
}

fun race(block: CommandListBuilder.() -> Unit): ParallelRaceGroup {
    val builder = CommandListBuilder()
    builder.block()
    return ParallelRaceGroup(*builder.commands.toTypedArray())
}

fun deadline(deadlineCommand: Command, block: CommandListBuilder.() -> Unit): DeadlineCommandGroup {
    val builder = CommandListBuilder()
    builder.block()
    return DeadlineCommandGroup(deadlineCommand, *builder.commands.toTypedArray())
}

