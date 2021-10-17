package com.npbeta.colorQPay.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.awt.image.BufferedImage

class QRCodeHelper(url: String) {
    val image: BufferedImage = toBufferedImage(
        QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, 128, 128))

    private fun toBufferedImage(bitMatrix: BitMatrix): BufferedImage {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (i in 0 until width) {
            for (j in 0 until height) {
                image.setRGB(i, j, if (bitMatrix[i, j]) -0x1000000 else 0XFFFFFF)
            }
        }
        return image
    }
}
