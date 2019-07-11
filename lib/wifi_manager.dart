import 'dart:async';

import 'package:flutter/services.dart';

class WifiManager {
  static const MethodChannel _channel =
  const MethodChannel('wifi_manager');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> connect({
    String ssid,
    String password
  }) async {
    bool result;

    try {
      result = await _channel.invokeMethod('connect', {
        "ssid": ssid.toString(),
        "password": password.toString()
      });
    } on MissingPluginException catch (e) {
      print("MissingPluginException : ${e.toString()}");
    }

    return result;
  }

  static Future<String> getSSID() async {
    Map<String, String> htArguments = new Map();
    String sResult;
    try {
      sResult = await _channel.invokeMethod('ssid', htArguments);
    } on MissingPluginException catch (e) {
      print("MissingPluginException : ${e.toString()}");
    }
    return sResult;
  }

  static disconnect() async {
    Map<String, bool> htArguments = new Map();
    try {
      await _channel.invokeMethod('disconnect', htArguments);
    } on MissingPluginException catch (e) {
      print("MissingPluginException : ${e.toString()}");
    }
  }
}
