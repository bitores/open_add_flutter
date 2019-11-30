
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';


class FrameView extends StatefulWidget {
  const FrameView({
    Key key,
    this.onFrameViewCreated,
  }) : super(key: key);

  final FrameViewCreatedCallback onFrameViewCreated;

  @override
  State<StatefulWidget> createState() => _FrameViewState();
}

typedef void FrameViewCreatedCallback(FrameViewController controller);

class _FrameViewState extends State<FrameView> {
  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'hua.ad/frameview',
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    }
    return Text('');
  }

  void _onPlatformViewCreated(int id) {
    if (widget.onFrameViewCreated == null) {
      return;
    }
    widget.onFrameViewCreated(new FrameViewController._(id));
  }
}

class FrameViewController {
  FrameViewController._(int id) : _channel = new MethodChannel('hua.ad/frameview$id');
  final MethodChannel _channel;

  /****
   * 穿山甲开屏调用  广告id
   */
  Future<void> loadSplashAd(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loadSplashAd', codeId);
  }
  /****
   * 穿山甲native调用  广告id
   */
  Future<void> loaddBannerCsj(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loaddBannerCsj', codeId);
  }

  Future<void> loadFeedAdCsj(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loadFeedAd', codeId);
  }


  Future<void> loadNativeAd(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loadNativeAd', codeId);
  }

  // ignore: slash_for_doc_comments
  /******百度***/
  Future<void> loadBannerBaidu(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loadBannerBaidu', codeId);
  }
  Future<void> loadFeedAdBaidu(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loadFeedAdBaidu', codeId);
  }
  Future<void> loadSplashAdBaidu(String codeId) async {
    assert(codeId != null);
    return _channel.invokeMethod('loadSplashAdBaidu', codeId);
  }

// ignore: slash_for_doc_comments
  /**gdt****/
  Future<void> loadSplashAdGdt(String appId,String codeId) async {
    assert(codeId != null);
    var arrays=List();
    arrays.add(appId);
    arrays.add(codeId);
    return _channel.invokeMethod('loadSplashAdGdt', arrays);
  }

}
