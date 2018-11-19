package com.example.hjdo.doslist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.hjdo.doslist.UserListAdapter.RecyclerViewClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    String URL_STRING = "https://api.github.com/users";

    RecyclerView recyclerView;
    UserListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<UserItem> userList = new ArrayList<UserItem>();
    RecyclerViewClickListener listener;

    ProgressDialog dialog;
    int value;

    public static final String KEY_PREF_NUMBER = "userNumberOpen";
    private boolean isLinear = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        //TODO setting에서 보여질 아이템수 변경
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);

        //TODO app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // 메인 액티비티로부터 전달받은 인텐트를 확인합니다.
        Intent intent = getIntent();

        //TODO 리스트 불러올동안 dialog출력
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("잠시만 기다려주세요");

        //TODO recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //TODO click 하면 user detail로 이동
        listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                String loginId = userList.get(position).getLogin();
                UserDetailTask userDetailTask = null;
                try {
                    userDetailTask = new UserDetailTask(URL_STRING, loginId);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                userDetailTask.execute();
            }
        };
    }

    protected void onResume() {
        super.onResume();
        if(isOnline(this)==false){
            showMessage();
        }else{
            onLoadUserListTask();
        }

//        task = new WaitTask();
//        task.execute(100);
    }

    //TODO setting에서 보여질 아이템수 변경
    private String getSettingListCount() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(UserListActivity.KEY_PREF_NUMBER, "30");
    }

    private void onLoadUserListTask(){
        NetworkTask networkTask = null;

        try {
            networkTask = new NetworkTask(new URL(URL_STRING+"?per_page="+getSettingListCount()), null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        networkTask.execute();
    }

    //TODO 리스트 불러올동안 dialog출력
//    class WaitTask extends AsyncTask<Integer, Integer, Integer>{
//        protected void onPreExecute() {
//            value = 0;
//            dialog.show();
//        }
//
//        protected Integer doInBackground(Integer ... values) {
//            while (isCancelled() == false) {
//                value++;
//                if (value >= 10) {
//                    break;
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {}
//            }
//            return value;
//        }
//
//        protected void onPostExecute(Integer result) {
//            dialog.dismiss();
//        }
//
//        protected void onCancelled() {
//            dialog.dismiss();
//        }
//    }

    //TODO https url 연결해서 recyclerview로 보여주기
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private URL url;
        private ContentValues values;

        public NetworkTask(URL url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        protected void onPreExecute() {
            value = 0;
            dialog.show();
        }
        @Override
        protected String doInBackground(Void... prams) {
            HttpURLConnection httpURLConnection = null;

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                httpURLConnection.disconnect();
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            int position = 0;
            // TODO Parsing
            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<UserItem>>() {}.getType();
            userList = gson.fromJson(s, userListType);

            adapter = new UserListAdapter(UserListActivity.this, userList, listener, isLinear);
            recyclerView.setAdapter(adapter);
            dialog.dismiss();
        }
    }

    //TODO https url+loginID 연결해서 user Detail information 보여주기
    public class UserDetailTask extends AsyncTask<Void, Void, String> {

        private URL url;
        private String loginId;

        public UserDetailTask(String url, String loginId) throws MalformedURLException {
            this.loginId = loginId;
            this.url = new URL(url+"/"+loginId);
        }

        @Override
        protected String doInBackground(Void... prams) {
            HttpURLConnection httpURLConnection = null;

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                httpURLConnection.disconnect();
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            int position = 0;
            // TODO Parsing
            Gson gson = new Gson();

            UserItemDetail userItemDetail = gson.fromJson(s, UserItemDetail.class);
            Intent intent = new Intent(getApplicationContext(), UserDetailActivity.class);
            intent.putExtra("userItemDetail", userItemDetail);
            startActivity(intent);

        }
    }

    // TODO App Bar - setting(login info, show item number), change view type(list/grid)
    @Override

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent SettingActivity = new Intent(this, SettingsActivity.class);
                startActivity(SettingActivity);
                return true;
            case R.id.menu_product_change_view:
                isLinear = !isLinear;
                supportInvalidateOptionsMenu();
                recyclerView.setLayoutManager(isLinear ? new LinearLayoutManager(this) : new GridLayoutManager(this,2));
                adapter = new UserListAdapter(UserListActivity.this, userList, listener, isLinear);
                recyclerView.setAdapter(adapter);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    // TODO network 연결 상태 확인
    private static boolean isOnline(Context context) {
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState(); // wifi
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return true;
            }
            NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState(); // mobile ConnectivityManager.TYPE_MOBILE
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    private void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("네트워크 연결이 끊겼습니다.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
