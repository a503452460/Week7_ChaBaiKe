package com.cbf.week7_chabaike.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbf.week7_chabaike.R;
import com.cbf.week7_chabaike.asyncTask.MyByteAsyncTask;
import com.cbf.week7_chabaike.beans.Tea;
import com.cbf.week7_chabaike.callback.MyBytesCallBack;
import com.cbf.week7_chabaike.utils.MyLruCache;
import com.cbf.week7_chabaike.utils.SDcardUtils;

import java.io.File;
import java.util.List;

import static com.cbf.week7_chabaike.R.id.wap_thumb;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class ListViewAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<Tea.DataBean> data;
    private MyLruCache mMyLruCache;

    public ListViewAdapter(android.content.Context context, List<Tea.DataBean> data) {
        this.context = context;
        this.data = data;
        mMyLruCache = MyLruCache.obtMyLruCache((int) (Runtime.getRuntime().maxMemory()/8));
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(data.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        ViewHolder holder = null;
        if(convertView!=null){
            ret = convertView;
            holder = (ViewHolder) ret.getTag();
        }else{
            ret = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.create_time = (TextView) ret.findViewById(R.id.create_time);
            holder.nickname = (TextView) ret.findViewById(R.id.nickname);
            holder.source = (TextView) ret.findViewById(R.id.source);
            holder.title = (TextView) ret.findViewById(R.id.title);
            holder.wap_thumb= (ImageView) ret.findViewById(wap_thumb);
            ret.setTag(holder);
        }
        Tea.DataBean dataBean = data.get(position);
        holder.create_time.setText(dataBean.getCreate_time());
        holder.nickname.setText(dataBean.getNickname());
        holder.source.setText(dataBean.getSource());
        holder.title.setText(dataBean.getTitle());
        Bitmap bitmap = Bitmap.createBitmap(50,50, Bitmap.Config.ALPHA_8);
//        Log.i("tag2",dataBean.getWap_thumb());
        if(dataBean.getWap_thumb()!=null&&!dataBean.getWap_thumb().equals("")){
            holder.wap_thumb.setTag(dataBean.getWap_thumb());
            getBitmapToImage(holder.wap_thumb,dataBean.getWap_thumb());
//            final String imagePath = dataBean.getWap_thumb();
//            holder.wap_thumb.setImageBitmap(bitmap);
//            holder.wap_thumb.setTag(imagePath);
//            String fileName = imagePath.substring(imagePath.lastIndexOf("/")+1);
//            final ViewHolder finalHolder = holder;
//            new MyByteAsyncTask(new MyBytesCallBack() {
//                @Override
//                public void onCallBack(byte[] bytes) {
//                    if(bytes!=null) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                        String tag = (String) finalHolder.wap_thumb.getTag();
//                        if (bitmap != null && imagePath.equals(tag)) {
//                            finalHolder.wap_thumb.setImageBitmap(bitmap);
//                        }
//                    }else{
//                        finalHolder.wap_thumb.setImageResource(R.mipmap.ic_launcher);
//                    }
//                }
//            }, Environment.DIRECTORY_PICTURES,null,fileName,2,null).execute(imagePath);
        }

        return ret;
    }

    private void getBitmapToImage(final ImageView imageView, String wap_thumb1) {
        final String fileName = wap_thumb1.substring(wap_thumb1.lastIndexOf("/")+1);
        String filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+
                File.separator+fileName;
        Bitmap bitmap = mMyLruCache.get(fileName);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else if(SDcardUtils.fileIsExists(filePath)){
            byte[] bytes = SDcardUtils.pickbyteFromSDCard(filePath);
            if(bytes!=null&&bytes.length!=0){
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                mMyLruCache.put(fileName,bitmap1);
                imageView.setImageBitmap(bitmap1);
            }
        }else if(imageView.getTag().equals(wap_thumb1)){
            new MyByteAsyncTask(new MyBytesCallBack() {
                @Override
                public void onCallBack(byte[] bytes) {
                    if(bytes!=null&&bytes.length!=0){
                        Bitmap bitmap2 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        mMyLruCache.put(fileName,bitmap2);
                        imageView.setImageBitmap(bitmap2);
                    }
                }
            },Environment.DIRECTORY_PICTURES,null,fileName,MyByteAsyncTask.TYPE_FLIE,context).execute(wap_thumb1);
        }
    }

    private class ViewHolder{
        private TextView title,source,nickname,create_time;
        private ImageView wap_thumb;
    }
}
