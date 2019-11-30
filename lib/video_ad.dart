
import 'package:flutter/services.dart';

/*激励视频**/
class VideoAd{

  static Future<void>  initVieo(String code) async {
    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('loadRewardVideoAd',code);
      print("channel    $data");
    } catch(e) {
      print(e.toString());
    }

  }

  static Future<void> showVieo() async {

    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('loadRewardVideoAdShow');
      print("showVieo    $data");
    } catch(e) {
      print(e.toString());
    }

  }

  static Future<void>  initVieoBaidu(String code) async {
    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('loadRewardVideoAdBaidu',code);
      print("channel    $data");
    } catch(e) {
      print(e.toString());
    }

  }

  static Future<void> showVieoBaidu() async {
    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('loadRewardVideoAdBaiduShow');
      print("showVieo    $data");
    } catch(e) {
      print(e.toString());
    }

  }
// 5001121    APP测试媒体
  static Future<void> initAdCsj(var name) async {
    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('initAdCsj',name);
      print("showVieo    $data");
    } catch(e) {
      print(e.toString());
    }
  }

  static Future<void> initAdBaidu(String code) async {
    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('initAdBaidu',code);
      print("showVieo    $data");
    } catch(e) {
      print(e.toString());
    }

  }
  static Future<void> initAdGdt(String code) async {
    try {
      final channel = const MethodChannel('hua.ad.video');
      final String data = await channel.invokeMethod('initAdGdt',code);
      print("showVieo    $data");
    } catch(e) {
      print(e.toString());
    }

  }
}
