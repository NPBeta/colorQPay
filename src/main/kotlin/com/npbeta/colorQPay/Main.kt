package com.npbeta.colorQPay

import com.alibaba.fastjson.JSON
import com.npbeta.colorQPay.RobotSDK.*
import com.npbeta.colorQPay.RobotSDK.pack.from.SendGroupMessagePack
import com.npbeta.colorQPay.RobotSDK.pack.from.SendGroupPrivateImageFilePack
import com.npbeta.colorQPay.RobotSDK.pack.from.SendGroupPrivateMessagePack
import com.npbeta.colorQPay.RobotSDK.pack.to.GroupMessageEventPack
import com.npbeta.colorQPay.config.ConfigObj
import com.npbeta.colorQPay.config.ConfigUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.collections.ArrayList

class Main {
    companion object {
        val logger: Logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME)
        lateinit var RunDir: String
        lateinit var Config: ConfigObj
        private val robot = Robot()

        @JvmStatic
        fun main(arg: Array<String>) {
            RunDir = System.getProperty("user.dir") + "/"
            val configUtils = ConfigUtils()
            if (configUtils.read(RunDir)) {
                logger.fatal("请修改配置文件后重启插件")
                return
            }
            val robotConfig: RobotConfig = object : RobotConfig() {
                init {
                    Name = "QPay"
                    IP = "127.0.0.1"
                    Port = 23333
                    Pack = object : ArrayList<Int?>() {
                        init {
                            add(49)
                        }
                    }
                    Groups = null
                    QQs = null
                    RunQQ = 0
                    Time = 10000
                    Check = true
                    CallAction = ICall { type: Byte, data: String ->
                        if (type == 49.toByte()) {
                            val gPack: GroupMessageEventPack = JSON.parseObject(
                                data,
                                GroupMessageEventPack::class.java
                            )
                            val message = gPack.message[gPack.message.size - 1].split(" ".toRegex()).toTypedArray()
                            if (message[0] == Config.prefix) {
                                logger.debug("Group: ${gPack.id}") //来自群
                                logger.debug("From: ${gPack.fid}") //来自 QQ
                                logger.debug("Message: ") //信息链
                                for (item in gPack.message) {
                                    logger.debug(item)
                                }
                                sendGroupMessage(gPack.id, gPack.fid, "已收到充值请求，请查看私聊消息")
                                Thread(ChargeHelper(message[1].toDouble(), gPack.id, gPack.fid)).start()
                            }
                        }
                    }
                    LogAction =
                        ILog { type: LogType, data: String -> logger.info("机器人日志: $type: $data") }
                    StateAction =
                        IState { type: StateType -> logger.debug("机器人状态:$type") }
                }
            }
            robot.Set(robotConfig)
            robot.IsFirst = false
            robot.Start()
            while (true) {
                val data = readLine()
                if (data == "stop") {
                    robot.Stop()
                    return
                }
            }
        }

        fun sendGroupMessage(group: Long, user: Long, text: String) {
            val pack = SendGroupMessagePack()
            pack.id = group //群号
            pack.qq = robot.QQs[0] //机器人 QQ
            pack.message = object : ArrayList<String>() {
                init {
                    add("at:{$user}")
                    add(text)
                }
            }
            robot.addTask(BuildPack.Build(pack, 52))
        }

        fun sendGroupPrivateMessage(group: Long, user: Long, text: String) {
            val pack = SendGroupPrivateMessagePack()
            pack.id = group //群号
            pack.fid = user //私聊成员 QQ
            pack.qq = robot.QQs[0] //机器人 QQ
            pack.message = object : ArrayList<String?>() {
                init { add(text) }
            }
            robot.addTask(BuildPack.Build(pack, 53))
        }

        fun sendGroupPrivateImage(group: Long, user: Long, image: String) {
            val pack = SendGroupPrivateImageFilePack()
            pack.id = group
            pack.fid = user
            pack.file = image
            robot.addTask(BuildPack.Build(pack, 76))
        }
    }
}