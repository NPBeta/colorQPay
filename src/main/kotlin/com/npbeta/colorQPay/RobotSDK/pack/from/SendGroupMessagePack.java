package com.npbeta.colorQPay.RobotSDK.pack.from;

import com.npbeta.colorQPay.RobotSDK.pack.PackBase;

import java.util.List;

/*
52 [插件]发送群消息
id:群号
message:消息
 */
public class SendGroupMessagePack extends PackBase {
    public long id;
    public List<String> message;
}
