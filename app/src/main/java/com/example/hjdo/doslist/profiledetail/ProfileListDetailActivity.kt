package com.example.hjdo.doslist.profiledetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hjdo.doslist.R
import com.example.hjdo.doslist.data.UserItemDetail
import kotlinx.android.synthetic.main.activity_profile_list_detail.*

class ProfileListDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_list_detail)

        val intent = intent
        val userItemDetail = intent.getSerializableExtra("userItemDetail") as UserItemDetail

        Glide.with(this).load(userItemDetail.avatar_url).into(imageView_userImage_detail)
        textView_userName.text = userItemDetail.name
        textView_userLogin_detail.text = userItemDetail.login
        textView_userLocation.text = userItemDetail.location
        textView_email.text = userItemDetail.email
        textView_html_url.text = userItemDetail.html_url

        val address = userItemDetail.location.toString()
        val url = userItemDetail.html_url.toString()

        textView_userLocation.setOnClickListener {
            openMap(this@ProfileListDetailActivity, address)
        }

        textView_html_url.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    //Google Map 연동
    fun openMap(context: Context, address: String): Boolean {
        val uriBuilder = Uri.Builder().scheme("geo").path("0,0").appendQueryParameter("q", address)
        val intent = Intent(Intent.ACTION_VIEW, uriBuilder.build())
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            return true
        }
        return false
    }
}