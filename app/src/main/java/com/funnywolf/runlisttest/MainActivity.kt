package com.funnywolf.runlisttest

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callback.setOnClickListener {
            doCallback()
        }
        rxJava.setOnClickListener {
            doRxJava()
        }
        coroutine.setOnClickListener {
            doCoroutine()
        }
        runList.setOnClickListener {
            doRunList()
        }
        logText.setOnClickListener {
            logText.text = ""
        }
    }

    private fun doCallback() {
        log("doCallback", true)
        Thread {
            val a = getDataA(1)
            runOnUiThread {
                val resultA = doSomethingOnMainA(a)
                Thread {
                    val b = getDataB(resultA)
                    runOnUiThread {
                        val resultB = doSomethingOnMainB(b)
                        log("doCallback: Result = $resultB")
                    }
                }.start()
            }
        }.start()
    }

    private fun doRxJava() {
        log("doRxJava", true)
        val d = Observable.just(1)
                .observeOn(Schedulers.io())
                .map { getDataA(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .map { doSomethingOnMainA(it) }
                .observeOn(Schedulers.io())
                .map { getDataB(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .map { doSomethingOnMainB(it) }
                .subscribe { log("doRxJava: Result = $it") }
    }

    private fun doCoroutine() {
        log("doCoroutine", true)
        GlobalScope.launch(Dispatchers.Main) {
            val a = withContext(Dispatchers.IO) { getDataA(1) }
            val resultA = doSomethingOnMainA(a)
            val b = withContext(Dispatchers.IO) { getDataB(resultA) }
            val resultB = doSomethingOnMainB(b)
            log("doCoroutine: Result = $resultB")
        }
    }

    private fun doRunList() {
        log("doRunList", true)
        RunList.runOnBackground({ integer -> getDataA(integer) }, 1)
                .runOnUiThread { string -> doSomethingOnMainA(string) }
                .runOnBackground { integer -> getDataB(integer) }
                .runOnUiThread { string -> log("doRunList: Result = ${doSomethingOnMainB(string)}") }
                .start()
    }

    private fun getDataA(i: Int): String {
        log("getDataA: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        return (i + 1).toString()
    }

    private fun doSomethingOnMainA(i: String): Int {
        log("doSomethingOnMainA: ${Thread.currentThread().name}")
        return (i + 1).toInt()
    }

    private fun getDataB(i: Int): String {
        log("getDataB: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        return (i + 1).toString()
    }

    private fun doSomethingOnMainB(i: String): Int {
        log("doSomethingOnMainB: ${Thread.currentThread().name}")
        return (i + 1).toInt()
    }

    private fun log(log: String, clear: Boolean = false) {
        runOnUiThread {
            if (clear) { log }
            else { "${logText.text}\n$log" }
                    .also { logText.text = it }
        }
    }

}