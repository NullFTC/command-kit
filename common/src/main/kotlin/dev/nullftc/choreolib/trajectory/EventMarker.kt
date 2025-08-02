package dev.nullftc.choreolib.trajectory

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * A marker for an event inside a trajectory.
 */
data class EventMarker(
    val timestamp: Double,
    val event: String
) {
    /**
     * The JSON deserializer for EventMarkers.
     */
    class Deserializer: JsonDeserializer<EventMarker> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): EventMarker {
            try {
                val targetTimestamp = json.asJsonObject["from"].asJsonObject["targetTimestamp"].asDouble
                val offset = json.asJsonObject["from"].asJsonObject["offset"].asJsonObject["val"].asDouble
                val event =  json.asJsonObject["name"].asString

                return EventMarker(targetTimestamp + offset, event)
            } catch (e: Exception) {
                return EventMarker(-1.0, "")
            }
        }
    }

    /**
     * Offsets the marker by a given double (timestamp)
     */
    fun offsetBy(offset: Double): EventMarker {
        return EventMarker(timestamp + offset, event)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventMarker) return false

        return timestamp == other.timestamp && event == other.event
    }

    override fun hashCode(): Int {
        return 31 * timestamp.hashCode() + event.hashCode()
    }
}