# 流传输模块

该模块负责音视频流媒体的传输

基于rtsp/rtp/rtcp协议

## 流程

由RTSPClient控制和服务器的连接以及音视频流的传输

1. RtspClient发送命令, 建立和服务端的连接
    1. 建立和服务端的socket连接
    2. Announce
    3. Setup, 获取sessionId
    4. Play
    5. Options轮询服务器检测断线

2. 确保和服务器连接后, 音视频流(Stream)会分别开启传输线程, 传输线程中做以下工作
    1. Stream采集音视频数据
    2. Stream对音视频数据进行编码
    3. Packetizer进行协议打包并使用RtpSocket发送到服务端

3. 停止流传输, 断开和服务器的连接
    1. 停止音视频的采集和打包发送线程
    2. 发送teardown命令, 关闭socket
