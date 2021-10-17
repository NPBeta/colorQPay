package com.npbeta.colorQPay.utils

import com.npbeta.colorQPay.Main
import com.npbeta.colorQPay.payments.alipay.AliPayCall
import com.npbeta.colorQPay.payments.alipay.AliPayCallBack
import java.io.File
import javax.imageio.ImageIO

class ChargeHelper(
    private val price: Double,
    private val group: Long,
    private val user: Long,
    ): Runnable {

    override fun run() {
        Main.logger.info("正在调起支付")
        val payTask = AliPayCall(price, group, user, AliPayCallBack)
        Thread(payTask).start()
        payTask.sendQRCode {
            Main.logger.info("正在发送二维码")
            val imagePath = "${Main.RunDir}temp/${payTask.orderId}.png"
            val imageFile = File(imagePath)
            File(imagePath).createNewFile()
            ImageIO.write(payTask.qrCodeImage, "png", imageFile)
            Main.sendGroupPrivateImage(group, user, imagePath)
            Main.sendGroupPrivateMessage(group, user, "请在 5 分钟内扫码并支付")
        }
    }
}