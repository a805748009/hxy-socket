package nafos.server.handle.http

import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.cookie.Cookie
import io.netty.handler.codec.http.cookie.DefaultCookie
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList

/**
 * @Description respone
 * @Author      xinyu.huang
 * @Time        2019/11/24 21:02
 */
class NsRespone(version: HttpVersion, status: HttpResponseStatus) : DefaultFullHttpResponse(version, status) {
    var cookies: MutableList<Cookie> = ArrayList()

    fun setCookie(key: String, value: String): NsRespone {
        cookies.add(DefaultCookie(key, value))
        return this
    }

    fun setHeader(key: String, value: String) {
        headers().set(key, value)
    }

    /**
     * @Desc 返回文件
     * @Param filePath  要返回的文件路径
     * @Param fileName  返回文件在前端的名字显示
     * @Author hxy
     * @Time 2019/10/24 12:19
     */
    fun returnFile(filePath: String, fileName: String): NsRespone {
        val file = File(filePath)
        this.setHeader("Content-Disposition", "attachment;fileName=$fileName")
        this.setHeader(HttpHeaderNames.CONTENT_TYPE.toString(), "multipart/form-data")
        this.status = HttpResponseStatus.OK
        var `in`: FileInputStream? = null
        try {
            `in` = FileInputStream(file)
            this.content().writeBytes(`in`.channel, 0L, `in`.available())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return this
    }

    fun returnFile(byteArray: ByteArray, fileName: String): NsRespone {
        this.setHeader("Content-Disposition", "attachment;fileName=$fileName")
        this.setHeader(HttpHeaderNames.CONTENT_TYPE.toString(), "multipart/form-data")
        this.status = HttpResponseStatus.OK
        this.content().writeBytes(byteArray)
        return this
    }

    /**
     * @Desc     重定向
     * @Author   hxy
     * @Time     2019/10/24 13:07
     */
    fun returnRedirect(path: String): NsRespone {
        this.setHeader("location", path)
        this.status = HttpResponseStatus.FOUND
        return this
    }
}