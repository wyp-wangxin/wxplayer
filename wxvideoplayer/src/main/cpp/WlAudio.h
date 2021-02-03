//
// Created by yangw on 2018-2-28.
//

#ifndef MYMUSIC_WLAUDIO_H
#define MYMUSIC_WLAUDIO_H

#include "WlQueue.h"
#include "pthread.h"
#include "AndroidLog.h"
#include "WlCallJava.h"
extern "C"
{
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include "libavcodec/avcodec.h"
#include "libswresample/swresample.h"
#include <libavutil/time.h>
};

class WlAudio {

public:
    int streamIndex = -1;
    AVCodecContext *avCodecContext = NULL;
    AVCodecParameters *codecpar = NULL;
    WlQueue * queue =NULL;
    WlPlaystatus * playstatus = NULL;
    WlCallJava * wlCallJava=NULL;

    pthread_t thread_play;

    uint8_t *buffer = NULL;
    int data_size;
    int sample_rate;

    int duration = 0;
    AVRational time_base;
    double clock;//总的播放时长
    double now_time;//当前frame时间
    double last_tiem; //上一次调用时间

    // 引擎接口
    SLObjectItf engineObject = NULL;
    SLEngineItf engineEngine = NULL;

    //混音器
    SLObjectItf outputMixObject = NULL;
    SLEnvironmentalReverbItf outputMixEnvironmentalReverb = NULL;
    SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;

    //pcm
    SLObjectItf pcmPlayerObject = NULL;
    SLPlayItf pcmPlayerPlay = NULL;

    //缓冲器队列接口
    SLAndroidSimpleBufferQueueItf pcmBufferQueue = NULL;

    pthread_mutex_t codecMutex;//主要用来控制seek 时候 和解码的过程

public:
    WlAudio(WlPlaystatus* wlPlaystatus,int sample_rate,WlCallJava * wlCallJava);
    ~WlAudio();

    void play();

    int resampleAudio();
    void initOpenSLES();
    int getCurrentSampleRateForOpensles(int sample_rate);

    void pause();

    void resume();

    void stop();

    void release();
};


#endif //MYMUSIC_WLAUDIO_H
