package com.cbf.week7_chabaike;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cbf.week7_chabaike.adapters.ListViewAdapter;
import com.cbf.week7_chabaike.beans.Tea;
import com.cbf.week7_chabaike.utils.CollcetSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private CollcetSQLiteOpenHelper mCollcetSQLiteOpenHelper = new CollcetSQLiteOpenHelper(this);
    private ListView mListView;
    private ImageView goBack,goHome;
    private BaseAdapter mAdapter;
    private List<Tea.DataBean> data = new ArrayList<>();
    private Cursor mCursor;
    private String path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent&id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        getSupportActionBar().hide();
        initView();
        initOnClick();
        initListView();
        initData();

    }

    private void initListView() {
        mAdapter = new ListViewAdapter(this,data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectActivity.this,WebActivity.class);
                String Web_Path =path+id;
                intent.putExtra("path",Web_Path);
                CollectActivity.this.startActivity(intent);
            }

        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
                builder.setIcon(R.mipmap.ic_logo);
                final String title = ((Tea.DataBean) mAdapter.getItem(position)).getTitle();
                builder.setTitle(title);
                View dialog = LayoutInflater.from(CollectActivity.this).inflate(R.layout.delete_or_no,null);
                final Button delete = (Button) dialog.findViewById(R.id.delete);
                Button no = (Button) dialog.findViewById(R.id.or_no);
                builder.setView(dialog);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CollectActivity.this);
                        builder.setIcon(R.mipmap.ic_logo);
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = mCollcetSQLiteOpenHelper.getReadableDatabase();
                                int delete_collect =db.delete(mCollcetSQLiteOpenHelper.getTableName(),"title=?",new String[]{title});
                                if(delete_collect!=0){
                                    initData();
                                    Toast.makeText(CollectActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(CollectActivity.this,"删除失败",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.create().show();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                return true;

            }
        });
    }

    private void initData() {
        data.clear();
        SQLiteDatabase db = mCollcetSQLiteOpenHelper.getReadableDatabase();
        mCursor = db.query(mCollcetSQLiteOpenHelper.getTableName(),new String[]{"*"},null,null,null,null,null);
        while (mCursor.moveToNext()){
            Tea.DataBean dataBean = new Tea.DataBean();
            dataBean.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
            dataBean.setSource(mCursor.getString(mCursor.getColumnIndex("source")));
            dataBean.setId(mCursor.getString(mCursor.getColumnIndex("id")));
            dataBean.setCreate_time(mCursor.getString(mCursor.getColumnIndex("create_time")));
            dataBean.setNickname(mCursor.getString(mCursor.getColumnIndex("author")));
            data.add(dataBean);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void initOnClick() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectActivity.this.onBackPressed();
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
    }
}
