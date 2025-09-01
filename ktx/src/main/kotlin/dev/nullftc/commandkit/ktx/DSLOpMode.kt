package dev.nullftc.commandkit.ktx

import dev.nullftc.commandkit.command.Command
import dev.nullftc.commandkit.opmode.CommandOpMode

open class DSLOpMode(val pre: Boolean = false, val block: CommandOpMode.() -> Unit): CommandOpMode() {
    override fun prerun() {
        if (pre) {
            block(this)
        }
    }

    override fun run() {
        if (!pre) {
            block(this)
        }
    }

    operator fun Command.unaryPlus() {
        schedule(this)
    }
}