package coloryr.colormirai.RobotSDK.pack.from;

import coloryr.colormirai.RobotSDK.pack.PackBase;

/*
69 [插件]设置群名片
id:群号
fid:成员QQ号
card:新的群名片
 */
public class SetGroupMemberCard extends PackBase {
    public long id;
    public long fid;
    public String card;
}
