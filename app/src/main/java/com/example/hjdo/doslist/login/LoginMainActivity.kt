package com.example.hjdo.doslist.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.hjdo.doslist.Constants
import com.example.hjdo.doslist.R
import com.example.hjdo.doslist.profile.ProfileListActivity
import kotlinx.android.synthetic.main.activity_login_main.*

class LoginMainActivity: AppCompatActivity() {

    val id: String = Constants.LOGIN_ID
    val pw: String = Constants.LOGIN_PW

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        passwdEditText.addTextChangedListener(textWatcher)
        loginButton.isEnabled = false
        loginButton.setOnClickListener {
            checkUser()
        }


        emailEditText.setText(Constants.LOGIN_ID)
        passwdEditText.setText(Constants.LOGIN_PW)
    }

    private fun checkUser(){
        if (loginButton.isEnabled) {
            if (emailEditText.text.toString() == id) {
                if (passwdEditText.text.toString() == pw) {
                    val intent = Intent(applicationContext, ProfileListActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this@LoginMainActivity, R.string.login_main_toast_message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable) {
            if(emailEditText.toString().isNotEmpty()){
                loginButton.isEnabled = s.toString().isNotEmpty()
            } else {
                loginButton.isEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

    }

}