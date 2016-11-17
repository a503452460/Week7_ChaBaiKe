package com.cbf.week7_chabaike;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cbf.week7_chabaike.asyncTask.MyByteAsyncTask;
import com.cbf.week7_chabaike.beans.Web;
import com.cbf.week7_chabaike.callback.MyBytesCallBack;
import com.cbf.week7_chabaike.utils.HistorySQLiteHelper;
import com.cbf.week7_chabaike.utils.InternetUtils;
import com.cbf.week7_chabaike.utils.CollcetSQLiteOpenHelper;
import com.cbf.week7_chabaike.utils.SDcardUtils;

import java.io.File;

public class WebActivity extends AppCompatActivity {
    private TextView title,date;
    private String path;
    private WebView mWebView;

    private Web mWeb;
    private CollcetSQLiteOpenHelper mCollcetSQLiteOpenHelper;
    private HistorySQLiteHelper mHistorySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        mCollcetSQLiteOpenHelper = new CollcetSQLiteOpenHelper(this);
        mHistorySQLiteHelper = new HistorySQLiteHelper(this);
        initView();
        initWebView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        String fileName = path.substring(path.lastIndexOf("/")+1);
        if(InternetUtils.isConnected(this)){
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    loadData(bytes);
                }
            },null,null,fileName,MyByteAsyncTask.TYPE_CHACHE,this).execute(path);
        }else {
            String filePath = getExternalCacheDir().getAbsolutePath()+ File.separator+fileName;
            if(SDcardUtils.fileIsExists(filePath)){
                byte[] bytes = SDcardUtils.pickbyteFromSDCard(filePath);
                loadData(bytes);

            }
        }

    }

    private void loadData(byte[] bytes) {
        if(bytes!=null){

//            JSONObject jsonObject = new JSONObject(new String(bytes))
            mWeb = JSON.parseObject(new String(bytes), Web.class);
            title.setText(mWeb.getData().getTitle());
            date.setText("时间："+mWeb.getData().getCreate_time()+"来源："+mWeb.getData().getSource());
            mWebView.loadDataWithBaseURL(path,mWeb.getData().getWap_content(),"text/html","utf-8",null);
            SQLiteDatabase db = mHistorySQLiteHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            Web.DataBean data = mWeb.getData();
            values.put("title", data.getTitle());
            values.put("id",data.getId());
            values.put("create_time",data.getCreate_time());
            values.put("source",data.getSource());
            values.put("author",data.getAuthor());
            values.put("weiboUrl",data.getWeiboUrl());
            db.insert(mHistorySQLiteHelper.getTableName(), null, values);
        }
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClient());
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        mWebView = (WebView) findViewById(R.id.webview);
    }
    public void click(View view){
        switch (view.getId()){
            case R.id.back:
                this.onBackPressed();
                break;
            case R.id.share:
                break;
            case R.id.collect:
                long insert = 0;
                if(mWeb!=null){
                    SQLiteDatabase db = mCollcetSQLiteOpenHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    Web.DataBean data = mWeb.getData();
                    values.put("title",data.getTitle());
                    values.put("id",data.getId());
                    values.put("create_time",data.getCreate_time());
                    values.put("source",data.getSource());
                    values.put("author",data.getAuthor());
                    values.put("weiboUrl",data.getWeiboUrl());
                    insert = db.insert(mCollcetSQLiteOpenHelper.getTableName(),null,values);
                }if(insert ==0){
                    Toast.makeText(this,"收藏失败",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
