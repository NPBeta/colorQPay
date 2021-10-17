package coloryr.colormirai.RobotSDK.pack.to;

import coloryr.colormirai.RobotSDK.pack.PackBase;

/*
79 [机器人]成员群恢复（事件）
id: QQ群
fid: 成员QQ号
 */
public class MemberJoinRetrieveEventPack extends PackBase {
    public long id;
    public long fid;

    public MemberJoinRetrieveEventPack(long qq, long id, long fid) {
        this.qq = qq;
        this.id = id;
        this.fid = fid;
    }
}
