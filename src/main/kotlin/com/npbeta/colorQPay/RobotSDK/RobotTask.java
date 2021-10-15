package com.npbeta.colorQPay.RobotSDK;

class RobotTask {
    public byte index;
    public String data;

    public RobotTask(byte type, String s) {
        index = type;
        data = s;
    }
}
