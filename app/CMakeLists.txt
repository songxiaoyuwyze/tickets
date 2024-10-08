cmake_minimum_required(VERSION 3.4.1)
add_library( ffmpegaudioplayer
        SHARED
        src/main/cpp/ffmpegaudioplayer.cpp)
find_library( log-lib
        log )
find_library( android-lib
        android )
set(distribution_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})


add_library( avutil
        SHARED
        IMPORTED )
set_target_properties( avutil
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavutil.so)

add_library( swresample
        SHARED
        IMPORTED )
set_target_properties( swresample
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libswresample.so)

add_library( avcodec
        SHARED
        IMPORTED )
set_target_properties( avcodec
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavcodec.so)

add_library( avfilter
        SHARED
        IMPORTED )
set_target_properties( avfilter
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavfilter.so)

add_library( swscale
        SHARED
        IMPORTED )
set_target_properties( swscale
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libswscale.so)

add_library( avformat
        SHARED
        IMPORTED )
set_target_properties( avformat
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavformat.so)


# 同上还要通过add_library,set_target_properties
# 设置swresample-2，avcodec-57，avfilter-6，swscale-4avformat-57

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}  -g -Wall -std=c++11 -Wno-deprecated-declarations")
include_directories(src/main/cpp)
include_directories(src/main/jniLibs/includes/${ANDROID_ABI})

target_link_libraries(ffmpegaudioplayer
avutil      #工具库
swresample    #音频采样数据格式转换
avcodec      #编解码
avfilter      #滤镜特效处理
swscale       #视频像素数据格式转换
avformat     #封装格式处理
OpenSLES
${log-lib}
${android-lib})
