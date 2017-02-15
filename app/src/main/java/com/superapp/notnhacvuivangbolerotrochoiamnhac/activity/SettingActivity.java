package com.superapp.notnhacvuivangbolerotrochoiamnhac.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.superapp.notnhacvuivangbolerotrochoiamnhac.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onRateClick(View v){
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
//        uri = Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.superapp.guessthepresidentushistoryquiztrivia");
        //uri=Uri.parse("http://appvn.com/android/details?id=com.superapp.guessthepresidentushistoryquiztrivia");
        uri=Uri.parse("http://play.google.com/store/apps/details?id=com.superapp.guessthepresidentushistoryquiztrivia");
        // uri = Uri.parse("http://apps.samsung.com/mercury/topApps/topAppsDetail.as?productId=000000758087");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }
    public void onLikeUsClick(View v){
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
        uri = Uri.parse("https://www.facebook.com/supercoolappteam");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }
    public void onAboutUsClick(View v){
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
        uri = Uri.parse("http://www.bestappsforphone.com");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }
    public void onHotGameClick(View v){
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
        //uri=Uri.parse("http://www.bestappsforphone.com/appotagameofthemonth");
//        uri =Uri.parse("http://www.bestappsforphone.com/kindlegameofthemonth");
        uri=Uri.parse("http://www.bestappsforphone.com/gameofthemonth");
        //   uri = Uri.parse("http://www.bestappsforphone.com/samsunggameofthemonth");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

}
