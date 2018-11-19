package com.example.hjdo.doslist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.net.MalformedURLException;

import jp.wasabeef.glide.transformations.CropTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.hjdo.doslist.R.id.textView_userLocation;
import static com.example.hjdo.doslist.R.id.textView_userName;

public class UserDetailActivity extends AppCompatActivity{

    ImageView image;
    TextView name;
    TextView login;
    TextView location;
    TextView email;
    TextView html_url;

    String address="";
    String url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);

        Intent intent = getIntent();
        UserItemDetail userItemDetail = (UserItemDetail) intent.getSerializableExtra("userItemDetail");

        image = (ImageView) findViewById(R.id.imageView_userImage_detail);
        name = (TextView) findViewById(R.id.textView_userName);
        login = (TextView) findViewById(R.id.textView_userLogin_detail);
        location = (TextView) findViewById(R.id.textView_userLocation);
        email = (TextView) findViewById(R.id.textView_email);
        html_url = (TextView) findViewById(R.id.textView_html_url);

        Glide.with(this).load(userItemDetail.getAvatar_url()).into(image);
        name.setText(userItemDetail.getName());
        login.setText(userItemDetail.getLogin());
        location.setText(userItemDetail.getLocation());
        email.setText(userItemDetail.getEmail());
        html_url.setText(userItemDetail.getHtml_url());

        address = userItemDetail.getLocation().toString();
        url = userItemDetail.getHtml_url().toString();

        //TODO location Google Map 연동
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(UserDetailActivity.this, address);
            }
        });

        //TODO URL 연동
        html_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    //TODO Google Map 연동
    public static boolean openMap(Context context, String address) {
        Uri.Builder uriBuilder = new Uri.Builder().scheme("geo").path("0,0").appendQueryParameter("q", address);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
