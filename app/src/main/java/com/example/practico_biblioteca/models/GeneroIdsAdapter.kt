package com.example.practico_biblioteca.models

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class GeneroIdsAdapter : JsonDeserializer<List<Int>>, JsonSerializer<List<Int>> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Int> {
        if (json == null || json.isJsonNull || !json.isJsonArray) return emptyList()

        return json.asJsonArray.mapNotNull { item ->
            when {
                item.isJsonPrimitive -> item.asIntOrNull()
                item.isJsonObject -> item.asJsonObject.get("id")?.asIntOrNull()
                else -> null
            }
        }
    }

    override fun serialize(
        src: List<Int>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonArray = JsonArray()
        src.orEmpty().forEach { id -> jsonArray.add(JsonPrimitive(id)) }
        return jsonArray
    }

    private fun JsonElement.asIntOrNull(): Int? {
        return try {
            asInt
        } catch (_: NumberFormatException) {
            null
        } catch (_: UnsupportedOperationException) {
            null
        } catch (_: IllegalStateException) {
            null
        }
    }
}

