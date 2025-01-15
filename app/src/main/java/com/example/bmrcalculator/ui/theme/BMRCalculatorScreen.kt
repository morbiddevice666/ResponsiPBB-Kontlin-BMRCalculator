import android.content.Context
import android.content.Context.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Definisi warna kustom
private val PrimaryBlue = Color(0xFF1A73E8)
private val SecondaryBlue = Color(0xFF5C9CE6)
private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE3F2FD),
        Color(0xFFBBDEFB)
    )
)

@Composable
fun BMRCalculatorScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("bmr_prefs", MODE_PRIVATE)

    var name by remember { mutableStateOf(sharedPreferences.getString("name", "") ?: "") }
    var age by remember { mutableStateOf(sharedPreferences.getString("age", "") ?: "") }
    var weight by remember { mutableStateOf(sharedPreferences.getString("weight", "") ?: "") }
    var height by remember { mutableStateOf(sharedPreferences.getString("height", "") ?: "") }
    var isMale by remember { mutableStateOf(sharedPreferences.getBoolean("isMale", true)) }
    var bmr by remember { mutableStateOf(sharedPreferences.getFloat("bmr", 0f)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = BackgroundGradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BMR Calculator",
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryBlue,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama", color = PrimaryBlue) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                focusedLabelColor = PrimaryBlue,
                cursorColor = PrimaryBlue
            )
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Usia", color = PrimaryBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                focusedLabelColor = PrimaryBlue,
                cursorColor = PrimaryBlue
            )
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Berat (kg)", color = PrimaryBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                focusedLabelColor = PrimaryBlue,
                cursorColor = PrimaryBlue
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Tinggi (cm)", color = PrimaryBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                focusedLabelColor = PrimaryBlue,
                cursorColor = PrimaryBlue
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = isMale,
                    onClick = { isMale = true },
                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                )
                Text("Pria", color = PrimaryBlue)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !isMale,
                    onClick = { isMale = false },
                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                )
                Text("Wanita", color = PrimaryBlue)
            }
        }

        Button(
            onClick = {
                if (age.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty()) {
                    bmr = calculateBMR(
                        isMale,
                        weight.toFloat(),
                        height.toFloat(),
                        age.toInt()
                    )

                    // Save to SharedPreferences
                    sharedPreferences.edit().apply {
                        putString("name", name)
                        putString("age", age)
                        putString("weight", weight)
                        putString("height", height)
                        putBoolean("isMale", isMale)
                        putFloat("bmr", bmr)
                        apply()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Hitung BMR", color = Color.White)
        }

        if (bmr > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hasil BMR",
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format("%.2f kalori/hari", bmr),
                        style = MaterialTheme.typography.headlineMedium,
                        color = SecondaryBlue
                    )
                }
            }
        }
    }
}

fun calculateBMR(isMale: Boolean, weight: Float, height: Float, age: Int): Float {
    return if (isMale) {
        66f + (13.7f * weight) + (5f * height) - (6.8f * age)
    } else {
        655f + (9.6f * weight) + (1.8f * height) - (4.7f * age)
    }
}
