package com.npbeta.colorQPay.config

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.npbeta.colorQPay.Main
import java.io.*
import java.nio.charset.StandardCharsets

class ConfigUtils {

    private lateinit var configFile: File

    fun parseFile(configFile: File): String {
        val reader = InputStreamReader(
            FileInputStream(configFile), StandardCharsets.UTF_8
        )
        val bf = BufferedReader(reader)
        val buf = CharArray(4096)
        var length: Int
        val data = StringBuilder()
        while (bf.read(buf).also { length = it } != -1) {
            data.append(String(buf, 0, length))
        }
        bf.close()
        reader.close()
        return data.toString()
    }

    fun read(local: String): Boolean {
        try {
            configFile = File("${local}Config.json")
            if (!configFile.exists()) {
                configFile.createNewFile()
                Main.Config = ConfigObj()
                save()
                return true
            } else {
                Main.Config = JSON.parseObject(parseFile(configFile), ConfigObj::class.java)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun save() {
        try {
            val out = FileOutputStream(configFile)
            val write = OutputStreamWriter(
                out, StandardCharsets.UTF_8
            )
            write.write(JSON.toJSONString(Main.Config, SerializerFeature.PrettyFormat))
            write.close()
            out.close()
        } catch (e: Exception) {
            Main.logger.error("配置文件保存失败", e)
            e.printStackTrace()
        }
    }
}