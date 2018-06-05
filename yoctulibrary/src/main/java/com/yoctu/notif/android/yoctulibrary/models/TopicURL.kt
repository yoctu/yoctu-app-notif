package com.yoctu.notif.android.yoctulibrary.models

/**
 * This model contain the URL and Api Key
 *
 * Created on 05.06.18.
 */
class TopicURL() {

    var apiKey: String? = ""
    var url: String = ""

    constructor(apiKey: String?, url: String): this() {
        this.apiKey = apiKey
        this.url = url
    }

    override fun toString() = "URL is $url and the api key is $apiKey"
}