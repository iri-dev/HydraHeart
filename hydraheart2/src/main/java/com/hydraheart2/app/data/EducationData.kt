package com.hydraheart2.app.data

import androidx.compose.ui.graphics.Color
import com.hydraheart2.app.R

object EducationData {
    val topics: List<EducationTopic> = listOf(
        EducationTopic(
            titleRes = R.string.edu_water_title,
            icon = "💧",
            colour = Color(0xFF60A5FA),
            category = TopicCategory.HYDRATION,
            content = "Water is at the centre of almost every biological process. It regulates core body temperature, acts as a lubricant for joints, transports oxygen and nutrients to cells, and flushes metabolic waste through your kidneys. Your brain is roughly 75% water — which is why mental clarity, mood, and reaction time are among the first things to suffer when you fall behind. Even mild dehydration of just 1–2% of body weight can measurably impair cognitive function, physical endurance, and emotional regulation. Unlike hunger, thirst is a lagging signal — by the time you feel it, dehydration has often already begun. Staying consistently hydrated throughout the day is far more effective than trying to catch up in the evening.",
            previewText = "You're probably slightly dehydrated right now and don't know it",
            summary = "Water is the foundation of everything your body does — regulating temperature, carrying nutrients, and flushing waste. Most people underestimate just how much their daily function depends on staying consistently hydrated throughout the day.",
            summaryMale = "Water is the foundation of everything your body does — regulating temperature, carrying nutrients, and flushing waste. Most men underestimate their needs because thirst arrives late; by then, you're already running on less than optimal.",
            keyFacts = listOf(
                "~60% of your body weight is water",
                "Just 1–2% loss can impair concentration, mood, and reaction time",
                "Women need approximately 2.0–2.2 litres per day on average",
                "Pale yellow urine is the most reliable free hydration check"
            ),
            keyFactsMale = listOf(
                "~60% of your body weight is water",
                "Just 1–2% loss can impair concentration, mood, and reaction time",
                "Men need approximately 2.5–3.0 litres per day on average",
                "Pale yellow urine is the most reliable free hydration check"
            ),
            whatGoesWrong = listOf(
                "Persistent headaches — often the first signal of under-hydration",
                "Brain fog, reduced focus, and slower thinking",
                "Fatigue and low energy even after a full night's sleep",
                "Dry skin, chapped lips, and dull complexion over time",
                "Constipation and sluggish digestion"
            ),
            whatThisMeansForYou = "Your daily water target is personal — not just a single fixed number. Activity level, heat, body size, and even the food you eat all shift what you actually need. HydraHeart calculates a target tailored to your profile, which is a much better starting point than any generic guideline.",
            whatThisMeansForYouMale = "Men generally need more water than the widely quoted averages, especially if you exercise or live somewhere warm. Your target in HydraHeart is calculated using your profile — weight, activity level, and climate — so it reflects what your body actually needs rather than a one-size figure.",
            practicalTips = listOf(
                "Sip steadily throughout the day rather than catching up in the evening",
                "Drink a full glass of water first thing in the morning before anything else",
                "Keep a water bottle visible on your desk — out of sight, out of mind",
                "Increase intake during exercise, hot weather, illness, or after alcohol",
                "Water-rich foods (cucumber, watermelon, celery) count toward your total"
            ),
            insightCallout = "You can go weeks without food — but only days without water. It's the most essential thing you consume.",
            mythVsFact = listOf(
                MythFact("You need exactly 8 glasses a day", "Eight glasses is a rough guideline, not a universal rule. Your actual needs depend on your body, activity, diet, and climate."),
                MythFact("If you're not thirsty, you're hydrated", "Thirst is a lagging signal — it often kicks in after dehydration has already begun, especially as you age.")
            ),
            hydraComment = "Consistent sipping always beats occasional big glasses. I notice when you go quiet for too long 💧"
        ),
        EducationTopic(
            titleRes = R.string.edu_water_needs_title,
            icon = "🥛",
            colour = Color(0xFF38BDF8),
            category = TopicCategory.HYDRATION,
            content = "Water needs are highly individual and influenced by several overlapping factors. Body size is the biggest driver — larger bodies contain more water and lose it faster. Activity level matters enormously: even moderate exercise can require an extra 500–1000ml per session. Climate is critical — in hot or humid environments, sweat losses can reach 1–2 litres per hour during activity. Diet plays a surprising role too: people who eat plenty of fruits and vegetables get roughly 20% of their daily water from food alone, meaning their drink intake can be lower without consequence. Pregnancy and breastfeeding significantly increase fluid requirements. Age also shifts needs — older adults have a diminished sense of thirst and need to be more intentional about drinking.",
            previewText = "Your perfect water intake isn't actually 2 litres",
            summary = "The '2 litres a day' rule is a helpful starting point, but the truth is that your actual needs depend on who you are and how you live. Getting this personalised makes a real difference to how you feel every day.",
            summaryMale = "The widely cited hydration guidelines tend to underestimate what most men actually need. Body size, activity, and heat all push your requirements above the average — knowing your real target matters.",
            keyFacts = listOf(
                "Women: approximately 2.0–2.2 L/day from all sources",
                "20% of daily fluid typically comes from food",
                "Exercise adds ~500–1000ml per session",
                "Hot climates can double your fluid needs"
            ),
            keyFactsMale = listOf(
                "Men: approximately 2.5–3.0 L/day from all sources",
                "20% of daily fluid typically comes from food",
                "Exercise adds ~500–1000ml per session",
                "Hot climates can double your fluid needs"
            ),
            whatGoesWrong = listOf(
                "Consistently under-drinking leads to chronic low-grade dehydration",
                "Over time, this impairs kidney function and increases UTI risk",
                "Energy, mood, and focus deteriorate without you realising why",
                "Skin and digestive health decline gradually over weeks"
            ),
            whatThisMeansForYou = "HydraHeart calculates your personal water goal using your weight, activity level, and climate setting. It's worth checking this is set correctly in your profile — the difference between a generic target and a personalised one can be several hundred millilitres a day.",
            whatThisMeansForYouMale = "HydraHeart uses your weight, activity level, and climate to calculate a personal target. Most male users find their real needs are noticeably higher than what generic apps suggest — check your profile settings are accurate for the most useful goal.",
            practicalTips = listOf(
                "Check your HydraHeart goal reflects your actual activity level and climate",
                "Add 300–500ml on days you exercise, even lightly",
                "In summer or hot weather, increase your target by 500ml at minimum",
                "Count herbal teas, milk, and broth — not just plain water"
            ),
            insightCallout = "Your body size, where you live, and how active you are matter far more than any single daily number.",
            hydraComment = "There's no universal magic number. Your goal in here is built around you, not some average 💧"
        ),
        EducationTopic(
            titleRes = R.string.edu_mild_dehy_title,
            icon = "🌡️",
            colour = Color(0xFF7DD3FC),
            category = TopicCategory.HYDRATION,
            content = "Mild dehydration — defined as roughly 1–3% of body weight in fluid loss — is far more common than most people realise, and it often goes completely unnoticed. The brain and body can function, but at a measurably reduced level. Research by the University of Connecticut found that mild dehydration in women caused notable mood changes, reduced concentration, and increased perception of task difficulty even during gentle exercise. The most accessible sign to watch for is urine colour: pale straw yellow indicates good hydration; anything darker than apple juice suggests you need to drink. Thirst is a useful signal but often arrives later than the dehydration itself, so using visual cues like urine colour is more reliable as an ongoing check.",
            previewText = "You'll feel it in your mood before you ever feel thirsty",
            summary = "Mild dehydration starts well before you feel thirsty, and its effects are surprisingly significant. It's one of the most common and most overlooked reasons for low energy, poor focus, and a flat mood during the day.",
            keyFacts = listOf(
                "Mild dehydration = ~1–3% body weight fluid loss",
                "Affects mood, concentration, and physical endurance",
                "Dark yellow urine is often the first visible sign",
                "Thirst arrives after dehydration has already begun"
            ),
            whatGoesWrong = listOf(
                "Difficulty concentrating and slower thinking",
                "Increased anxiety, irritability, and low mood",
                "Headaches — dehydration is one of the most common triggers",
                "Feeling physically tired and sluggish despite enough sleep",
                "Dry mouth, slightly dry skin, and reduced energy"
            ),
            whatThisMeansForYou = "If you regularly hit mid-afternoon feeling foggy or flat, dehydration is one of the first things to check. It's easy to overlook because the symptoms feel so generic — but a consistent drinking habit often fixes them faster than any supplement or coffee.",
            practicalTips = listOf(
                "Check your urine colour midday — it's your simplest hydration monitor",
                "If you're getting headaches regularly, try increasing water intake first",
                "Set a reminder to drink every 90 minutes during work or study",
                "Keep a glass of water at your desk at all times — visible, accessible"
            ),
            insightCallout = "Dark yellow urine by midday means your body is already working harder than it needs to.",
            hydraComment = "If you feel foggy and you haven't had much water today — start there. It's usually the answer 🌡️"
        ),
        EducationTopic(
            titleRes = R.string.edu_severe_dehy_title,
            icon = "⚠️",
            colour = Color(0xFFFB923C),
            category = TopicCategory.HYDRATION,
            content = "Severe dehydration — defined as 5–8% or more of body weight in fluid loss — is a medical situation that goes well beyond everyday tiredness or thirst. At this level, the body struggles to maintain blood pressure, heart rate rises, and organ function becomes compromised. It most commonly occurs during intense heat exposure, prolonged vomiting or diarrhoea, endurance exercise without adequate fluid replacement, or a combination of factors. The signs are clear: very dark or absent urine, extreme thirst, dizziness or fainting, rapid heartbeat, confusion, and sunken eyes. Children and older adults are at higher risk because their bodies have smaller fluid reserves and, in older adults, a reduced sense of thirst. Severe dehydration requires prompt rehydration — in serious cases, intravenously in a medical setting.",
            previewText = "Know the difference before it becomes urgent",
            summary = "Most dehydration is mild and manageable at home, but severe dehydration is a genuine medical concern that can escalate quickly if not recognised. Knowing the signs could matter more than you think.",
            keyFacts = listOf(
                "Severe = 5–8%+ body weight fluid loss",
                "Can cause dangerously low blood pressure and rapid heart rate",
                "Warning signs include confusion, no urination, and sunken eyes",
                "Children and elderly adults are at highest risk"
            ),
            whatGoesWrong = listOf(
                "Blood pressure drops, causing dizziness, fainting, or collapse",
                "Kidney function is severely impaired — can lead to acute kidney injury",
                "Electrolyte imbalance affects heart rhythm and muscle control",
                "Confusion and reduced consciousness in extreme cases",
                "Can be fatal if untreated in vulnerable individuals"
            ),
            whatThisMeansForYou = "Day-to-day, severe dehydration is unlikely unless you're dealing with illness, extreme heat, or very intense exercise. But knowing the difference between feeling a bit dehydrated and being in an urgent situation is important — especially when travelling, during illness, or in summer heat.",
            practicalTips = listOf(
                "In very hot weather or during intense exercise, drink before you feel thirsty",
                "During vomiting or diarrhoea, prioritise rehydration drinks with electrolytes",
                "If someone shows confusion or faintness alongside dark urine — seek medical help",
                "Oral rehydration salts (ORS) are the most effective rapid rehydration option"
            ),
            insightCallout = "No urine for 8+ hours and extreme fatigue or confusion are signals to take seriously — not push through.",
            hydraComment = "Most of the time this is everyday stuff — but knowing the serious signs matters. Stay on top of your intake ⚠️"
        ),
        EducationTopic(
            titleRes = R.string.edu_too_much_water_title,
            icon = "🌊",
            colour = Color(0xFF93C5FD),
            category = TopicCategory.HYDRATION,
            content = "Overhydration — medically called hyponatraemia when sodium is diluted — is rare but real. It occurs when someone drinks far more water than their kidneys can process (roughly more than 0.8–1 litre per hour for extended periods), diluting sodium levels in the blood. Sodium is critical for nerve and muscle function, and when it drops too low, symptoms range from nausea and headache to, in severe cases, seizures and loss of consciousness. This risk is most associated with endurance athletes who drink large quantities of plain water during prolonged events without replacing electrolytes. In everyday life, drinking too much water is virtually impossible for most people — the kidneys can comfortably handle the normal range of intake without any strain. The bigger concern for most people is drinking too little, not too much.",
            previewText = "Can you actually drink too much water?",
            summary = "You've probably heard warnings about drinking too much water. For most people in everyday life, this is not a real concern — but for endurance athletes or those drinking very large quantities rapidly, it's worth understanding.",
            keyFacts = listOf(
                "Kidneys can process ~0.8–1 litre per hour maximum",
                "Overhydration dilutes blood sodium — called hyponatraemia",
                "Most at risk: endurance athletes drinking only plain water",
                "For average daily drinking, overhydration is virtually impossible"
            ),
            whatGoesWrong = listOf(
                "Nausea, headache, and confusion from low blood sodium",
                "Muscle weakness and cramps when electrolytes are diluted",
                "In extreme cases, seizures or loss of consciousness",
                "Swelling in hands, feet, or face"
            ),
            whatThisMeansForYou = "If you're not running marathons, you genuinely don't need to worry about this. Drink when you're thirsty, aim for your personal target in HydraHeart, and don't stress about going slightly over on an active day. The real risk for most people runs in the opposite direction.",
            practicalTips = listOf(
                "During endurance exercise, alternate plain water with an electrolyte drink",
                "Don't try to 'force' large quantities in a short time — sip consistently",
                "If you're running a marathon or ultra event, follow an event-specific hydration plan",
                "For everyday life, drink to thirst and let your urine colour guide you"
            ),
            mythVsFact = listOf(
                MythFact("Drinking more water is always better", "Your kidneys have a processing limit. In everyday amounts, more water is usually fine — but drinking litres in a very short window is not beneficial."),
                MythFact("Overhydration is a common danger", "It's extremely rare outside endurance sport. Most people are at far greater risk of mild dehydration than overhydration.")
            ),
            hydraComment = "Yes it exists — but it's not something most people need to lose sleep over. Just be consistent, not excessive 🌊"
        ),
        EducationTopic(
            titleRes = R.string.edu_morning_hydration_title,
            icon = "🌅",
            colour = Color(0xFFFDA4AF),
            category = TopicCategory.HYDRATION,
            content = "Your body loses approximately 500ml of water overnight through breathing and skin evaporation, even without visible sweating. You wake up in a mild state of dehydration every single morning — which is part of why grogginess, a dry mouth, and low energy are so common first thing. Drinking water in the morning jumpstarts your kidneys, helps your body flush overnight metabolic waste, and begins restoring cognitive function before caffeine even enters the picture. In contrast, evening drinking has less rehydrating impact per glass because your kidneys slow down fluid processing in preparation for sleep. Drinking large amounts close to bedtime also disrupts sleep quality through nocturia (waking to urinate). The morning window is your highest-return hydration opportunity of the day.",
            previewText = "The most important glass of water is the one you haven't had yet today",
            summary = "Morning hydration is one of the highest-value habits you can build. Your body has been losing fluid all night, and the first glass of water sets the tone for how you feel and function for hours afterwards.",
            keyFacts = listOf(
                "~500ml of fluid is lost overnight via breathing and skin",
                "Morning water supports kidney flush and cognitive wake-up",
                "Drinking large amounts late evening disrupts sleep",
                "Morning is your highest-return hydration window"
            ),
            whatGoesWrong = listOf(
                "Skipping morning water extends overnight dehydration into the afternoon",
                "Late evening drinking causes night waking and reduced sleep quality",
                "Starting the day caffeine-first worsens dehydration before it's corrected",
                "Morning headaches are often dehydration — not sleep quality"
            ),
            whatThisMeansForYou = "If you have a morning coffee before you've had any water, your body is running on a caffeine hit while already mildly dehydrated. A single glass of water before your coffee takes thirty seconds and makes a measurable difference to how your morning feels.",
            practicalTips = listOf(
                "Keep a glass of water on your bedside table and drink it before getting up",
                "Front-load your intake — aim for 500ml in the first hour of your day",
                "Slow down on drinks after 7pm to protect sleep quality",
                "Have water before your first coffee, not after"
            ),
            insightCallout = "One glass of water before your first coffee is one of the highest-return health habits you can build.",
            hydraComment = "Water before coffee, not instead of it. That one shift makes your mornings feel different 🌅"
        ),
        EducationTopic(
            titleRes = R.string.edu_dehy_headaches_title,
            icon = "🤕",
            colour = Color(0xFFFBBF24),
            category = TopicCategory.HYDRATION,
            content = "Dehydration is one of the most common and consistently overlooked triggers for headaches. When your body loses fluid, blood volume decreases, which can cause blood vessels in the brain to temporarily narrow. To compensate, these vessels dilate — and that dilation, along with changes in brain pressure and reduced oxygen delivery, is what triggers the characteristic dull or throbbing pain. Dehydration headaches often appear at the front of the head and worsen when you move around or bend down. They typically respond well to hydration within 30–60 minutes — much faster than they respond to painkillers alone. Caffeine withdrawal, alcohol, and heat all contribute to dehydration-related headaches, making hydration the first intervention rather than medication.",
            previewText = "Most headaches respond to water faster than painkillers",
            summary = "If you get frequent headaches, dehydration is one of the first things worth investigating. It causes measurable changes in brain pressure and blood vessel behaviour that produce real, physical pain — and it's very treatable.",
            keyFacts = listOf(
                "Blood volume drops with dehydration, triggering vascular changes in the brain",
                "Dehydration headaches often improve within 30–60 minutes of drinking",
                "Caffeine withdrawal is its own separate headache trigger",
                "Alcohol, heat, and illness compound dehydration-related head pain"
            ),
            whatGoesWrong = listOf(
                "Regular dehydration headaches reduce productivity and focus",
                "Taking painkillers without addressing the cause becomes a cycle",
                "Caffeine used to mask headaches can worsen dependency over time",
                "Headaches worsen in hot weather if fluid intake doesn't increase to match"
            ),
            whatThisMeansForYou = "Next time you get a headache, drink two glasses of water before reaching for medication. If it clears within an hour, dehydration was the cause. Tracking this pattern can save you significant discomfort — and a lot of paracetamol.",
            practicalTips = listOf(
                "Try 400–500ml of water when a headache starts before taking painkillers",
                "Track whether headaches correlate with low HydraHeart intake days",
                "If you wake with a headache, drink water immediately — overnight dehydration is likely",
                "Reduce alcohol and caffeine before situations that already cause headaches"
            ),
            insightCallout = "Two glasses of water is worth trying before any headache medication. The response time will tell you everything.",
            hydraComment = "Most afternoon headaches have a very simple answer. Have you had enough water today? 🤕"
        ),
        EducationTopic(
            titleRes = R.string.edu_dehy_tired_title,
            icon = "😴",
            colour = Color(0xFFA78BFA),
            category = TopicCategory.HYDRATION,
            content = "Fatigue is one of the most common symptoms of dehydration — and one of the most misattributed. When you're dehydrated, your blood becomes slightly more viscous, making your heart work harder to pump it around your body. This increased cardiac effort reduces the oxygen and nutrient supply to working muscles and the brain, creating the sensation of tiredness and heaviness even if you've had a full night of sleep. Studies have shown that even 1–2% dehydration reduces endurance performance by up to 10% and makes the same physical task feel significantly harder. Dehydration also impairs the synthesis of certain neurotransmitters, contributing to mental fatigue and that flat, unmotivated feeling that can appear in the afternoon without obvious cause.",
            previewText = "Fix your energy levels by fixing this one habit",
            summary = "Unexplained fatigue is one of the most frequent signs of mild chronic dehydration. If you regularly feel tired during the day without a clear reason, hydration is one of the first and simplest things to examine.",
            keyFacts = listOf(
                "1–2% dehydration reduces physical endurance by up to 10%",
                "Thicker blood means the heart works harder, causing fatigue",
                "Dehydration reduces oxygen delivery to muscles and the brain",
                "Afternoon energy crashes are often dehydration, not just 'post-lunch dip'"
            ),
            whatGoesWrong = listOf(
                "Physical tasks feel harder and take more effort than they should",
                "Mental energy drops — concentration becomes laboured and slow",
                "The 3pm slump hits harder and lasts longer",
                "Exercise feels disproportionately difficult and recovery slows"
            ),
            whatThisMeansForYou = "Before you reach for coffee to push through an afternoon energy slump, try drinking 300–400ml of water first and waiting 15 minutes. Dehydration fatigue responds surprisingly quickly to hydration — and unlike caffeine, it doesn't come with a later crash.",
            practicalTips = listOf(
                "Drink 300ml of water when afternoon fatigue hits before anything else",
                "Avoid letting your HydraHeart progress fall below 50% by midday",
                "If exercise feels unusually hard, check your pre-workout hydration",
                "Add a pinch of sea salt or electrolytes if fatigue follows sweat-heavy sessions"
            ),
            hydraComment = "Coffee covers up tiredness. Water actually fixes one of the most common causes of it 😴"
        ),
        EducationTopic(
            titleRes = R.string.edu_electrolytes_title,
            icon = "⚡",
            colour = Color(0xFF4ADE80),
            category = TopicCategory.ELECTROLYTES,
            content = "Electrolytes are minerals that carry electrical charges and govern some of your body's most fundamental processes. The key ones — sodium, potassium, magnesium, calcium, chloride, and phosphorus — regulate fluid balance between your cells, transmit nerve signals, enable muscle contractions, and maintain pH levels in the blood. Every heartbeat, every muscle movement, and every conscious thought depends on precise electrolyte concentrations. You lose electrolytes continuously through sweat, urine, and breathing. On a moderate activity day, fluid and electrolyte replacement through normal food and drink is sufficient. But during intense exercise, hot weather, illness involving vomiting or diarrhoea, or prolonged fasting, electrolyte losses can exceed what food alone replaces — making targeted replenishment important.",
            previewText = "Your heartbeat depends on minerals most people quietly ignore",
            summary = "Electrolytes do far more than most people realise. They are the electrical system of your body — governing everything from how your heart beats to how your muscles contract and recover. Most of the time your diet handles them quietly, but certain situations demand more attention.",
            keyFacts = listOf(
                "Sodium target: under 2300mg/day (about 1 tsp of salt)",
                "Potassium target: ~2600mg/day for women (bananas, avocado, leafy greens)",
                "Magnesium target: ~310–320mg/day — many people fall short",
                "Lost through sweat, urine, and breathing throughout the day"
            ),
            keyFactsMale = listOf(
                "Sodium target: under 2300mg/day (about 1 tsp of salt)",
                "Potassium target: ~3400mg/day for men (bananas, avocado, leafy greens)",
                "Magnesium target: ~400–420mg/day — many people fall short",
                "Lost through sweat, urine, and breathing throughout the day"
            ),
            whatGoesWrong = listOf(
                "Muscle cramps and spasms — a classic sign of low magnesium or potassium",
                "Irregular heartbeat and palpitations in severe cases of sodium imbalance",
                "Weakness, confusion, and extreme fatigue from prolonged imbalance",
                "After intense exercise, headaches and nausea despite drinking plain water"
            ),
            whatThisMeansForYou = "Most of the time, a balanced diet keeps your electrolytes in check. The exceptions are heavy sweating sessions, hot weather, vomiting or diarrhoea, or if you drink very large amounts of plain water without any food. In those situations, plain water isn't enough.",
            whatThisMeansForYouMale = "If you train hard or sweat heavily at work, your electrolyte needs are higher than average. Plain water replaces the liquid volume but not the minerals — and that gap is often what makes recovery slower and cramps more frequent.",
            practicalTips = listOf(
                "Coconut water is a natural, lower-sugar electrolyte drink after moderate exercise",
                "Add a small pinch of sea salt to water after heavy or prolonged sweat",
                "Sports drinks are most useful for sessions lasting over 60–90 minutes",
                "Eat potassium-rich foods daily: bananas, sweet potato, spinach, avocado",
                "Magnesium-rich foods — dark chocolate, nuts, seeds — double as a good evening snack"
            ),
            insightCallout = "After a tough session, plain water replaces the fluid but not the minerals your muscles need to recover.",
            hydraComment = "After a tough workout, plain water isn't always enough. Replenish what you actually lost ⚡"
        ),
        EducationTopic(
            titleRes = R.string.edu_when_electrolytes_title,
            icon = "🧂",
            colour = Color(0xFF34D399),
            category = TopicCategory.ELECTROLYTES,
            content = "The human body is remarkably efficient at maintaining electrolyte balance under normal conditions, so for most people on most days, plain water is entirely sufficient. The situations where this changes are specific: exercise lasting over 60–90 minutes causes significant sodium and potassium losses through sweat that plain water cannot replace; hot weather dramatically increases sweat rate and mineral loss; prolonged vomiting or diarrhoea depletes both fluid and electrolytes rapidly; and very low-carbohydrate or ketogenic diets cause the kidneys to excrete more sodium and potassium than usual. In these contexts, an electrolyte drink, tablet, or electrolyte-rich food makes a meaningful difference to how quickly you recover and how you feel during the event.",
            previewText = "Plain water isn't always the right answer",
            summary = "Knowing when you need electrolytes rather than plain water is a practical skill that most people never develop. The answer changes based on what your body is going through — and it matters more than the marketing suggests.",
            keyFacts = listOf(
                "Exercise over 60–90 min warrants electrolyte replacement",
                "In the heat, sweat rate can reach 1–2L/hour with significant mineral loss",
                "Vomiting and diarrhoea are the fastest routes to electrolyte depletion",
                "Keto/low-carb diets increase sodium and potassium loss via kidneys"
            ),
            whatGoesWrong = listOf(
                "Drinking only plain water during long exercise causes dilution of sodium",
                "Post-illness recovery slows without electrolyte replenishment",
                "Cramps during or after exercise are often electrolyte-related, not just lactic acid",
                "Feeling worse despite drinking lots of water is a sign of electrolyte imbalance"
            ),
            whatThisMeansForYou = "For a regular working day with light activity, your food and water cover everything. On days with intense exercise, high heat, or illness, having an electrolyte option ready makes a practical difference — and it doesn't need to be expensive or complicated.",
            practicalTips = listOf(
                "For exercise under 60 minutes, plain water is almost always enough",
                "During illness, use oral rehydration salts (ORS) not sports drinks",
                "In hot weather, increase salty food intake slightly alongside extra water",
                "Electrolyte tablets or powder are practical and far cheaper than sports drinks"
            ),
            hydraComment = "Plain water is perfect most of the time. Knowing when it isn't is what changes things 🧂"
        ),
        EducationTopic(
            titleRes = R.string.edu_low_electrolytes_title,
            icon = "📉",
            colour = Color(0xFF6EE7B7),
            category = TopicCategory.ELECTROLYTES,
            content = "Low electrolytes — particularly low sodium (hyponatraemia), low potassium (hypokalaemia), or low magnesium — can cause a distinctive cluster of symptoms that are often mistaken for general tiredness or stress. Sodium is the most critical for immediate function; even modest drops cause the brain and muscles to underperform. Potassium deficiency is common and linked to muscle cramps, weakness, and irregular heartbeat. Magnesium deficiency is thought to affect up to 50% of people in developed countries and is associated with poor sleep, anxiety, muscle twitching, and fatigue. Blood tests can confirm deficiencies, but symptom patterns are often a sufficient guide to when dietary attention or supplementation is warranted.",
            previewText = "Cramps, poor sleep, anxiety — they could all share one cause",
            summary = "Electrolyte deficiencies are surprisingly common and frequently go undiagnosed. The symptoms — cramps, poor sleep, fatigue, and anxiety — are easy to attribute to other causes, which is why this area is so often overlooked.",
            keyFacts = listOf(
                "Magnesium deficiency is estimated to affect up to 50% of people",
                "Low potassium causes muscle weakness, cramps, and fatigue",
                "Low sodium — even mild — impairs brain and muscle function",
                "Most deficiencies respond within days to dietary adjustment"
            ),
            whatGoesWrong = listOf(
                "Persistent muscle cramps, especially in legs and at night",
                "Sleep disruption and difficulty staying asleep",
                "Increased anxiety and sensitivity to stress",
                "Irregular or strong heartbeat (palpitations) in potassium/magnesium deficiency"
            ),
            whatThisMeansForYou = "If you're experiencing regular muscle cramps, poor sleep alongside otherwise good habits, or unexplained low mood, your magnesium or potassium intake is worth reviewing before assuming it's something more complex.",
            practicalTips = listOf(
                "Add magnesium-rich foods: dark chocolate, almonds, spinach, pumpkin seeds",
                "Eat potassium-rich foods daily: bananas, avocado, potato, sweet potato",
                "Consider a magnesium glycinate supplement in the evening if sleep is poor",
                "Avoid high-sugar sports drinks as a routine electrolyte source"
            ),
            hydraComment = "Those leg cramps at night? Often magnesium. That anxious edge? Sometimes low electrolytes 📉"
        ),
        EducationTopic(
            titleRes = R.string.edu_natural_electrolytes_title,
            icon = "🥥",
            colour = Color(0xFF86EFAC),
            category = TopicCategory.ELECTROLYTES,
            content = "The best sources of electrolytes are whole foods and minimally processed drinks — not the heavily marketed sports drinks that often come loaded with sugar and artificial colours. Coconut water is one of the most well-balanced natural electrolyte drinks: it contains notable potassium, magnesium, and a small amount of sodium with relatively low sugar. Milk — including fortified plant milks — provides calcium, potassium, and some sodium. Whole fruits and vegetables are excellent sources: bananas and avocados are potassium-rich; dark leafy greens offer magnesium and calcium; olives and pickles contain sodium. For replenishment during intense exercise or illness, oral rehydration salts (ORS) are the most clinically effective and affordable option, often outperforming commercial sports drinks.",
            previewText = "Skip the sports drinks — your kitchen already has what you need",
            summary = "You don't need expensive sports drinks to keep your electrolytes balanced. The best sources are ordinary foods and a few simple drinks — and knowing which ones to reach for makes the whole thing easy and affordable.",
            keyFacts = listOf(
                "Coconut water: high potassium, moderate magnesium, lower sugar than sports drinks",
                "Bananas: ~422mg potassium — the original post-exercise snack",
                "Dark leafy greens: rich in magnesium and calcium",
                "Oral rehydration salts (ORS) are the most effective and affordable option for illness"
            ),
            whatGoesWrong = listOf(
                "Relying on sports drinks adds significant sugar to your daily intake",
                "Avoiding salty foods entirely can push sodium too low on active days",
                "Plant-based diets need intentional potassium and magnesium focus"
            ),
            whatThisMeansForYou = "Before spending money on electrolyte tablets or fancy drinks, check if your everyday diet is already doing most of the work. A banana, some leafy greens, and decent food variety covers most people's electrolyte needs without any supplements.",
            practicalTips = listOf(
                "Coconut water after exercise is a clean, effective, lower-sugar sports drink alternative",
                "Eat one potassium-rich food every day: banana, avocado, potato, spinach",
                "For illness or intense heat, ORS sachets are inexpensive and highly effective",
                "A small pinch of sea salt in water during long runs is practical and cheap"
            ),
            hydraComment = "Coconut water, bananas, and leafy greens do more than any expensive sports drink. Real food first 🥥"
        ),
        EducationTopic(
            titleRes = R.string.edu_caffeine_title,
            icon = "☕",
            colour = Color(0xFFA0724A),
            category = TopicCategory.CAFFEINE,
            content = "Caffeine works by blocking adenosine receptors in the brain. Adenosine is a neurotransmitter that builds up throughout the day and promotes sleepiness — caffeine prevents it from binding, which is why you feel more alert. This effect peaks 30–60 minutes after consumption and persists for several hours. Up to 400mg per day (roughly 4 cups of brewed coffee) is the widely accepted safe upper limit for healthy adults. At higher doses, caffeine overstimulates the nervous system, causing jitteriness, elevated heart rate, anxiety, and digestive discomfort. Caffeine also has a mild diuretic effect at high doses, though at moderate intake the fluid in caffeinated drinks still contributes positively to overall hydration. The half-life of caffeine is approximately 5–6 hours, meaning half the caffeine from a 3pm coffee is still active at 9pm.",
            previewText = "You're probably not using caffeine as effectively as you could",
            summary = "Caffeine is one of the most widely consumed psychoactive substances in the world — and at moderate doses, it genuinely works. But the same mechanisms that make it effective also make it easy to misuse, especially when it comes to timing.",
            keyFacts = listOf(
                "Safe daily limit: ~400mg — roughly 4 cups of brewed coffee",
                "Half-life is 5–6 hours — a 3pm coffee is still active at 9pm",
                "Peak effect: 30–60 minutes after consumption",
                "During pregnancy: limit to under 200mg per day"
            ),
            keyFactsMale = listOf(
                "Safe daily limit: ~400mg — roughly 4 cups of brewed coffee",
                "Half-life is 5–6 hours — a 3pm coffee is still active at 9pm",
                "Peak effect: 30–60 minutes after consumption",
                "Athletes with high muscle mass may metabolise caffeine slightly faster"
            ),
            whatGoesWrong = listOf(
                "Consuming after 2pm disrupts sleep onset and sleep quality",
                "Exceeding 400mg causes anxiety, jitteriness, and racing heart",
                "Regular high doses build tolerance, reducing effectiveness over time",
                "Caffeine withdrawal causes headaches, fatigue, and irritability",
                "Using caffeine to mask tiredness creates a cycle of poor sleep and higher dependency"
            ),
            whatThisMeansForYou = "Most people don't realise how late caffeine stays active in their system. If you regularly struggle to fall asleep or sleep feels unrestorative, your afternoon or evening caffeine is a strong first suspect — even if it feels like it \"doesn't affect\" you.",
            practicalTips = listOf(
                "Set a caffeine cutoff at 2pm — or earlier if sleep is sensitive",
                "Switch to herbal tea or decaf after lunch without sacrificing the ritual",
                "Track caffeine across all sources: coffee, tea, energy drinks, pre-workout",
                "If you feel jittery or anxious, you've exceeded your personal threshold — not the guideline",
                "Take a periodic caffeine break (2–3 days) to reset tolerance"
            ),
            insightCallout = "Caffeine doesn't give you energy — it temporarily disguises the absence of it. The debt comes later.",
            mythVsFact = listOf(
                MythFact("Coffee dehydrates you", "At moderate doses, coffee's mild diuretic effect is offset by the fluid it contains. A cup of coffee still counts toward hydration."),
                MythFact("Caffeine tolerance means it no longer affects sleep", "Tolerance reduces the stimulating effects — but caffeine continues to block adenosine and disrupt sleep architecture even in regular users.")
            ),
            hydraComment = "That 3pm coffee might be why you're staring at the ceiling at midnight ☕"
        ),
        EducationTopic(
            titleRes = R.string.edu_caffeine_timing_title,
            icon = "⏰",
            colour = Color(0xFFB45309),
            category = TopicCategory.CAFFEINE,
            content = "The timing of caffeine intake has a greater effect on its usefulness than the total amount consumed. Cortisol — your body's natural alertness hormone — peaks shortly after waking (typically 30–45 minutes after rising) and creates a window of natural high alertness that caffeine actually cannot improve. Having caffeine during this cortisol peak wastes its effect and accelerates tolerance. Research suggests the optimal window for caffeine is approximately 90–120 minutes after waking, when cortisol begins its first dip. This is when caffeine genuinely adds alertness rather than simply stacking on top of already-high wakefulness. The second consideration is the cutoff time: with a 5–6 hour half-life, caffeine consumed after 2pm will still be meaningfully active during early sleep, suppressing deep sleep quality even if it doesn't prevent sleep onset entirely.",
            previewText = "The best time for your first coffee isn't first thing in the morning",
            summary = "The time you have your first coffee matters more than most people realise. Getting the timing right means caffeine works harder, tolerance builds slower, and sleep stays protected — all without reducing how much you enjoy it.",
            keyFacts = listOf(
                "Optimal first caffeine window: ~90 minutes after waking",
                "Cortisol peaks right after waking — caffeine then is mostly wasted",
                "Caffeine half-life is 5–6 hours — 2pm is a sensible cutoff for most",
                "Caffeine suppresses deep sleep even when it doesn't prevent falling asleep"
            ),
            whatGoesWrong = listOf(
                "Coffee immediately on waking builds cortisol tolerance and crashes harder later",
                "Late caffeine impairs deep sleep stages without preventing sleep onset",
                "Poor deep sleep leads to feeling unrefreshed, which drives earlier, larger caffeine doses",
                "The cycle: bad sleep → more caffeine → worse sleep → more caffeine"
            ),
            whatThisMeansForYou = "Waiting 90 minutes before your first coffee sounds difficult, but most people who try it notice they need less caffeine overall and feel more evenly alert throughout the day. It's the kind of small change with disproportionately large returns.",
            practicalTips = listOf(
                "Have water and a light breakfast first, then your coffee 90 minutes after waking",
                "Set a daily caffeine cutoff alarm at 1:30–2pm",
                "If you struggle without morning coffee, it's often withdrawal — try tapering gradually",
                "Experiment with delaying your first cup by 30 minutes each week until you reach 90"
            ),
            insightCallout = "Your cortisol is already doing caffeine's job for the first hour after waking. Let it work.",
            hydraComment = "The best time for your first coffee is probably later than you think. Your body is already alert right after waking ⏰"
        ),
        EducationTopic(
            titleRes = R.string.edu_caffeine_anxiety_title,
            icon = "😰",
            colour = Color(0xFFD97706),
            category = TopicCategory.CAFFEINE,
            content = "Caffeine stimulates the release of adrenaline and activates the sympathetic nervous system — the same 'fight or flight' system triggered by stress and perceived danger. For most people at moderate doses this produces useful alertness. But in people with anxiety disorders, high cortisol levels, or genetic sensitivity to caffeine (linked to variations in the CYP1A2 enzyme), even moderate caffeine can trigger or significantly amplify anxiety, restlessness, and physical symptoms like a racing heart, sweating, and muscle tension. Caffeine also lowers the threshold for panic attacks in people with panic disorder. The key distinction is that caffeine doesn't create anxiety from nothing — it amplifies whatever background state already exists. If your baseline is already stressed or anxious, caffeine adds fuel to that fire.",
            previewText = "That second coffee might be feeding your anxiety, not your focus",
            summary = "The relationship between caffeine and anxiety is more nuanced than a simple 'caffeine causes anxiety.' It depends on your genetics, your current stress levels, and how much you're consuming — but understanding it gives you real control.",
            keyFacts = listOf(
                "Caffeine activates the same sympathetic 'fight or flight' response as stress",
                "Genetic CYP1A2 variations make some people significantly more caffeine-sensitive",
                "Even 1–2 cups can worsen anxiety in sensitive individuals",
                "Caffeine lowers the threshold for panic attacks in panic disorder"
            ),
            whatGoesWrong = listOf(
                "Restlessness, racing thoughts, and muscle tension that mimics anxiety",
                "Racing heart and palpitations in sensitive individuals",
                "Heightened emotional reactivity and reduced stress tolerance",
                "Poor sleep from late caffeine compounding next-day anxiety"
            ),
            whatThisMeansForYou = "If you experience anxiety and also drink significant caffeine, reducing intake — even by one cup — is worth trialling for two weeks. Many people notice a marked reduction in baseline anxiety without realising caffeine was the driver. It's not weakness; it's biology.",
            practicalTips = listOf(
                "If anxiety is present, reduce caffeine gradually over 1–2 weeks (not cold turkey)",
                "Switch one daily coffee to green tea — lower caffeine with L-theanine that blunts jitteriness",
                "Avoid caffeine on high-stress days when anxiety baseline is already elevated",
                "Track your caffeine intake alongside mood — the correlation often becomes clear quickly"
            ),
            insightCallout = "Caffeine doesn't create anxiety — it amplifies whatever your nervous system is already doing.",
            hydraComment = "If you're anxious and you've had three coffees today, they're probably not helping 😰"
        ),
        EducationTopic(
            titleRes = R.string.edu_caffeine_sleep_title,
            icon = "🌙",
            colour = Color(0xFFF59E0B),
            category = TopicCategory.CAFFEINE,
            content = "Caffeine disrupts sleep through its core mechanism of blocking adenosine receptors. Adenosine is what makes you feel progressively sleepier throughout the day; by blocking it, caffeine delays sleep pressure. But here's the underappreciated part: caffeine continues to block these receptors for hours after consumption — and when it finally clears, the accumulated adenosine floods the receptors at once, causing a rapid energy crash. More critically, even when caffeine doesn't prevent falling asleep, research by Matthew Walker and others has shown it measurably reduces the amount of slow-wave (deep) sleep — the most restorative stage. This means you can sleep 8 hours but still wake feeling unrefreshed if late caffeine is disrupting sleep architecture. This creates a self-perpetuating cycle: poor quality sleep → more caffeine to compensate → further sleep disruption.",
            previewText = "Caffeine is still in your system at midnight even if you feel tired",
            summary = "The caffeine-sleep relationship goes deeper than just staying awake. Even when you fall asleep on time, late caffeine quietly steals your deep sleep — the stage responsible for feeling genuinely restored the next morning.",
            keyFacts = listOf(
                "Caffeine half-life: 5–6 hours — a 3pm coffee is still ~50% active at 9pm",
                "Caffeine reduces slow-wave (deep) sleep even when sleep onset is normal",
                "Poor deep sleep causes next-day fatigue, driving higher caffeine consumption",
                "Individual metabolism varies — some people clear caffeine much more slowly"
            ),
            whatGoesWrong = listOf(
                "Falling asleep but waking feeling unrefreshed and foggy",
                "Sleep becomes lighter, with more frequent micro-arousals",
                "A cycle of poor sleep driving more caffeine, which drives poorer sleep",
                "Chronic poor sleep from late caffeine compounds into mood and health issues over months"
            ),
            whatThisMeansForYou = "If you've been telling yourself that caffeine 'doesn't affect your sleep' because you can fall asleep fine — check how you feel when you wake. Deep sleep quality is invisible until its absence starts showing up as persistent tiredness, poor mood, and slower thinking.",
            practicalTips = listOf(
                "Move your caffeine cutoff to 1–2pm and track sleep quality for one week",
                "If you're a slow caffeine metaboliser, 12pm may be the right cutoff for you",
                "Try two caffeine-free days and notice the sleep difference — the contrast is usually striking",
                "Switch afternoon drinks to herbal tea: chamomile, peppermint, or rooibos"
            ),
            insightCallout = "You can fall asleep perfectly and still have caffeine stealing your deep sleep without you knowing.",
            hydraComment = "Sleeping isn't the same as sleeping well. Late caffeine is one of the most common reasons why 🌙"
        ),
        EducationTopic(
            titleRes = R.string.edu_caffeine_crash_title,
            icon = "⚡",
            colour = Color(0xFFEF4444),
            category = TopicCategory.CAFFEINE,
            content = "The caffeine crash — the sharp drop in energy and alertness that follows a caffeine peak — is the result of adenosine floods. While caffeine is blocking adenosine receptors, adenosine continues to build up. When caffeine clears, this built-up adenosine binds rapidly to the now-available receptors, creating a sudden, often intense drop in energy. The crash is worsened by blood sugar fluctuations when caffeine is combined with sugar (as in energy drinks or sweet coffee), by inadequate sleep that causes higher underlying adenosine pressure, and by drinking caffeine on an empty stomach. High doses and rapid consumption accelerate the peak and steepen the crash. Understanding this cycle helps make deliberate choices about dose, timing, and food pairing that smooth out the energy curve significantly.",
            previewText = "The 3pm slump has a very specific biological cause",
            summary = "Caffeine crashes are predictable and largely preventable. They're caused by a specific biological mechanism — not weakness or poor sleep alone — and small changes to how you consume caffeine can eliminate most of them.",
            keyFacts = listOf(
                "Crashes occur when blocked adenosine floods receptors as caffeine clears",
                "Sugar combined with caffeine amplifies the crash through blood sugar drop",
                "Drinking caffeine on an empty stomach worsens both peak and crash",
                "Smaller, spread doses cause less dramatic peaks and crashes than one large hit"
            ),
            whatGoesWrong = listOf(
                "Sharp mid-afternoon energy drop, irritability, and difficulty concentrating",
                "Reaching for more caffeine to fix the crash — extending the cycle",
                "Adding sugar to caffeine makes the energy and mood swing more extreme",
                "Persistent crashes throughout the day signal over-reliance on caffeine"
            ),
            whatThisMeansForYou = "The easiest way to reduce caffeine crashes is to eat before or alongside your coffee, reduce the dose slightly, and avoid sweet additions. A smaller, later coffee often delivers more sustained focus than a large early one.",
            practicalTips = listOf(
                "Eat something before or with caffeine — never on a completely empty stomach",
                "Avoid combining caffeine with high-sugar drinks or sugary food",
                "Spread caffeine across 2 smaller doses rather than one large amount",
                "If crashes are frequent, try cutting total caffeine by 25% for two weeks"
            ),
            hydraComment = "The bigger the spike, the steeper the crash. Smaller, earlier doses almost always win ⚡"
        ),
        EducationTopic(
            titleRes = R.string.edu_sugar_title,
            icon = "🍬",
            colour = Color(0xFFF472B6),
            category = TopicCategory.SUGAR,
            content = "Added sugar — sucrose and fructose added to processed foods and drinks, as distinct from naturally occurring sugars in fruit — is metabolised differently to complex carbohydrates. It enters the bloodstream rapidly, causing a fast glucose spike, followed by an insulin response, followed by a drop that often falls below baseline — the classic energy crash. The World Health Organisation recommends limiting free sugars to under 10% of total calorie intake (roughly 50g per day for most adults), with the additional guidance that under 5% (about 25g) provides further health benefits. The difficulty is that sugar hides effectively in drinks — even ones marketed as healthy — making it easy to exceed the daily limit before eating a single meal.",
            previewText = "One can of cola is almost your entire daily sugar allowance",
            summary = "Added sugar hides in plain sight across most commercial drinks. The guidelines are clear, the impacts are real, and the amount hiding in everyday beverages is consistently surprising — even in products that don't taste particularly sweet.",
            keyFacts = listOf(
                "WHO recommended limit: 50g/day (ideal: under 25g)",
                "One 330ml cola: ~35g added sugar — most of your daily allowance",
                "\"Healthy\" fruit juice: 21–36g of sugar per 250ml glass",
                "Energy drinks: 27–54g of sugar per 500ml can"
            ),
            whatGoesWrong = listOf(
                "Blood sugar spikes and crashes cause energy swings and increased hunger",
                "High sugar intake is strongly associated with weight gain, particularly visceral fat",
                "Excess sugar contributes to inflammation, skin breakouts, and accelerated ageing",
                "Long-term high intake is linked to type 2 diabetes, fatty liver, and cardiovascular disease",
                "Poor dental health — sugar feeds bacteria that erode tooth enamel"
            ),
            whatThisMeansForYou = "You don't need to eliminate sugar — but reading labels on drinks is genuinely revelatory. Most people are surprised how quickly fruit juices, flavoured waters, and health drinks contribute to exceeding the daily limit. Swapping a few regular drinks makes a significant difference without feeling like deprivation.",
            practicalTips = listOf(
                "Read the 'of which sugars' line on drink labels — not just total carbs",
                "Sparkling water with fresh citrus or cucumber gives the fizz without the sugar",
                "Choose whole fruit over juice — the fibre slows sugar absorption significantly",
                "Energy drinks and flavoured coffees are among the highest hidden-sugar sources",
                "Zero-sugar versions are a better swap than high-sugar originals — despite the debate"
            ),
            insightCallout = "Most people are surprised to find they exceed the WHO sugar guideline before their first meal of the day.",
            mythVsFact = listOf(
                MythFact("Fruit juice is a healthy drink", "Juicing removes the fibre that slows sugar absorption. A glass of orange juice contains nearly as much sugar as cola — just with vitamins alongside it."),
                MythFact("Natural sugar is always fine", "Fructose from fruit within whole food is buffered by fibre. Concentrated natural sugars in juice or smoothies behave more like added sugar metabolically.")
            ),
            hydraComment = "A lot of drinks market themselves as healthy while quietly loading you with sugar. Check the label 👀"
        ),
        EducationTopic(
            titleRes = R.string.edu_hidden_sugar_title,
            icon = "👀",
            colour = Color(0xFFEC4899),
            category = TopicCategory.SUGAR,
            content = "Drinks are the single biggest vehicle for hidden sugar in most people's diets — and the category where marketing most consistently misleads. Sports drinks typically contain 14–17g of sugar per 500ml, often positioned as health products. Flavoured lattes from coffee chains can contain 30–50g of sugar per serving. 'Natural' energy drinks and vitamin waters frequently carry 15–25g. Kombucha — widely marketed as a wellness drink — contains 8–18g per bottle. Even supposedly clean products like smoothies can hit 40–60g of sugar per serving if they contain fruit concentrates or added sweeteners. The only reliable tool for navigating this is reading the actual label, specifically the 'of which sugars' figure per serving — and comparing it to the 25–50g daily guideline.",
            previewText = "The drinks labelled healthy often have the most sugar",
            summary = "Hidden sugar in drinks is one of the most common reasons people exceed their daily sugar limit without realising it. The worst offenders aren't always the obvious ones — often it's the health-positioned products that surprise people most.",
            keyFacts = listOf(
                "Flavoured lattes: 30–50g sugar in a single serving",
                "Sports drinks: 14–17g per 500ml — not just post-exercise",
                "Vitamin waters and 'natural' energy drinks: 15–25g per bottle",
                "Shop smoothies: 40–60g in a single cup with fruit concentrates"
            ),
            whatGoesWrong = listOf(
                "Unknowingly exceeding the daily sugar limit before lunch",
                "Marketing language ('natural', 'vitamin-enriched', 'organic') masks high sugar content",
                "Regular high-sugar drinks normalise sweet taste, making water feel insufficient",
                "Blood sugar instability from multiple sugary drinks across the day"
            ),
            whatThisMeansForYou = "The fix isn't complicated — it's just reading labels. Checking the 'of which sugars' per serving on any drink before buying it takes seconds and gives you genuine control over something that quietly adds up over years.",
            practicalTips = listOf(
                "Always check 'of which sugars' per serving — not just calories or total carbs",
                "Black coffee, plain tea, and sparkling water are zero-sugar drink defaults",
                "Kombucha is a good swap for fizzy drinks, but choose low-sugar varieties (under 5g)",
                "Make your own smoothies with whole fruit to know exactly what's in them"
            ),
            insightCallout = "The healthiest-looking drink in the café is sometimes the one with the most sugar. The label always tells the truth.",
            hydraComment = "The health halo on some drinks is really something. Always, always check the label 👀"
        ),
        EducationTopic(
            titleRes = R.string.edu_sugar_crash_title,
            icon = "📊",
            colour = Color(0xFFFB7185),
            category = TopicCategory.SUGAR,
            content = "A sugar crash — formally called reactive hypoglycaemia — occurs when a rapid rise in blood glucose triggers a sharp insulin response that overshoots, driving blood sugar below the normal range. The sequence is predictable: high-sugar food or drink causes a fast glucose spike, insulin is released in proportion to that spike (sometimes exceeding what's needed), blood sugar drops rapidly, and the resulting low triggers physical symptoms including shakiness, irritability, difficulty concentrating, intense hunger, and sometimes headache or nausea. The higher and faster the initial spike, the steeper the subsequent drop. Foods that combine high sugar with high caffeine — energy drinks, sweet coffee, sugary pre-workouts — create particularly sharp cycles because caffeine independently elevates cortisol and adds to blood glucose instability.",
            previewText = "Your afternoon energy crash is entirely predictable — and fixable",
            summary = "Sugar crashes aren't random — they follow a specific biological pattern that, once understood, becomes largely preventable. Knowing what drives them gives you real control over your energy levels throughout the day.",
            keyFacts = listOf(
                "Reactive hypoglycaemia: blood sugar drops sharply after a high spike",
                "Symptoms peak 1–3 hours after consuming high-sugar food or drink",
                "Caffeine combined with sugar accelerates and steepens the cycle",
                "Fibre and protein dramatically slow glucose absorption and blunt crashes"
            ),
            whatGoesWrong = listOf(
                "Shakiness, irritability, and intense hunger between meals",
                "Difficulty concentrating during the drop — often misread as distraction",
                "Reaching for more sugar to fix the crash, perpetuating the cycle",
                "Energy becomes unpredictable and hard to manage across the day"
            ),
            whatThisMeansForYou = "If your energy swings up sharply after sweet drinks and crashes a couple of hours later, the cycle is sugar-driven. The simplest fix is pairing sweet things with fibre or protein — a banana with a handful of almonds, for example — which slows absorption and prevents the spike-drop pattern.",
            practicalTips = listOf(
                "Always pair sweet drinks or snacks with a source of protein or fibre",
                "Whole fruit causes a smaller spike than juice because fibre slows absorption",
                "Avoid high-sugar drinks on an empty stomach — this creates the sharpest spikes",
                "Switch from fizzy drinks to sparkling water with a small amount of fruit juice"
            ),
            hydraComment = "That 3pm energy slump after a sweet drink? That's the crash talking. You can prevent it 📊"
        ),
        EducationTopic(
            titleRes = R.string.edu_sweeteners_title,
            icon = "🔬",
            colour = Color(0xFFE879F9),
            category = TopicCategory.SUGAR,
            content = "Artificial sweeteners — including aspartame, sucralose, acesulfame K, and saccharin — provide sweetness with minimal or zero calories and no blood sugar impact. They are approved as safe by major regulatory bodies including the FDA, EFSA, and WHO at normal consumption levels. However, research on their long-term effects on gut microbiome composition, appetite regulation, and insulin response is ongoing and inconclusive. Some studies suggest that intensely sweet tastes (even without calories) may maintain a preference for very sweet foods, potentially affecting eating patterns over time. More recent research has raised questions about cardiovascular effects of some sweeteners (particularly erythritol and xylitol at very high doses), though these findings are preliminary. For most people, artificially sweetened drinks are a substantially better choice than their sugary equivalents — the key risk is using them as permission to overeat elsewhere.",
            previewText = "Sweeteners aren't the villain they're made out to be",
            summary = "Artificial sweeteners occupy a complex middle ground in nutrition science. They're significantly better than sugar in many contexts, but they're not without open questions. Understanding the actual evidence helps you make a genuinely informed choice.",
            keyFacts = listOf(
                "All major sweeteners are approved safe by FDA, EFSA, and WHO at normal use",
                "No blood sugar impact — making them useful for calorie and glucose management",
                "Gut microbiome effects are real but not yet fully understood",
                "Zero-sugar drinks are a better swap from high-sugar drinks for most people"
            ),
            whatGoesWrong = listOf(
                "Using 'zero sugar' drinks as mental permission to eat more elsewhere",
                "Very high sweetener intake may affect gut microbiome composition over time",
                "Maintaining a high preference for very sweet tastes makes water feel dull",
                "Some people experience digestive sensitivity to certain sweeteners (sorbitol, xylitol)"
            ),
            whatThisMeansForYou = "If you're switching from regular fizzy drinks to zero-sugar versions, that's a genuine improvement. The goal longer-term is reducing the dependence on intense sweetness — but as a transition strategy, sugar-free works. Don't let perfect be the enemy of better.",
            practicalTips = listOf(
                "Zero-sugar drinks are a better choice than high-sugar equivalents for most people",
                "Gradually reduce sweetness preference by mixing full-sugar with sparkling water",
                "Don't treat 'diet' or 'zero' drinks as a free pass for extra sweet food elsewhere",
                "Experiment with naturally flavoured water (mint, citrus, ginger) to reduce sweetener reliance"
            ),
            mythVsFact = listOf(
                MythFact("Artificial sweeteners cause cancer", "At doses consumed in everyday drinks, there is no established cancer risk. The studies suggesting risk used far higher amounts than any person would realistically consume."),
                MythFact("Diet drinks are just as bad as regular", "For blood sugar, calories, and dental health, they are meaningfully different. They're not perfect, but they are a real improvement over high-sugar equivalents.")
            ),
            hydraComment = "Zero-sugar drinks aren't villains. They're a legitimate stepping stone. Just don't stop there 🔬"
        ),
        EducationTopic(
            titleRes = R.string.edu_alcohol_title,
            icon = "🍷",
            colour = Color(0xFFA855F7),
            category = TopicCategory.ALCOHOL,
            content = "Alcohol's dehydrating mechanism is specific and significant. Ethanol suppresses the production of antidiuretic hormone (ADH), also known as vasopressin, in the pituitary gland. ADH normally signals the kidneys to reabsorb water and concentrate urine. Without it, the kidneys produce far more dilute urine — approximately 100–120ml of extra fluid loss per standard drink. This is in addition to the volume of the drink itself, meaning alcohol creates a net fluid deficit rather than contributing to hydration. Spirits are the most dehydrating because they contain the highest ethanol concentration per volume. Beer dehydrates less per glass than spirits, but the volume consumed often makes total dehydration comparable. Wine sits in between. The dry mouth, headache, and fatigue of a hangover are substantially dehydration symptoms amplified by acetaldehyde (the toxic metabolite of alcohol).",
            previewText = "Alcohol takes more fluid from your body than it gives",
            summary = "Alcohol is uniquely dehydrating because it actively suppresses the hormone that tells your kidneys to conserve water. This is why drinking alcohol leaves you more dehydrated than simply not drinking anything at all.",
            keyFacts = listOf(
                "Each standard drink causes ~100–120ml extra fluid loss",
                "Alcohol suppresses ADH — the hormone that conserves water in your kidneys",
                "Spirits are the most dehydrating per ml; beer the least per drink",
                "Low-risk guideline: no more than 14 units per week, spread over several days"
            ),
            whatGoesWrong = listOf(
                "Dehydration causes the classic hangover headache, dry mouth, and fatigue",
                "Alcohol disrupts sleep architecture, reducing REM and deep sleep",
                "Acetaldehyde — alcohol's metabolite — is directly toxic and contributes to nausea",
                "Regular drinking progressively worsens skin hydration, clarity, and elasticity",
                "Alcohol in hot weather or after exercise can cause rapid severe dehydration"
            ),
            whatThisMeansForYou = "You don't need to stop drinking to drink smarter. Alternating alcohol with water, eating alongside drinking, and having a large glass of water before bed after a night out are genuinely effective strategies that reduce both next-day effects and longer-term impact.",
            practicalTips = listOf(
                "Alternate every alcoholic drink with a full glass of still water",
                "Drink a large glass of water before bed after a night out",
                "Eat a proper meal before or alongside drinking — it slows alcohol absorption significantly",
                "Avoid alcohol before exercise or on very hot days — dehydration compounds quickly",
                "Lower-ABV options (wine spritzers, low-alcohol beer) significantly reduce fluid loss"
            ),
            insightCallout = "Every standard drink creates a net fluid deficit. Matching it with water isn't optional if you want to feel well the next day.",
            hydraComment = "For every drink, I need you to match it with water. That's the deal 🫧"
        ),
        EducationTopic(
            titleRes = R.string.edu_alcohol_sleep_title,
            icon = "🛏️",
            colour = Color(0xFF9333EA),
            category = TopicCategory.ALCOHOL,
            content = "The popular belief that alcohol helps sleep is one of the most consequential misconceptions in health and wellness. While alcohol does reduce the time it takes to fall asleep (sleep latency), it severely disrupts sleep architecture — specifically REM sleep, which is the stage associated with emotional processing, memory consolidation, and mental restoration. Even moderate drinking (1–2 units) measurably reduces REM sleep in the first half of the night. The rebound effect in the second half of the night — as alcohol is metabolised — causes lighter, more fragmented sleep and earlier waking. Heart rate remains elevated throughout the night, and sleep quality monitoring consistently shows significantly poorer sleep scores on nights with alcohol, even when total sleep hours are similar. Over time, using alcohol as a sleep aid creates dependency and progressively worsens the underlying sleep issues it's masking.",
            previewText = "That nightcap is costing you deep sleep you won't notice losing",
            summary = "Alcohol and good sleep are a genuine contradiction. It feels like a relaxing wind-down, but the science is consistent: even small amounts meaningfully damage sleep quality in ways you feel the next day — even if you don't connect the cause.",
            keyFacts = listOf(
                "Alcohol reduces REM sleep — the most mentally restorative stage",
                "Sleep quality monitoring shows significant reduction even after 1–2 drinks",
                "Rebound alertness in the second half of night causes lighter, fragmented sleep",
                "Heart rate stays elevated throughout the night, reducing sleep efficiency"
            ),
            whatGoesWrong = listOf(
                "Waking earlier than intended and being unable to return to sleep",
                "Vivid or disturbing dreams in the rebound REM period later in the night",
                "Feeling unrefreshed despite sleeping a full night",
                "Long-term: growing dependency on alcohol to feel sleepy, worsening underlying insomnia"
            ),
            whatThisMeansForYou = "If you reach for a drink to wind down before bed regularly, there's a strong chance it's making your sleep — and by extension, your mood, focus, and energy the next day — meaningfully worse. The relaxation feels real; the sleep quality cost is also real.",
            practicalTips = listOf(
                "Replace evening alcohol with herbal tea, warm milk, or a magnesium drink",
                "If sleep is poor after drinking, try tracking it with wearable data — the correlation is usually striking",
                "Avoid alcohol within 3 hours of bed for noticeably better sleep quality",
                "Address underlying sleep issues directly rather than using alcohol as a mask"
            ),
            insightCallout = "Alcohol makes you fall asleep faster and sleep worse. That trade isn't worth it most nights.",
            hydraComment = "You might fall asleep faster, but you're not actually sleeping well after drinks. Your body knows 🛏️"
        ),
        EducationTopic(
            titleRes = R.string.edu_hangovers_title,
            icon = "🥴",
            colour = Color(0xFF7C3AED),
            category = TopicCategory.ALCOHOL,
            content = "A hangover is a cluster of symptoms caused by several simultaneous processes: dehydration from ADH suppression; acetaldehyde toxicity as the liver metabolises alcohol; alcohol-induced inflammation through cytokine release; disrupted sleep architecture; blood sugar fluctuations; and, for those with alcohol sensitivity, a histamine response from fermented drinks. Contrary to popular belief, the primary hangover driver is acetaldehyde (a toxic metabolite) and systemic inflammation — not dehydration alone. This is why drinking water helps with headache and dry mouth but doesn't resolve the nausea, fatigue, and cognitive fog that are inflammation and acetaldehyde-driven. There is no proven 'hangover cure' — the only effective strategy is time, rest, and supportive care. Greasy food, paracetamol (never ibuprofen on an empty stomach after drinking), hydration, electrolytes, and sleep are the evidence-supported supports.",
            previewText = "A hangover is three different problems happening at once",
            summary = "Hangovers are more complex than just dehydration. Understanding what's actually happening makes it easier to support your body properly — and to avoid the myths that don't help.",
            keyFacts = listOf(
                "Acetaldehyde (toxic alcohol metabolite) is a primary hangover driver",
                "Systemic inflammation causes the fatigue, brain fog, and nausea",
                "Dehydration causes the headache, dry mouth, and thirst component",
                "No supplement or food 'cures' a hangover — time and rest are unavoidable"
            ),
            whatGoesWrong = listOf(
                "Reaching for ibuprofen on an empty stomach after drinking risks gastric damage",
                "Hair of the dog delays — not prevents — the hangover and worsens the cycle",
                "Ignoring electrolytes means hydration recovery is slower and less effective",
                "Forcing food on severe nausea worsens symptoms — gentle hydration first"
            ),
            whatThisMeansForYou = "The best hangover strategy is pre-emptive: alternating drinks with water, eating a full meal, and having 500ml of water with an electrolyte before bed reduces the severity reliably. After the fact, rest, oral rehydration, and paracetamol if needed are the only genuinely effective tools.",
            practicalTips = listOf(
                "Drink 500ml of water with an electrolyte tablet before sleeping after a night out",
                "Eat a proper meal before or during drinking — food slows alcohol absorption dramatically",
                "Paracetamol for headache is safer than ibuprofen on an irritated stomach",
                "Avoid 'hair of the dog' — it delays and sometimes worsens the eventual hangover",
                "Bland carbohydrates (toast, rice) are the most tolerated food during a hangover"
            ),
            hydraComment = "There's no magic cure — but there are things that genuinely help. Water and electrolytes before sleep is the best investment you can make 🥴"
        ),
        EducationTopic(
            titleRes = R.string.edu_drinking_hydration_title,
            icon = "🫧",
            colour = Color(0xFFBF5AF2),
            category = TopicCategory.ALCOHOL,
            content = "Mitigating alcohol's dehydrating effects is practical and meaningful, even if no strategy fully eliminates the net fluid deficit that ethanol creates. The most evidence-supported approaches are: eating a substantial meal before or during drinking, which slows alcohol absorption and reduces the ADH suppression effect; alternating alcoholic drinks with water throughout the evening, which partially replaces ongoing fluid losses; choosing lower-ABV options where possible, since dehydration is directly proportional to alcohol content; and drinking 500ml of water with an electrolyte source before bed, which provides the body's best opportunity for overnight rehydration. Drinking coconut water or oral rehydration salts before sleep provides superior electrolyte replacement compared to plain water alone, because alcohol also depletes sodium and potassium alongside fluid.",
            previewText = "Two simple habits that make a real difference the morning after",
            summary = "Drinking and staying reasonably hydrated aren't incompatible — it just takes a few deliberate habits. These strategies won't eliminate all effects but make a measurable difference to how you feel the next morning.",
            keyFacts = listOf(
                "Food before drinking slows alcohol absorption and reduces ADH suppression",
                "Alternating drinks with water partially offsets ongoing fluid loss",
                "Pre-bed rehydration with electrolytes outperforms plain water",
                "Lower-ABV drinks directly reduce the dehydration load"
            ),
            whatGoesWrong = listOf(
                "Drinking on an empty stomach dramatically worsens dehydration and intoxication speed",
                "Skipping water through the evening creates a compound deficit by the end of the night",
                "Going to bed without any rehydration means 6–8 hours of no fluid intake alongside ongoing dehydration"
            ),
            whatThisMeansForYou = "The most effective single habit you can build around drinking is the pre-bed routine: 500ml of water, an electrolyte tablet or coconut water, and a light snack if your stomach allows it. It sounds simple because it is — and the next-day difference is consistent.",
            practicalTips = listOf(
                "Eat a proper meal — not snacks — before or during a drinking occasion",
                "One glass of water for every alcoholic drink throughout the evening",
                "Keep electrolyte tablets or coconut water at home for the pre-bed routine",
                "Choose wine spritzers, low-alcohol beer, or lighter options when possible"
            ),
            hydraComment = "It's not about not drinking — it's about being smart while you do. Water between drinks genuinely helps 🫧"
        ),
        EducationTopic(
            titleRes = R.string.edu_energy_title,
            icon = "🔋",
            colour = Color(0xFFFACC15),
            category = TopicCategory.ENERGY_DRINKS,
            content = "Energy drinks typically contain 80–200mg of caffeine per 500ml can, plus varying amounts of sugar, taurine, guarana, B vitamins, and other additives. The combination of high caffeine and significant sugar creates a pronounced spike-and-crash cycle: blood glucose rises rapidly from the sugar, energy and alertness increase from the caffeine, then both decline as insulin responds to the glucose and caffeine begins to clear. Taurine is an amino acid present in most energy drinks; at the quantities used, evidence for any independent benefit beyond placebo is weak. The dehydrating effect of energy drinks comes from both the caffeine (mild diuretic at higher doses) and the high sugar content drawing water into the digestive tract. This is why energy drinks appear as a significant dehydration penalty in HydraHeart — the gross liquid volume is misleading as a hydration measure.",
            previewText = "Energy drinks are engineered for a spike, not for sustained energy",
            summary = "Energy drinks are engineered for short-term stimulation, not sustained energy or hydration. Understanding exactly what's inside them helps you make informed choices — not marketing-driven ones.",
            keyFacts = listOf(
                "Caffeine: 80–200mg per 500ml — up to half the daily safe limit in one can",
                "Sugar: 27–54g per 500ml — potentially over the daily limit in one drink",
                "Net hydration significantly less than the liquid volume consumed",
                "Most guidelines recommend no more than 1 per day maximum"
            ),
            whatGoesWrong = listOf(
                "Energy spike followed by a hard crash 1–2 hours later",
                "High daily use builds caffeine tolerance and dependency",
                "Combining with alcohol masks intoxication levels dangerously",
                "High sugar versions contribute meaningfully to daily sugar excess",
                "Excess caffeine from energy drinks causes anxiety, poor sleep, and palpitations"
            ),
            whatThisMeansForYou = "If you're reaching for energy drinks regularly, the underlying issues are almost always sleep quality and nutrition. Energy drinks treat the symptom (tiredness) while worsening the causes (poor sleep from late caffeine, blood sugar instability). Addressing those directly removes the need.",
            practicalTips = listOf(
                "Never mix energy drinks with alcohol — it masks intoxication and is genuinely dangerous",
                "If you rely on them daily, sleep and nutrition are the real issues to address first",
                "Choose sugar-free versions if you're going to drink them — the caffeine effect is identical",
                "B vitamins and taurine are not 'magic' — the stimulant effect comes from the caffeine",
                "One per day maximum; avoid after 2pm to protect sleep"
            ),
            insightCallout = "The buzz in an energy drink is the caffeine. Everything else is marketing and flavouring.",
            mythVsFact = listOf(
                MythFact("Taurine gives energy drinks their effect", "Taurine is an amino acid with some physiological roles, but evidence for it causing stimulation at energy drink doses is very weak. The effect is from caffeine."),
                MythFact("Sugar-free energy drinks are fine to drink freely", "They remove the sugar issue but the high caffeine content still carries all the same sleep, anxiety, and cardiovascular concerns.")
            ),
            hydraComment = "The rush feels amazing until it doesn't. Stay honest with yourself about how many you're having 🔋"
        ),
        EducationTopic(
            titleRes = R.string.edu_energy_vs_coffee_title,
            icon = "⚡",
            colour = Color(0xFFFACC15),
            category = TopicCategory.ENERGY_DRINKS,
            content = "Although a strong cup of coffee and a standard energy drink can contain similar amounts of caffeine, energy drinks feel more intense for several compounding reasons. First, the caffeine is often paired with guarana — a natural caffeine source that is absorbed more slowly and extends the stimulant window. Second, the rapid sugar load in most energy drinks causes a simultaneous blood glucose spike, amplifying alertness in the short term but sharpening the eventual crash. Third, energy drinks are carbonated, which accelerates gastric emptying — meaning caffeine reaches the bloodstream faster than it does from coffee. Fourth, many people drink an entire 500ml can in one sitting, consuming the full caffeine dose in 10–15 minutes, whereas coffee is typically sipped over 20–30 minutes. The combination of delivery speed, sugar, and additional stimulants creates a more pronounced and less controllable stimulant effect than equivalent coffee.",
            previewText = "The same caffeine hits very differently when it arrives all at once",
            summary = "Energy drinks and coffee can contain the same caffeine on paper — but the experience is quite different. Understanding why helps you make more deliberate choices about when and how you use them.",
            keyFacts = listOf(
                "Carbonation accelerates caffeine absorption compared to still drinks",
                "Guarana adds a slower-release caffeine layer on top of the direct caffeine content",
                "Most people drink an entire energy drink faster than a cup of coffee",
                "The sugar spike amplifies alertness short-term and sharpens the crash"
            ),
            whatGoesWrong = listOf(
                "The combined stimulant load can cause heart palpitations in sensitive individuals",
                "Faster absorption means the caffeine ceiling is hit more sharply — anxiety spikes",
                "The more intense spike means the crash is correspondingly harder",
                "Regular use builds tolerance faster than coffee because the dose is higher and faster"
            ),
            whatThisMeansForYou = "If you want caffeine for sustained focus rather than a sharp jolt, coffee drunk slowly is almost always a better vehicle than an energy drink. The delivery mechanism of an energy drink is designed for stimulation, not for calm, steady concentration.",
            practicalTips = listOf(
                "Sip slowly if you're drinking them — the delivery speed is a big part of the intensity",
                "A double espresso often contains less total caffeine than a standard 500ml energy drink",
                "If you feel jittery or anxious, the total dose is too high — switch to coffee and reduce",
                "Avoid on an empty stomach; food slows absorption and blunts the spike significantly",
                "Once you know how the drink hits you, plan the timing around when you actually need it"
            ),
            insightCallout = "Carbonation and speed of consumption do more work in an energy drink than the label suggests.",
            hydraComment = "Fast, carbonated, full of sugar — it's basically designed to hit hard. That's the point ⚡"
        ),
        EducationTopic(
            titleRes = R.string.edu_energy_crash_title,
            icon = "📉",
            colour = Color(0xFFFACC15),
            category = TopicCategory.ENERGY_DRINKS,
            content = "The crash after an energy drink is the result of two mechanisms colliding simultaneously. The first is the caffeine-adenosine rebound: caffeine works by blocking adenosine receptors in the brain, delaying the accumulation of the sleepiness signal. When caffeine clears — typically 1–2 hours after a large dose — adenosine floods back, often more intensely than before the drink because it has been building up throughout the blocking period. The second mechanism is the blood glucose crash: the rapid sugar absorption from the drink causes a sharp insulin response, which drives blood glucose down below baseline — leaving you more fatigued than if you had never had the sugar at all. These two crashes are temporally aligned, so when the energy drink wears off, both effects hit at roughly the same time. The result is a fatigue that feels more pronounced than the tiredness you started with.",
            previewText = "The harder the spike, the harder the fall — there is no avoiding this",
            summary = "The crash after an energy drink isn't just tiredness returning — it's two different biological processes colliding. Understanding them makes it easier to plan around them and reduce their impact.",
            keyFacts = listOf(
                "Caffeine blocks adenosine receptors — when it clears, adenosine surges back",
                "The sugar spike triggers an insulin response that drives glucose below baseline",
                "Both crashes typically arrive simultaneously, 1–2 hours after consumption",
                "The more intense the initial spike, the more pronounced the crash tends to be"
            ),
            whatGoesWrong = listOf(
                "Reaching for another energy drink to manage the crash creates a dependency loop",
                "Repeated large spikes and crashes destabilise energy levels throughout the day",
                "Late-afternoon crashes make it hard to function without additional stimulants",
                "Over time the pattern disrupts circadian rhythm and natural energy cycles"
            ),
            whatThisMeansForYou = "The crash is not unavoidable — it can be significantly blunted by choosing sugar-free versions (removing the glucose crash), drinking slowly, and not exceeding one per day. But it cannot be fully eliminated with high-dose caffeine. If you are managing energy through back-to-back drinks, the underlying issue is sleep or nutrition — not a caffeine deficit.",
            practicalTips = listOf(
                "Sugar-free versions reduce the glucose crash significantly — still have the caffeine rebound though",
                "Eating something before or alongside an energy drink blunts both crash components",
                "Staying hydrated throughout helps cushion the fatigue when the drink wears off",
                "Avoid a second energy drink to manage the crash — it extends the cycle, not the energy",
                "Rest is the only thing that actually resolves adenosine buildup"
            ),
            insightCallout = "The crash is the debt coming due — every spike borrows from what comes after.",
            hydraComment = "The crash is literally your brain collecting what you owe it. You can't really negotiate 📉"
        ),
        EducationTopic(
            titleRes = R.string.edu_energy_alcohol_title,
            icon = "🍹",
            colour = Color(0xFFFACC15),
            category = TopicCategory.ENERGY_DRINKS,
            content = "Mixing energy drinks with alcohol is genuinely dangerous in a way that goes beyond just the two substances combined. Alcohol is a central nervous system depressant — it slows reactions, impairs coordination, and produces the subjective feeling of intoxication that signals the body is impaired. Caffeine is a CNS stimulant. When the two are combined, the stimulant effect of caffeine significantly masks the sedative signals of alcohol: you feel more alert, more capable of continuing to drink, and less intoxicated than you actually are. This creates a dangerous disconnect between perceived intoxication and actual impairment — blood alcohol concentration rises as if you were drinking without the mixer, but your subjective experience is much closer to sobriety. Multiple studies have found that people who mix energy drinks and alcohol consume more alcohol per session, are more likely to make risky decisions, and are more likely to experience alcohol-related harm than those who drink alcohol alone. The combination does not reduce blood alcohol — it just disguises it.",
            previewText = "It makes you feel sober while your blood alcohol climbs just as high",
            summary = "Mixing energy drinks with alcohol is one of the more genuinely risky habits around drinking culture — not because the combination is chemically dangerous in itself, but because it systematically undermines your ability to judge how drunk you actually are.",
            keyFacts = listOf(
                "Caffeine masks the sedative signals of alcohol — you feel more sober than you are",
                "Blood alcohol rises just as high as if no mixer were used",
                "People who mix the two tend to drink significantly more per session",
                "The combination increases risk of alcohol-related harm and poor decision-making"
            ),
            whatGoesWrong = listOf(
                "Severely underestimating alcohol intoxication level — feeling in control when you aren't",
                "Drinking more than intended because the energy drink masks the usual stopping signals",
                "Severe dehydration: both alcohol and caffeine increase fluid loss simultaneously",
                "Magnified anxiety and heart palpitations from high stimulant plus depressant loading",
                "Waking up more ill than expected because the caffeine concealed the true alcohol impact"
            ),
            whatThisMeansForYou = "The practical rule here is simple: if you are going to drink alcohol, do not use energy drinks as the mixer. This is not about occasional moderation — it is a category of risk worth avoiding entirely, because the mechanism of harm is not dose-dependent. Even one energy drink mixed with alcohol distorts your self-assessment reliably.",
            practicalTips = listOf(
                "Do not use energy drinks as an alcohol mixer — the risk is not dose-dependent",
                "If you're out and feeling unusually alert while drinking, check how much you've consumed",
                "Water between drinks remains the most effective way to pace alcohol intake",
                "The dehydration from both combined means a worse next day — drink extra water before bed",
                "Be aware that other people around you may be more impaired than they appear"
            ),
            insightCallout = "You cannot caffeinate your way out of being drunk. The caffeine just hides it from you.",
            hydraComment = "I always notice when your drinks list has both. The combination is the one I worry about most 🍹"
        ),
        EducationTopic(
            titleRes = R.string.edu_energy_sugarfree_title,
            icon = "🏷️",
            colour = Color(0xFFFACC15),
            category = TopicCategory.ENERGY_DRINKS,
            content = "Sugar-free energy drinks replace sucrose and glucose with artificial sweeteners — typically sucralose, acesulfame-K, or erythritol. This removes the blood glucose spike and the corresponding insulin crash, which is a genuine improvement for energy stability and caloric intake. However, every other aspect of the energy drink remains entirely unchanged. The caffeine content is identical — typically 80–160mg per 500ml. The dehydrating effect from high caffeine persists. The sleep disruption risk from late consumption is exactly the same. The anxiety, palpitation, and tolerance-building effects of the caffeine are unchanged. The caffeine crash — driven by adenosine rebound rather than blood sugar — still happens. The net hydration deficit tracked in HydraHeart is still present, though slightly smaller than the full-sugar version. Sugar-free is the better of two options if you are going to drink energy drinks regularly, but it is worth being clear about what it actually solves and what it does not.",
            previewText = "Sugar-free fixes the sugar problem, but every caffeine risk stays exactly the same",
            summary = "Sugar-free energy drinks solve the blood glucose issue and reduce calories, but the caffeine-related effects — crashes, sleep disruption, anxiety, and dependency — are completely unchanged. Knowing which risks remain helps you decide whether the swap actually matters for your goals.",
            keyFacts = listOf(
                "Sugar-free removes the glucose spike and insulin crash — a genuine improvement",
                "Caffeine content in sugar-free versions is identical to the full-sugar version",
                "Sleep disruption, caffeine crash, and anxiety risk are all entirely unchanged",
                "Net hydration penalty is marginally smaller but still significant"
            ),
            whatGoesWrong = listOf(
                "Treating sugar-free as 'safe' and consuming more freely than warranted",
                "Still experiencing the full caffeine crash because the rebound mechanism is unchanged",
                "Sleep disruption if consumed in the afternoon — sugar-free does not help with this",
                "Ongoing caffeine tolerance and dependency builds at the same rate"
            ),
            whatThisMeansForYou = "If you're choosing between full-sugar and sugar-free, choose sugar-free — it is the meaningfully better option. But if the question is whether sugar-free makes energy drinks fine to drink freely, the answer is no. The caffeine load is still the primary concern, and that is completely unaffected by the sweetener swap.",
            practicalTips = listOf(
                "Sugar-free is the better choice if you're going to drink them — it genuinely is",
                "Still treat the caffeine content exactly as you would a strong coffee",
                "The 2pm cut-off applies equally to sugar-free versions — caffeine is still caffeine",
                "Do not exceed one per day regardless of the sugar content",
                "If you rely on them for energy, the root issue is still sleep and nutrition"
            ),
            insightCallout = "Sugar-free addresses 25% of the concerns. The other 75% are all about the caffeine.",
            hydraComment = "Sugar-free is a better choice, but it's not a free pass. The caffeine is identical 🏷️"
        ),
        EducationTopic(
            titleRes = R.string.edu_skin_title,
            icon = "✨",
            colour = Color(0xFFEC4899),
            category = TopicCategory.BEAUTY,
            content = "Your skin is your body's largest organ and contains approximately 64% water. Its primary barrier function — protecting against pathogens, UV, and transepidermal water loss — depends on adequate cellular hydration. When you're consistently well-hydrated, the stratum corneum (outer skin layer) retains flexibility and integrity; when dehydrated, it becomes compromised, leading to dryness, tightening, and increased sensitivity. Research has shown that in people who typically drink below adequate levels, increasing water intake produces measurable improvements in skin hydration, elasticity, and density within four weeks. The relationship between hydration and the visible signs of ageing (fine lines, dullness) is genuine — dehydrated skin amplifies the appearance of fine lines because it lacks the plumpness that keeps them less visible. Skincare products work at the surface; hydration works at the cellular level — and the difference is visible.",
            previewText = "No serum works as well as drinking enough water consistently",
            summary = "Your skin reflects internal hydration more visibly than almost any other system. Skincare products work at the surface — but hydration works from within, and the difference shows in a way no serum can replicate.",
            keyFacts = listOf(
                "Your skin is approximately 64% water",
                "Dehydration accelerates the visible appearance of fine lines and dullness",
                "Improved elasticity is measurable within 4 weeks of better hydration habits",
                "The skin barrier — your protection against ageing and infection — depends on hydration"
            ),
            whatGoesWrong = listOf(
                "Skin becomes dry, tight, and more prone to irritation and sensitivity",
                "Fine lines appear more pronounced because dehydrated skin lacks plumpness",
                "Complexion looks dull, uneven, and less luminous",
                "Dark circles and puffiness are worsened by fluid retention from poor overall balance",
                "Chronic dehydration accelerates visible skin ageing over years"
            ),
            whatThisMeansForYou = "The most affordable and underrated skincare step costs nothing — it's just consistent daily water intake. If you're spending significant amounts on topical skincare while not hitting your water target consistently, the internal foundation isn't there to support what goes on the surface.",
            practicalTips = listOf(
                "Consistency over volume — steady daily intake beats occasional large amounts",
                "Water-rich foods (cucumber, watermelon, celery, strawberries) contribute meaningfully",
                "Skin benefits typically become visible after 3–4 weeks of consistent good habits",
                "Alcohol and high sugar intake both visibly affect skin — reducing both shows relatively quickly",
                "Green tea provides hydration plus antioxidants that support skin health from within"
            ),
            insightCallout = "The most expensive serum in the world can't replace what consistent hydration does from the inside.",
            hydraComment = "The most expensive serum in the world can't fix what dehydration does from the inside. Start here ✨"
        ),
        EducationTopic(
            titleRes = R.string.edu_lips_dry_title,
            icon = "💋",
            colour = Color(0xFFF43F5E),
            category = TopicCategory.BEAUTY,
            content = "The lips are uniquely vulnerable to dehydration because they are among the thinnest skin on the body, contain no oil glands to provide self-moisturisation, and are constantly exposed to evaporation through breathing and speaking. The lip mucosa is also highly permeable, meaning moisture loss is rapid compared to facial skin. Lip dryness and chapping are among the earliest and most visible signs of systemic dehydration, often appearing before other symptoms are noticeable. Environmental factors compound the problem: cold or dry air, sun exposure, central heating, and wind all accelerate evaporative moisture loss from the lips. Habitual lip licking is counterproductive — saliva evaporates quickly and takes additional moisture from the lips with it, worsening dryness over time. Consistent internal hydration is the most effective long-term solution.",
            previewText = "Reach for water before you reach for lip balm",
            summary = "Chapped and dry lips are easy to blame on the weather — but more often, they're a reliable early signal from your body that you haven't been drinking enough. The fix usually starts from the inside.",
            keyFacts = listOf(
                "Lips have no oil glands — they cannot self-moisturise like skin",
                "Lip skin is thinner and more permeable, losing moisture faster",
                "Dry lips are often the earliest visible sign of dehydration",
                "Lip licking worsens dryness — saliva evaporates and takes moisture with it"
            ),
            whatGoesWrong = listOf(
                "Chapped, cracked, or peeling lips that don't respond to balm alone",
                "Increased sensitivity and discomfort when smiling or eating",
                "Lip licking habit that progressively worsens the dryness",
                "Dry, irritated skin at the corners of the mouth alongside lip dryness"
            ),
            whatThisMeansForYou = "Before reaching for another lip balm, check your water intake for the day. Balm helps at the surface, but it can't fix the underlying cause if you're consistently under-hydrated. The combination of better hydration and a quality barrier balm is far more effective than either alone.",
            practicalTips = listOf(
                "Increase water intake if lips are persistently dry — check your HydraHeart total",
                "Use a lip balm with SPF daily and reapply before going outdoors",
                "Breathe through your nose when possible — mouth breathing dries lips significantly faster",
                "Avoid licking your lips — it worsens dryness over time",
                "Humidify your bedroom during winter when central heating drops air moisture"
            ),
            hydraComment = "Chapped lips are your body asking for more water before it says anything louder 💋"
        ),
        EducationTopic(
            titleRes = R.string.edu_dark_circles_title,
            icon = "👁️",
            colour = Color(0xFFFDA4AF),
            category = TopicCategory.BEAUTY,
            content = "Dark circles under the eyes are caused by several factors, and dehydration is one of the most direct contributors. When the body is under-hydrated, the delicate tissue under the eyes — where skin is thinnest — becomes more translucent, making the blood vessels and dark tissue beneath more visible. The skin also loses plumpness, causing slight hollowing that creates a shadow effect. Simultaneously, dehydration promotes water retention as a compensatory mechanism, which can cause puffiness and swelling — making the shadow-hollow contrast more pronounced. Salt, alcohol, and poor sleep all compound the effect: salt causes fluid retention around the eyes; alcohol disrupts sleep and worsens puffiness; poor sleep increases blood vessel dilation beneath thin skin. Addressing hydration is one of the few approaches that genuinely targets multiple causes simultaneously.",
            previewText = "Dehydration makes dark circles visibly worse — the fix is simpler than you think",
            summary = "Dark circles have multiple causes — genetics, sleep, and ageing all play a role — but dehydration is a consistent aggravator that's easy to overlook and worth addressing first before anything else.",
            keyFacts = listOf(
                "Under-eye skin is the thinnest on the body — most sensitive to dehydration",
                "Dehydration causes skin to thin and lose plumpness, deepening the shadow",
                "Water retention from dehydration adds puffiness, making circles more visible",
                "Alcohol, salt, and poor sleep all compound the dehydration effect"
            ),
            whatGoesWrong = listOf(
                "Skin becomes more translucent, making vessels and tissue beneath more visible",
                "Puffiness from fluid retention increases the shadow-hollow contrast",
                "Poor sleep worsens blood vessel dilation under thin skin, darkening the appearance",
                "Salt-heavy diets cause disproportionate water retention around the eyes"
            ),
            whatThisMeansForYou = "If dark circles are a concern, the most practical starting point is: consistent daily hydration, reducing late alcohol and salt, and protecting sleep. These don't erase genetic predisposition, but they meaningfully reduce the factors that you can control.",
            practicalTips = listOf(
                "Drink water consistently throughout the day — not just in the evening",
                "Reduce alcohol and high-sodium food, which both worsen under-eye puffiness",
                "Sleep on a slightly elevated pillow to reduce overnight fluid pooling under eyes",
                "Cold compresses or chilled spoons in the morning help temporarily with puffiness",
                "Green tea bags cooled in the fridge — the caffeine and antioxidants help constrict vessels"
            ),
            hydraComment = "Dark circles are often dehydration and poor sleep wearing each other's jackets. Fix both 👁️"
        ),
        EducationTopic(
            titleRes = R.string.edu_hair_dry_title,
            icon = "💆",
            colour = Color(0xFFFF8FAB),
            category = TopicCategory.BEAUTY,
            content = "Hair strands are approximately 25% water, and adequate hydration is essential for maintaining hair elasticity, shine, and strength. The hair shaft is a dead structure — it doesn't receive nutrients from blood vessels — but the follicle that produces it is very much alive and dependent on proper blood flow and nutrient delivery, both of which are impaired by dehydration. When the body is consistently under-hydrated, it prioritises fluid delivery to vital organs, leaving hair follicles with reduced supply. This can slow hair growth rate, make strands more brittle and prone to breakage, and reduce scalp moisture, contributing to flakiness and itchiness. Hair products work externally; scalp and strand health from the inside depends on consistent internal hydration, micronutrient supply, and blood flow.",
            previewText = "Healthy hair starts from inside, not from what you put on it",
            summary = "Most hair health conversations focus on external products — but the foundation of healthy hair is internal. Dehydration directly affects the follicle's nutrient supply and the strand's moisture content in ways no conditioner fully corrects.",
            keyFacts = listOf(
                "Hair strands contain approximately 25% water",
                "Dehydration reduces blood flow to follicles, slowing growth",
                "Brittle, dull hair with increased breakage is a sign of poor internal hydration",
                "Hair follicles are sensitive to micronutrient deficiencies alongside fluid"
            ),
            whatGoesWrong = listOf(
                "Increased hair breakage and split ends despite good external care",
                "Reduced hair growth rate over time",
                "Dry, itchy scalp that doesn't respond fully to topical treatments",
                "Loss of shine and elasticity that makes hair feel and look dull"
            ),
            whatThisMeansForYou = "If you're struggling with dry or brittle hair and have good external hair care habits, internal hydration is worth revisiting. It won't undo damage already done, but consistently good hydration supports the follicle environment that new growth depends on.",
            practicalTips = listOf(
                "Aim for consistent daily hydration — erratic patterns affect scalp health more than you might expect",
                "Biotin, iron, and zinc support hair follicle health alongside good hydration",
                "Reduce heat styling when hair is already dehydrated or brittle",
                "A healthy, varied diet matters as much as water for hair health"
            ),
            hydraComment = "Your hair tells stories about your internal health. Good hydration is one of the chapters it notices 💆"
        ),
        EducationTopic(
            titleRes = R.string.edu_calcium_title,
            icon = "🦴",
            colour = Color(0xFFFEF3C7),
            category = TopicCategory.WELLBEING,
            content = "Calcium is the most abundant mineral in the human body and far more than a bone-building nutrient. It's essential for muscle contraction (including the heart), nerve signal transmission, blood clotting, and hormone secretion. The vast majority of body calcium — about 99% — is stored in bones and teeth, where it provides structural strength. The remaining 1% in the blood is tightly regulated because even small fluctuations cause serious disruptions to heart and muscle function. Adults require approximately 1000mg per day, rising to 1200mg for women over 50 and men over 70 as bone density maintenance becomes more critical. Vitamin D is essential for calcium absorption from the gut — without adequate vitamin D, calcium intake becomes far less effective regardless of how much you consume.",
            previewText = "Calcium matters long after you stop growing — most people don't get enough",
            summary = "Calcium does far more than build bones. It's essential for muscle contraction, nerve signalling, and blood clotting. Most people associate it with childhood, but consistent calcium intake matters throughout your entire life.",
            keyFacts = listOf(
                "Daily target: 1000mg for most adults (1200mg for women over 50)",
                "One glass of whole milk provides approximately 300mg",
                "Fortified plant milks typically offer 300–450mg per serving",
                "Vitamin D is essential for calcium absorption — the two work together"
            ),
            keyFactsMale = listOf(
                "Daily target: 1000mg for most adults (1200mg for men over 70)",
                "One glass of whole milk provides approximately 300mg",
                "Fortified plant milks typically offer 300–450mg per serving",
                "Vitamin D is essential for calcium absorption — the two work together"
            ),
            whatGoesWrong = listOf(
                "Long-term low calcium intake leads to osteoporosis — bones become fragile and fracture-prone",
                "Muscle cramps and spasms — calcium is essential for muscle contraction",
                "Poor dental health and increased risk of tooth decay",
                "In severe deficiency: irregular heart rhythm and nerve dysfunction"
            ),
            whatThisMeansForYou = "Bone density loss is gradual and silent until it becomes a fracture. The habits you build now — particularly in your 20s and 30s — have compounding effects on bone health decades later. Getting adequate calcium daily is a long-game investment with no visible short-term signal to confirm it's working.",
            whatThisMeansForYouMale = "Bone health is often framed as a women's concern, but men lose bone density with age too — particularly after 70. Building strong bones in your younger years is genuinely protective. Calcium combined with weight-bearing exercise and vitamin D is the most effective combination.",
            practicalTips = listOf(
                "Include dairy or fortified plant alternatives with at least two meals per day",
                "Vitamin D supports calcium absorption — get some sunlight or supplement in winter",
                "Dark leafy greens like kale and bok choy are excellent plant-based sources",
                "Calcium supplements are less effective than food sources — prioritise dietary intake",
                "Weight-bearing exercise (walking, running, strength training) further protects bone density"
            ),
            insightCallout = "Your bones are silently depending on what you eat today. The consequences of neglect show up decades later.",
            hydraComment = "Calcium isn't just a childhood thing — your bones are quietly depending on you every single day 🦴"
        ),
        EducationTopic(
            titleRes = R.string.edu_brain_fog_title,
            icon = "🧠",
            colour = Color(0xFF818CF8),
            category = TopicCategory.WELLBEING,
            content = "Brain fog — the subjective experience of mental haziness, difficulty concentrating, slow thinking, and reduced cognitive clarity — has several causes, and dehydration is one of the most common and most overlooked. The brain is approximately 75% water, and even mild dehydration measurably impairs working memory, attention, and psychomotor speed. Research shows that 1–2% dehydration reduces cognitive performance in tasks requiring sustained concentration, and the effect is dose-dependent — more dehydration, more impairment. Beyond dehydration, brain fog is associated with poor sleep, blood sugar instability, excess caffeine or alcohol, and chronic inflammation from poor diet. The overlap of causes makes it important to address the basics — hydration, sleep, nutrition, and movement — before assuming a more complex cause.",
            previewText = "Brain fog is usually a symptom, not a condition — and water is often the fix",
            summary = "Brain fog is real, frustrating, and frequently caused by a combination of factors most people haven't examined. Dehydration is consistently one of them — and one of the easiest to address.",
            keyFacts = listOf(
                "The brain is approximately 75% water",
                "1–2% dehydration impairs working memory and sustained attention",
                "Blood sugar crashes, poor sleep, and excess caffeine all compound fog",
                "Cognitive clarity often improves within 20–30 minutes of rehydrating"
            ),
            whatGoesWrong = listOf(
                "Difficulty with tasks requiring sustained focus or multi-step thinking",
                "Slower processing speed and reactions — things take more effort than they should",
                "Increased forgetfulness and difficulty retaining information in the short term",
                "Low motivation and that flat, detached feeling of not quite being present"
            ),
            whatThisMeansForYou = "Before assuming brain fog has a complex cause, check the basics: How much water have you had today? How well did you sleep? Have you eaten balanced meals? These aren't dismissive questions — they genuinely account for the majority of mild cognitive impairment most people experience.",
            practicalTips = listOf(
                "Drink 400ml of water when mental clarity drops — wait 20 minutes before escalating to caffeine",
                "Eat balanced meals with protein and slow carbs to stabilise blood sugar and thinking",
                "Prioritise sleep quality — poor deep sleep is one of the biggest fog drivers",
                "Regular gentle movement (a 10-minute walk) improves blood flow and clears fog quickly"
            ),
            insightCallout = "Dehydration can make you feel genuinely foggy and slow — and the fix often takes less than 30 minutes.",
            hydraComment = "Before you question everything, drink some water and give it 20 minutes. Genuinely 🧠"
        ),
        EducationTopic(
            titleRes = R.string.edu_mood_hydration_title,
            icon = "🌸",
            colour = Color(0xFFA78BFA),
            category = TopicCategory.WELLBEING,
            content = "The relationship between hydration and mood is well-documented but underappreciated. Multiple studies — including research from the University of Connecticut's Human Performance Laboratory — have shown that mild dehydration (as little as 1.5% fluid loss) causes statistically significant increases in fatigue, anxiety, tension, and negative mood, alongside reductions in energy and cognitive performance. The mechanism involves several pathways: reduced blood flow to the brain, changes in neurotransmitter synthesis (serotonin production requires adequate hydration), hormonal stress responses triggered by dehydration, and the physical discomfort of symptoms like headache and fatigue which themselves influence mood. Interestingly, some of these mood effects are more pronounced in women than men in controlled studies — likely related to hormonal interactions with fluid regulation.",
            previewText = "Your mood is more sensitive to dehydration than your body is",
            summary = "Your mood and hydration are more connected than most people realise. The science is clear: even mild under-hydration changes how you feel emotionally — not just physically — and it's one of the most underappreciated drivers of a flat, irritable, or anxious baseline.",
            keyFacts = listOf(
                "1.5% dehydration significantly increases anxiety, fatigue, and negative mood",
                "Serotonin synthesis is partially dependent on adequate hydration",
                "Mood effects from dehydration appear before physical thirst in many people",
                "Women appear more susceptible to mood changes from mild dehydration"
            ),
            whatGoesWrong = listOf(
                "Increased irritability and emotional reactivity without an obvious trigger",
                "A flat, low-energy mood that doesn't respond to normal mood-boosters",
                "Heightened anxiety and tension, particularly in the afternoon",
                "Reduced emotional resilience — small things feel disproportionately difficult"
            ),
            whatThisMeansForYou = "If you notice a pattern of feeling more irritable, anxious, or emotionally flat in the afternoon — especially on days when you've been busy and haven't drunk much — hydration is worth tracking as a variable. Many people are surprised how much a consistent intake improves their emotional baseline.",
            practicalTips = listOf(
                "Track water intake on days you feel emotionally flat or irritable — look for patterns",
                "Front-load water intake in the morning to reduce afternoon mood dips",
                "Avoid caffeine as a mood fix in the afternoon — address the hydration first",
                "Magnesium also supports mood regulation — worth eating magnesium-rich foods daily"
            ),
            insightCallout = "Hydration affects how you feel emotionally, not just physically. Your mood notices before your thirst does.",
            hydraComment = "On the days you feel a bit off for no reason — when did you last have water? It matters more than you'd think 🌸"
        ),
        EducationTopic(
            titleRes = R.string.edu_sleep_hydration_title,
            icon = "🌙",
            colour = Color(0xFF6366F1),
            category = TopicCategory.WELLBEING,
            content = "Hydration and sleep have a bidirectional relationship. Dehydration impairs sleep quality by increasing core body temperature (hydration helps with temperature regulation), increasing the likelihood of nocturnal leg cramps (often electrolyte-related but compounded by dehydration), and causing discomfort that disrupts sleep continuity. Conversely, poor sleep impairs the body's fluid-regulating hormone production — including ADH and cortisol — affecting the next day's hydration status and increasing cravings for caffeinated and high-sugar drinks. The body also manages much of its fluid regulation and renal function during the night. Drinking large amounts close to bedtime disrupts sleep through nocturia (waking to urinate), so the goal is to front-load intake during the day, taper down in the evening, and rehydrate promptly in the morning.",
            previewText = "Hydration and sleep are quietly affecting each other every single night",
            summary = "Hydration and sleep influence each other more than most people realise. Poor habits with one tend to undermine the other — creating a cycle that affects energy, mood, and health in overlapping ways.",
            keyFacts = listOf(
                "Dehydration raises core temperature and causes cramps that disrupt sleep",
                "Poor sleep impairs ADH production, affecting next-day hydration",
                "Leg cramps during sleep are often electrolyte and hydration-related",
                "Front-load drinking through the day; taper off 2–3 hours before bed"
            ),
            whatGoesWrong = listOf(
                "Night-time leg cramps waking you during sleep",
                "Sleeping lightly with increased restlessness from temperature dysregulation",
                "Waking feeling dry-mouthed and dehydrated, compounding the next day",
                "Post-poor-sleep cravings for caffeine and sugar worsen both issues"
            ),
            whatThisMeansForYou = "The timing of hydration matters as much as the total. Drinking most of your water in the morning and afternoon, slowing down after dinner, and rehydrating promptly in the morning creates the best environment for both hydration and sleep quality.",
            practicalTips = listOf(
                "Aim for 80% of your daily target before 6pm",
                "Slow down drinks 2–3 hours before bed to reduce night waking",
                "Place a glass of water by your bed to drink immediately on waking",
                "Magnesium before bed helps with leg cramps and supports sleep quality"
            ),
            hydraComment = "Getting the timing right matters as much as the total. Front-load your day, protect your nights 🌙"
        ),
        EducationTopic(
            titleRes = R.string.edu_hydration_habit_title,
            icon = "✅",
            colour = Color(0xFFFB923C),
            category = TopicCategory.HABITS,
            content = "Building a consistent hydration habit follows the same principles as any other behaviour change: environment design, habit stacking, and identity-based framing matter more than willpower alone. Environment design — having water visible, accessible, and ready to drink — is the single most effective intervention in hydration research, reducing the friction of drinking to essentially zero. Habit stacking involves linking water drinking to existing habits (after waking, before coffee, with each meal, before bed) so that the new behaviour rides on established ones. Identity framing — thinking of yourself as someone who takes care of their body, rather than someone 'trying to drink more water' — creates sustainable motivation. Apps, tracking, and reminder systems provide useful scaffolding while habits are forming, though the goal is eventually for the behaviour to feel automatic.",
            previewText = "People who drink consistently haven't got more willpower — they've designed better habits",
            summary = "The people who drink consistently aren't necessarily more disciplined — they've made drinking water the easier choice. Understanding how habits form makes it possible to build a hydration routine that actually sticks.",
            keyFacts = listOf(
                "Visible, accessible water is the single most effective hydration intervention",
                "Habit stacking — linking water to existing habits — dramatically increases consistency",
                "Tracking in apps like HydraHeart provides external accountability while habits form",
                "It takes 21–66 days for a behaviour to become automatic — depending on complexity"
            ),
            whatGoesWrong = listOf(
                "Relying on thirst means drinking reactively rather than consistently",
                "No visual cue (hidden water bottles) makes forgetting extremely easy",
                "Starting with too large a goal leads to early failure and abandonment",
                "Expecting immediate results — hydration benefits are real but often gradual"
            ),
            whatThisMeansForYou = "You don't need perfect motivation to build a hydration habit. You need a glass of water on your desk, a reminder at two fixed points in the day, and a gentle tracking system that doesn't feel like a chore. HydraHeart is designed to be exactly that scaffolding — the habit grows around it.",
            practicalTips = listOf(
                "Keep a 500ml bottle on your desk or work surface at all times — visible and filled",
                "Link water to three fixed daily moments: waking, lunch, and 4pm",
                "Use HydraHeart's reminders system to prompt drinking at regular intervals",
                "Start small: committing to 1 litre consistently beats failing at 2 litres",
                "Track your streak — consistency matters more than volume in the early stages"
            ),
            insightCallout = "You don't need willpower. You need a water bottle in front of you. Environment wins every time.",
            hydraComment = "The secret to drinking more water is making not drinking it slightly harder than drinking it ✅"
        ),
        EducationTopic(
            titleRes = R.string.edu_forget_water_title,
            icon = "🔔",
            colour = Color(0xFFF97316),
            category = TopicCategory.HABITS,
            content = "Forgetting to drink water is one of the most common self-reported barriers to adequate hydration — and it's a behaviour with a clear psychological basis. Water drinking has no strong sensory reward (unlike food or caffeine), no immediate consequence for skipping it (thirst is delayed), and is easily displaced by focused work or other activities. Cognitive absorption during tasks that require sustained attention reliably suppresses awareness of internal states including thirst, hunger, and fatigue. This is why people in flow states — deeply focused on work, study, creative tasks, or gaming — are among the most likely to underdrink. The solution isn't trying to 'remember' to drink, which is cognitively demanding — it's using external cues (reminders, visible bottles, linked habits) to remove the memory requirement entirely.",
            previewText = "Forgetting to drink isn't a lack of discipline — your brain is wired this way",
            summary = "Forgetting to drink isn't about lack of care or discipline — it's a predictable result of how the brain prioritises tasks. Understanding why it happens makes the solution obvious.",
            keyFacts = listOf(
                "Deep focus reliably suppresses awareness of internal states including thirst",
                "Thirst arrives after dehydration has already begun — making it a poor reminder",
                "External cues (reminders, visible bottles) outperform internal awareness",
                "High-demand days — long meetings, creative work, deadlines — are the highest-risk"
            ),
            whatGoesWrong = listOf(
                "Consistent under-drinking on busy or focused days — the days when you most need to hydrate",
                "Trying to catch up in the evening with large amounts — less effective than steady intake",
                "Relying on thirst as the signal creates a perpetual lag behind actual needs"
            ),
            whatThisMeansForYou = "On your most focused, productive days, you're at the highest risk of forgetting to drink. That's not a coincidence — it's your brain prioritising the immediate task. The fix is removing the memory requirement: set HydraHeart reminders, keep water visible, and link drinking to existing moments in the day.",
            practicalTips = listOf(
                "Set HydraHeart reminders at your two lowest-drinking periods of the day",
                "Keep water in your direct line of sight — if you can see it, you'll drink it",
                "Use a large bottle so you can see progress visually throughout the day",
                "Pair water with every caffeine drink as a rule — coffee arrives, water follows"
            ),
            hydraComment = "You don't forget to eat or breathe. Water just needs to become that kind of automatic. I'll help 🔔"
        ),
        EducationTopic(
            titleRes = R.string.edu_low_intake_recovery_title,
            icon = "🔄",
            colour = Color(0xFFEF4444),
            category = TopicCategory.HABITS,
            content = "Recovering from a low-intake day is not about chugging water to reach a target in the evening — it's about gentle, sustained rehydration that the body can actually process effectively. The kidneys can only absorb roughly 800–1000ml of water per hour. Drinking beyond this rate simply increases urine output without improving hydration. The most effective recovery strategy is a moderate, consistent intake over several hours alongside some electrolyte replenishment, which helps the body retain and distribute the fluid rather than simply excreting it. Adequate electrolytes — particularly sodium and potassium — are what allow absorbed water to actually enter cells and tissues rather than passing through. The next day's priority is to return to consistent pacing rather than playing catch-up again.",
            previewText = "Trying to catch up on water in the evening doesn't work the way you think",
            summary = "A bad hydration day happens to everyone. The question is how to recover smartly — because trying to catch up all at once doesn't work the way it feels like it should.",
            keyFacts = listOf(
                "Kidneys process ~800–1000ml per hour maximum — excess is simply excreted",
                "Electrolytes (sodium, potassium) help water actually enter cells rather than pass through",
                "Gradual rehydration over several hours is more effective than one large volume",
                "The best recovery is returning to consistent pacing the following day"
            ),
            whatGoesWrong = listOf(
                "Drinking large quantities quickly overwhelms kidney capacity — most is wasted",
                "Without electrolytes, extra plain water may pass through rather than rehydrate tissues",
                "An all-or-nothing mindset leads to cycles of poor days and over-correction"
            ),
            whatThisMeansForYou = "On a day when you've drunk very little, the evening plan is: 500ml now with a small snack or electrolyte source, 500ml over the next couple of hours, and then your usual bedtime glass. Tomorrow, start early and don't let it fall behind again — that's the only real recovery strategy.",
            practicalTips = listOf(
                "On a low-intake day, start gentle evening recovery with 500ml over 30–60 minutes",
                "Add an electrolyte tablet or pinch of salt to one glass to improve absorption",
                "Don't try to hit your full daily target in 2 hours — it doesn't work physiologically",
                "The morning after: drink 500ml before your first coffee and front-load the day"
            ),
            hydraComment = "Everyone has a bad day. The trick is tomorrow being better, not trying to fix everything tonight 🔄"
        )
    )

    fun groupedByCategory(): Map<TopicCategory, List<EducationTopic>> = topics.groupBy { it.category }

    fun topicsForCategory(category: TopicCategory): List<EducationTopic> = topics.filter { it.category == category }

    fun contentLabel(titleRes: Int): String? = when (titleRes) {
        R.string.edu_water_title -> "Deep dive"
        R.string.edu_water_needs_title -> "Personalised"
        R.string.edu_mild_dehy_title -> "Most missed"
        R.string.edu_severe_dehy_title -> "Important"
        R.string.edu_too_much_water_title -> "Myth buster"
        R.string.edu_morning_hydration_title -> "Quick win"
        R.string.edu_dehy_headaches_title -> "Most missed"
        R.string.edu_dehy_tired_title -> "Most missed"
        R.string.edu_electrolytes_title -> "Deep dive"
        R.string.edu_when_electrolytes_title -> "Quick read"
        R.string.edu_low_electrolytes_title -> "Signs to know"
        R.string.edu_natural_electrolytes_title -> "Quick read"
        R.string.edu_caffeine_title -> "Deep dive"
        R.string.edu_caffeine_timing_title -> "Quick win"
        R.string.edu_caffeine_anxiety_title -> "Worth knowing"
        R.string.edu_caffeine_sleep_title -> "Worth knowing"
        R.string.edu_caffeine_crash_title -> "Quick read"
        R.string.edu_sugar_title -> "Eye opener"
        R.string.edu_hidden_sugar_title -> "Eye opener"
        R.string.edu_sugar_crash_title -> "Quick read"
        R.string.edu_sweeteners_title -> "Myth buster"
        R.string.edu_alcohol_title -> "Deep dive"
        R.string.edu_alcohol_sleep_title -> "Most missed"
        R.string.edu_hangovers_title -> "Quick read"
        R.string.edu_drinking_hydration_title -> "Quick win"
        R.string.edu_energy_title -> "Eye opener"
        R.string.edu_skin_title -> "Glow tip"
        R.string.edu_lips_dry_title -> "Glow tip"
        R.string.edu_dark_circles_title -> "Glow tip"
        R.string.edu_hair_dry_title -> "Glow tip"
        R.string.edu_calcium_title -> "Quick read"
        R.string.edu_brain_fog_title -> "Most missed"
        R.string.edu_mood_hydration_title -> "Worth knowing"
        R.string.edu_sleep_hydration_title -> "Worth knowing"
        R.string.edu_hydration_habit_title -> "Quick win"
        R.string.edu_forget_water_title -> "Quick read"
        R.string.edu_low_intake_recovery_title -> "Quick win"
        else -> null
    }
}
