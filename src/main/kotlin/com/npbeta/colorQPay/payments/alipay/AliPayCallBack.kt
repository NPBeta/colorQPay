package com.npbeta.colorQPay.payments.alipay

import com.npbeta.colorQPay.Main
import com.npbeta.colorQPay.utils.MCPanelAPIHelper
import com.npbeta.colorQPay.utils.MySQLHelper


object AliPayCallBack {

    fun charge(price: Double, user: Long, payment: AliPayCall): Boolean {
        val player = user.toString()
        addLogs(player, price, "支付完成", payment)
        val cmd = "${Main.Config.chargeCommand} ${getRoleName(player)} ${price * Main.Config.chargeMultiplier}"
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