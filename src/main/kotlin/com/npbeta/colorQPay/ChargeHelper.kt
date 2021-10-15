package com.npbeta.colorQPay

import com.npbeta.colorQPay.payments.AliPayCall
import com.npbeta.colorQPay.payments.AliPayCallBack
import java.io.File
import javax.imageio.ImageIO

class ChargeHelper(
    private val price: Double,
    private val group: Long,
    private val user: Long,

    ): Runnable {

    override fun run() {
        val payTask = AliPayCall(price, group, user, AliPayCallBack)
        payTask.sendQRCode {
            val imagePath = "${Main.RunDir}temp/${payTask.orderId}.png"
            ImageIO.write(payTask.qrCodeImage, "png", File(imagePath))
            Main.sendGroupPrivateImage(group, user, imagePath)
            Main.sendGroupPrivateMessage(group, user, "请在 5 分钟内扫码并支付")
        }
    }
}