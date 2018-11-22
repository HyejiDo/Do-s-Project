package com.example.hjdo.doslist.profile

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.hjdo.doslist.R
import com.example.hjdo.doslist.data.UserItem


class ProfileListViewHolder(val view: View) : RecyclerView.ViewHolder(view){

    private val layoutList by lazy { view.findViewById<LinearLayout>(R.id.layoutList) }
    private val tvUserLogin by lazy { view.findViewById<TextView>(R.id.textView_userLogin) }
    private val tvUserType by lazy { view.findViewById<TextView>(R.id.textView_userType) }
    private val tvUserImage by lazy { view.findViewById<ImageView>(R.id.imageView_userImage) }

    fun onBind(data: UserItem, position: Int, listener: ProfileListAdapter.OnItemClickListener){
        tvUserLogin.text = data.login
        tvUserType.text = data.type
        Glide.with(view).load(data.avatar_url).apply(bitmapTransform(CircleCrop())).into(tvUserImage)

        view.setOnClickListener {
            listener.onItemClick(position)
        }
    }
}