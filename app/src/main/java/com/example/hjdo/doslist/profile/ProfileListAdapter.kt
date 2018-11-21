package com.example.hjdo.doslist.profile

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.hjdo.doslist.R
import com.example.hjdo.doslist.data.UserItem
import java.util.ArrayList

class ProfileListAdapter(private var context: Context, private var githubData: List<UserItem>?, private var isLinear: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterView.OnItemClickListener{
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val KEY_PREF_NUMBER = "userNumberOpen"

//    interface RecyclerViewClickListener {
//        fun onClick(view: View, position: Int)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(if(isLinear) R.layout.profile_item_row_layout_list else R.layout.profile_item_row_layout_grid, parent, false)
        return ProfileListViewHolder(v, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val view = holder as ProfileListViewHolder
        githubData?.get(position)?.let { view.onBind(it, position) }
    }

    override fun getItemCount(): Int {
        return if (githubData != null) githubData!!.size  else 0
    }
}