package com.npbeta.colorQPay.utils

import com.npbeta.colorQPay.Main
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

class MySQLHelper {

    private val ds = HikariDataSource()
    private val url: String = Main.Config.mysqlAddress
    private val username: String = Main.Config.mysqlUsername
    private val password: String = Main.Config.mysqlPassword
    private val database: String = Main.Config.mysqlDatabase

    @Throws(java.sql.SQLException::class)
    fun connect(): Connection {
        ds.jdbcUrl = "jdbc:mysql://$url/$database?characterEncoding=UTF-8&characterSetResults=UTF8"
        ds.username = username
        ds.password = password
        ds.isReadOnly = true
        return ds.connection
    }

    fun query(qq: String): String {
        val connection = connect()
        val sql = connection.prepareStatement("SELECT `name` FROM `minecraft_qq_player` WHERE `QQ`=?")
        sql.setString(1, qq)
        val result = sql.executeQuery()
        ds.close()
        while (result.next()) {
            return try { result.getString(1) } catch (e: Exception) { "" }
        }
        return ""
    }
}