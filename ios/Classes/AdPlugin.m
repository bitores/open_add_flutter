#import "AdPlugin.h"
#import <ad/ad-Swift.h>

@implementation AdPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAdPlugin registerWithRegistrar:registrar];
}
@end
