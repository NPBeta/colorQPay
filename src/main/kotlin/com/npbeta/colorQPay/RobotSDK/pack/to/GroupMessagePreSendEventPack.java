package com.npbeta.colorQPay.RobotSDK.pack.to;

import com.npbeta.colorQPay.RobotSDK.pack.PackBase;

import java.util.List;

/*
29 [机器人]在发送群消息前广播（事件）
id:群号
message:消息
 */
public class GroupMessagePreSendEventPack extends PackBase {
    public long id;
    public List<String> message;
}
