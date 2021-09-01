package com.test.appsfactorytask.core.api.repository.error

import java.lang.RuntimeException

class SaveAlbumException(
    val mbid: String,
    val name: String
) : RuntimeException("Could not save album $name mbid = $mbid")