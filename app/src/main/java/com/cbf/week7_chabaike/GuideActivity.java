package com.cbf.week7_chabaike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cbf.week7_chabaike.adapters.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ImageView[] icons;
    private ViewPager mViewPager;
    private List<View> list = new ArrayList<>();
    private Button go;

    private int[] images = { R.mipmap.slide1, R.mipmap.slide2, R.mipmap.slide3 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();
        initView();

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        go = (Button) findViewById(R.id.go);
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView.setImageResource(this.getResources().getIdentifier("slide"+(i+1),"mipmap",getPackageName()));
            this.list.add(imageView);
            mViewPager.setAdapter(new MyPagerAdapter(this.list,this));
//            initIcons();
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position==2){
                        go.setVisibility(View.VISIBLE);
//                        list.get(position).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
//                                GuideActivity.this.startActivity(intent);
//                                GuideActivity.this.finish();
//                            }
//                        });
                        go.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                                GuideActivity.this.startActivity(intent);
                                GuideActivity.this.finish();
                            }
                        });
                    }
                    else{
                        go.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void initIcons() {
        icons = new ImageView[this.images.length];
        for (int i = 0; i < images.length; i++) {

        }
    }
}
