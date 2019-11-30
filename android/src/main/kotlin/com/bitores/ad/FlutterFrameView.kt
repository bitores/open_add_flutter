package com.bitores.ad_example

import android.app.Activity
import android.content.Context
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.MainThread
import androidx.core.app.CoreComponentFactory
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.baidu.mobads.*
import com.bumptech.glide.Glide
import com.bytedance.sdk.openadsdk.*
import com.bitores.ad.WeakHandler
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import java.util.ArrayList
import com.bitores.ad.R
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.constants.LoadAdParams
import com.qq.e.comm.pi.POFactory
import com.qq.e.comm.util.AdError
import org.json.JSONObject
import kotlin.math.min

class FlutterFrameView(val mContext: Context, val messenger: BinaryMessenger, val id: Int, val activity: Activity) : PlatformView, MethodChannel.MethodCallHandler, WeakHandler.IHandler {


    private var frameView: ViewGroup? = null
    private var methodChannel: MethodChannel? = null
    private val data: MutableList<TTFeedAd> = mutableListOf()

    // val mHandler = WeakHandler(this)
    companion object {

        val MSG_GO_MAIN = 1
        val AD_TIME_OUT = 5000L
    }

    init {

        frameView = FrameLayout(mContext)
        methodChannel = MethodChannel(messenger, "hua.ad/frameview$id")
        methodChannel?.setMethodCallHandler(this)
        //mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT)
        // TogetherAdSplash.loadSplashAd(mContext,"801121648",frameView!!)
        // TogetherAdSplash.loadSplashAd(context,"801121648",frameView!!)

    }


    override fun onMethodCall(p0: MethodCall, p1: MethodChannel.Result) {
        when (p0.method) {
            "loadSplashAdBaidu" -> {
                println("FlutterFrameView --loadSplashAdBaidu...")
                val code = p0.arguments as String
                loadSplashAdBaidu(code)

            }
            "loadSplashAd" -> {
                println("FlutterFrameView --loadSplashAd...")
                val code = p0.arguments as String
                loadSplashAd(code, p1)
            }
            "loadFeedAd" -> {
                val code = p0.arguments as String
                loadFeedAd(code)
                p1.success(null)

            }
            "loadNativeAd" -> {
                val code = p0.arguments as String
                loadNativeAd(code)
                p1.success(null)
            }
            "loadBannerBaidu" -> {
                val code = p0.arguments as String
                loadBannerBaidu(code)
                p1.success(null)
            }

            "loadFeedAdBaidu" -> {
                val code = p0.arguments as String
                loadFeedAdBaidu(code)
                p1.success(null)
            }
            "loadSplashAdGdt" -> {
                val code = p0.arguments as List<String>
                loadSplashAdGdt(code[0],code[1])
                p1.success(null)
            }


            else ->
                p1.notImplemented()

        }

    }


    override fun getView(): View {
        return frameView!!
    }

    override fun dispose() {

    }

    //901121737
    fun loadFeedAd(code: String) {


        val adSlot = AdSlot.Builder()
                .setCodeId(code)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(10) //请求广告数量为1到3条
                .build()
        TTAdSdk.getAdManager().createAdNative(mContext).loadFeedAd(adSlot, object : TTAdNative.FeedAdListener {
            override fun onFeedAdLoad(p0: MutableList<TTFeedAd>?) {

                println("onFeedAdLoad" + p0?.size)
                if (p0?.size!! > 0) {
                    data.addAll(p0)
                    frameView?.removeAllViews()
                    //frameView?.addView(p0[0].adView)
                    //frameView?.addView(p0[0].adView)
                    val view: LinearLayout = LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_small_pic, frameView, false) as LinearLayout
                    frameView?.addView(view)
                    setFeedAdData(view, p0[0])
                    // frameView.add

                    println("onFeedAdLoad  " + p0[0].description)
                }
                // RxBus.instance.post(FeedEvent(p0))


            }

            override fun onError(p0: Int, p1: String?) {

            }

        })
    }

    //801121648
    //开屏广告是否已经加载
    private var mHasLoaded: Boolean = false


    override fun handleMsg(msg: Message?) {
        if (msg?.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                goToMainActivity()
            }
        }
    }

    /****
     * 开屏
     */
    fun loadSplashAd(code: String, p1: MethodChannel.Result) {

        val adSlot = AdSlot.Builder()
                .setCodeId(code)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build()

        TTAdSdk.getAdManager().createAdNative(mContext).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            @MainThread
            override fun onError(code: Int, message: String) {
                mHasLoaded = true

                goToMainActivity()
            }

            @MainThread
            override fun onTimeout() {
                mHasLoaded = true
                goToMainActivity()
            }

            @MainThread
            override fun onSplashAdLoad(ad: TTSplashAd?) {

                mHasLoaded = true
                //mHandler.removeCallbacksAndMessages(null)
                if (ad == null) {
                    return
                }
                flag = false
                //获取SplashView
                val temp=ad as  com.bytedance.sdk.openadsdk.core.splash.c 
                val msg=Message()
                msg.what=1
                temp.a(msg)
               // ad.setNotAllowSdkCountdown()
                val view = ad.splashView
            
                //view.a()
                frameView?.removeAllViews()
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                frameView?.addView(view)
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();
                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View, type: Int) {

                    }

                    override fun onAdShow(view: View, type: Int) {

                    }

                    override fun onAdSkip() {
                        goToMainActivity()

                    }

                    override fun onAdTimeOver() {
                        goToMainActivity()
                    }
                })
            }
        }, 5000)
        p1.success(null)


    }

    private var flag = false

    fun goToMainActivity() {
        if (!flag) {
            RxBus.instance.post(FrameEvent())
            flag = true
        }
    }

    fun loadSplashAdBaidu(code: String) {
        AdSettings.setSupportHttps(false)
        val ad = SplashAd(activity, frameView, object : SplashLpCloseListener {
            override fun onAdFailed(p0: String?) {
                goToMainActivity()
            }

            override fun onAdDismissed() {
                goToMainActivity()
            }

            override fun onAdPresent() {

            }

            override fun onAdClick() {

            }

            override fun onLpClosed() {

            }

        }, code, true)

    }


    /*****
     * banner
     */
    fun loadNativeAd(codeId: String) {

        val adSlot = AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 257)
                .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .setAdCount(1)
                .build()


        TTAdSdk.getAdManager().createAdNative(mContext).loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
            override fun onNativeAdLoad(p0: MutableList<TTNativeAd>?) {
                println("loadNativeAd ${p0?.size}")
                if (p0?.size!! > 0) {
                    frameView?.removeAllViews()

                    val bannerView = LayoutInflater.from(mContext).inflate(com.bitores.ad.R.layout.native_banner, frameView, false)
                            ?: return

                    frameView?.addView(bannerView)

                    setFeedAdData(bannerView, p0[0])
                    // frameView.add


                }

            }


            override fun onError(p0: Int, p1: String?) {

            }

        })

    }

    private fun setFeedAdData(nativeView: View, nativeAd: TTNativeAd) {

        nativeView.findViewById<TextView>(R.id.tv_native_ad_title)?.text = nativeAd.title
        nativeView.findViewById<TextView>(R.id.tv_native_ad_desc)?.text = nativeAd.description
        view.findViewById<TextView>(R.id.tv_source).text = "穿山甲广告"
        if (nativeAd.imageList != null && !nativeAd.imageList.isEmpty()) {
            val image = nativeAd.imageList[0]
            if (image != null && image.isValid) {
                val im = nativeView.findViewById<ImageView>(R.id.iv_native_image)
                Glide.with(mContext).load(image.imageUrl).into(im)
            }
        }
        val btn = nativeView.findViewById<View>(R.id.btn_native_creative) as Button
        //可根据广告类型，为交互区域设置不同提示信息
        when (nativeAd.interactionType) {
            TTAdConstant.INTERACTION_TYPE_DOWNLOAD -> {

            }
            TTAdConstant.INTERACTION_TYPE_DIAL -> {
                //mCreativeButton?.visibility = View.VISIBLE
                btn?.text = "立即拨打"
            }
            TTAdConstant.INTERACTION_TYPE_LANDING_PAGE, TTAdConstant.INTERACTION_TYPE_BROWSER -> {
                // mCreativeButton?.visibility = View.VISIBLE
                btn?.text = "查看详情"
            }
            else -> {
                //mCreativeButton?.visibility = View.GONE
                Toast.makeText(mContext, "交互类型异常", Toast.LENGTH_SHORT)
            }
        }
        //可以被点击的view, 也可以把nativeView放进来意味整个广告区域可被点击
        val clickViewList = ArrayList<View>()
        clickViewList.add(nativeView)
        //触发创意广告的view（点击下载或拨打电话）
        val creativeViewList = ArrayList<View>()
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        creativeViewList.add(nativeView)
        creativeViewList.add(btn)

        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        nativeAd.registerViewForInteraction(nativeView as ViewGroup, clickViewList, creativeViewList, null, object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(view: View, ad: TTNativeAd?) {
                if (ad != null) {
                    //   TToast.show(mContext, "广告" + ad.title + "被点击")
                    Toast.makeText(mContext, "广告被点击", Toast.LENGTH_SHORT)
                }
            }

            override fun onAdCreativeClick(view: View, ad: TTNativeAd?) {
                if (ad != null) {
                    //  TToast.show(mContext, "广告" + ad.title + "被创意按钮被点击")
                }
            }

            override fun onAdShow(ad: TTNativeAd?) {
                if (ad != null) {
                    //TToast.show(mContext, "广告" + ad.title + "展示")
                }
            }
        })

    }






    fun loadBannerBaidu(code: String) {
        val adView = AdView(activity, code)
        adView.setListener(object : AdViewListener {
            override fun onAdFailed(p0: String?) {

            }

            override fun onAdShow(p0: JSONObject?) {

            }

            override fun onAdClick(p0: JSONObject?) {

            }

            override fun onAdReady(p0: AdView?) {

            }

            override fun onAdSwitch() {

            }

            override fun onAdClose(p0: JSONObject?) {

            }

        })
        val dm = DisplayMetrics()
        (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(dm)

        val winW = dm.widthPixels
        val winH = dm.heightPixels
        val width = min(winW, winH)
        val height = width * 3 / 20
        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
        val rllp = RelativeLayout.LayoutParams(width, height)
        //rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        frameView?.removeAllViews()
        frameView?.addView(adView)
    }

    //3143845
    fun loadFeedAdBaidu(code: String) {
        println("loadFeedAdBaidu  ${code}")
        val baidu = BaiduNative(activity, code, object : BaiduNative.BaiduNativeNetworkListener {

            override fun onNativeFail(arg0: NativeErrorCode) {
                Log.w("ListViewActivity", "onNativeFail reason:" + arg0.name)
            }

            override fun onNativeLoad(arg0: List<NativeResponse>?) {
                // 一个广告只允许展现一次，多次展现、点击只会计入一次
                println("loadFeedAdBaidu  ${arg0?.size}")
                if (arg0 != null && arg0.isNotEmpty()) {

                    val response = arg0[0]
                    println("loadFeedAdBaidu  ${response?.desc}")
                    val bannerView = LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_small_pic, frameView, false)
                   // val aq = AQuery(bannerView)
                    //aq.id()
                    frameView?.removeAllViews()
                    frameView?.addView(bannerView)
                    println("loadFeedAdBaidu  ${frameView}")
                    bannerView.findViewById<TextView>(R.id.tv_native_ad_title)?.text = response.title
                    bannerView.findViewById<TextView>(R.id.tv_native_ad_desc)?.text = response.desc
                    view.findViewById<TextView>(R.id.tv_source).text = "百度广告"

                    val im = bannerView.findViewById<ImageView>(R.id.iv_native_image)
                    Glide.with(mContext).load(response.imageUrl).into(im)
                    val btn = bannerView.findViewById<View>(R.id.btn_native_creative) as Button
                    btn.text =  if (response.isDownloadApp) "下载" else "查看"
                    response.recordImpression(bannerView)
                    bannerView.setOnClickListener {
                        response.handleClick(bannerView)
                    }



                }
            }

        })

        /**
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        val requestParameters = RequestParameters.Builder()
                .downloadAppConfirmPolicy(
                        RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE).build()

        baidu.makeRequest(requestParameters)



    }
    
    fun loadSplashAdGdt(appId :String,code: String){
       // POFactory
        //POFactoryImpl

       val splashAD = SplashAD(activity, frameView, appId, code, object : SplashADListener{
           override fun onADExposure() {

           }

           override fun onADDismissed() {
           }

           override fun onADPresent() {
           }

           override fun onNoAD(p0: AdError?) {
           }

           override fun onADClicked() {
           }

           override fun onADTick(p0: Long) {
           }

       },3000)
        val params = LoadAdParams()
        //params.loginAppId = "testAppId"
        //params.loginOpenid = "testOpenId"

        //params.uin = "testUin"
       // splashAD.setLoadAdParams(params)
        splashAD.fetchAndShowIn(frameView)

    }

}