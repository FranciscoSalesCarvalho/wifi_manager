import 'package:flutter/material.dart';

import 'package:wifi_manager/wifi_manager.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  var _ssid = "";

  _connect() async {
    await WifiManager
        .connect(ssid: "Vane_Control_1DC0E0", password: "vanecontrol2019");
  }

  _getSSID() async {
    _ssid = await WifiManager.getSSID();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              RaisedButton(onPressed: () {
                setState(() {
                  _getSSID();
                });
              }, child: Text('Connect')),
              Text(_ssid)
            ],
          ),
        ),
      ),
    );
  }
}