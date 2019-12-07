//
//  AdViewPlugin.swift
//  Runner
//
//  Created by 黄明华 on 2019/8/21.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

import Foundation

class AdViewPlugin {
    static func registerWith(registry:FlutterPluginRegistry) {
        let pluginKey = "bitores.ad.ios";
        if (registry.hasPlugin(pluginKey)) {return};
        let registrar = registry.registrar(forPlugin: pluginKey);
        let messenger = registrar.messenger() as! (NSObject & FlutterBinaryMessenger)
        registrar.register(AdFactory(messenger:messenger),withId: "AdView");
    }
}
