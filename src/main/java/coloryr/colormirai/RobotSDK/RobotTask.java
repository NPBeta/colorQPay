package coloryr.colormirai.RobotSDK;

class RobotTask {
    public byte index;
    public String data;

    public RobotTask(byte type, String s) {
        index = type;
        data = s;
    }
}
