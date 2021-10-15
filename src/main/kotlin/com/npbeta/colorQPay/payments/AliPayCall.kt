package com.npbeta.colorQPay.payments

import com.npbeta.colorQPay.PayAPI
import com.lly835.bestpay.model.PayResponse
import com.npbeta.colorQPay.Main
import com.npbeta.colorQPay.QRCodeHelper
import java.awt.image.BufferedImage
import java.util.*

class AliPayCall(
    val price: Double,
    val group: Long,
    val user: Long,
    private val callback: AliPayCallBack,
) {

    private val maxTime = 300   // 5m
    lateinit var orderId: String
    lateinit var qrCodeImage: BufferedImage
    private var loadSuccess = false
    private lateinit var response: PayResponse

    init {
        object : Runnable {
            override fun run() {
                response = PayAPI.native("喵喵舰长服充值", price)
                if (response.codeUrl == null) {
                    Main.sendGroupPrivateMessage(group, user, "订单创建失败，请重试")
                    return
                }
                orderId = response.orderId
                qrCodeImage = QRCodeHelper(response.codeUrl).image
                class QueryTask : TimerTask() {

                    private var paySuccess = false
                    var timer = 0

                    override fun run() {
                        if (timer++ > maxTime) {
                            AliPayCallBack.addLogs(user.toString(), price, "支付超时", this@AliPayCall)
                            cancel()
                        }
                        if (PayAPI.query(orderId) && !paySuccess) {
                            paySuccess = true
                            AliPayCallBack.charge(price, user, this@AliPayCall)
                            cancel()
                        }
                    }
                }
                Timer().schedule(QueryTask(), Date(), 2000)
                loadSuccess = true
            }
        }
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