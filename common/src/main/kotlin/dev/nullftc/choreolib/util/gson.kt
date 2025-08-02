package dev.nullftc.choreolib.util

import com.google.gson.GsonBuilder
import dev.nullftc.choreolib.trajectory.EventMarker

val gson = GsonBuilder()
    .registerTypeAdapter(EventMarker::class.java, EventMarker.Deserializer())
    .create()