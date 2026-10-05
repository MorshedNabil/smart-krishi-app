package com.nabil.smartkrishi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nabil.smartkrishi.R

val KantumruyPro = FontFamily(
    Font(R.font.kantumruy_pro_regular, FontWeight.Normal),
    Font(R.font.kantumruy_pro_medium, FontWeight.Medium),
    Font(R.font.kantumruy_pro_bold, FontWeight.Bold),
    Font(R.font.kantumru_pro_semibold, FontWeight.SemiBold),
    Font(R.font.kantumruy_pro_light, FontWeight.Light),
    Font(R.font.kantumruy_pro_thin, FontWeight.Thin),
    Font(R.font.kantumruy_pro_italic, FontWeight.Normal, FontStyle.Italic)
)

// Set of Material typography styles to start with. Material Typography is customized for this app
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    titleMedium = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = KantumruyPro,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)