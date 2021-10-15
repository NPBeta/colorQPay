package com.npbeta.colorQPay.RobotSDK.pack.from;

import com.npbeta.colorQPay.RobotSDK.pack.PackBase;

import java.util.List;

/*
54 [插件]发送好友消息
id:好友QQ号
message:消息
 */
public class SendFriendMessagePack extends PackBase {
    public long id;
    public List<String> message;
}
