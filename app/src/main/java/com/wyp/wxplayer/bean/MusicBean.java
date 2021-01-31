package com.wyp.wxplayer.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by wschun on 2016/8/23.
 */
public class MusicBean implements Serializable {
    public String title;
    public String path;
    public int duration;
    public long size;
    public String artist;

    public static MusicBean fromCursor(Cursor cursor){
        MusicBean musicBean=new MusicBean();
        String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        musicBean.title=name;
        musicBean.path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        musicBean.duration=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        musicBean.size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        musicBean.artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        return  musicBean;
    }
}
