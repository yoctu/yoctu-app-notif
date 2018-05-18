package com.yoctu.notif.android.yoctuappnotif.ui.topic.asynctask

import android.os.AsyncTask
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackChannelsResponse
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.mapper.YoctuMapper
import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
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

    override fun doInBackground(vararg params: String?): String {
        var httpURLConnection: HttpURLConnection? = null
        try {
            Log.d(YoctuUtils.TAG_DEBUG,params[0])
            val myURL = URL(params.get(0))
            httpURLConnection = myURL.openConnection() as HttpURLConnection

            httpURLConnection.requestMethod = "GET"
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.doInput = true

            code = httpURLConnection.responseCode
            var inputStream: InputStream? = null
            when(code){
                200 -> {
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
                404 -> {
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
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        this.callback.getChannels(code, data, error)
    }

}