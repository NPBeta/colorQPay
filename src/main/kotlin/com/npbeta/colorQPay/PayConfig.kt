package com.npbeta.colorQPay

import com.lly835.bestpay.config.AliPayConfig
import com.lly835.bestpay.service.impl.BestPayServiceImpl

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