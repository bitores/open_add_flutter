package com.bitores.ad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.baidu.mobads.rewardvideo.RewardVideoAd
import com.bytedance.sdk.openadsdk.*
import com.bitores.ad_example.FrameEvent
import com.bitores.ad_example.FrameViewPlugin
import com.bitores.ad_example.RxBus
import com.bitores.ad_example.VideoEvent
import com.rumtel.ad.TogetherAd

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

import java.util.ArrayList

open class BaseActivity : FlutterActivity() {

    companion object{
        var eventSink: EventChannel.EventSink?=null
        val adList= mutableListOf<TTFeedAd>()
    }
    private val SHARE_CHANNEL = "channel:Chenli"

    private val list= arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TogetherAd.initBaiduAd(this,"e866cfb0", mutableMapOf())  e866cfb0  b8cad9fa
        //TogetherAd.initCsjAd(this,"5001121","APP测试媒体",true)
       // TogetherAd.initCsjAd(application,"5001121","APP测试媒体",mutableMapOf())
       // TogetherAd.initBaiduAd(application,"e866cfb0", mutableMapOf())
        //TogetherAd.initGDTAd(application,"d3bdb4cf-e72b-4040-9439-9856020fbe8b",mutableMapOf())

        // setContentView(R.layout.mian)
        //TTAdSdk.getAdManager().requestPermissionIfNecessary(this)
        FrameViewPlugin.registerWith(this,this)

        // 5026313   全民看书    926313166
        //baidu:3,gdt:7,   5001121  APP测试媒体      901121987
       // TTAdSdk.getAdManager().requestPermissionIfNecessary(this)\


        MethodChannel(this.flutterView,"hua.ad.video").setMethodCallHandler { methodCall, result ->
            when(methodCall.method){
                //视频
                "loadRewardVideoAd" ->{
                    val  code=methodCall.arguments as String
                    loadRewardVideoAd(code)
                    result.success(null)
                }
                "loadRewardVideoAdShow"->{
                    if (videoAd==null){
                        result.error("100","视频加载失败","")
                    }else{
                        videoAd?.showRewardVideoAd(this)
                        result.success(null)
                    }
                }
                "loadRewardVideoAdBaidu" ->{
                    val  code=methodCall.arguments as String
                    loadRewardVideoAdBaidu(code)
                    result.success(null)
                }
                "loadRewardVideoAdBaiduShow"->{
                    if (mRewardVideoAd==null){
                        result.error("101","视频加载失败","")
                    }else{
                        mRewardVideoAd?.show()
                        result.success(null)
                    }
                }
                "initAdCsj" ->{
                    TogetherAd.initCsjAd(application,methodCall.argument<String>("code")!!,methodCall.argument<String>("name")!!,mutableMapOf())
                }
                "initAdBaidu" ->{
                    val  code=methodCall.arguments as String
                    TogetherAd.initBaiduAd(application,code, mutableMapOf())
                    TogetherAd.initGDTAd(application,"d3bdb4cf-e72b-4040-9439-9856020fbe8b",mutableMapOf())
                }
                "initAdGdt" ->{
                    val  code=methodCall.arguments as String
                    TogetherAd.initGDTAd(application,code,mutableMapOf())
                }
                "checkSelfPermission"->{
                    val flag=checkSelfPermission(application, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    result.success(!flag)
                }
                "handlefPermission"->{
                    TTAdSdk.getAdManager().requestPermissionIfNecessary(this)
                }


            }
        }


        EventChannel(this.flutterView,"hua.ad.event").setStreamHandler(object : EventChannel.StreamHandler{
            @SuppressLint("CheckResult")
            override fun onListen(p0: Any?, p1: EventChannel.EventSink?) {

                RxBus.instance?.toObservable(FrameEvent::class.java)?.subscribe {
                    print("event $it   p1  $p1")
                    p1?.success("onAdSkip")
                }

                RxBus.instance?.toObservable(VideoEvent::class.java)?.subscribe {
                    print("VideoEvent $it   p1  $p1")
                    p1?.success(it.code)
                }

                
                eventSink=p1
                println("hua.ad.event. eventSink...$eventSink ")
                /*RxBus.instance?.toObservable(NextEvent::class.java)?.subscribe {
                    print("event $it   p1  $p1")
                    p1?.success("next")
                }*/
                if (list.size>0){
                    println("hua.ad.event. success...$eventSink ")
                    p1?.success("next")
                    list.remove("next")
                }


                //eventSink=p1
            }

            override fun onCancel(p0: Any?) {
                //eventSink=null
            }

        })

    }


    override fun onResume() {
        super.onResume()
        val lackedPermission = ArrayList<String>()

       /* if (!checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (lackedPermission.size == 0) {
            // 权限都已经有了，那么直接调用SDK
            println("lackedPermission== "+lackedPermission.size +"---"+eventSink)
            //RxBus.instance.post(NextEvent())
            eventSink?.success("next")
            if (eventSink==null){
                list.add("next")
            }
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            val requestPermissions = arrayOfNulls<String>(lackedPermission.size)
            lackedPermission.toArray(requestPermissions)

            requestPermissions(requestPermissions, 1000)
        }*/
    }

    //901121365
    private  var videoAd:TTRewardVideoAd?=null
    fun loadRewardVideoAd(code:String){
        val adSlot1 = AdSlot.Builder()
                .setCodeId(code)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build()

        TTAdSdk.getAdManager().createAdNative(application).loadRewardVideoAd(adSlot1,object :TTAdNative.RewardVideoAdListener {
            override fun onRewardVideoAdLoad(p0: TTRewardVideoAd?) {
                videoAd=p0
                videoAd?.setRewardAdInteractionListener(object :TTRewardVideoAd.RewardAdInteractionListener{
                    override fun onRewardVerify(p0: Boolean, p1: Int, p2: String?) {
                        // RxBus.instance.post(VideoEvent("onAdClose"))
                    }

                    override fun onSkippedVideo() {
                        //跳过
                        RxBus.instance.post(VideoEvent("onSkippedVideo"))
                    }

                    override fun onAdShow() {

                    }

                    override fun onAdVideoBarClick() {
                        RxBus.instance.post(VideoEvent("onAdClick"))
                    }

                    override fun onVideoComplete() {
                        RxBus.instance.post(VideoEvent("playCompletion"))
                    }

                    override fun onAdClose() {
                        RxBus.instance.post(VideoEvent("onAdClose"))
                    }

                    override fun onVideoError() {

                    }

                })

               // p0?.showRewardVideoAd(this@BaseActivity)
            }

            override fun onRewardVideoCached() {

            }

            override fun onError(p0: Int, p1: String?) {

            }


        })

    }



    private fun checkSelfPermission(context: Context, permission: String): Boolean {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                val method = Context::class.java.getMethod("checkSelfPermission",
                        String::class.java)
                return method.invoke(context, permission) as Int == PackageManager.PERMISSION_GRANTED
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }


    var  mRewardVideoAd:RewardVideoAd?=null
    val TAG="baiduad"
    fun loadRewardVideoAdBaidu(code: String){

        mRewardVideoAd = RewardVideoAd(this, code, object : RewardVideoAd.RewardVideoAdListener {
            override fun onVideoDownloadSuccess() {
                // 视频缓存成功
                // 说明：如果想一定走本地播放，那么收到该回调之后，可以调用show
                Log.i(TAG, "onVideoDownloadSuccess,isReady=" + mRewardVideoAd?.isReady)
            }

            override fun onVideoDownloadFailed() {
                // 视频缓存失败，如果想走本地播放，可以在这儿重新load下一条广告，最好限制load次数（4-5次即可）。
                Log.i(TAG, "onVideoDownloadFailed")
            }

            override fun playCompletion() {
                // 播放完成回调，媒体可以在这儿给用户奖励
                Log.i(TAG, "playCompletion")
                RxBus.instance.post(VideoEvent("playCompletion"))
            }

            override fun onAdShow() {
                // 视频开始播放时候的回调
                Log.i(TAG, "onAdShow")
            }

            override fun onAdClick() {
                // 广告被点击的回调
                Log.i(TAG, "onAdClick")
                RxBus.instance.post(VideoEvent("onAdClick"))
            }

            override fun onAdClose(playScale: Float) {
                // 用户关闭了广告
                // 说明：关闭按钮在mssp上可以动态配置，媒体通过mssp配置，可以选择广告一开始就展示关闭按钮，还是播放结束展示关闭按钮
                // 建议：收到该回调之后，可以重新load下一条广告,最好限制load次数（4-5次即可）
                // playScale[0.0-1.0],1.0表示播放完成，媒体可以按照自己的设计给予奖励
                Log.i(TAG, "onAdClose$playScale")
                if (playScale<1.0){
                    //跳过
                    RxBus.instance.post(VideoEvent("onSkippedVideo"))
                }
                RxBus.instance.post(VideoEvent("onAdClose"))
            }

            override fun onAdFailed(arg0: String) {
                // 广告失败回调 原因：广告内容填充为空；网络原因请求广告超时
                // 建议：收到该回调之后，可以重新load下一条广告，最好限制load次数（4-5次即可）
                Log.i(TAG, "onAdFailed   $arg0")
                //loadRewardVideoAdBaidu(code)

            }
        }, false)

    }


    //TogetherAd.initCsjAd(application,"5026313","全民看书",xunFeiIdMap)
   //826313791
    //TogetherAdSplash.loadBannerAd(this,"926313166",fl)

    //TogetherAdSplash.loadSplashAd(this,"801121648",findViewById<FrameLayout>(R.id.fl))


}
