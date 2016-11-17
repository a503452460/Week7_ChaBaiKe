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
    private int num =3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        textView = (TextView) findViewById(R.id.textView);
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
                    SplashActivity.this.finish();
                }else {
                    Intent intent2 = new Intent(SplashActivity.this,MainActivity.class);
                    SplashActivity.this.startActivity(intent2);
                    SplashActivity.this.finish();
                }
            }
        },3000L);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        num--;
                        textView.setText(num+"秒后自动进入");
                        if(num<1){
                            timer.cancel();
                        }
                    }
                });

            }
        },1000L,1000L);


    }
}
