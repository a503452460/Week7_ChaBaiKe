package com.cbf.week7_chabaike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private Boolean isFirst;
    private TextView textView;
    private SharedPreferences mSharedPreferences;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mSharedPreferences = getSharedPreferences("firstIn_spf",0);
        isFirst = mSharedPreferences.getBoolean("isFirst",true);
        timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isFirst) {
                    Intent intent1 = new Intent(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(intent1);
                    SharedPreferences.Editor editor = SplashActivity.this.mSharedPreferences.edit();
                    editor.putBoolean("isFirst", false);
                    editor.commit();
                }else {
                    Intent intent2 = new Intent(SplashActivity.this,MainActivity.class);
                    SplashActivity.this.startActivity(intent2);
                }
            }
        },3000L);


    }
}
