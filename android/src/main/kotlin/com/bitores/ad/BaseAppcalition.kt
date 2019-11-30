package com.bitores.ad

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Pair
import java.util.HashMap

class BaseAppcalition :Application(){
    private val launcherMap = HashMap<String, Pair<String, String>>()
    override fun onCreate() {
        super.onCreate()
        config(this)
    }

    internal fun config(context: Context) {
        try {
            val packageName = context.packageName
            //Get all activity classes in the AndroidManifest.xml
            val packageInfo = context.packageManager.getPackageInfo(
                    packageName, PackageManager.GET_ACTIVITIES or PackageManager.GET_META_DATA)
            if (packageInfo.activities != null) {
                for (activity in packageInfo.activities) {
                    val metaData = activity.metaData
                    if (metaData != null && metaData.containsKey("id")
                            && metaData.containsKey("content") && metaData.containsKey("action")) {
                        Log.e("gdt", activity.name)
                        try {
                            Class.forName(activity.name)
                        } catch (e: ClassNotFoundException) {
                            continue
                        }

                        val id = metaData.getString("id")
                        val content = metaData.getString("content")
                        val action = metaData.getString("action")
                        launcherMap[action] = Pair(id, content)
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }
}