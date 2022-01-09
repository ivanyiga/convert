package com.ivanyiga.convert.models

import org.json.JSONObject

data class currenciesModel(
    var currName: String?,
    var currSymbol: String?,
    var last_updated: String?,
    var quote: JSONObject
)