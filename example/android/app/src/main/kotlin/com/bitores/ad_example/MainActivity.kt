package com.bitores.ad_example

import android.os.Bundle

import com.bitores.ad.BaseActivity

import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
  }
}
