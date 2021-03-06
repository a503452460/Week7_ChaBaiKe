package com.cbf.week7_chabaike;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cbf.week7_chabaike.adapters.ListViewAdapter;
import com.cbf.week7_chabaike.adapters.MyFragmentStatePagerAdapter;
import com.cbf.week7_chabaike.asyncTask.MyByteAsyncTask;
import com.cbf.week7_chabaike.beans.Tea;
import com.cbf.week7_chabaike.callback.MyBytesCallBack;
import com.cbf.week7_chabaike.utils.InternetUtils;
import com.cbf.week7_chabaike.utils.SDcardUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    private int currentPositon = 1;
    private BaseAdapter adapter;
    private LinearLayout more;
    private PullToRefreshListView mPullToRefreshListView;
    private TextView loadMore;
    private String path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getHeadlines&rows=15&page=1";
    private int page = 1;
    private boolean isBottom;
    private ListView listView;
    private ImageView backTop;
    private List<Tea.DataBean> data = new ArrayList<>();
    private List<Fragment> list = new ArrayList<>();
    private FragmentStatePagerAdapter headAdapter;
    private ViewPager viewPager_head;
    private String Web_path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent&id=";
    private int index;
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 911:
                    currentPositon++;
                    if(currentPositon>2){
                        currentPositon=0;
                    }
                    viewPager_head.setCurrentItem(currentPositon,false);
                    this.sendEmptyMessageDelayed(911,2000);
                    break;
            }

        }
    };
    private View mHeadView;
    private String mFileName;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListViewAdapter(getContext(),data);
        Bundle bundle = getArguments();
        index = bundle.getInt("index");
        switch (index){
            case 0:
                initHeadView();
                break;
            case 1:
                path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=16&rows=15&page=1";
                break;
            case 2:
                path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=52&rows=15&page=1";

                break;
            case 3:
                path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=53&rows=15&page=1";
                break;
            case 4:
                path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=54&rows=15&page=1";
                break;
        }
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View ret = inflater.inflate(R.layout.fragment_first, container, false);
        initView(ret);
//        listView = (ListView) ret.findViewById(R.id.listView);

//        Bundle bundle = getArguments();
//        index = bundle.getInt("index");
//        switch (index){
//            case 0:
//                initHeadView();
//                break;
//            case 1:
//                path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=16&rows=15&page=1";
//                break;
//            case 2:
//                path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=52&rows=15&page=1";
//
//                break;
//            case 3:
//                path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=53&rows=15&page=1";
//                break;
//            case 4:
//                path ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=54&rows=15&page=1";
//                break;
//        }
        if(index==0){
            listView.addHeaderView(mHeadView);
        }


//        initListView();
        initLoadMore();
        initPullToRefreshListView();



        initListViewScrollListener();
        initTopListener();



        return ret;
    }

    private void initView(View ret) {
        loadMore = (TextView) ret.findViewById(R.id.loadMore);
        more = (LinearLayout) ret.findViewById(R.id.more);
        backTop = (ImageView) ret.findViewById(R.id.backTop);
        mPullToRefreshListView = (PullToRefreshListView) ret.findViewById(R.id.pullToRefreshListView);
        listView = mPullToRefreshListView.getRefreshableView();
    }

    private void initHeadView() {

        mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.head,null);
        viewPager_head = (ViewPager) mHeadView.findViewById(R.id.viewPager_head);
//        View ret = LayoutInflater.from(getContext()).inflate(R.layout.fragment_head, null);
        initViewPager_head();
//        initIndicator(ret);
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
        mHandle.sendEmptyMessageDelayed(911,0);
//            }
//        },0,2000);


    }
//    private View[] indicators = new View[3];
//    private  int lastSelected =0;
//    private void initIndicator(View ret) {
//        LinearLayout container = (LinearLayout) ret.findViewById(R.id.container);
//        Log.i("tag1", "initIndicator: "+container.getChildAt(1));
//        for (int i = 1; i < container.getChildCount(); i++) {
//            indicators[i-1] = container.getChildAt(i);
//
//            Log.i("tag", "initIndicator: "+container);
//
//            indicators[i-1].setEnabled(true);
//            indicators[i-1].setTag(i-1);
//            indicators[i-1].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index = (int) v.getTag();
//                    viewPager_head.setCurrentItem(index,true);
//                }
//            });
//        }
//        indicators[0].setEnabled(false);
//    }

    private void initViewPager_head() {

        Fragment fragment = null;
        for (int i = 0; i < 3; i++) {
            fragment= new HeadFragment();
            Bundle bundle = new Bundle();

            bundle.putInt("index",i);

            fragment.setArguments(bundle);

            list.add(fragment);
        }
        viewPager_head.setOffscreenPageLimit(2);
        headAdapter = new MyFragmentStatePagerAdapter(getFragmentManager(),list);
        viewPager_head.setAdapter(headAdapter);
        viewPager_head.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((MainActivity) getContext()).getViewPager().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
//        viewPager_head.setCurrentItem(1);
//        viewPager_head.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                indicators[position].setEnabled(false);
//                indicators[lastSelected].setEnabled(true);
//                lastSelected = position;
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    private void initPullToRefreshListView() {
//        adapter = new ListViewAdapter(getContext(),data);
        mPullToRefreshListView.setAdapter(adapter);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
                if(mPullToRefreshListView.isRefreshing()){
                    mPullToRefreshListView.onRefreshComplete();;
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
                if(mPullToRefreshListView.isRefreshing()){
                    mPullToRefreshListView.onRefreshComplete();;
                }
            }
        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getContext(),WebActivity.class);
                String path = Web_path+id;
                intent.putExtra("path",path);
                getContext().startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("是否删除?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int width = getContext().getResources().getDisplayMetrics().widthPixels;
                        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                                Animation.RELATIVE_TO_SELF,-1,Animation.RELATIVE_TO_SELF,0
                        ,Animation.RELATIVE_TO_SELF,0);
                        translate.setDuration(2000);
                        view.startAnimation(translate);
                        translate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (index==0){
                                    data.remove(position-2);
                                }else{
                                    data.remove(position-1);
                                }
                                adapter.notifyDataSetChanged();
                                int count = listView.getChildCount();
                                AnimationSet set = new AnimationSet(true);
                                AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                                alphaAnimation.setDuration(1000);

                                ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                                        Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                                scaleAnimation.setDuration(1000);
                                set.addAnimation(alphaAnimation);
                                set.addAnimation(scaleAnimation);
                                int curentTop = view.getTop();
                                for(int i=0;i<count;i++){
                                    View itemView = listView.getChildAt(i);
                                    if(itemView.getTop()>=curentTop){
                                        itemView.startAnimation(set);
                                    }
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
//                        if (index==0){
//                            data.remove(position-2);
//                        }else{
//                            data.remove(position-1);
//                        }



                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                return true;
            }

        });
    }

    private void initTopListener() {
        backTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(0);
            }
        });
    }

    private void initListViewScrollListener() {
        mPullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        if(isBottom) {
                            more.setVisibility(View.VISIBLE);
                            loadMoreData();
                        }else{
                            more.setVisibility(View.GONE);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottom = firstVisibleItem+visibleItemCount==totalItemCount;
            }
        });
    }

    private void initLoadMore() {
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreData();
            }
        });

    }

    private void loadMoreData() {
        page++;
        String path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getHeadlines&rows=15&page="+page;
//        new MyByteAsyncTask(new MyBytesCallBack() {
//            @Override
//            public void onCallBack(byte[] bytes) {
//                Tea tea = JSON.parseObject(new String(bytes),Tea.class);
//                data.addAll(tea.getData());
//                adapter.notifyDataSetChanged();
//            }
//        },false).execute(path);
        initData();
    }

    private void initListView() {
        adapter = new ListViewAdapter(getContext(),data);
        listView.setAdapter(adapter);
    }

    private void initData() {
//        String root = getContext().getExternalFilesDir(null).getAbsolutePath();
        mFileName = "Tea"+index+"page"+page;
        if(InternetUtils.isConnected(getContext())){

            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {

                    Tea tea = JSON.parseObject(new String(bytes),Tea.class);
                    data.addAll(tea.getData());
                    adapter.notifyDataSetChanged();
                }
            },null,null, mFileName,MyByteAsyncTask.TYPE_CHACHE,getContext()).execute(path);
        }else {
            String filePath = getContext().getExternalCacheDir().getAbsolutePath()+File.separator+mFileName;
            byte[] bytes =SDcardUtils.pickbyteFromSDCard(filePath);
            Tea tea = JSON.parseObject(new String(bytes),Tea.class);
            data.addAll(tea.getData());
            adapter.notifyDataSetChanged();
        }
    }

}
