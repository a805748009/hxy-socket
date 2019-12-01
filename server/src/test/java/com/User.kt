package com

import nafos.server.relation.BaseUser

/**
 * @Description 用户类
 * @Author      xinyu.huang
 * @Time        2019/12/1 13:23
 */
data class User(
        var userName:String? = null,
        var password:String? = null
) :BaseUser<String>{
    override fun getUserId(): String {
        return userName!!
    }

}

