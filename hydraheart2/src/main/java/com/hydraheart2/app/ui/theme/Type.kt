package com.hydraheart2.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val playfairDisplay = GoogleFont("Playfair Display")

val PlayfairFont = FontFamily(
    Font(googleFont = playfairDisplay, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = playfairDisplay, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = playfairDisplay, fontProvider = provider, weight = FontWeight.Bold)
)

private val dmSans = GoogleFont("DM Sans")

val QuicksandFont = FontFamily(
    Font(googleFont = dmSans, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = dmSans, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = dmSans, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = dmSans, fontProvider = provider, weight = FontWeight.Bold)
)

val HydraTypography = Typography(
    displayLarge = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Bold, fontSize = 42.sp, letterSpacing = (-1).sp),
    displayMedium = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Bold, fontSize = 36.sp, letterSpacing = (-0.5).sp),
    displaySmall = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Bold, fontSize = 28.sp),
    headlineLarge = TextStyle(fontFamily = PlayfairFont, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, letterSpacing = (-0.3).sp),
    headlineMedium = TextStyle(fontFamily = PlayfairFont, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    headlineSmall = TextStyle(fontFamily = PlayfairFont, fontWeight = FontWeight.Normal, fontSize = 18.sp),
    titleLarge = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, letterSpacing = 0.2.sp),
    titleSmall = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Medium, fontSize = 13.sp, letterSpacing = 0.3.sp),
    bodyLarge = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp),
    labelMedium = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontFamily = QuicksandFont, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.8.sp)
)
