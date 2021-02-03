//
// Created by yangw on 2018-5-14.
//


#include "WlVideo.h"

WlVideo::WlVideo(WlPlaystatus *playstatus, WlCallJava *wlCallJava) {

    this->playstatus = playstatus;
    this->wlCallJava = wlCallJava;
    queue = new WlQueue(playstatus);
    pthread_mutex_init(&codecMutex, NULL);
}

void * playVideo(void *data)
{

    WlVideo *video = static_cast<WlVideo *>(data);

    while(video->playstatus != NULL && !video->playstatus->exit)
    {

        if(video->playstatus->seek)
        {
            av_usleep(1000 * 100);
            continue;
        }
        if(video->playstatus->pause)
        {
            av_usleep(1000 * 100);
            continue;
        }
        if(video->queue->getQueueSize() == 0)//判断队列里面是否有数据
        {//没有数据的时候要返回在加载的信息给用户
            if(!video->playstatus->load)
            {
                video->playstatus->load = true;
                video->wlCallJava->onCallLoad(CHILD_THREAD, true);
            }
            av_usleep(1000 * 100);
            continue;
        } else{
            if(video->playstatus->load)
            {
                video->playstatus->load = false;//加载完了，要告诉用户
                video->wlCallJava->onCallLoad(CHILD_THREAD, false);
            }
        }
        AVPacket *avPacket = av_packet_alloc();
        if(video->queue->getAvpacket(avPacket) != 0)
        {//不等于0就是没有拿到avPacket ，释放
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            continue;
        }
        if(video->codectype == CODEC_MEDIACODEC)
        {
            if(av_bsf_send_packet(video->abs_ctx, avPacket) != 0)// 送入AVPacket过滤
            {
                av_packet_free(&avPacket);
                av_free(avPacket);
                avPacket = NULL;
                continue;
            }
            while(av_bsf_receive_packet(video->abs_ctx, avPacket) == 0)//接收过滤后的AVPacke
            {
                LOGE("开始解码");

                double diff = video->getFrameDiffTime(NULL, avPacket);
                LOGE("diff is %f", diff);

                av_usleep(video->getDelayTime(diff) * 1000000);
                video->wlCallJava->onCallDecodeAVPacket(avPacket->size, avPacket->data);

                av_packet_free(&avPacket);
                av_free(avPacket);
                continue;
            }
            avPacket = NULL;
        }
        else if(video->codectype == CODEC_SOFTWARE_YUV)
        {

            //把packet送给ffmpeg解码
            pthread_mutex_lock(&video->codecMutex);
            if(avcodec_send_packet(video->avCodecContext, avPacket) != 0)
            {
                av_packet_free(&avPacket);
                av_free(avPacket);
                avPacket = NULL;
                pthread_mutex_unlock(&video->codecMutex);
                continue;
            }
            AVFrame *avFrame = av_frame_alloc();
            //获取解码后的帧
            if(avcodec_receive_frame(video->avCodecContext, avFrame) != 0)
            {
                av_frame_free(&avFrame);
                av_free(avFrame);
                avFrame = NULL;
                av_packet_free(&avPacket);
                av_free(avPacket);
                avPacket = NULL;
                pthread_mutex_unlock(&video->codecMutex);
                continue;
            }
            LOGE("子线程解码一个AVframe成功");
            if(avFrame->format == AV_PIX_FMT_YUV420P)
            {
                LOGE("当前视频是YUV420P格式");

                double diff = video->getFrameDiffTime(avFrame, NULL);
                LOGE("diff is %f", diff);

                av_usleep(video->getDelayTime(diff) * 1000000);//音视频同步处理
                video->wlCallJava->onCallRenderYUV(
                        video->avCodecContext->width,
                        video->avCodecContext->height,
                        avFrame->data[0],
                        avFrame->data[1],
                        avFrame->data[2]);
            } else{
                LOGE("当前视频不是YUV420P格式,需要转成YUV420P格式");
                AVFrame *pFrameYUV420P = av_frame_alloc();
                int num = av_image_get_buffer_size(
                        AV_PIX_FMT_YUV420P,//要转化后的格式
                        video->avCodecContext->width,
                        video->avCodecContext->height,
                        1);//默认填1
                //分配buffer
                uint8_t *buffer = static_cast<uint8_t *>(av_malloc(num * sizeof(uint8_t)));
                av_image_fill_arrays(
                        pFrameYUV420P->data,
                        pFrameYUV420P->linesize,//每一行的长度
                        buffer,
                        AV_PIX_FMT_YUV420P,
                        video->avCodecContext->width,
                        video->avCodecContext->height,
                        1);
                //SwsContext 转化编码格式的上下文
                SwsContext *sws_ctx = sws_getContext(
                        video->avCodecContext->width,
                        video->avCodecContext->height,
                        video->avCodecContext->pix_fmt,
                        video->avCodecContext->width,
                        video->avCodecContext->height,
                        AV_PIX_FMT_YUV420P,
                        SWS_BICUBIC,//选择转码算法
                        NULL, NULL, NULL);

                if(!sws_ctx)
                {
                    av_frame_free(&pFrameYUV420P);
                    av_free(pFrameYUV420P);
                    av_free(buffer);
                    pthread_mutex_unlock(&video->codecMutex);
                    continue;
                }
                sws_scale(
                        sws_ctx,
                        reinterpret_cast<const uint8_t *const *>(avFrame->data),
                        avFrame->linesize,
                        0,
                        avFrame->height,
                        pFrameYUV420P->data,
                        pFrameYUV420P->linesize);
                //渲染

                double diff = video->getFrameDiffTime(avFrame, NULL);
                LOGE("diff is %f", diff);

                av_usleep(video->getDelayTime(diff) * 1000000);

                video->wlCallJava->onCallRenderYUV(
                        video->avCodecContext->width,
                        video->avCodecContext->height,
                        pFrameYUV420P->data[0],
                        pFrameYUV420P->data[1],
                        pFrameYUV420P->data[2]);

                av_frame_free(&pFrameYUV420P);
                av_free(pFrameYUV420P);
                av_free(buffer);
                sws_freeContext(sws_ctx);
            }

            av_frame_free(&avFrame);
            av_free(avFrame);
            avFrame = NULL;
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            pthread_mutex_unlock(&video->codecMutex);
        }


    }
    return 0;
}

void WlVideo::play() {

    if(playstatus != NULL && !playstatus->exit)
    {
        pthread_create(&thread_play, NULL, playVideo, this);
    }
}

void WlVideo::release() {

    if(queue != NULL)
    {
        queue->noticeQueue();
    }
    pthread_join(thread_play, NULL);

    if(queue != NULL)
    {
        delete(queue);
        queue = NULL;
    }
    if(abs_ctx != NULL)
    {
        av_bsf_free(&abs_ctx);
        abs_ctx = NULL;
    }
    if(avCodecContext != NULL)
    {
        pthread_mutex_lock(&codecMutex);
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = NULL;
        pthread_mutex_unlock(&codecMutex);
    }

    if(playstatus != NULL)
    {
        playstatus = NULL;
    }
    if(wlCallJava != NULL)
    {
        wlCallJava = NULL;
    }

}

WlVideo::~WlVideo() {
    pthread_mutex_destroy(&codecMutex);
}

double WlVideo::getFrameDiffTime(AVFrame *avFrame, AVPacket *avPacket) {

    double pts = 0;
    if(avFrame != NULL)
    {
        pts = av_frame_get_best_effort_timestamp(avFrame);
    }
    if(avPacket != NULL)
    {
        pts = avPacket->pts;
    }
    if(pts == AV_NOPTS_VALUE)
    {
        pts = 0;
    }
    pts *= av_q2d(time_base);//视频的PTS（播放时间戳）

    if(pts > 0)
    {
        clock = pts;
    }

    double diff = audio->clock - clock;
    return diff;
}

double WlVideo::getDelayTime(double diff) {

    if(diff > 0.003)//差值在3ms之间默认是同步的，不处理
    {//说明音频超前了，
        LOGD("diff > 0.003");
        delayTime = delayTime * 2 / 3; //减小每帧休眠时间
        if(delayTime < defaultDelayTime / 2)//如果减小到小于默认帧休眠时间的一般
        {
            delayTime = defaultDelayTime * 2 / 3;//那就从新处理一下delayTime
        }
        else if(delayTime > defaultDelayTime * 2)
        {
            delayTime = defaultDelayTime * 2;
        }
    }
    else if(diff < - 0.003)//差值在3ms之间默认是同步的，不处理
    {//说明视屏超前了，
        LOGD("diff < - 0.003");
        delayTime = delayTime * 3 / 2;//那就增加视屏帧休眠的时间
        if(delayTime < defaultDelayTime / 2)
        {
            delayTime = defaultDelayTime * 2 / 3;
        }
        else if(delayTime > defaultDelayTime * 2)
        {
            delayTime = defaultDelayTime * 2;
        }
    }
    else if(diff == 0.003)
    {

    }
	
    if(diff >= 0.5)//如果差值都大于0.5了，就不需要视屏渲染休眠了，让视频赶紧追上音频渲染
    {
        LOGD("diff >= 0.5");
        delayTime = 0;
    }
    else if(diff <= -0.5)//说明视屏渲染已经大大超前了，
    {
        LOGD("diff <= -0.5");
        delayTime = defaultDelayTime * 2;
    }

    if(fabs(diff) >= 10)
    {
        delayTime = defaultDelayTime;
    }
    return delayTime;
}
