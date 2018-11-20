package com.example.hjdo.doslist.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hjdo.doslist.R;
import com.example.hjdo.doslist.profile.UserListActivity;

public class LoginActivity extends AppCompatActivity{

    private String id = "hjdo@konai.com";
    private String pw = "1234";

    EditText emailText;
    EditText passwdText;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO Splash-기존테마로 되돌리기
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.emailEditText);
        passwdText = (EditText) findViewById(R.id.passwdEditText);
//        emailText.setText("hjdo@konai.com");
//        passwdText.setText("1234");

        emailText.addTextChangedListener(textWatcher);
        passwdText.addTextChangedListener(textWatcher);

        button = (Button)findViewById(R.id.loginButton);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(button.isEnabled()){
                    if(emailText.getText().toString().equals(id)){
                        if(passwdText.getText().toString().equals(pw)){
                            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, R.string.login_main_toast_message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //TODO Button Selector - textWatcher, editText에 값이 둘다 들어오면 true
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>0){
                button.setEnabled(true);
            }else{
                button.setEnabled(false);
            }
            setEnable(setButtonEnable(emailText,passwdText));
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void setEnable(boolean isEnable){
        button.setEnabled(isEnable);
    }

    private boolean setButtonEnable (EditText editText, EditText editText2){
        boolean isAllFull = false;

        if(editText != null && editText.length()>0 && editText2 != null && editText2.length()>0){
            isAllFull = true;
        }
        return isAllFull;
    }
}
