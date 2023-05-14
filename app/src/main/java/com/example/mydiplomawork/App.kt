package com.example.mydiplomawork

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.github.orangegangsters.lollipin.lib.managers.LockManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initStrictMode()
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeathOnNetwork()
                .build()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectNonSdkApiUsage()
                    .detectCleartextNetwork()
                    .penaltyLog()
                    .build()
            )
        }
    }
}