package com.hydraheart2.app.data

import androidx.compose.ui.graphics.Color
import com.hydraheart2.app.R
import com.hydraheart2.app.data.models.Drink
import com.hydraheart2.app.data.models.DrinkCategory

object DrinkDatabase {
    private val WATER_BLUE = Color(0xFF60A5FA)
    private val WATER_LIGHT = Color(0xFF93C5FD)
    private val COFFEE_BROWN = Color(0xFFA0724A)
    private val TEA_GREEN = Color(0xFF86B87A)
    private val TEA_AMBER = Color(0xFFD4A76A)
    private val ENERGY_GREEN = Color(0xFF4ADE80)
    private val ENERGY_YELLOW = Color(0xFFFACC15)
    private val SODA_BROWN = Color(0xFF8B5E3C)
    private val SODA_ORANGE = Color(0xFFFB923C)
    private val SODA_CLEAR = Color(0xFFD4E8D0)
    private val JUICE_ORANGE = Color(0xFFFF9F43)
    private val JUICE_APPLE = Color(0xFFC8D86A)
    private val JUICE_PINK = Color(0xFFE8607A)
    private val MILK_WHITE = Color(0xFFF5F0E8)
    private val MILK_CHOC = Color(0xFF8B6F47)
    private val MILK_ALMOND = Color(0xFFE8DCC8)
    private val ALCOHOL_GOLD = Color(0xFFD4A030)
    private val ALCOHOL_RED = Color(0xFF9B2335)
    private val ALCOHOL_WHITE_WINE = Color(0xFFE8E0B0)
    private val ALCOHOL_DARK = Color(0xFF2C1810)
    private val ALCOHOL_CLEAR = Color(0xFFD0D8E0)

    val categories: List<DrinkCategory> = listOf(
        DrinkCategory(
            "water", "Water", R.string.drink_cat_water, "💧", Color(0xFF60A5FA),
            listOf(
                Drink("w250", "Water", 250, 250, 0, 0f, 0, 0f, 0, 0, 0, 0, 0, 0, 0f, WATER_BLUE),
                Drink("w350", "Water", 350, 350, 0, 0f, 0, 0f, 0, 0, 0, 0, 0, 0, 0f, WATER_BLUE),
                Drink("w500", "Water", 500, 500, 0, 0f, 0, 0f, 0, 0, 0, 0, 0, 0, 0f, WATER_BLUE),
                Drink("w750", "Water", 750, 750, 0, 0f, 0, 0f, 0, 0, 0, 0, 0, 0, 0f, WATER_BLUE),
                Drink("w1000", "Water", 1000, 1000, 0, 0f, 0, 0f, 0, 0, 0, 0, 0, 0, 0f, WATER_BLUE),
                Drink("fw330", "Flavoured Water", 330, 327, 0, 3f, 0, 0f, 12, 5, 0, 0, 0, 0, 0f, WATER_LIGHT),
                Drink("fw500", "Flavoured Water", 500, 495, 0, 5f, 0, 0f, 18, 8, 0, 0, 0, 0, 0f, WATER_LIGHT),
                Drink("spark330", "Sparkling Water", 330, 330, 0, 0f, 5, 0f, 0, 20, 2, 1, 0, 0, 0f, WATER_LIGHT),
                Drink("spark500", "Sparkling Water", 500, 500, 0, 0f, 8, 0f, 0, 30, 3, 2, 0, 0, 0f, WATER_LIGHT),
                Drink("coco330", "Coconut Water", 330, 310, 0, 11f, 40, 0f, 46, 105, 600, 25, 0, 20, 0f, WATER_LIGHT)
            )
        ),
        DrinkCategory(
            "coffee", "Coffee & Tea", R.string.drink_cat_coffee, "☕", Color(0xFFA0724A),
            listOf(
                Drink("espresso", "Espresso Shot", 30, 28, 63, 0f, 2, 0f, 1, 4, 34, 6, 0, 3, 0.05f, COFFEE_BROWN),
                Drink("double_esp", "Double Espresso", 60, 55, 126, 0f, 4, 0f, 2, 8, 68, 12, 0, 6, 0.06f, COFFEE_BROWN),
                Drink("americano", "Americano", 355, 340, 150, 0f, 5, 0f, 5, 10, 70, 12, 0, 5, 0.08f, COFFEE_BROWN),
                Drink("latte", "Caffè Latte", 355, 300, 75, 13f, 170, 0f, 120, 80, 230, 16, 0, 130, 0.05f, COFFEE_BROWN),
                Drink("cappuccino", "Cappuccino", 240, 200, 75, 8f, 110, 0f, 80, 55, 160, 12, 0, 90, 0.05f, COFFEE_BROWN),
                Drink("flatwhite", "Flat White", 240, 200, 130, 11f, 150, 0f, 100, 65, 200, 14, 0, 110, 0.06f, COFFEE_BROWN),
                Drink("icedcoffee", "Iced Coffee", 475, 440, 165, 20f, 15, 0f, 80, 15, 80, 10, 0, 8, 0.08f, COFFEE_BROWN),
                Drink("mocha", "Caffè Mocha", 355, 280, 90, 28f, 150, 0f, 230, 90, 280, 20, 0, 140, 0.05f, COFFEE_BROWN),
                Drink("frappuccino", "Frappuccino", 475, 350, 65, 46f, 100, 0f, 280, 130, 180, 10, 0, 90, 0.06f, COFFEE_BROWN),
                Drink("blacktea", "Black Tea", 240, 235, 47, 0f, 0, 0f, 2, 7, 88, 3, 0, 2, 0.02f, TEA_AMBER),
                Drink("greentea", "Green Tea", 240, 236, 28, 0f, 0, 0f, 2, 2, 20, 2, 0, 1, 0.01f, TEA_GREEN),
                Drink("matcha", "Matcha Latte", 355, 310, 70, 12f, 150, 0f, 110, 70, 200, 10, 0, 100, 0.02f, TEA_GREEN),
                Drink("herbal", "Herbal Tea", 240, 237, 0, 0f, 5, 0f, 2, 2, 15, 2, 0, 1, 0f, TEA_AMBER),
                Drink("chai", "Chai Latte", 355, 290, 50, 21f, 130, 0f, 140, 75, 190, 12, 0, 100, 0.03f, TEA_AMBER)
            )
        ),
        DrinkCategory(
            "energy", "Energy Drinks", R.string.drink_cat_energy, "⚡", Color(0xFF4ADE80),
            listOf(
                Drink("monster500", "Monster Original", 500, 440, 160, 55f, 0, 0f, 220, 370, 10, 0, 0, 0, 0.15f, ENERGY_GREEN),
                Drink("monsterultra", "Monster Ultra", 500, 470, 150, 0f, 0, 0f, 10, 370, 10, 0, 0, 0, 0.12f, ENERGY_GREEN),
                Drink("monstermango", "Monster Mango Loco", 500, 435, 150, 54f, 0, 0f, 210, 330, 5, 0, 0, 0, 0.15f, ENERGY_YELLOW),
                Drink("redbull250", "Red Bull", 250, 215, 80, 27f, 0, 0f, 112, 105, 5, 12, 0, 0, 0.12f, ENERGY_YELLOW),
                Drink("redbullsf", "Red Bull Sugar Free", 250, 235, 80, 0f, 0, 0f, 5, 105, 5, 12, 0, 0, 0.1f, ENERGY_YELLOW),
                Drink("rockstar500", "Rockstar Original", 500, 440, 160, 62f, 0, 0f, 260, 230, 5, 0, 0, 0, 0.15f, ENERGY_GREEN),
                Drink("rockstarsf", "Rockstar Sugar Free", 500, 470, 160, 0f, 0, 0f, 10, 220, 5, 0, 0, 0, 0.12f, ENERGY_GREEN)
            )
        ),
        DrinkCategory(
            "soda", "Soft Drinks", R.string.drink_cat_soda, "🥤", Color(0xFFF472B6),
            listOf(
                Drink("cola330", "Coca-Cola", 330, 295, 32, 35f, 0, 0f, 139, 15, 0, 0, 0, 40, 0.05f, SODA_BROWN),
                Drink("cola500", "Coca-Cola", 500, 447, 48, 53f, 0, 0f, 210, 23, 0, 0, 0, 61, 0.05f, SODA_BROWN),
                Drink("dietcoke330", "Diet Coke", 330, 328, 42, 0f, 0, 0f, 1, 28, 0, 0, 0, 30, 0.03f, SODA_BROWN),
                Drink("pepsi330", "Pepsi", 330, 295, 38, 36f, 0, 0f, 150, 20, 0, 0, 0, 35, 0.05f, SODA_BROWN),
                Drink("pepsimax330", "Pepsi Max", 330, 325, 43, 0f, 0, 0f, 1, 18, 0, 0, 0, 28, 0.03f, SODA_BROWN),
                Drink("pepsimaxcherry", "Pepsi Max Cherry", 330, 325, 43, 0f, 0, 0f, 1, 18, 0, 0, 0, 28, 0.03f, SODA_BROWN),
                Drink("dew330", "Mountain Dew", 330, 293, 54, 46f, 0, 0f, 170, 60, 0, 0, 0, 0, 0.06f, ENERGY_YELLOW),
                Drink("fanta330", "Fanta Orange", 330, 300, 0, 36f, 0, 0f, 140, 25, 0, 0, 0, 0, 0.04f, SODA_ORANGE),
                Drink("sprite330", "Sprite", 330, 300, 0, 33f, 0, 0f, 130, 35, 0, 0, 0, 0, 0.04f, SODA_CLEAR),
                Drink("7up330", "7UP", 330, 300, 0, 33f, 0, 0f, 130, 30, 0, 0, 0, 0, 0.04f, SODA_CLEAR),
                Drink("tango330", "Tango Orange", 330, 300, 0, 30f, 0, 0f, 120, 15, 0, 0, 0, 0, 0.04f, SODA_ORANGE),
                Drink("lemonade330", "Lemonade", 330, 305, 0, 28f, 0, 0f, 110, 20, 15, 0, 0, 0, 0.03f, SODA_CLEAR)
            )
        ),
        DrinkCategory(
            "juice", "Fruit Juice", R.string.drink_cat_juice, "🍊", Color(0xFFFB923C),
            listOf(
                Drink("oj250", "Orange Juice", 250, 220, 0, 21f, 28, 0f, 112, 2, 496, 27, 0, 42, 0f, JUICE_ORANGE),
                Drink("apple250", "Apple Juice", 250, 220, 0, 24f, 20, 0f, 114, 10, 250, 12, 0, 17, 0f, JUICE_APPLE),
                Drink("pineapple250", "Pineapple Juice", 250, 215, 0, 25f, 33, 0f, 132, 5, 325, 30, 0, 20, 0f, JUICE_ORANGE),
                Drink("cranberry250", "Cranberry Juice", 250, 218, 0, 31f, 20, 0f, 137, 5, 195, 15, 0, 13, 0f, JUICE_PINK),
                Drink("grape250", "Grape Juice", 250, 212, 0, 36f, 28, 0f, 152, 13, 334, 25, 0, 27, 0f, JUICE_APPLE),
                Drink("smoothie330", "Berry Smoothie", 330, 290, 0, 30f, 60, 0f, 150, 25, 350, 20, 0, 35, 0f, JUICE_PINK)
            )
        ),
        DrinkCategory(
            "milk", "Milk & Calcium", R.string.drink_cat_milk, "🥛", Color(0xFFFEF3C7),
            listOf(
                Drink("milk250", "Whole Milk", 250, 220, 0, 12f, 300, 0f, 150, 105, 366, 27, 100, 230, 0f, MILK_WHITE),
                Drink("semimilk250", "Semi-Skimmed Milk", 250, 225, 0, 12f, 305, 0f, 115, 107, 380, 28, 105, 240, 0f, MILK_WHITE),
                Drink("skimmilk250", "Skimmed Milk", 250, 228, 0, 12f, 305, 0f, 83, 107, 382, 28, 105, 247, 0f, MILK_WHITE),
                Drink("chocmilk250", "Chocolate Milk", 250, 210, 5, 24f, 280, 0f, 190, 150, 420, 33, 0, 255, 0f, MILK_CHOC),
                Drink("almondmilk250", "Almond Milk", 250, 238, 0, 7f, 450, 0f, 39, 170, 176, 16, 0, 24, 0f, MILK_ALMOND),
                Drink("oatmilk250", "Oat Milk", 250, 230, 0, 7f, 350, 0f, 120, 100, 190, 36, 0, 142, 0f, MILK_ALMOND),
                Drink("soymilk250", "Soy Milk", 250, 230, 0, 6f, 300, 0f, 80, 90, 300, 38, 0, 120, 0f, MILK_ALMOND)
            )
        ),
        DrinkCategory(
            "alcohol", "Alcohol", R.string.drink_cat_alcohol, "🍷", Color(0xFFA855F7),
            listOf(
                Drink("beer330", "Beer (330ml)", 330, 305, 0, 0f, 14, 10.4f, 145, 14, 96, 21, 0, 50, 0.2f, ALCOHOL_GOLD),
                Drink("beer568", "Beer (Pint)", 568, 525, 0, 0f, 24, 17.9f, 250, 24, 165, 36, 0, 86, 0.2f, ALCOHOL_GOLD),
                Drink("guinness", "Guinness (Pint)", 568, 530, 0, 10f, 20, 18.6f, 210, 18, 130, 30, 0, 70, 0.18f, ALCOHOL_DARK),
                Drink("cider330", "Cider (330ml)", 330, 300, 0, 16f, 8, 11.9f, 160, 10, 80, 5, 0, 10, 0.22f, ALCOHOL_GOLD),
                Drink("redwine", "Red Wine (175ml)", 175, 145, 0, 1f, 12, 18.9f, 133, 6, 187, 18, 0, 34, 0.35f, ALCOHOL_RED),
                Drink("whitewine", "White Wine (175ml)", 175, 148, 0, 2f, 13, 16.8f, 121, 7, 104, 14, 0, 22, 0.35f, ALCOHOL_WHITE_WINE),
                Drink("rosewine", "Rosé Wine (175ml)", 175, 147, 0, 3f, 10, 17.5f, 125, 6, 90, 12, 0, 18, 0.35f, JUICE_PINK),
                Drink("prosecco", "Prosecco (125ml)", 125, 105, 0, 2f, 8, 10.9f, 86, 4, 60, 8, 0, 15, 0.35f, ALCOHOL_WHITE_WINE),
                Drink("vodka", "Vodka Shot", 25, 16, 0, 0f, 0, 8f, 56, 0, 1, 0, 0, 0, 0.5f, ALCOHOL_CLEAR),
                Drink("gin", "Gin Shot", 25, 16, 0, 0f, 0, 8f, 56, 1, 1, 0, 0, 0, 0.5f, ALCOHOL_CLEAR),
                Drink("rum", "Rum Shot", 25, 16, 0, 0f, 0, 8f, 56, 0, 1, 0, 0, 0, 0.5f, ALCOHOL_GOLD),
                Drink("jd", "Jack Daniel's", 25, 15, 0, 0f, 0, 8f, 56, 0, 1, 0, 0, 0, 0.5f, ALCOHOL_GOLD),
                Drink("gandt", "Gin & Tonic", 225, 190, 0, 17f, 0, 8f, 124, 12, 0, 0, 0, 0, 0.3f, ALCOHOL_CLEAR),
                Drink("vodkacoke", "Vodka & Coke", 225, 190, 19, 21f, 0, 8f, 143, 9, 0, 0, 0, 24, 0.3f, SODA_BROWN)
            )
        )
    )

    fun getAllDrinks(): List<Drink> = categories.flatMap { it.drinks }

    fun getDrinkById(id: String): Drink? = getAllDrinks().firstOrNull { it.id == id }

    fun getCategoryForDrink(drinkId: String): DrinkCategory? =
        categories.firstOrNull { category -> category.drinks.any { it.id == drinkId } }
}
