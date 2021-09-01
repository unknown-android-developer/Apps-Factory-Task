package com.test.appsfactorytask.core.api.webservice.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.test.appsfactorytask.core.api.webservice.response.TrackResponse
import com.test.appsfactorytask.core.api.webservice.response.TracksResponse
import kotlin.jvm.Throws

class TracksResponseTypeAdapter : TypeAdapter<TracksResponse>() {

    override fun write(out: JsonWriter?, value: TracksResponse?) = Unit

    override fun read(`in`: JsonReader?): TracksResponse {
        var tracks: List<TrackResponse> = emptyList()
        `in`?.apply {
            beginObject()
            if (peek() == JsonToken.NAME) {
                if (nextName() == "album") {
                    tracks = readAlbum(this)
                }
            }
            endObject()
        }
        return TracksResponse(tracks)
    }

    private fun readAlbum(reader: JsonReader): List<TrackResponse> {
        var tracks: List<TrackResponse> = emptyList()
        reader.beginObject()
        while (reader.hasNext()) {
            if (reader.nextName() == "tracks") {
                tracks = readTracks(reader)
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()
        return tracks
    }

    private fun readTracks(reader: JsonReader): List<TrackResponse> {
        var tracks: List<TrackResponse> = emptyList()
        reader.beginObject()
        while (reader.hasNext()) {
            if (reader.nextName() == "track" && reader.peek() != JsonToken.NULL) {
                tracks = try {
                    tryReadTrack(reader)
                } catch (e: IllegalStateException) {
                    listOfNotNull(readTrack(reader))
                }
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()
        return tracks
    }

    @Throws(IllegalStateException::class)
    private fun tryReadTrack(reader: JsonReader): List<TrackResponse> {
        val tracks = mutableListOf<TrackResponse?>()
        reader.beginArray()
        while (reader.hasNext()) {
            tracks += readTrack(reader)
        }
        reader.endArray()
        return tracks.filterNotNull()
    }

    private fun readTrack(reader: JsonReader): TrackResponse? {
        var name: String? = null
        reader.beginObject()
        while (reader.hasNext()) {
            val fieldName = reader.nextName()
            if (fieldName == "name") {
                name = reader.nextString()
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()
        return name?.let { TrackResponse(it) }
    }
}