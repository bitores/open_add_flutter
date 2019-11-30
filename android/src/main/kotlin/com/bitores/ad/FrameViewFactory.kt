package com.bitores.ad_example

import android.app.Activity
import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class FrameViewFactory(private val messenger: BinaryMessenger,val activity: Activity): PlatformViewFactory(StandardMessageCodec.INSTANCE) {


    override fun create(p0: Context?, p1: Int, p2: Any?): PlatformView {

        return  FlutterFrameView(p0!!,messenger,p1,activity)
    }
}