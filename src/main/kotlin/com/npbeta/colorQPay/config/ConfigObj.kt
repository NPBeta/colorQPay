package com.npbeta.colorQPay.config

class ConfigObj {
    var ip: String
    var port: Int
    var group: List<Long>
    var prefix: String
    var chargeMultiplier: Int
    var appid: String
    var privateKey: String
    var alipayPublicKey: String

    init {
        ip = "127.0.0.1"
        port = 23333
        group = listOf()
        prefix = ".充值"
        chargeMultiplier = 100
        appid = ""
        privateKey = ""
        alipayPublicKey = ""
    }
}