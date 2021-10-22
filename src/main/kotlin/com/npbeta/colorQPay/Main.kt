package com.npbeta.colorQPay

import coloryr.colormirai.RobotSDK.*
import coloryr.colormirai.RobotSDK.pack.from.SendGroupMessagePack
import coloryr.colormirai.RobotSDK.pack.from.SendGroupPrivateImageFilePack
import coloryr.colormirai.RobotSDK.pack.from.SendGroupPrivateMessagePack
import coloryr.colormirai.RobotSDK.pack.to.GroupMessageEventPack
import com.alibaba.fastjson.JSON
import com.npbeta.colorQPay.config.ConfigObj
import com.npbeta.colorQPay.config.ConfigUtils
import com.npbeta.colorQPay.utils.ChargeHelper
import com.npbeta.colorQPay.utils.MySQLHelper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.system.exitProcess

object Main {
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
            exitProcess(-1)
        }
        val robotConfig: RobotConfig = object : RobotConfig() {
            init {
                Name = "QPay"
                IP = Config.ip
                Port = Config.port
                Pack = object : ArrayList<Int?>() {
                    init {
                        add(49)
                    }
                }
                Groups = Config.group
                RunQQ = Config.qq
                Time = 10000
                Check = true
                CallAction = ICall { type: Byte, data: String ->
                    if (type == 49.toByte()) {
                        val gPack: GroupMessageEventPack =
                            JSON.parseObject(
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
                            if (message.size == 2) {
                                if (checkInput(gPack, message[1])) {
                                    Thread(ChargeHelper(message[1].toDouble(), gPack.id, gPack.fid)).start()
                                }
                            } else {
                                logger.warn("收到非法指令")
                                sendGroupMessage(gPack.id, gPack.fid, "命令格式: ${Config.prefix} 整数金额")
                            }
                        }
                    }
                }
                LogAction =
                    ILog { type: LogType, data: String ->
                        logger.info("机器人日志: $type: $data")
                    }
                StateAction =
                    IState { type: StateType ->
                        logger.debug("机器人状态:$type")
                    }
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

    fun checkInput(pack: GroupMessageEventPack, amount: String): Boolean {
        if (MySQLHelper().query(pack.fid.toString()) == "") {
            logger.warn("QQ 用户未绑定角色名")
            sendGroupMessage(pack.id, pack.fid, "请先绑定角色名到 QQ")
        } else {
            try {
                val price = amount.toInt()
                if (price in 1..2000) {
                    logger.info("收到充值请求，准备发送私聊消息")
                    sendGroupMessage(pack.id, pack.fid, "已收到充值请求，请查看私聊消息")
                    return true
                } else {
                    logger.warn("收到错误金额")
                    sendGroupMessage(pack.id, pack.fid, "请输入小于等于 2000 的正整数")
                }
            } catch (e: NumberFormatException) {
                logger.warn("收到非法参数")
                sendGroupMessage(pack.id, pack.fid, "请输入数字")
            }
        }
        return false
    }

    fun sendGroupMessage(group: Long, user: Long, text: String) {
        val pack = SendGroupMessagePack()
        pack.id = group //群号
        pack.qq = Config.qq //机器人 QQ
        pack.message = object : ArrayList<String>() {
            init {
                add("at:$user")
                add(text)
            }
        }
        robot.addTask(BuildPack.Build(pack, 52))
        logger.info("发送消息到 $group")
    }

    fun sendGroupPrivateMessage(group: Long, user: Long, text: String) {
        val pack = SendGroupPrivateMessagePack()
        pack.id = group //群号
        pack.fid = user //私聊成员 QQ
        pack.qq = Config.qq //机器人 QQ
        pack.message = object : ArrayList<String?>() {
            init {
                add(text)
            }
        }
        robot.addTask(BuildPack.Build(pack, 53))
        logger.info("发送消息到来自群 $group 的 $user")
    }

    fun sendGroupPrivateImage(group: Long, user: Long, image: String) {
        val pack = SendGroupPrivateImageFilePack()
        pack.id = group
        pack.fid = user
        pack.qq = Config.qq //机器人 QQ
        pack.file = image
        robot.addTask(BuildPack.Build(pack, 76))
        logger.info("发送图片到来自群 $group 的 $user")
    }
}