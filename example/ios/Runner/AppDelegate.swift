import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
   GeneratedPluginRegistrant.register(with: self)
    // AdPlugin.nameregisterWithRegistry(registry: self)
    
    // AdViewPlugin.registerWith(registry:  self)
    // let event = registrar(forPlugin: "AdViewPlugin")
    // let test = registrar(forPlugin: "bitores.ad.test")

    // 
   
    
    // let googleMapChannel = FlutterMethodChannel.init(name: "bitores.ad.test",binaryMessenger: test.messenger());
    // googleMapChannel.setMethodCallHandler({
    //     (call: FlutterMethodCall, result: FlutterResult) -> Void in
    //     if(call.method == "startLoaction"){
    //         //do something
    //     }else if(call.method == "stopLoaction"){
            
    //     }else if(call.method == "getPlatformVersion"){
    //         //do something
    //         let iosVersion  = UIDevice.current.systemVersion//ios版本
            
    //         //result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
    //         result(iosVersion)
    //     }else if(call.method == "stopLoaction"){
            
    //     }
    // });

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
