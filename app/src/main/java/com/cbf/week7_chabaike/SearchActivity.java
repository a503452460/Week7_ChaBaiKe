package com.cbf.week7_chabaike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cbf.week7_chabaike.adapters.ListViewAdapter;
import com.cbf.week7_chabaike.asyncTask.MyByteAsyncTask;
import com.cbf.week7_chabaike.beans.Tea;
import com.cbf.week7_chabaike.callback.MyBytesCallBack;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ListView mListView;
    private ImageView goBack, goHome;
    private TextView search_name;
    private List<Tea.DataBean> data = new ArrayList<>();
    private BaseAdapter mListViewAdapter;
    private String SEARCH_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.searcListByTitle&search=";
    private String Web_path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent&id=";
    private String mString;
    private String mSearchPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        Intent intent = getIntent();
        mString = intent.getStringExtra("path");
        mSearchPath = SEARCH_URL+mString;
        initOnclick();
        initListView();
        initData();
    }

    private void initData() {
        search_name.setText(mString);
        new MyByteAsyncTask(new MyBytesCallBack() {
            @Override
            public void onCallBack(byte[] bytes) {
                Tea tea = JSON.parseObject(new String(bytes),Tea.class);
                data.addAll(tea.getData());
                mListViewAdapter.notifyDataSetChanged();
            }
        },false).execute(mSearchPath);
    }

    private void initListView() {
        mListViewAdapter = new ListViewAdapter(this,data);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this,WebActivity.class);
                String path = Web_path+id;
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });

    }

    private void initOnclick() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        goBack = (ImageView) findViewById(R.id.collect_back);
        goHome = (ImageView) findViewById(R.id.gohome);
        search_name = (TextView) findViewById(R.id.search_name);
    }
}
