# wifi_manager_example

Demonstrates how to use the wifi_manager plugin.

## Getting Started

```
_connect() async {
    await WifiManager
        .connect(ssid: "Vane_Control_1DC0E0", password: "vanecontrol2019");
  }

  _getSSID() async {
    _ssid = await WifiManager.getSSID();
  }
```
