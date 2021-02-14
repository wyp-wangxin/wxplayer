package com.wyp.wxplayer.fragment.localpage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.wxplayer.bean.LocalVideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2021/2/9.
 */

public class LocalPresenter implements LocalMvp.Presenter {

    private LocalMvp.View mView;
    private Context mContext;
    private List<LocalVideoBean> mLocalVideoBeans;
    public LocalPresenter( Context context, LocalMvp.View view1) {
        mView = view1;
        mContext=context;
        mLocalVideoBeans = new ArrayList<>();
    }

    @Override
    public void loadData() {

        getSpecificTypeOfFile(mContext, new String[]{".doc",".apk"});
        if(mLocalVideoBeans==null){
            MyLog.d("wwxx mLocalVideoBeans==null");
        }
        mView.setData(mLocalVideoBeans);
    }


    public void getSpecificTypeOfFile(Context context, String[] extension)
    {
        //从外存中获取
        Uri fileUri= MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和不含后缀的文件名
        String[] projection=new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
        };
        //构造筛选语句
        String selection="";
        for(int i=0;i<extension.length;i++)
        {
            if(i!=0)
            {
                selection=selection+" OR ";
            }
            selection=selection+ MediaStore.Files.FileColumns.DATA+" LIKE '%"+extension[i]+"'";
        }
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder= MediaStore.Files.FileColumns.DATE_MODIFIED;
        //获取内容解析器对象
        ContentResolver resolver=context.getContentResolver();
        //获取游标
        Cursor cursor=resolver.query(fileUri, projection, selection, null, sortOrder);
        if(cursor==null)
            return;
        //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
        mLocalVideoBeans.clear();
        if(cursor.moveToLast())
        {
            do{
                //输出文件的完整路径
                LocalVideoBean localVideoBean = new LocalVideoBean();
                localVideoBean.setName(cursor.getString(1));
                localVideoBean.setPath(cursor.getString(0));
                mLocalVideoBeans.add(localVideoBean);
                //String data=cursor.getString(0);

                Log.d("tag", " getString(0):"+cursor.getString(0)+" getString(1): "+cursor.getString(1));
            }while(cursor.moveToPrevious());
        }
        cursor.close();

    }



}
