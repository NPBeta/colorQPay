package com.npbeta.colorQPay.payments.alipay

import com.lly835.bestpay.model.PayResponse
import com.npbeta.colorQPay.Main
import com.npbeta.colorQPay.payments.PayAPI
import com.npbeta.colorQPay.utils.QRCodeHelper
import java.awt.image.BufferedImage
import java.util.*

class AliPayCall(
    val price: Double,
    val group: Long,
    val user: Long,
    private val callback: AliPayCallBack,
): Runnable {

    private val maxTime = 300   // 5m
    lateinit var orderId: String
    lateinit var qrCodeImage: BufferedImage
    private var loadSuccess = false
    private lateinit var response: PayResponse

    override fun run() {
        Main.logger.info("开始创建订单")
        response = PayAPI.native("喵喵舰长服充值", price)
        if (response.codeUrl == null) {
            Main.logger.info("订单创建失败")
            Main.sendGroupPrivateMessage(group, user, "订单创建失败，请重试")
            return
        }
        orderId = response.orderId
        Main.logger.info("订单 $orderId 已创建")
        qrCodeImage = QRCodeHelper(response.codeUrl).image
        class QueryTask : TimerTask() {

            private var paySuccess = false
            var timer = 0

            override fun run() {
                if (timer++ > maxTime) {
                    callback.addLogs(user.toString(), price, "支付超时", this@AliPayCall)
                    Main.sendGroupPrivateMessage(group, user, "支付超时，请重新提交")
                    PayAPI.close(orderId)
                    cancel()
                }
                if (PayAPI.query(orderId) && !paySuccess) {
                    paySuccess = true
                    Main.logger.info("$user 支付 $price 成功")
                    Main.sendGroupPrivateMessage(group, user, "支付成功，正在提交充值")
                    if (callback.charge(price, user, this@AliPayCall)) {
                        Main.logger.info("$user 充值 $price 成功")
                        Main.sendGroupPrivateMessage(group, user, "充值成功")
                    } else {
                        Main.logger.warn("$user 充值 $price 失败")
                        Main.sendGroupPrivateMessage(group, user, "充值失败，请联系服主")
                    }
                    cancel()
                }
            }
        }
        Main.logger.debug("启动支付结果轮询")
        Timer().schedule(QueryTask(), Date(), 2000)
        loadSuccess = true
    }

    /**
     * 充值参数加载完成后执行
     */
    fun sendQRCode(runnable: Runnable) {
        class CheckLoadStatus: TimerTask() {
            override fun run() {
                if (loadSuccess) {
                    runnable.run()
                    cancel()
                }
            }
        }
        Timer().schedule(CheckLoadStatus(), Date(), 1000)
    }
}