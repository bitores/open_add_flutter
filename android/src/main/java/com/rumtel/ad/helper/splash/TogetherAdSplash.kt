package com.rumtel.ad.helper.splash

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import com.baidu.mobads.SplashAd
import com.baidu.mobads.SplashAdListener
import com.bytedance.sdk.openadsdk.*
import com.bitores.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.TogetherAd.mContext
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.util.*

/* 
 * (●ﾟωﾟ●) 开屏的广告
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
object TogetherAdSplash : AdBase {

    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null

    @Volatile
    private var stop = false

    /**
     * 显示开屏广告
     *
     * @param splashConfigStr "baidu:2,gdt:8"
     * @param adsParentLayout 容器
     * @param adListener      监听
     */
    fun showAdFull(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {
        stop = false
        startTimerTask(adListener)

        when (AdRandomUtil.getRandomAdName(splashConfigStr)) {
            AdNameType.BAIDU -> showAdFullBaiduMob(activity, splashConfigStr, adConstStr, adsParentLayout, adListener)

            AdNameType.CSJ -> showAdFullCsj(activity, splashConfigStr, adConstStr, adsParentLayout, adListener)
            else -> {
                if (stop) {
                    return
                }
                cancelTimerTask()

                adListener.onAdFailed(mContext.getString(R.string.all_ad_error))
                loge(mContext.getString(R.string.all_ad_error))
            }
        }
    }

    /**
     * 腾讯广点通
     */
    private fun showAdFullGDT(
        @NonNull activity: Activity,
        splashConfigStr: String,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {


    }



     fun loadBannerAd(ac:Activity,codeId: String,fl: FrameLayout) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
            .setCodeId(codeId) //广告位id
            .setSupportDeepLink(true)
            .setImageAcceptedSize(600, 257)
            .build()
        //step5:请求广告，对请求回调的广告作渲染处理
        TTAdSdk.getAdManager().createAdNative(ac).loadBannerAd(adSlot, object : TTAdNative.BannerAdListener {

            override fun onError(code: Int, message: String) {
                //TToast.show(this@BannerActivity, "load error : $code, $message")
                fl.removeAllViews()
            }

            override fun onBannerAdLoad(ad: TTBannerAd?) {
                if (ad == null) {
                    return
                }
                val bannerView = ad.bannerView ?: return
//设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
                ad.setSlideIntervalTime(30 * 1000)
               fl.removeAllViews()
                fl.addView(bannerView)
                //设置广告互动监听回调
                ad.setBannerInteractionListener(object : TTBannerAd.AdInteractionListener {
                    override fun onAdClicked(view: View, type: Int) {
                       //TToast.show(mContext, "广告被点击")
                    }

                    override fun onAdShow(view: View, type: Int) {
                        //TToast.show(mContext, "广告展示")
                    }
                })
                //（可选）设置下载类广告的下载监听
               // bindDownloadListener(ad)
                //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
                ad.setShowDislikeIcon(object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String) {

                        //用户选择不喜欢原因后，移除广告展示
                        //mBannerContainer.removeAllViews()
                    }

                    override fun onCancel() {

                    }
                })


            }
        })
    }

/****************************穿山甲********************************/

  
    /*****
     *开屏
     */
     fun loadSplashAdCsj(ac:Context,codeId: String,fl: ViewGroup,adListener: AdListenerSplashFull) {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
            .setCodeId(codeId)
            .setSupportDeepLink(true)
            .setImageAcceptedSize(1080, 1920)
            .build()
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        TTAdSdk.getAdManager().createAdNative(ac).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            @MainThread
            override fun onError(code: Int, message: String) {
                adListener.onAdFailed(AdNameType.CSJ.type)
                println("onError  $code  $message")
            }

            @MainThread
            override fun onTimeout() {
                adListener.onAdFailed(AdNameType.CSJ.type)
                println("onTimeout  ")
            }

            @MainThread
            override fun onSplashAdLoad(ad: TTSplashAd?) {

                println("开屏广告请求成功  ")
               // mHandler.removeCallbacksAndMessages(null)
                if (ad == null) {
                    return
                }
                //获取SplashView
                val view = ad.splashView
                fl.removeAllViews()
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                fl.addView(view)
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View, type: Int) {

                        println("开屏广告点击")
                    }

                    override fun onAdShow(view: View, type: Int) {

                        println("开屏广告展示")
                    }

                    override fun onAdSkip() {
                        adListener.onAdSkip(AdNameType.CSJ.type)
                        //println("开屏广告跳过")
                    }

                    override fun onAdTimeOver() {

                        println("开屏广告倒计时结束")

                    }
                })
            }
        }, 3000)
    }



    /**
     * 穿山甲
     */
    fun showAdBannerCsj(ac:Context,codeId: String,fl: ViewGroup) {
        try {

            //adListener.onStartRequest(AdNameType.CSJ.type)
            val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                wm.defaultDisplay.getRealSize(point)
            } else {

                wm.defaultDisplay.getSize(point)
            }
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            val adSlot = AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                //.setNativeAdType(AdSlot.TYPE_BANNER)
                .setImageAcceptedSize(600, 257)
                //.setImageAcceptedSize(point.x, point.y)
                .build()


            TTAdSdk.getAdManager().createAdNative(mContext).loadBannerAd(adSlot, object : TTAdNative.BannerAdListener {
                override fun onBannerAdLoad(splashAd: TTBannerAd?) {

                    if (stop) {
                        return
                    }
                    cancelTimerTask()
                    if (splashAd == null) {

                        return
                    }


                    logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.prepared)}")

                    fl.removeAllViews()
                    fl.addView(splashAd.bannerView)

                    splashAd.setBannerInteractionListener(object : TTBannerAd.AdInteractionListener {
                        override fun onAdClicked(view: View?, p1: Int) {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.clicked)}")

                        }


                        override fun onAdShow(p0: View?, p1: Int) {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.exposure)}")
                        }

                    })

                }


                override fun onError(errorCode: Int, errorMsg: String?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    loge("onError  ${AdNameType.CSJ.type}: $errorCode : $errorMsg")
                }
            })
        } catch (e: Exception) {
            if (stop) {
                return
            }
            cancelTimerTask()
            loge("${AdNameType.CSJ.type}: 崩溃异常")
            e.printStackTrace()
        }
    }


    /**
     * 穿山甲
     */
    fun loadInteractionAdCsj(ac:Activity,codeId: String) {
        try {

            //adListener.onStartRequest(AdNameType.CSJ.type)
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            val adSlot = AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    //.setNativeAdType(AdSlot.TYPE_BANNER)
                    .setImageAcceptedSize(600, 257)
                    //.setImageAcceptedSize(point.x, point.y)
                    .build()


            TTAdSdk.getAdManager().createAdNative(mContext).loadInteractionAd(adSlot, object : TTAdNative.InteractionAdListener {
                override fun onInteractionAdLoad(splashAd: TTInteractionAd?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()
                    if (splashAd == null) {

                        return
                    }


                    logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.prepared)}")
                    splashAd.showInteractionAd(ac )

                    /*fl.removeAllViews()
                    splashAd.showInteractionAd(mContext)
                    fl.addView()*/

                    splashAd.setAdInteractionListener(object : TTInteractionAd.AdInteractionListener {
                        override fun onAdClicked() {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.clicked)}")
                        }

                        override fun onAdShow() {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.exposure)}")
                        }

                        override fun onAdDismiss() {

                        }



                    })
                }


                override fun onError(errorCode: Int, errorMsg: String?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    loge("onError  ${AdNameType.CSJ.type}: $errorCode : $errorMsg")
                }
            })
        } catch (e: Exception) {
            if (stop) {
                return
            }
            cancelTimerTask()
            loge("${AdNameType.CSJ.type}: 崩溃异常")
            e.printStackTrace()
        }
    }
    fun loadFullScreenVideoAdCsj(ac:Context,codeId: String) {
        try {

            //adListener.onStartRequest(AdNameType.CSJ.type)
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            val adSlot = AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .setOrientation(TTAdConstant.VERTICAL)
                    .build()


            TTAdSdk.getAdManager().createAdNative(mContext).loadFullScreenVideoAd(adSlot, object : TTAdNative.FullScreenVideoAdListener {
                override fun onFullScreenVideoAdLoad(splashAd: TTFullScreenVideoAd?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()
                    if (splashAd == null) {

                        return
                    }


                    logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.prepared)}")

                    splashAd.showFullScreenVideoAd(ac as Activity)

                    splashAd.setFullScreenVideoAdInteractionListener(object : TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                        override fun onAdShow() {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.prepared)}")
                        }

                        override fun onSkippedVideo() {

                        }

                        override fun onAdVideoBarClick() {

                        }

                        override fun onVideoComplete() {

                        }

                        override fun onAdClose() {

                        }


                    })
                }

                override fun onFullScreenVideoCached() {
                   
                }
                


                override fun onError(errorCode: Int, errorMsg: String?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    loge("onError  ${AdNameType.CSJ.type}: $errorCode : $errorMsg")
                }
            })
        } catch (e: Exception) {
            if (stop) {
                return
            }
            cancelTimerTask()
            loge("${AdNameType.CSJ.type}: 崩溃异常")
            e.printStackTrace()
        }
    }


    /**
     * 百度Mob
     */
    private fun showAdFullBaiduMob(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {
        adListener.onStartRequest(AdNameType.BAIDU.type)

        SplashAd(mContext, adsParentLayout, object : SplashAdListener {
            override fun onAdPresent() {
                if (stop) {
                    return
                }
                cancelTimerTask()

                adListener.onAdPrepared(AdNameType.BAIDU.type)
                logd("${AdNameType.BAIDU.type}: ${mContext.getString(R.string.prepared)}")
            }

            override fun onAdDismissed() {
                logd("${AdNameType.BAIDU.type}: ${mContext.getString(R.string.dismiss)}")
                adListener.onAdDismissed()
            }

            override fun onAdFailed(s: String) {
                if (stop) {
                    return
                }
                cancelTimerTask()
                loge("${AdNameType.BAIDU.type}: $s")
                val newConfigPreMovie = splashConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdFull(activity, newConfigPreMovie, adConstStr, adsParentLayout, adListener)
            }

            override fun onAdClick() {
                logd("${AdNameType.BAIDU.type}: ${mContext.getString(R.string.clicked)}")
                adListener.onAdClick(AdNameType.BAIDU.type)
            }

        }, TogetherAd.idMapBaidu[adConstStr], true)
    }

    /**
     * 穿山甲
     */
    private fun showAdFullCsj(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {
        try {

            adListener.onStartRequest(AdNameType.CSJ.type)
            val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                wm.defaultDisplay.getRealSize(point)
            } else {

                wm.defaultDisplay.getSize(point)
            }
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAd.idMapCsj[adConstStr])
                .setSupportDeepLink(true)
                .setImageAcceptedSize(point.x, point.y)
                .build()
            TTAdSdk.getAdManager().createAdNative(mContext).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
                override fun onSplashAdLoad(splashAd: TTSplashAd?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    if (splashAd == null) {
                        loge("${AdNameType.CSJ.type}: 广告是 null")
                        val newSplashConfigStr = splashConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                        showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, adListener)
                        return
                    }

                    adListener.onAdPrepared(AdNameType.CSJ.type)
                    logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.prepared)}")

                    adsParentLayout.removeAllViews()
                    adsParentLayout.addView(splashAd.splashView)

                    splashAd.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                        override fun onAdClicked(view: View?, p1: Int) {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.clicked)}")
                            adListener.onAdClick(AdNameType.CSJ.type)
                        }

                        override fun onAdSkip() {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.dismiss)}")
                            adListener.onAdDismissed()
                        }

                        override fun onAdShow(p0: View?, p1: Int) {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.exposure)}")
                        }

                        override fun onAdTimeOver() {
                            logd("${AdNameType.CSJ.type}: ${mContext.getString(R.string.dismiss)}")
                            adListener.onAdDismissed()
                        }
                    })
                }

                override fun onTimeout() {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    loge("${AdNameType.CSJ.type}: ${mContext.getString(R.string.timeout)}")
                    val newSplashConfigStr = splashConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, adListener)
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    loge("${AdNameType.CSJ.type}: $errorCode : $errorMsg")
                    val newSplashConfigStr = splashConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, adListener)
                }
            }, 2500)//超时时间，demo 为 2000
        } catch (e: Exception) {
            if (stop) {
                return
            }
            cancelTimerTask()

            loge("${AdNameType.CSJ.type}: 崩溃异常")
            val newSplashConfigStr = splashConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
            showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, adListener)
        }
    }





    /**
     * 监听器
     */
    abstract interface AdListenerSplashFull {
        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)

        fun  onAdSkip(channel: String)
    }

    /**
     * 取消超时任务
     */
    private fun cancelTimerTask() {
        stop = false
        timer?.cancel()
        overTimerTask?.cancel()
    }

    /**
     * 开始超时任务
     */
    private fun startTimerTask(listener: AdListenerSplashFull) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask = OverTimerTask(listener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 超时任务
     */
    private class OverTimerTask(listener: AdListenerSplashFull) : TimerTask() {

        private var weakReference: AdListenerSplashFull?
//        private var weakRefContext: Activity?

        init {
            weakReference = listener
//            weakRefContext = mContext
        }

        override fun run() {
            stop = true
//            weakRefContext?.runOnUiThread {
            weakReference?.onAdFailed(mContext.getString(R.string.timeout))
            loge(mContext.getString(R.string.timeout) + weakReference)
            timer = null
            overTimerTask = null
//            }
            weakReference = null
//            weakRefContext = null
        }
    }
}