import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:io';
import 'package:flutter/services.dart';
// import 'package:ad/ad.dart';
import 'package:ad/video_ad.dart';
import 'package:ad/frame_view.dart';
import 'package:permission_handler/permission_handler.dart';

// void main() => runApp(MyApp());

 var gdtAppId="1101152570";
void main() async{
   runApp(MaterialApp(home: MyApp(),
       routes: <String, WidgetBuilder> {
         '/screen1': (BuildContext context) =>  FirstScreen(),
         '/screen2': (BuildContext context) =>  FirstScreen1(),
       }
   ));
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

Widget buildAdView(){
  return new Container(
    child: new UiKitView(viewType:  "bitores.ad/frameview"),
  );
  
}

class _MyAppState extends State<MyApp> {

  static const EventChannel eventChannel = EventChannel('bitores.ad.event');
  // static const MethodChannel testChannel = MethodChannel('bitores.ad.test');

  static const MethodChannel ios_channel = const MethodChannel('bitores.ad.ios');

  @override
  void initState() {
    
    super.initState();
    if(Platform.isIOS){
      //ios相关代码
      print('is ios');
      eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
    }else if(Platform.isAndroid){
      //android相关代码
      print('is android');
      eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
    }

    requestPermiss();
   //sdk init
    VideoAd.initAdCsj({"code":"5001121","name":"APP测试媒体"});
    VideoAd.initAdBaidu("e866cfb0");
    //VideoAd.initAdGdt("d3bdb4cf-e72b-4040-9439-9856020fbe8b");
    //激励视频初始化
    VideoAd.initVieo("901121365");
    VideoAd.initVieoBaidu("5925490");

  }





  requestPermiss() async {
    //请求权限
    Map<PermissionGroup, PermissionStatus> permissions = await PermissionHandler().requestPermissions([PermissionGroup.phone,PermissionGroup.storage]);
    //校验权限

    if(permissions[PermissionGroup.storage] != PermissionStatus.granted||permissions[PermissionGroup.phone] != PermissionStatus.granted){
      print("手机状态");
      // bool isOpened = await PermissionHandler().openAppSettings();  //打开设置
      // 弹窗提示
      Navigator.pushNamed(context, '/screen2');
    }else{
      print('---下一个界面');
      //下一个界面 next
      Navigator.pushNamed(context, '/screen1');//
      // Navigator.pushNamed(context, '/screen2');
    }

    

  }


/*  static Future<String> get platformVersion async {
    final String version = await testChannel.invokeMethod('getPlatformVersion');
    return version;
  }*/

  static Future<int> requestNativeAdd(int x, int y) async {
    int result = await ios_channel.invokeMethod('add', {"x": x, "y": y});
    print("requestNativeAdd=$result");
    return result;
  }



  void _onEvent(Object event) {
    print("_onEvent  $event");

    switch(event){

      case 'onAdSkip':
        Navigator.pop(context);
        Navigator.pushNamed(context, '/screen2');
        break;
      case 'next':
        Navigator.pop(context);
        Navigator.pushNamed(context, '/screen1');
        break;
    }
  }

  void _onError(Object event) {

  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: buildAdView(),
        ),
      ),
    );
  }
}
//

class FirstScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('feedAD'),
      ),
      body: new Center(
        child: FrameView(onFrameViewCreated: _onFrameViewCreated,)
      ),
    );
  }


  void _onFrameViewCreated(FrameViewController controller) {
    //穿山甲 开屏广告  广告id
     controller.loadSplashAd('801121648');
     //穿山甲 百度广告  广告id
     controller.loadSplashAdBaidu('2058622');

    // controller.loadSplashAdGdt(gdtAppId,'8863364436303842593');
  }

}
class FirstScreen1 extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('feedAD'),
      ),
      body:  Column(children: <Widget>[
        Container(
          child: FrameView(onFrameViewCreated: _onFrameViewCreated2,),
          height: 140,
          //width: 600,
        ),
        Container(
            child: FrameView(onFrameViewCreated: _onFrameViewCreated3,),
            height: 400,
            //width: 600,
        ),
        Center(child: GestureDetector(child:Text("看20分钟视频",),onTap: _onFrameViewCreated0,),)

      ],),
    );
  }


  void _onFrameViewCreated0(){
     //激励视频显示
    VideoAd.showVieo();
    //  VideoAd.showVieoBaidu();
  }

  void _onFrameViewCreated3(FrameViewController controller) {
    //穿山甲  feed广告  广告id
   controller.loadFeedAdCsj("901121737");
    //百度    广告id
  //  controller.loadFeedAdBaidu("2058628");


  }

  void _onFrameViewCreated2(FrameViewController controller) {
    //穿山甲   nativebanner 广告id
   controller.loadNativeAd("901121423");
    //百度   nativebanner 广告id
    // controller.loadBannerBaidu('2015351');
  }



}
