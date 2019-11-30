package com.bitores.ad_example

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxBus private constructor() {
    private val mBus: Subject<Any>

    init {
        mBus = PublishSubject.create<Any>().toSerialized()
    }

    /**
     * 发送事件
     */
    fun post(event: Any) {
        mBus.onNext(event)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }

    /**
     * 判断是否有订阅者
     */
    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    fun reset() {
        mDefaultInstance = null
    }

    companion object {
        @Volatile
        private var mDefaultInstance: RxBus? = null

        val instance: RxBus
            get() {
                if (mDefaultInstance == null) {
                    synchronized(RxBus::class.java) {
                        if (mDefaultInstance == null) {
                            mDefaultInstance = RxBus()
                        }
                    }
                }
                return mDefaultInstance!!
            }
    }

}
