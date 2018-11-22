package com.example.hjdo.doslist.profile

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.hjdo.doslist.R
import com.example.hjdo.doslist.data.UserItem
import com.example.hjdo.doslist.data.UserItemDetail
import com.example.hjdo.doslist.profiledetail.ProfileListDetailActivity
import com.example.hjdo.doslist.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_profile_list.*

class ProfileListActivity : AppCompatActivity(), ProfileListContract.View {

    private lateinit var profileListPresenter: ProfileListContract.Presenter
    private lateinit var profileListAdapter: ProfileListAdapter
    private lateinit var dialog: ProgressDialog

    private var userList: List<UserItem> ? = null
    private lateinit var listener: ProfileListAdapter.OnItemClickListener
    private var isLinear = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_list)

        profileListPresenter = ProfileListPresenter(this)

        //setting에서 보여질 아이템수 변경
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false)

        //app bar
        findViewById<Toolbar>(R.id.my_toolbar).also {
            setSupportActionBar(it)
        }

        //리스트 불러올동안 dialog출력
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setMessage(getString(R.string.profile_list_loading))

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)


        listener = object : ProfileListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val loginId = userList!![position].login
                profileListPresenter.getUserDetailData(loginId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isOnline(this)) {
            showMessage()
        } else {
            profileListPresenter.onLoadUserListTask()
        }
    }

    override fun showLoadingDialog(showDialog: Boolean) {

        if(showDialog){
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    override fun showDetailActivity(userItemDetail: UserItemDetail) {
        val intent = Intent(applicationContext, ProfileListDetailActivity::class.java)
        intent.putExtra("userItemDetail", userItemDetail)
        startActivity(intent)
    }

    override fun setUserList(userList:List<UserItem>){
        this.userList = userList
        profileListAdapter = ProfileListAdapter(this, userList, listener, isLinear)
        recycler_view.adapter = profileListAdapter
        showLoadingDialog(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_product_change_view -> {
                isLinear = !isLinear
                supportInvalidateOptionsMenu()
                recycler_view.layoutManager = if (isLinear) LinearLayoutManager(this) else GridLayoutManager(this, 2)
                profileListAdapter = ProfileListAdapter(this, userList, listener, isLinear)
                recycler_view.adapter = profileListAdapter
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //network 연결 상태 확인
    private fun isOnline(context: Context): Boolean {
        try {
            val conMan = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = conMan.getNetworkInfo(1).state // wifi
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return true
            }
            val mobile = conMan.getNetworkInfo(0).state // mobile ConnectivityManager.TYPE_MOBILE
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                return true
            }
        } catch (e: NullPointerException) {
            return false
        }

        return false
    }

    private fun showMessage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("안내")
        builder.setMessage("네트워크 연결이 끊겼습니다.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setNeutralButton("확인") { dialog, which -> finish() }
        val dialog = builder.create()
        dialog.show()
    }
}