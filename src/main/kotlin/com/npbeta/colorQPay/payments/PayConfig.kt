package com.npbeta.colorQPay.payments

import com.lly835.bestpay.config.AliPayConfig
import com.lly835.bestpay.service.impl.BestPayServiceImpl
import com.npbeta.colorQPay.Main

object PayConfig {

    private val aliPayConfig: AliPayConfig = object : AliPayConfig() {
        init {
            appId = Main.Config.appid
            privateKey = Main.Config.privateKey
            aliPayPublicKey = Main.Config.alipayPublicKey
        }
    }

    val bestPayService = BestPayServiceImpl()

    init {
        bestPayService.setAliPayConfig(aliPayConfig)
    }

}