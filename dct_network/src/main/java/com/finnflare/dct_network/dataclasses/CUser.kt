package com.finnflare.network.dataclasses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CUserData(
    @Json(name = "_ID")
    var mId: String?,
    @Json(name = "_DESCRIPTION")
    var mDescription: String?
)

@JsonClass(generateAdapter = true)
data class CUsersList(
    @Json(name = "USERS")
    var mUsers: List<CUserData>
)