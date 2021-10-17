package com.npbeta.colorQPay.config

class ConfigObj {
    var ip: String
    var port: Int
    var qq: Long
    var group: List<Long>
    var prefix: String
    var chargeMultiplier: Int
    var chargeCommand: String
    var appid: String
    var privateKey: String
    var alipayPublicKey: String
    var mcPanelAPIURL: String
    var mcPanelAPIKey: String
    var mysqlAddress: String
    var mysqlUsername: String
    var mysqlPassword: String
    var mysqlDatabase: String


    init {
        ip = "127.0.0.1"
        port = 23333
        qq = 0
        group = listOf()
        prefix = ".充值"
        chargeMultiplier = 100
        chargeCommand = "money give"
        appid = ""
        privateKey = ""
        alipayPublicKey = ""
        mcPanelAPIURL = ""
        mcPanelAPIKey = "apikey"
        mysqlAddress = "127.0.0.1:3306"
        mysqlUsername = "root"
        mysqlPassword = ""
        mysqlDatabase = "colorQPay"
    }
}