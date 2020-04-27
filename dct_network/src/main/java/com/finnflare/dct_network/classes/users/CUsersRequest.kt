package com.finnflare.dct_network.classes.users

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CUsersRequest(
    val header: Header,
    val request: Request
)