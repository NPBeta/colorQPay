package com.npbeta.colorQPay

import java.util.Calendar.*
import com.lly835.bestpay.enums.BestPayPlatformEnum
import com.lly835.bestpay.enums.BestPayTypeEnum
import com.lly835.bestpay.enums.OrderStatusEnum
import com.lly835.bestpay.model.OrderQueryRequest
import com.lly835.bestpay.model.PayRequest
import com.lly835.bestpay.model.PayResponse

object PayAPI {

    @JvmStatic
    fun createTradeNo(): String {
        val calendar = getInstance()
        return "${calendar.get(YEAR)}${calendar.get(MONTH) + 1}${calendar.get(DATE)}" +
                (System.currentTimeMillis() + (Math.random() * 10000000L).toLong()).toString()
    }

    /**
     * 调起支付宝支付
     */
    fun native(orderName: String, amount: Double): PayResponse {
        val request = PayRequest()
        val orderId = createTradeNo()
        request.payTypeEnum = BestPayTypeEnum.ALIPAY_QRCODE
        request.orderId = orderId
        request.orderName = orderName
        request.orderAmount = amount
        val response = PayConfig.bestPayService.pay(request)
        response.orderId = orderId
        response.outTradeNo = orderId
        response.orderAmount = amount
        return response
    }

    /**
     * 查询支付宝订单
     */
    fun query(orderId: String): Boolean {
        val request = OrderQueryRequest()
        request.platformEnum = BestPayPlatformEnum.ALIPAY
        request.orderId = orderId
        val response = PayConfig.bestPayService.query(request) ?: return false
        return response.orderStatusEnum == OrderStatusEnum.SUCCESS
    }
}