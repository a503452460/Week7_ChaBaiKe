package com.cbf.week7_chabaike;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cbf.week7_chabaike.asyncTask.MyByteAsyncTask;
import com.cbf.week7_chabaike.beans.Head;
import com.cbf.week7_chabaike.callback.MyBytesCallBack;
import com.cbf.week7_chabaike.utils.InternetUtils;
import com.cbf.week7_chabaike.utils.MyLruCache;
import com.cbf.week7_chabaike.utils.SDcardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;


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
    private MyLruCache mMyLruCache;


    public HeadFragment() {
        // Required empty public constructor
        mMyLruCache = MyLruCache.obtMyLruCache((int) (Runtime.getRuntime().maxMemory()/8));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_head, container, false);
        LinearLayout indicator_container = (LinearLayout) ret.findViewById(R.id.container);
        for (int i = 1; i < indicator_container.getChildCount(); i++) {
            indicator_container.getChildAt(i).setEnabled(false);
        }
        page = getArguments().getInt("index");

        indicator_container.getChildAt(page+1).setEnabled(true);


        initView(ret);
        initData();
        return ret;
    }

    private void initData() {
//        new MyByteAsyncTask(new MyBytesCallBack() {
//            @Override
//            public void onCallBack(byte[] bytes) {
//                Head head = JSON.parseObject(new String(bytes), Head.class);
//                Head.DataBean dataBean = head.getData().get(page);
//                imagePath = dataBean.getImage();
//                textView_head.setText(dataBean.getTitle());
//                new MyByteAsyncTask(new MyBytesCallBack() {
//                    @Override
//                    public void onCallBack(byte[] bytes) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                        image_head.setImageBitmap(bitmap);
//
//                    }
//                },false).execute(imagePath);
//            }
//        },false).execute(path);
        String head_fileName = "head"+page;
        if(InternetUtils.isConnected(getContext())) {
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    Head head = parseObject(new String(bytes), Head.class);
                    Head.DataBean dataBean = head.getData().get(page);
                    imagePath = dataBean.getImage();
                    textView_head.setText(dataBean.getTitle());
                    getBitmapToImage(image_head,imagePath);

//                    new MyByteAsyncTask(new MyBytesCallBack() {
//                        @Override
//                        public void onCallBack(byte[] bytes) {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            image_head.setImageBitmap(bitmap);
//
//
//                        }
//                    }, false).execute(imagePath);
                }
            }, null,null,head_fileName,3,getContext()).execute(path);

        }else {
            String path =getContext().getExternalCacheDir().getAbsolutePath()+File.separator+head_fileName;
            byte[] bytes = SDcardUtils.pickbyteFromSDCard(path);
            Head head = JSON.parseObject(new String(bytes),Head.class);
            Head.DataBean dataBean = head.getData().get(page);
            textView_head.setText(dataBean.getTitle());
            imagePath = dataBean.getImage();
            getBitmapToImage(image_head,imagePath);

        }
    }

    private void getBitmapToImage(final ImageView image_head, String imagePath) {
        final String fileName = imagePath.substring(imagePath.lastIndexOf("/")+1);
        String filePath = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ File.separator+fileName;
        Bitmap bitmap = mMyLruCache.get(fileName);
        if(bitmap!=null){
            image_head.setImageBitmap(bitmap);
        }else if (SDcardUtils.fileIsExists(filePath)){
            byte[] bytes =SDcardUtils.pickbyteFromSDCard(filePath);
            if(bytes!=null&&bytes.length!=0) {
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image_head.setImageBitmap(bitmap1);
                mMyLruCache.put(fileName, bitmap1);
            }
        }else {
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    image_head.setImageBitmap(bitmap2);
                    mMyLruCache.put(fileName,bitmap2);

                }
            },Environment.DIRECTORY_PICTURES,null,fileName,4,getContext()).execute(imagePath);
        }

    }

    private void initView(View ret) {
        image_head = (ImageView) ret.findViewById(R.id.image_head);
        textView_head = (TextView) ret.findViewById(R.id.textView_head1);
    }

}
