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
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_OK
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

/**
 * Created by gael on 18.05.18.
 */
class ChannelsAsynctaskHTTPS(val callback: CallbackChannelsResponse): AsyncTask<String, Void, String>() {

    private var code: Int = 200
    private var error: String? = null
    private var data: ResponseChannels? = null
    private var topiCURL: TopicURL? = null

    constructor(callback: CallbackChannelsResponse, obj: TopicURL): this(callback) {
        this.topiCURL = obj
    }

    override fun doInBackground(vararg params: String?): String {
        var httpsURLConnection: HttpsURLConnection? = null
        val currentURL = topiCURL?.url ?: params[0]
        try {
            val myURL = URL(params.get(0))
            Log.d(YoctuUtils.TAG_DEBUG,"${params[0]}")
            httpsURLConnection = myURL.openConnection() as HttpsURLConnection

            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.setRequestProperty("Content-Type", "application/json")
            httpsURLConnection.doInput = true
            topiCURL?.let { tu ->
                tu.apiKey?.let { key ->
                    if (!key.isEmpty())
                        httpsURLConnection.setRequestProperty("Authorization",key)
                }
            }

            code = httpsURLConnection.responseCode
            var inputStream: InputStream? = null
            when(code){
                HTTP_OK -> {
                    inputStream = httpsURLConnection.inputStream
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
                HTTP_NOT_FOUND -> {
                    inputStream = httpsURLConnection.errorStream
                    error = inputStream!!.bufferedReader().use(BufferedReader::readText)
                    Log.d(YoctuUtils.TAG_DEBUG,"error is ".plus(error))
                }
            }

        } catch (e: MalformedURLException) {
            code = -5
            e.printStackTrace()
            Log.d(YoctuUtils.TAG_DEBUG,e.message)
        }
        catch (e: UnknownHostException) {
            code = -5
            e.printStackTrace()
            Log.d(YoctuUtils.TAG_DEBUG,e.message)
        }finally {
            httpsURLConnection?.let { conns -> conns.disconnect() }
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        this.callback.getChannels(code, data, error)
    }

}