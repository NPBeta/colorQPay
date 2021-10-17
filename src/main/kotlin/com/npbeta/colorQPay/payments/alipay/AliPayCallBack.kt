package com.npbeta.colorQPay.payments.alipay

import com.npbeta.colorQPay.Main
import com.npbeta.colorQPay.utils.MCPanelAPIHelper
import com.npbeta.colorQPay.utils.MySQLHelper
import java.text.SimpleDateFormat
import java.util.*


object AliPayCallBack {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd,hh:mm:ss")

    fun charge(price: Double, user: Long, payment: AliPayCall): Boolean {
        addLogs(user.toString(), price, "支付完成", payment)
        val cmd = "${Main.Config.chargeCommand} ${getRoleName(user.toString())} ${price * Main.Config.chargeMultiplier}"
        return MCPanelAPIHelper().sendCommand(Main.Config.mcPanelAPIURL, Main.Config.mcPanelAPIKey, cmd)
    }

    fun addLogs(player: String, price: Double, type: String, payment: AliPayCall) {
        val date = Date()
        val time = dateFormat.format(date)
        Main.logger.info("$time.订单号", payment.orderId)
        Main.logger.info("$time.玩家", player)
        Main.logger.info("$time.金额", price)
        Main.logger.info("$time.状态", type)
    }

    private fun getRoleName(user: String): String {
        return MySQLHelper().query(user)
    }

}