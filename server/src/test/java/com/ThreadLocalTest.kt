package com

import kotlinx.coroutines.*

data class c(
        var s: String = "2"
)

fun main() {
    val threadLocal = ThreadLocal<c>()
    threadLocal.set(c("00"))
    for (i in 0..1500) {
        GlobalScope.launch(Dispatchers.Default + threadLocal.asContextElement(c(i.toString()))) {
            println(Thread.currentThread().name + "=" + i.toString())
            println(Thread.currentThread().name + "=" + threadLocal.get().s + "=" + i.toString())
        }
    }
    Thread.sleep(500)
    println("===" + threadLocal.get().s)

    while (true) {

    }
}