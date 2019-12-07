//
//  AdView.swift
//  Runner
//
//  Created by 黄明华 on 2019/8/21.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//
import Flutter
import Foundation


class AdView : NSObject,FlutterPlatformView{
    let frame: CGRect;
    let viewId: Int64;
    var messenger: FlutterBinaryMessenger!
    
    init(_ frame: CGRect,viewID: Int64,args :Any?, binaryMessenger: FlutterBinaryMessenger) {
        self.frame = frame;
        self.viewId = viewID;
        self.messenger=binaryMessenger;
    }
    
    
    func initMethodChannel(){
        let googleMapChannel = FlutterMethodChannel.init(name: "bitores.ad.ios",
                                                         binaryMessenger: messenger);
        googleMapChannel.setMethodCallHandler({
            (call: FlutterMethodCall, result: FlutterResult) -> Void in
            if(call.method == "startLoaction"){
                //do something
            }else if(call.method == "stopLoaction"){
                
            }else if(call.method == "getPlatformVersion"){
                //do something
                let iosVersion  = UIDevice.current.systemVersion//ios版本

                //result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
                result(iosVersion)
            }else if(call.method == "stopLoaction"){
                
            }
        });
       
    }
    
    
    func view() -> UIView {
        initMethodChannel()
        
        let textView = UITextView(frame: CGRect(x:30, y:200, width:UIScreen.main.bounds.width-60, height:200))
        //textView.delegate = self
        //self.view.addSubview(textView)
    
        //背景颜色设置
       // textView.backgroundColor = UIColor.grayColor()
        textView.text="flutter  插件"
        //设置textview里面的字体颜色
        textView.textColor =  UIColor.red
        
        //设置文本字体
        textView.font = UIFont.systemFont(ofSize: 16);//使用系统默认字体，指定14号字号
        //textView.font = UIFont(name: "Helvetica-Bold", size: 18)//指定字体，指定字号
        

       // mapView.text="阿姨洗吧思密达"
        //mapView.color=UIColor.red
       // mapView.frame = frame
        return textView;
    }
}
