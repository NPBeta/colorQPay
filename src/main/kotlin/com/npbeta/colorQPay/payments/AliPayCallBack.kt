package com.npbeta.colorQPay.payments

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.text.SimpleDateFormat
import java.util.*


object AliPayCallBack {

    private val logger: Logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd,hh:mm:ss")

    fun charge(price: Double, user: Long, payment: AliPayCall) {
        addLogs(user.toString(), price, "支付完成", payment)

        // charge money here

    }

    fun addLogs(player: String, price: Double, type: String, payment: AliPayCall) {
        val date = Date()
        val time = dateFormat.format(date)
        logger.info("$time.订单号", payment.orderId)
        logger.info("$time.玩家", player)
        logger.info("$time.金额", price)
        logger.info("$time.状态", type)
    }

}