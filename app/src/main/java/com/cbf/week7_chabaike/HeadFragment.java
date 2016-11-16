package com.cbf.week7_chabaike;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cbf.week7_chabaike.asyncTask.MyByteAsyncTask;
import com.cbf.week7_chabaike.beans.Head;
import com.cbf.week7_chabaike.callback.MyBytesCallBack;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeadFragment extends Fragment {
    private List<Head.DataBean> data = new ArrayList<>();
    private ImageView image_head;
    private TextView textView_head;
    private String path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getSlideshow&rows=15&page=1";
    private String imagePath = "" ;
    private int page;


    public HeadFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_head, container, false);
        page = getArguments().getInt("index");
        initView(ret);
        initData();
        return ret;
    }

    private void initData() {
        new MyByteAsyncTask(new MyBytesCallBack() {
            @Override
            public void onCallBack(byte[] bytes) {
                Head head = JSON.parseObject(new String(bytes), Head.class);
                Head.DataBean dataBean = head.getData().get(page);
                imagePath = dataBean.getImage();
                textView_head.setText(dataBean.getTitle());
                new MyByteAsyncTask(new MyBytesCallBack() {
                    @Override
                    public void onCallBack(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        image_head.setImageBitmap(bitmap);

                    }
                },false).execute(imagePath);
            }
        },false).execute(path);
        Log.i("tag", "imagePath"+imagePath);


    }

    private void initView(View ret) {
        image_head = (ImageView) ret.findViewById(R.id.image_head);
        textView_head = (TextView) ret.findViewById(R.id.textView_head1);
    }

}
