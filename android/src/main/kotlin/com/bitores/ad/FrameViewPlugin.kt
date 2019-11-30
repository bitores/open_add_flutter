package com.bitores.ad_example

import android.app.Activity
import com.bitores.ad.AdPlugin
import com.rumtel.ad.helper.splash.TogetherAdSplash
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

object FrameViewPlugin {

    const val ChannelName = "bitores.ad/frameview"
    const val ChannelKey= "bitores.ad/ad"

    @JvmStatic
    fun registerWith(registry:PluginRegistry,activity: Activity) {

        val registrys=  registry.registrarFor(ChannelKey)
        registrys.platformViewRegistry().registerViewFactory(ChannelName,FrameViewFactory(registrys.messenger(),activity))
        AdPlugin.registerWith(registry.registrarFor(ChannelName))

    }



}