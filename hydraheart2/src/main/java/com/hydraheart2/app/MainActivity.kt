package com.hydraheart2.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hydraheart2.app.viewmodel.HydraViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: HydraViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.onAppOpened()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onAppBackgrounded()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val hasSeenOnboarding = prefs.getBoolean("has_seen_onboarding", false)
        if (hasSeenOnboarding) {
            viewModel.onboardingComplete = true
        }
        viewModel.load(this)
        splashScreen.setKeepOnScreenCondition { !viewModel.isLoaded }
        setContent {
            HydraApp(viewModel, prefs)
        }
    }
}
