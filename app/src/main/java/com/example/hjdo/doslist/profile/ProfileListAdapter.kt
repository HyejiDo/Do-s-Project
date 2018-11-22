package com.example.hjdo.doslist.profile

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.hjdo.doslist.R
import com.example.hjdo.doslist.data.UserItem


class ProfileListAdapter(private var context: Context, private var githubData: List<UserItem>?, private var listener:OnItemClickListener, private var isLinear: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(if(isLinear) R.layout.profile_item_row_layout_list else R.layout.profile_item_row_layout_grid, parent, false)
        return ProfileListViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val view = holder as ProfileListViewHolder
        githubData?.get(position)?.let { view.onBind(it, position, listener) }
    }

    override fun getItemCount(): Int {
        return if (githubData != null) githubData!!.size  else 0
    }
}