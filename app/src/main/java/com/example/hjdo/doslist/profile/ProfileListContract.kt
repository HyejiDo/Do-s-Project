package com.example.hjdo.doslist.profile

import com.example.hjdo.doslist.BasePresenter
import com.example.hjdo.doslist.BaseView
import com.example.hjdo.doslist.data.UserItem
import com.example.hjdo.doslist.data.UserItemDetail

interface ProfileListContract {
    interface View: BaseView<Presenter> {
        fun showLoadingDialog(showDialog: Boolean)
        fun setUserList(list: List<UserItem>)
        fun showDetailActivity(userItemDetail: UserItemDetail)
    }
    interface Presenter: BasePresenter {
        fun onLoadUserListTask()
        fun getUserDetailData(loginId: String)
    }
}