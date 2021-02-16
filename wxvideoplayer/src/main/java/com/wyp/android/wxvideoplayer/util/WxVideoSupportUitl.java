package com.wyp.android.wxvideoplayer.util;

import android.media.MediaCodecList;

import com.wyp.android.wxvideoplayer.log.MyLog;

import java.util.HashMap;
import java.util.Map;

public class WxVideoSupportUitl {

    private static Map<String, String> codecMap = new HashMap<>();
    static {
        codecMap.put("h264", "video/avc");
        codecMap.put("h265", "video/hevc");
        codecMap.put("mpeg4", "video/mp4v-es");
        //codecMap.put("h264", "video/3gpp");
       // codecMap.put("h264", "video/x-vnd.on2.vp8");
       // codecMap.put("h264", "video/x-vnd.on2.vp9");
    }

    public static String findVideoCodecName(String ffcodename)
    {
        if(codecMap.containsKey(ffcodename))
        {
            return codecMap.get(ffcodename);
        }
        return "";
    }

    public static boolean isSupportCodec(String ffcodecname)
    {
        boolean supportvideo = false;
        int count = MediaCodecList.getCodecCount();
        for(int i = 0; i < count; i++)
        {
            String[] tyeps = MediaCodecList.getCodecInfoAt(i).getSupportedTypes();
            for(int j = 0; j < tyeps.length; j++)
            {
                if(tyeps[j].equals(findVideoCodecName(ffcodecname)))
                {
                    supportvideo = true;
                    break;
                }
            }
            if(supportvideo)
            {
                break;
            }
        }
        MyLog.d("supportvideo: "+supportvideo);
        return supportvideo;
    }
    public static void printhwcodec(){
        int count = MediaCodecList.getCodecCount();
        for(int i = 0; i < count; i++)
        {
            String[] tyeps = MediaCodecList.getCodecInfoAt(i).getSupportedTypes();

            for(int j = 0; j < tyeps.length; j++)
            {
                MyLog.d("codec : "+i+" "+tyeps[j]);
            }
        }
    }
}
