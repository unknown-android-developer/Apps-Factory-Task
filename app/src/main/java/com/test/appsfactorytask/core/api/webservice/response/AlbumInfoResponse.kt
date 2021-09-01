package com.test.appsfactorytask.core.api.webservice.response

data class TracksResponse(val track: List<TrackResponse>)

data class TrackResponse(val name: String)