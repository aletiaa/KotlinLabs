package com.example.fuelcalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fuelcalculatorapp.ui.theme.FuelCalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuelCalculatorAppTheme {
                CalculatorSwitcher()
            }
        }
    }
}

// Switch between Task 1 (Combustion) and Task 2 (Fuel Oil)
@Composable
fun CalculatorSwitcher(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Завдання 1", "Завдання 2")

    Column(modifier = modifier.fillMaxSize()) {
        // Tab Layout
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }

        // Switch between the calculators
        when (selectedTabIndex) {
            0 -> Task1Screen() // Task 1 (Combustion calculation)
            1 -> Task2Screen() // Task 2 (Fuel Oil Composition)
        }
    }
}

// Task 1: Fuel Combustion Calculation
@Composable
fun Task1Screen(modifier: Modifier = Modifier) {
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var nitrogen by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Калькулятор складу палива (Task 1)", fontWeight = FontWeight.Bold)

        // Input fields
        FuelInputField(label = "Вуглець CP (%)", value = carbon) { carbon = it }
        FuelInputField(label = "Водень HP (%)", value = hydrogen) { hydrogen = it }
        FuelInputField(label = "Сірка SP (%)", value = sulfur) { sulfur = it }
        FuelInputField(label = "Азот AP (%)", value = nitrogen) { nitrogen = it }
        FuelInputField(label = "Кисень OP (%)", value = oxygen) { oxygen = it }
        FuelInputField(label = "Волога W (%)", value = moisture) { moisture = it }
        FuelInputField(label = "Зола A (%)", value = ash) { ash = it }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to calculate the result
        Button(onClick = {
            val carbonValue = carbon.toDoubleOrNull()
            val hydrogenValue = hydrogen.toDoubleOrNull()
            val sulfurValue = sulfur.toDoubleOrNull()
            val nitrogenValue = nitrogen.toDoubleOrNull()
            val oxygenValue = oxygen.toDoubleOrNull()
            val moistureValue = moisture.toDoubleOrNull()
            val ashValue = ash.toDoubleOrNull()

            if (carbonValue == null || hydrogenValue == null || sulfurValue == null || nitrogenValue == null ||
                oxygenValue == null || moistureValue == null || ashValue == null) {
                errorMessage = "Будь ласка, введіть правильні значення."
                result = ""
            } else {
                errorMessage = ""
                result = calculateFuelCombustion(
                    carbonValue, hydrogenValue, sulfurValue, nitrogenValue, oxygenValue, moistureValue, ashValue
                )
            }
        }) {
            Text("Розрахувати")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(result)
    }
}

// Task 2: Fuel Oil Composition Calculation
@Composable
fun Task2Screen(modifier: Modifier = Modifier) {
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var vanadium by remember { mutableStateOf("") }
    var lowerHeatCombustion by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "Task 2: Fuel Oil Composition Calculator", fontWeight = FontWeight.Bold)

        // Input fields
        FuelInputField(label = "Вуглець (%)", value = carbon) { carbon = it }
        FuelInputField(label = "Водень (%)", value = hydrogen) { hydrogen = it }
        FuelInputField(label = "Кисень (%)", value = oxygen) { oxygen = it }
        FuelInputField(label = "Сірка (%)", value = sulfur) { sulfur = it }
        FuelInputField(label = "Волога W (%)", value = moisture) { moisture = it }
        FuelInputField(label = "Зола A (%)", value = ash) { ash = it }
        FuelInputField(label = "Ванадій (мг/кг)", value = vanadium) { vanadium = it }
        FuelInputField(label = "Нижча теплота згоряння (МДж/кг)", value = lowerHeatCombustion) { lowerHeatCombustion = it }

        Spacer(modifier = Modifier.height(4.dp))

        // Button to calculate the result
        Button(onClick = {
            val carbonValue = checkAndToDouble(carbon)
            val hydrogenValue = checkAndToDouble(hydrogen)
            val sulfurValue = checkAndToDouble(sulfur)
            val oxygenValue = checkAndToDouble(oxygen)
            val moistureValue = checkAndToDouble(moisture)
            val ashValue = checkAndToDouble(ash)
            val vanadiumValue = checkAndToDouble(vanadium)
            val lowerHeatCombustionValue = checkAndToDouble(lowerHeatCombustion)

            if (carbonValue == null || hydrogenValue == null || sulfurValue == null || oxygenValue == null
                || moistureValue == null || ashValue == null || vanadiumValue == null || lowerHeatCombustionValue == null) {
                errorMessage = "Одне з полів порожнє або містить неправильні дані!"
                result = ""
            } else {
                result = calculateFuelOilComposition(
                    carbonValue, hydrogenValue, sulfurValue, oxygenValue, moistureValue, ashValue, vanadiumValue, lowerHeatCombustionValue
                )
                errorMessage = ""
            }
        }) {
            Text("Розрахувати")
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(result)
    }
}

// Helper Composable for Input Fields
@Composable
fun FuelInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

// Helper function to validate input and convert to Double
fun checkAndToDouble(input: String): Double? {
    return try {
        val value = input.toDouble()
        if (value >= 0) value else null
    } catch (e: NumberFormatException) {
        null
    }
}

// Task 1 Calculation Function (Fuel Combustion)
fun calculateFuelCombustion(
    carbon: Double, hydrogen: Double, sulfur: Double, nitrogen: Double, oxygen: Double,
    moisture: Double, ash: Double
): String {
    // 1. dry and combastible calculations
    val crs = 100 / (100 - moisture)
    val crg = 100 / (100 - moisture - ash)

    //2. dry mass
    val dryCarbon = carbon * crs
    val dryHydrogen = hydrogen * crs
    val drySulfur = sulfur * crs
    val dryNitrogen = nitrogen * crs
    val dryOxygen = oxygen * crs
    val dryAsh = ash * crs

    // 3. Calculate combustible mass composition
    val combustCarbon = carbon * crg
    val combustHydrogen = hydrogen * crg
    val combustSulfur = sulfur * crg
    val combustNitrogen = nitrogen * crg
    val combustOxygen = oxygen * crg

    // 4. Calculate lower heat of combustion (Mendeleev's formula)
    val lowerHeatOfCombustion = (339 * carbon + 1030 * hydrogen - 108.8 * (oxygen - sulfur) - 25 * moisture) / 1000

    val Q_dry =  (lowerHeatOfCombustion + 0.025 * moisture) * crs

    val Q_burning = (lowerHeatOfCombustion + 0.025 * moisture) * crg
    // Format and return the result
    return """
        Нижча теплота згоряння: %.2f Мдж/кг
        Нижча теплота згоряння для сухої маси: %.2f МДж/кг
        Нижча теплота згоряння для горючої маси: %.2f МДж/кг
        
        Перехід від сухої до робочої:
        Вуглець: %.2f
        Водень: %.2f
        Сірка: %.2f
        Азот: %.2f
        Кисень: %.2f
        Зола: %.2f

        Перехід від робочої до горючої:
        Вуглець: %.2f
        Водень: %.2f
        Сірка: %.2f
        Азот: %.2f
        Кисень: %.2f

    """.trimIndent().format(
        Q_dry, Q_burning, lowerHeatOfCombustion, dryCarbon, dryHydrogen, drySulfur, dryNitrogen, dryOxygen, dryAsh,
        combustCarbon, combustHydrogen, combustSulfur, combustNitrogen, combustOxygen,
    )
}

// Task 2 Calculation Function (Fuel Oil Composition)
fun calculateFuelOilComposition(
    carbon: Double, hydrogen: Double, sulfur: Double, oxygen: Double, moisture: Double,
    ash: Double, vanadium: Double, lowerHeatCombustion: Double
): String {
    val cp = carbon * (100 - moisture - ash) / 100
    val hp = hydrogen * (100 - moisture - ash) / 100
    val sp = sulfur * (100 - moisture - ash) / 100
    val op = oxygen * (100 - moisture - ash) / 100
    val ap = ash * (100 - moisture) / 100
    val vp = vanadium * (100 - moisture) / 100
    val qri = lowerHeatCombustion * (100 - moisture - ash) / 100 - 0.025 * moisture

    return """
        Склад робочої маси мазуту:
        Вуглець: %.2f%%
        Водень: %.2f%%
        Сірка: %.2f%%
        Кисень: %.2f%%
        Зола: %.2f%%
        Ванадій: %.2f мг/кг
        Нижча теплота згоряння: %.2f МДж/кг
    """.trimIndent().format(cp, hp, sp, op, ap, vp, qri)
}
