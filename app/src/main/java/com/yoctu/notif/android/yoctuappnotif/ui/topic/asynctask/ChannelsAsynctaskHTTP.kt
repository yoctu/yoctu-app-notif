package com.yoctu.notif.android.yoctuappnotif.ui.topic.asynctask

import android.os.AsyncTask
import android.util.Log
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackChannelsResponse
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.mapper.YoctuMapper
import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels
import com.yoctu.notif.android.yoctulibrary.models.TopicURL
import org.json.JSONException
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException

/**
 * Created by gael on 18.05.18.
 */

class ChannelsAsynctaskHTTP(val callback: CallbackChannelsResponse): AsyncTask<String,Void,String>() {

    private var code: Int = 200
    private var error: String? = null
    private var data: ResponseChannels? = null
    private var topiCURL: TopicURL? = null

    constructor(callback: CallbackChannelsResponse, obj: TopicURL): this(callback) {
        this.topiCURL = obj
        Log.d(YoctuUtils.TAG_DEBUG," obj is $obj")
    }

    override fun doInBackground(vararg params: String?): String {
        var httpURLConnection: HttpURLConnection? = null
        val currentURL = topiCURL?.url ?: params[0]
        try {
            Log.d(YoctuUtils.TAG_DEBUG,params[0].plus(" - ").plus(currentURL))
            val myURL = URL(currentURL)
            httpURLConnection = myURL.openConnection() as HttpURLConnection

            httpURLConnection.requestMethod = "GET"
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.doInput = true
            topiCURL?.let { tu ->
                tu.apiKey?.let { key ->
                    if (!key.isEmpty())
                        httpURLConnection.setRequestProperty("Authorization",key)
                }
            }

            code = httpURLConnection.responseCode
            var inputStream: InputStream? = null
            when(code){
                HttpURLConnection.HTTP_OK -> {
                    inputStream = httpURLConnection.inputStream
                    var response = inputStream!!.bufferedReader().use(BufferedReader::readText)
                    try {
                        Log.d(YoctuUtils.TAG_DEBUG,"response is ".plus(response))
                        data = YoctuMapper.getResponseChannel(response)
                        Log.d(YoctuUtils.TAG_DEBUG," : ".plus(data?.data?.size))
                    }catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d(YoctuUtils.TAG_DEBUG," error is ".plus(e.message))
                    }
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    inputStream = httpURLConnection.errorStream
                    error = inputStream!!.bufferedReader().use(BufferedReader::readText)
                    Log.d(YoctuUtils.TAG_DEBUG,"error is ".plus(error))
                }
            }

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            Log.e(YoctuUtils.TAG_ERROR,e.message)
            code = -5
        }catch (e: UnknownHostException) {
            code = -5
            e.printStackTrace()
            Log.d(YoctuUtils.TAG_DEBUG,e.message)
        }finally {
            httpURLConnection?.let { conn -> conn.disconnect() }
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        this.callback.getChannels(code, data, error)
    }

}