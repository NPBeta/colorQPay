package com.npbeta.colorQPay.RobotSDK.pack.to;

import com.npbeta.colorQPay.RobotSDK.pack.PackBase;

/*
16 [机器人]主动或被动重新登录（事件）
message:原因消息
 */
public class BotReloginEventPack extends PackBase {
    public String message;

    public BotReloginEventPack(long qq, String message) {
        this.message = message;
        this.qq = qq;
    }
}
