package dev.nullftc.commandkit.ktx.ext

import dev.nullftc.commandkit.command.WaitCommand

fun delay(seconds: Double) = WaitCommand(seconds.toLong())