package com.hydraheart2.app.data

import androidx.compose.ui.graphics.Color

object ThemeData {
    val themes: LinkedHashMap<String, HydraTheme> = linkedMapOf(
        "skyLight" to HydraTheme(
            "skyLight", "Sky Light", "☀️",
            Color(0xFFF7FBFF), Color(0xFFEAF4FF), Color(0xFFDFEEFF),
            Color(0xFF6BB6FF), Color(0xFFA9D3FF), Color(0xFFDFF1FF),
            Color(0xFFFFFFFF), Color(0xFFD9EAFB),
            Color(0xFF223042), Color(0xFF5E7186), Color(0xFF8A9BB0), Color(0xFFF0F6FF), Color(0xFFDFF1FF), Color(0xFF74BCFF),
            isLight = true
        ),
        "obsidianFlow" to HydraTheme(
            "obsidianFlow", "Obsidian Flow", "🖤",
            Color(0xFF0B0D12), Color(0xFF141922), Color(0xFF0E1118),
            Color(0xFF6E8BFF), Color(0xFF8C7CFF), Color(0xFFA6B0C2),
            Color(0x14718BFF), Color(0x26273043),
            Color(0xFFF4F7FB), Color(0xFFA6B0C2), Color(0xFF5D6780), Color(0xF20B0D12), Color(0xFF202C45), Color(0xFF5B7DFF),
            isLight = false
        ),
        "pureWater" to HydraTheme(
            "pureWater", "Pure Water", "🫧",
            Color(0xFFF4FDFF), Color(0xFFDDF7FF), Color(0xFFCCF2FF),
            Color(0xFF4ECDF5), Color(0xFF8FEAFF), Color(0xFFCFF8FF),
            Color(0xD9FFFFFF), Color(0xFFBFEFFF),
            Color(0xFF173443), Color(0xFF4D7180), Color(0xFF7A9AAA), Color(0xFFF0FAFF), Color(0xFFCFF8FF), Color(0xFF58D7F8),
            isLight = true
        ),
        "roseGold" to HydraTheme(
            "roseGold", "Rose", "🌹",
            Color(0xFF1D1116), Color(0xFF341823), Color(0xFF28121C),
            Color(0xFFC94E7A), Color(0xFFE88AA9), Color(0xFFFFF5F8),
            Color(0x14C94E7A), Color(0x266B334B),
            Color(0xFFFFF5F8), Color(0xFFD4A8B8), Color(0xFF8A6878), Color(0xF21D1116), Color(0xFF7D3652), Color(0xFFD85F89),
            isLight = false
        ),
        "cherryBlossom" to HydraTheme(
            "cherryBlossom", "Cherry Blossom", "🌸",
            Color(0xFFFFF7FA), Color(0xFFFFE1EA), Color(0xFFFFD6E3),
            Color(0xFFF39AB8), Color(0xFFFFD0DD), Color(0xFFFFF0F5),
            Color(0xFFFFFFFF), Color(0xFFF7CDD9),
            Color(0xFF4B2B36), Color(0xFF86606C), Color(0xFFA88898), Color(0xFFFFF5F8), Color(0xFFFFF0F5), Color(0xFFF6A9C1),
            isLight = true
        ),
        "matcha" to HydraTheme(
            "matcha", "Matcha", "🍵",
            Color(0xFFF6FAF3), Color(0xFFE3EEDC), Color(0xFFD8E8CE),
            Color(0xFF8BAA82), Color(0xFFB7CCAE), Color(0xFFEAF4E2),
            Color(0xFFFFFFFC), Color(0xFFCFE0C4),
            Color(0xFF2F3C2B), Color(0xFF667564), Color(0xFF8A9A86), Color(0xFFF2F8EE), Color(0xFFEAF4E2), Color(0xFF9CBD91),
            isLight = true
        ),
        "pearls" to HydraTheme(
            "pearls", "Pearls", "🤍",
            Color(0xFFFCFAF6), Color(0xFFF1ECE3), Color(0xFFE8E0D5),
            Color(0xFFCBBBA3), Color(0xFFF4E7D3), Color(0xFFFFF8EE),
            Color(0xFFFFFFFF), Color(0xFFE7DED0),
            Color(0xFF332C24), Color(0xFF7C7266), Color(0xFFA09488), Color(0xFFF8F4EE), Color(0xFFFFF8EE), Color(0xFFE7D9C6),
            isLight = true, unlockLevel = 2
        ),
        "cloudNine" to HydraTheme(
            "cloudNine", "Cloud Nine", "☁️",
            Color(0xFFF7FAFF), Color(0xFFE7EDFF), Color(0xFFDDE5FF),
            Color(0xFFAAB6FF), Color(0xFFBDE4FF), Color(0xFFEEF2FF),
            Color(0xFFFFFFFF), Color(0xFFD6E0FA),
            Color(0xFF2E3552), Color(0xFF69718E), Color(0xFF8E96B0), Color(0xFFF2F5FF), Color(0xFFEEF2FF), Color(0xFFA9C8FF),
            isLight = true, unlockLevel = 2
        ),
        "strawberryMilk" to HydraTheme(
            "strawberryMilk", "Strawberry Milk", "🍓",
            Color(0xFFFFF8FA), Color(0xFFFFE4EC), Color(0xFFFFD8E4),
            Color(0xFFF7A5BE), Color(0xFFFFD0DD), Color(0xFFFFE9F0),
            Color(0xFFFFFFFF), Color(0xFFF6D1DD),
            Color(0xFF4A2E38), Color(0xFF8A6A75), Color(0xFFAA8A95), Color(0xFFFFF5F8), Color(0xFFFFE9F0), Color(0xFFF8B1C8),
            isLight = true, unlockLevel = 2
        ),
        "icyMorning" to HydraTheme(
            "icyMorning", "Icy Morning", "❄️",
            Color(0xFFF9FCFF), Color(0xFFE8F3FB), Color(0xFFDCEDF8),
            Color(0xFF90C7F7), Color(0xFFC5D7E7), Color(0xFFEEF7FF),
            Color(0xFFFFFFFF), Color(0xFFD4E3EF),
            Color(0xFF23313E), Color(0xFF617488), Color(0xFF8898A8), Color(0xFFF4F8FF), Color(0xFFEEF7FF), Color(0xFF98D0FF),
            isLight = true, unlockLevel = 2
        ),
        "oatLatte" to HydraTheme(
            "oatLatte", "Oat Latte", "☕",
            Color(0xFFFAF4EC), Color(0xFFE8D7C4), Color(0xFFDECCB8),
            Color(0xFFB4845E), Color(0xFFD5A97F), Color(0xFFF0DEC8),
            Color(0xFFFFFDFB), Color(0xFFD9C2AA),
            Color(0xFF3D2C20), Color(0xFF786355), Color(0xFF9A8676), Color(0xFFF6EEE4), Color(0xFFF0DEC8), Color(0xFFC39269),
            isLight = true, unlockLevel = 2
        ),
        "honeyGlow" to HydraTheme(
            "honeyGlow", "Honey Glow", "🍯",
            Color(0xFFFFF7E8), Color(0xFFFBE7BC), Color(0xFFF5DCA8),
            Color(0xFFE2A93B), Color(0xFFFFD27A), Color(0xFFFFE7A8),
            Color(0xFFFFFDF8), Color(0xFFF2D79C),
            Color(0xFF4A3217), Color(0xFF7A5A33), Color(0xFF9A7A53), Color(0xFFFFF8EE), Color(0xFFFFE7A8), Color(0xFFF0B547),
            isLight = true, unlockLevel = 3
        ),
        "glazed" to HydraTheme(
            "glazed", "Glazed", "🍩",
            Color(0xFFFFF7F1), Color(0xFFFFDCCF), Color(0xFFFFD0BE),
            Color(0xFFF19A78), Color(0xFFFFC0A8), Color(0xFFFFE4D8),
            Color(0xFFFFFFFC), Color(0xFFF4C5B2),
            Color(0xFF4C2D1F), Color(0xFF885F4A), Color(0xFFA87F6A), Color(0xFFFFF4EE), Color(0xFFFFE4D8), Color(0xFFF6A886),
            isLight = true, unlockLevel = 3
        ),
        "sunsetHour" to HydraTheme(
            "sunsetHour", "Sunset Hour", "🌅",
            Color(0xFFFFF4EC), Color(0xFFFFD3B3), Color(0xFFFFC8A0),
            Color(0xFFF27C5A), Color(0xFFFFB26F), Color(0xFFFFD9A6),
            Color(0xFFFFFDF9), Color(0xFFF0BE99),
            Color(0xFF4A2B1D), Color(0xFF8A614A), Color(0xFFAA816A), Color(0xFFFFF6EE), Color(0xFFFFD9A6), Color(0xFFF59668),
            isLight = true, unlockLevel = 3
        ),
        "botanicalGarden" to HydraTheme(
            "botanicalGarden", "Botanical Garden", "🌿",
            Color(0xFFF5FAF3), Color(0xFFDDEAD7), Color(0xFFD0E0C8),
            Color(0xFF5F9C63), Color(0xFF9DC88F), Color(0xFFEAF5E4),
            Color(0xFFFFFDF9), Color(0xFFC9DDC1),
            Color(0xFF243223), Color(0xFF5B6F58), Color(0xFF7A8E76), Color(0xFFF0F8EC), Color(0xFFEAF5E4), Color(0xFF74B978),
            isLight = true, unlockLevel = 3
        ),
        "berryCream" to HydraTheme(
            "berryCream", "Berry Cream", "🫐",
            Color(0xFFFFF6F8), Color(0xFFFFD9E3), Color(0xFFFFCCD8),
            Color(0xFFE96C95), Color(0xFFFFAAC4), Color(0xFFFFD3E0),
            Color(0xFFFFFFFF), Color(0xFFF6C7D5),
            Color(0xFF4A2432), Color(0xFF7E5261), Color(0xFF9E7281), Color(0xFFFFF2F6), Color(0xFFFFD3E0), Color(0xFFF48AAA),
            isLight = true, unlockLevel = 3
        ),
        "midnightGlam" to HydraTheme(
            "midnightGlam", "Midnight Glam", "🌙",
            Color(0xFF120C1D), Color(0xFF211531), Color(0xFF1A1028),
            Color(0xFF8D6CFF), Color(0xFFC8C2E8), Color(0xFFF8F6FF),
            Color(0x148D6CFF), Color(0x2649336A),
            Color(0xFFF8F6FF), Color(0xFFBDB6D3), Color(0xFF8A82A0), Color(0xF2120C1D), Color(0xFF5A4590), Color(0xFF8B74F8),
            isLight = false, unlockLevel = 4
        ),
        "champagne" to HydraTheme(
            "champagne", "Champagne", "🥂",
            Color(0xFFFFF9EF), Color(0xFFF8E7C6), Color(0xFFF2DDB2),
            Color(0xFFD8B15A), Color(0xFFF0D892), Color(0xFFFCEAB5),
            Color(0xFFFFFCF7), Color(0xFFE9D5AA),
            Color(0xFF433018), Color(0xFF796143), Color(0xFF998363), Color(0xFFFFF8EC), Color(0xFFFCEAB5), Color(0xFFE4BC62),
            isLight = true, unlockLevel = 4
        ),
        "oceanDepths" to HydraTheme(
            "oceanDepths", "Ocean Depths", "🌊",
            Color(0xFF0D1D24), Color(0xFF14333B), Color(0xFF102A32),
            Color(0xFF4EB9B3), Color(0xFF9BE3D7), Color(0xFFF2FFFD),
            Color(0x144EB9B3), Color(0x262C6670),
            Color(0xFFF2FFFD), Color(0xFFA7C8C4), Color(0xFF6A9A96), Color(0xF20D1D24), Color(0xFF1F555A), Color(0xFF59C6BF),
            isLight = false, unlockLevel = 4
        ),
        "afterglow" to HydraTheme(
            "afterglow", "Afterglow", "🌇",
            Color(0xFF2F1A10), Color(0xFF4A2020), Color(0xFF352050),
            Color(0xFFFF8C78), Color(0xFF9D8CFF), Color(0xFFFFF8FC),
            Color(0x1FFFC6A8), Color(0x26FFC6A8),
            Color(0xFFFFF8FC), Color(0xFFE3D1E8), Color(0xFFB0A0B8), Color(0xF22F1A10), Color(0xFFFFB6A1), Color(0xFFFF9A86),
            isLight = false, unlockLevel = 4
        ),
        "sugarPlum" to HydraTheme(
            "sugarPlum", "Sugar Plum", "🧁",
            Color(0xFFFFF7FF), Color(0xFFF1D9FF), Color(0xFFE8CCFF),
            Color(0xFFC98BFF), Color(0xFFFFA8D8), Color(0xFFF6DBFF),
            Color(0xFFFFFFFF), Color(0xFFE7C7FA),
            Color(0xFF442C52), Color(0xFF7B6588), Color(0xFF9A86A8), Color(0xFFFFF4FF), Color(0xFFF6DBFF), Color(0xFFD29CFF),
            isLight = true, unlockLevel = 4
        ),
        "auraFlow" to HydraTheme(
            "auraFlow", "Aura Flow", "🌈",
            Color(0xFF1A1020), Color(0xFF201530), Color(0xFF151025),
            Color(0xFFC58CFF), Color(0xFF7FCBFF), Color(0xFFF4D8FF),
            Color(0x14C58CFF), Color(0x26F0E6FF),
            Color(0xFFF8F0FF), Color(0xFFBBA8D0), Color(0xFF8878A0), Color(0xF21A1020), Color(0xFFF4D8FF), Color(0xFFA88DFF),
            isLight = false, unlockLevel = 5
        ),
        "starlight" to HydraTheme(
            "starlight", "Starlight", "🌌",
            Color(0xFF0D1021), Color(0xFF1A1C36), Color(0xFF12152C),
            Color(0xFFA698FF), Color(0xFF7AB8FF), Color(0xFFCAD0FF),
            Color(0x14A698FF), Color(0x26343A6A),
            Color(0xFFF6F7FF), Color(0xFFAAB0D3), Color(0xFF7880A8), Color(0xF20D1021), Color(0xFFCAD0FF), Color(0xFF7A8BFF),
            isLight = false, unlockLevel = 5
        ),
        "cosmicGirl" to HydraTheme(
            "cosmicGirl", "Cosmic Girl", "🔮",
            Color(0xFF0F1122), Color(0xFF1A1D3B), Color(0xFF141830),
            Color(0xFFA179FF), Color(0xFFC9D4FF), Color(0xFFF7F8FF),
            Color(0x14A179FF), Color(0x264E4F7F),
            Color(0xFFF7F8FF), Color(0xFFAFB4D6), Color(0xFF7E82A8), Color(0xF20F1122), Color(0xFF7B6DFF), Color(0xFF8C83FF),
            isLight = false, unlockLevel = 5
        ),
        "darkAcademia" to HydraTheme(
            "darkAcademia", "Dark Academia", "📚",
            Color(0xFF15110E), Color(0xFF241B16), Color(0xFF1C1612),
            Color(0xFFA57B3F), Color(0xFFD5B073), Color(0xFFF7F0E8),
            Color(0x14A57B3F), Color(0x264A382A),
            Color(0xFFF7F0E8), Color(0xFFB7A495), Color(0xFF887868), Color(0xF215110E), Color(0xFF5C4631), Color(0xFFB88A48),
            isLight = false, unlockLevel = 5
        ),
        "sapphireNight" to HydraTheme(
            "sapphireNight", "Sapphire Night", "💎",
            Color(0xFF0A1330), Color(0xFF12204A), Color(0xFF0E1A3E),
            Color(0xFF3F73FF), Color(0xFF7EA6FF), Color(0xFFF4F8FF),
            Color(0x143F73FF), Color(0x26304B88),
            Color(0xFFF4F8FF), Color(0xFFAAB9DC), Color(0xFF7888B0), Color(0xF20A1330), Color(0xFF294C9B), Color(0xFF4C7EFF),
            isLight = false, unlockLevel = 5
        ),
        "barbie" to HydraTheme(
            "barbie", "Barbie", "💗",
            Color(0xFFFFF1F8), Color(0xFFFFC5E5), Color(0xFFFFB8DE),
            Color(0xFFFF3FA0), Color(0xFFFF73BF), Color(0xFFFFB8DE),
            Color(0xFFFFFFFF), Color(0xFFFF99D0),
            Color(0xFF581C3D), Color(0xFF9A4A73), Color(0xFFBA6A93), Color(0xFFFFF0F6), Color(0xFFFFB8DE), Color(0xFFFF57AE),
            isLight = true, unlockAchievement = "perfect_day"
        ),
        "neonPulse" to HydraTheme(
            "neonPulse", "Neon Pulse", "⚡",
            Color(0xFF090A12), Color(0xFF12152A), Color(0xFF0E1120),
            Color(0xFF9D5CFF), Color(0xFF42C8FF), Color(0xFFF7F6FF),
            Color(0x149D5CFF), Color(0x263A2F69),
            Color(0xFFF7F6FF), Color(0xFFA9A6C7), Color(0xFF787598), Color(0xF2090A12), Color(0xFF6A3CFF), Color(0xFF6F7DFF),
            isLight = false, unlockAchievement = "week_streak"
        ),
        "emberGlow" to HydraTheme(
            "emberGlow", "Ember Glow", "🔥",
            Color(0xFF120B09), Color(0xFF22120E), Color(0xFF1A0E0A),
            Color(0xFFE6672E), Color(0xFFB93B28), Color(0xFFFFF5EF),
            Color(0x14E6672E), Color(0x265A2C1F),
            Color(0xFFFFF5EF), Color(0xFFD1AAA0), Color(0xFFA08070), Color(0xF2120B09), Color(0xFF7D311A), Color(0xFFF2763F),
            isLight = false, unlockAchievement = "two_litres"
        ),
        "mintChoc" to HydraTheme(
            "mintChoc", "Mint Choc", "🍫",
            Color(0xFFF4FBF7), Color(0xFFD7EBDD), Color(0xFFCCE4D0),
            Color(0xFF73BCA3), Color(0xFF4A342C), Color(0xFFDCF4E7),
            Color(0xFFFFFDF8), Color(0xFFB8D6C0),
            Color(0xFF2E2A27), Color(0xFF67615C), Color(0xFF88827C), Color(0xFFF0F8F4), Color(0xFFDCF4E7), Color(0xFF7BC7AB),
            isLight = true, unlockAchievement = "variety"
        )
    )

    fun getTheme(id: String): HydraTheme = themes[id] ?: themes["skyLight"]!!

    fun getUnlockedThemes(level: Int, achievements: List<String>): List<HydraTheme> =
        themes.values.filter { theme ->
            theme.unlockLevel <= level &&
                (theme.unlockAchievement == null || achievements.contains(theme.unlockAchievement))
        }

    fun isThemeUnlocked(id: String, level: Int, achievements: List<String>): Boolean {
        val theme = themes[id] ?: return false
        val levelOk = theme.unlockLevel <= level
        val achieveOk = theme.unlockAchievement == null || achievements.contains(theme.unlockAchievement)
        return levelOk && achieveOk
    }
}
