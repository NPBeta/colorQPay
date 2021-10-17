package com.npbeta.colorQPay.payments.alipay

import com.npbeta.colorQPay.Main
import com.npbeta.colorQPay.utils.MCPanelAPIHelper
import com.npbeta.colorQPay.utils.MySQLHelper
import java.text.SimpleDateFormat


object AliPayCallBack {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd,hh:mm:ss")

    fun charge(price: Double, user: Long, payment: AliPayCall): Boolean {
        addLogs(user.toString(), price, "支付完成", payment)
        val cmd = "${Main.Config.chargeCommand} ${getRoleName(user.toString())} ${price * Main.Config.chargeMultiplier}"
        return MCPanelAPIHelper().sendCommand(Main.Config.mcPanelAPIURL, Main.Config.mcPanelAPIKey, cmd)
    }

    fun addLogs(player: String, price: Double, type: String, payment: AliPayCall) {
        Main.logger.info("订单号: ${payment.orderId}")
        Main.logger.info("玩家: $player")
        Main.logger.info("金额: $price")
        Main.logger.info("状态: $type")
    }

    private fun getRoleName(user: String): String {
        return MySQLHelper().query(user)
    }

}