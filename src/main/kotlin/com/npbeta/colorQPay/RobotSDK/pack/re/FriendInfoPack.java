package com.npbeta.colorQPay.RobotSDK.pack.re;

import com.npbeta.colorQPay.RobotSDK.pack.PackBase;
import com.npbeta.colorQPay.RobotSDK.pack.UserProfile;

/*
92 [插件]获取朋友信息
id:QQ号
img:头像图片
remark:好友备注
userProfile:用户详细资料
 */
public class FriendInfoPack extends PackBase {
    public long id;
    public String img;
    public String remark;
    public UserProfile userProfile;
}
