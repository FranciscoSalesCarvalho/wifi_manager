import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:wifi_manager/wifi_manager.dart';

void main() {
  const MethodChannel channel = MethodChannel('wifi_manager');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await WifiManager.platformVersion, '42');
  });
}
