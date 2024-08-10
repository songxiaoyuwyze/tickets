#include <jni.h>
#include <android/log.h>
#include <string>

extern "C" {
#include <libavformat/avformat.h>
#include <libavcodec/avcodec.h>
#include <libswresample/swresample.h>
}
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"FFmpegAudioPlayer",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"FFmpegAudioPlayer",FORMAT,##__VA_ARGS__);
extern "C" JNIEXPORT void JNICALL
Java_com_example_ffmpegaudioplayer_MainActivity_decodeAudio(JNIEnv *env,jobject /* this */,jstring _src, jstring _out) {
    const char *src = env->GetStringUTFChars(_src, 0);
    const char *out = env->GetStringUTFChars(_out, 0);

    avformat_network_init();//注册所有容器解码器
    AVFormatContext *fmt_ctx = avformat_alloc_context();

    if (avformat_open_input(&fmt_ctx, src, NULL, NULL) < 0) {//打开文件
        LOGE("open file error");
        return;
    }
    if (avformat_find_stream_info(fmt_ctx, NULL) < 0) {//读取音频格式文件信息
        LOGE("find stream info error");
        return;
    }
    //获取音频索引
    int audio_stream_index = -1;
    for (int i = 0;i<fmt_ctx->nb_streams;i++) {
        if (fmt_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_index = i;
            LOGI("find audio stream index");
            break;
        }
    }
    //获取解码器
    AVCodecContext *codec_ctx = avcodec_alloc_context3(NULL);
    avcodec_parameters_to_context(codec_ctx, fmt_ctx->streams[audio_stream_index]->codecpar);
    const AVCodec *codec = avcodec_find_decoder(codec_ctx->codec_id);
    //打开解码器
    if (avcodec_open2(codec_ctx, codec, NULL) < 0) {
        LOGE("could not open codec");
        return;
    }
    //分配AVPacket和AVFrame内存，用于接收音频数据，解码数据
    AVPacket *packet = av_packet_alloc();
    AVFrame *frame = av_frame_alloc();
    int got_frame;//接收解码结果
    int index = 0;
    //pcm输出文件
    FILE *out_file = fopen(out, "wb");
    while (av_read_frame(fmt_ctx, packet) == 0) {//将音频数据读入packet
        if (packet->stream_index == audio_stream_index) {//取音频索引packet
            if (avcodec_send_packet(codec_ctx,  packet) <0) {//将packet解码成AVFrame
                LOGE("decode error:%d", index);
                break;
            }
            if (got_frame > 0) {
                LOGI("decode frame:%d", index++);
                fwrite(frame->data[0], 1, static_cast<size_t>(frame->linesize[0]),out_file); //想将单个声道pcm数据写入文件
                }
            }
        }
        LOGI("decode finish...");
        //释放资源
        av_packet_unref(packet);
        av_frame_free(&frame);
        avcodec_close(codec_ctx);
        avformat_close_input(&fmt_ctx);
        fclose(out_file);
    }
