package com.cbf.week7_chabaike;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbf.week7_chabaike.adapters.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView more,back,goHome,search;
    private EditText mEditText;
    private TextView collet,history;

    private List<Fragment> list = new ArrayList<>();
    private String[] mTitleArray = new String[]{"头条","百科","资讯","经营","数据"};

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initView();
        initClick();
        initViewPager();
        initTabLayout();
    }

    private void initClick() {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);

            }
        });
        collet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CollectActivity.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = mEditText.getText().toString().trim();
                if(search!=null&&!search.equals("")){
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("path",search);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,"无搜索条件", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViewPager() {
        for (int i = 0; i < mTitleArray.length; i++) {
            Fragment fragment = new FirstFragment();

            Bundle bundle = new Bundle();

            bundle.putInt("index",i);

            fragment.setArguments(bundle);

            list.add(fragment);
        }

        FragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),list,mTitleArray);
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
//        int length = mTitleList.length;
//        for (int i = 0; i < length; i++) {
//            TextView textView = new TextView(this);
//            textView.setTag(i);
//            textView.setText(mTitleList[i]);
//            textView.setOnClickListener(this);
//            LinearLayout.LayoutParams
//        }
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        more = (ImageView) findViewById(R.id.open_menu);
        back = (ImageView) findViewById(R.id.menu_back);
        goHome = (ImageView) findViewById(R.id.gohome);
        search = (ImageView) findViewById(R.id.image_search);
        mEditText = (EditText) findViewById(R.id.edit_search);
        collet = (TextView) findViewById(R.id.myshoucang);
        history = (TextView) findViewById(R.id.lishijilu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.openmenu);
    }

//    @Override
//    public void onClick(View v) {
//        int index = (int) v.getTag();
//        mViewPager.setCurrentItem(index);
//    }
}
