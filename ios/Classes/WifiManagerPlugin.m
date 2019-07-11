#import "WifiManagerPlugin.h"
#import <wifi_manager/wifi_manager-Swift.h>

@implementation WifiManagerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftWifiManagerPlugin registerWithRegistrar:registrar];
}
@end
