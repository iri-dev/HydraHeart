package com.hydraheart2.app.data

import androidx.compose.ui.graphics.Color

data class EducationTopic(
    val titleRes: Int,
    val icon: String,
    val colour: Color,
    val category: TopicCategory,
    val content: String,
    val previewText: String,
    val summary: String,
    val summaryMale: String? = null,
    val keyFacts: List<String>,
    val keyFactsMale: List<String>? = null,
    val whatGoesWrong: List<String>,
    val whatThisMeansForYou: String,
    val whatThisMeansForYouMale: String? = null,
    val practicalTips: List<String>,
    val insightCallout: String? = null,
    val mythVsFact: List<MythFact>? = null,
    val hydraComment: String
)
