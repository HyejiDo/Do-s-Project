package com.example.hjdo.doslist.profile

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.preference.PreferenceManager
import com.example.hjdo.doslist.data.UserItem
import com.example.hjdo.doslist.data.UserItemDetail
import com.example.hjdo.doslist.profiledetail.UserDetailActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class ProfileListPresenter(private val profileView: ProfileListContract.View)
    : ProfileListContract.Presenter {

    private var URL_STRING = "https://api.github.com/users"
    internal var value: Int = 0

    /**************************************************
     * Support methods
     */
    private val context: Context
        get() = profileView as Context

    private val activity: Activity
        get() = profileView as Activity

    override fun onLoadUserListTask() {
        var networkTask: NetworkTask? = null

        try {
            networkTask = NetworkTask(URL(URL_STRING + "?per_page=" + getSettingListCount()), null)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        networkTask!!.execute()
    }

    inner class NetworkTask(private val url: URL, private var values: ContentValues?) : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            values = null
            profileView.showLoadingDialog(true)
        }

        override fun doInBackground(vararg prams: Void): String? {
            var httpURLConnection: HttpURLConnection? = null

            try {
                httpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.requestMethod = "GET"
                val reader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                var line: String?
                val response = StringBuilder()
                while (true) {
                    line = reader.readLine() ?: break
                    response.append(line)
                }
                reader.close()

                httpURLConnection.disconnect()
                return response.toString()

            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            //Parsing
            val gson = Gson()
            val userListType = object : TypeToken<ArrayList<UserItem>>() { }.type
            val userList = gson.fromJson(s, userListType) as ArrayList<UserItem>

            profileView.setUserList(userList)
        }
    }

    override fun getUserDetailData(loginId: String) {
        var userDetailTask: UserDetailTask? = null
        try {
            userDetailTask = UserDetailTask(URL_STRING, loginId)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        userDetailTask!!.execute()
    }

    inner class UserDetailTask @Throws(MalformedURLException::class)
    constructor(url: String, private val loginId: String) : AsyncTask<Void, Void, String>() {

        private val url: URL

        init {
            this.url = URL("$url/$loginId")
        }

        override fun doInBackground(vararg prams: Void): String? {
            var httpURLConnection: HttpURLConnection? = null

            try {
                httpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.requestMethod = "GET"
                val reader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                var line: String
                val response = StringBuilder()
                line = reader.readLine()
                while ((line) != null) {
                    response.append(line)
                    line = reader.readLine()
                }
                reader.close()

                httpURLConnection.disconnect()
                return response.toString()

            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            val position = 0
            // Parsing
            val gson = Gson()
            val userItemDetail = gson.fromJson(s, UserItemDetail::class.java)
            profileView.showDetailActivity(userItemDetail)

        }
    }

    private fun getSettingListCount(): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(UserListActivity.KEY_PREF_NUMBER, "30")
    }
}