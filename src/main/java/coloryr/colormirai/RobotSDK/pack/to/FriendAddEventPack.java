package coloryr.colormirai.RobotSDK.pack.to;

import coloryr.colormirai.RobotSDK.pack.PackBase;

/*
18 [机器人]成功添加了一个新好友（事件）
id:好友QQ号
 */
public class FriendAddEventPack extends PackBase {
    public long id;

    public FriendAddEventPack(long qq, long id) {
        this.id = id;
        this.qq = qq;
    }
}
