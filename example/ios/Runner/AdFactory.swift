//
//  AdFactory.swift
//  Runner
//
//  Created by 黄明华 on 2019/8/21.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

import Foundation
import Flutter
class AdFactory : NSObject,FlutterPlatformViewFactory{
    
    var messenger: FlutterBinaryMessenger!
    
    func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return AdView(frame,viewID : viewId , args : args,binaryMessenger:messenger);
    }
    
    @objc public init(messenger: (NSObject & FlutterBinaryMessenger)?) {
        super.init()
        self.messenger = messenger
    }
    
}
