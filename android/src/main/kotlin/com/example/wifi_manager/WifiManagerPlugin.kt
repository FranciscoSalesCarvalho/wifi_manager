package com.example.wifi_manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class WifiManagerPlugin(
        private val activity: Activity
) : MethodCallHandler {

    private val context: Context = activity.applicationContext
    private val wifiManager: WifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    init {
        askLocationPermission()
    }

    companion object {

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "wifi_manager")

            val wifiConnectorPlugin = WifiManagerPlugin(registrar.activity())

            channel.setMethodCallHandler(wifiConnectorPlugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "connect" -> connect(call, result)
            "disconnect" -> disconnect(result)
            "ssid" -> getSSID(result)
            else -> result.notImplemented()
        }
    }

    private fun connect(call: MethodCall, result: Result) {
        Thread(Runnable {
            val ssid: String = call.argument("ssid")!!
            val password: String = call.argument("password")!!

            val connected = connectTo(ssid, password)

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                result.success(connected)
            }
        }).start()
    }

    private fun connectTo(ssid: String, password: String): Boolean {
        wifiManager.isWifiEnabled = true
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", password)

        val netId = wifiManager.addNetwork(wifiConfig)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        return wifiManager.reconnect()
    }

    private fun disconnect(result: Result) {
        val disconnect = wifiManager.disconnect()
        result.success(disconnect)
    }

    private fun getSSID(result: Result) {
        askLocationPermission()

        val info = wifiManager.connectionInfo
        var ssid = info.ssid
        if (ssid.startsWith("\"") && ssid.endsWith("\""))
            ssid = ssid.substring(1, ssid.length - 1)

        result.success(ssid)
    }

    private fun askLocationPermission() {
        val PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1

        if(notLocationPermissionGranted()){
            activity.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION)
        }
    }

    private fun notLocationPermissionGranted(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
    }
}
