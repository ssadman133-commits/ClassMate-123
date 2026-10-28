package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

data class CurrencyOption(
    val symbol: String,
    val code: String,
    val name: String
)

val AvailableCurrencies = listOf(
    CurrencyOption("৳", "BDT", "Bangladeshi Taka"),
    CurrencyOption("$", "USD", "US Dollar"),
    CurrencyOption("₹", "INR", "Indian Rupee"),
    CurrencyOption("€", "EUR", "Euro"),
    CurrencyOption("£", "GBP", "British Pound"),
    CurrencyOption("₱", "PHP", "Philippine Peso"),
    CurrencyOption("RM", "MYR", "Malaysian Ringgit"),
    CurrencyOption("C$", "CAD", "Canadian Dollar"),
    CurrencyOption("A$", "AUD", "Australian Dollar")
)

data class DistributionPreset(
    val label: String,
    val percentages: List<Double>
)

fun getPresetsForCount(count: Int): List<DistributionPreset> {
    return when (count) {
        2 -> listOf(
            DistributionPreset("50 - 50", listOf(50.0, 50.0)),
            DistributionPreset("60 - 40", listOf(60.0, 40.0)),
            DistributionPreset("70 - 30", listOf(70.0, 30.0)),
            DistributionPreset("Custom", emptyList())
        )
        3 -> listOf(
            DistributionPreset("40-30-30", listOf(40.0, 30.0, 30.0)),
            DistributionPreset("Equal (33%)", listOf(33.34, 33.33, 33.33)),
            DistributionPreset("50-25-25", listOf(50.0, 25.0, 25.0)),
            DistributionPreset("Custom", emptyList())
        )
        4 -> listOf(
            DistributionPreset("Equal (25%)", listOf(25.0, 25.0, 25.0, 25.0)),
            DistributionPreset("40-20-20-20", listOf(40.0, 20.0, 20.0, 20.0)),
            DistributionPreset("Custom", emptyList())
        )
        5 -> listOf(
            DistributionPreset("Equal (20%)", listOf(20.0, 20.0, 20.0, 20.0, 20.0)),
            DistributionPreset("Custom", emptyList())
        )
        6 -> listOf(
            DistributionPreset("Equal (16.7%)", listOf(16.7, 16.7, 16.7, 16.7, 16.6, 16.6)),
            DistributionPreset("Custom", emptyList())
        )
        else -> listOf(
            DistributionPreset("Equal Split", List(count) { 100.0 / count }),
            DistributionPreset("Custom", emptyList())
        )
    }
}

fun getOrdinalTitle(index: Int): String {
    val num = index + 1
    val suffix = when {
        num % 100 in 11..13 -> "th"
        num % 10 == 1 -> "st"
        num % 10 == 2 -> "nd"
        num % 10 == 3 -> "rd"
        else -> "th"
    }
    return "$num$suffix Installment"
}

fun getOrdinalShort(index: Int): String {
    val num = index + 1
    val suffix = when {
        num % 100 in 11..13 -> "th"
        num % 10 == 1 -> "st"
        num % 10 == 2 -> "nd"
        num % 10 == 3 -> "rd"
        else -> "th"
    }
    return "$num$suffix"
}

private val installmentColors = listOf(
    Color(0xFF0D9488), // Teal
    Color(0xFFD97706), // Amber
    Color(0xFF7C3AED), // Violet
    Color(0xFF0284C7), // Sky Blue
    Color(0xFFEC4899), // Pink
    Color(0xFF059669)  // Emerald
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuitionCalculatorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Currency selection state
    var selectedCurrency by rememberSaveable { mutableStateOf("৳") }
    var isCurrencyMenuOpen by remember { mutableStateOf(false) }

    // Core inputs - initialized to empty/0 so calculator starts clean
    var totalCreditsText by rememberSaveable { mutableStateOf("") }
    var perCreditFeeText by rememberSaveable { mutableStateOf("") }
    var waiverPercentText by rememberSaveable { mutableStateOf("0") }
    var labFeeText by rememberSaveable { mutableStateOf("") }
    var otherFeeText by rememberSaveable { mutableStateOf("") }

    // Dynamic Installment setup
    var installmentCount by rememberSaveable { mutableIntStateOf(3) }
    var selectedPresetLabel by rememberSaveable { mutableStateOf("40-30-30") }
    var customPcts by rememberSaveable {
        mutableStateOf(listOf("40", "30", "30"))
    }
    var isPaidList by rememberSaveable {
        mutableStateOf(listOf(false, false, false))
    }

    // Formatting helpers
    val numberFormatter = remember {
        DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))
    }

    fun changeInstallmentCount(newCount: Int) {
        installmentCount = newCount
        val presets = getPresetsForCount(newCount)
        val defaultPreset = presets.first()
        selectedPresetLabel = defaultPreset.label
        val newPcts = when (newCount) {
            2 -> listOf("50", "50")
            3 -> listOf("40", "30", "30")
            4 -> listOf("25", "25", "25", "25")
            5 -> listOf("20", "20", "20", "20", "20")
            6 -> listOf("16.7", "16.7", "16.7", "16.7", "16.6", "16.6")
            else -> List(newCount) { numberFormatter.format(100.0 / newCount) }
        }
        customPcts = newPcts
        isPaidList = List(newCount) { false }
    }

    // Calculations
    val credits = totalCreditsText.toDoubleOrNull() ?: 0.0
    val perCreditFee = perCreditFeeText.toDoubleOrNull() ?: 0.0
    val grossTuition = (credits * perCreditFee).coerceAtLeast(0.0)

    val waiverPct = (waiverPercentText.toDoubleOrNull() ?: 0.0).coerceIn(0.0, 100.0)
    val waiverSavings = grossTuition * (waiverPct / 100.0)
    val tuitionAfterWaiver = (grossTuition - waiverSavings).coerceAtLeast(0.0)

    val labFee = (labFeeText.toDoubleOrNull() ?: 0.0).coerceAtLeast(0.0)
    val otherFee = (otherFeeText.toDoubleOrNull() ?: 0.0).coerceAtLeast(0.0)
    val totalAdditionalFees = labFee + otherFee

    val totalNetPayable = tuitionAfterWaiver + totalAdditionalFees

    // Dynamic Installment percentages
    val percentages = customPcts.map { it.toDoubleOrNull() ?: 0.0 }
    val sumPercentages = percentages.sum()
    val isPercentageValid = abs(sumPercentages - 100.0) < 0.2

    val installmentAmounts = percentages.map { pct ->
        (totalNetPayable * (pct / 100.0)).coerceAtLeast(0.0)
    }

    val paidTotal = installmentAmounts.filterIndexed { idx, _ ->
        isPaidList.getOrElse(idx) { false }
    }.sum()
    val dueTotal = (totalNetPayable - paidTotal).coerceAtLeast(0.0)

    // Helper functions for sharing & copying
    fun buildSummaryText(): String {
        return buildString {
            appendLine("🎓 Semester Tuition Fee Breakdown")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("• Total Credits: $credits")
            appendLine("• Per Credit Fee: $selectedCurrency${numberFormatter.format(perCreditFee)}")
            appendLine("• Gross Tuition Fee: $selectedCurrency${numberFormatter.format(grossTuition)}")
            if (waiverPct > 0.0) {
                appendLine("• Scholarship/Waiver: ${numberFormatter.format(waiverPct)}% (-$selectedCurrency${numberFormatter.format(waiverSavings)})")
                appendLine("• Tuition after Waiver: $selectedCurrency${numberFormatter.format(tuitionAfterWaiver)}")
            }
            if (labFee > 0.0) {
                appendLine("• Laboratory Fee: $selectedCurrency${numberFormatter.format(labFee)}")
            }
            if (otherFee > 0.0) {
                appendLine("• Other / Registration Fee: $selectedCurrency${numberFormatter.format(otherFee)}")
            }
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("💰 Total Net Payable: $selectedCurrency${numberFormatter.format(totalNetPayable)}")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📅 $installmentCount Installments Breakdown:")
            percentages.forEachIndexed { index, pct ->
                val amount = installmentAmounts.getOrElse(index) { 0.0 }
                val isPaid = isPaidList.getOrElse(index) { false }
                val title = getOrdinalTitle(index)
                appendLine("• $title (${numberFormatter.format(pct)}%): $selectedCurrency${numberFormatter.format(amount)} [${if (isPaid) "Paid ✓" else "Due"}]")
            }
            appendLine("----------------------------")
            appendLine("Remaining Due: $selectedCurrency${numberFormatter.format(dueTotal)}")
            appendLine("Generated via ClassMate")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Tuition Fee Calculator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Semester credits & $installmentCount installments",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Currency Selector dropdown button
                    Box {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            tonalElevation = 2.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { isCurrencyMenuOpen = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = selectedCurrency,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "▼",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = isCurrencyMenuOpen,
                            onDismissRequest = { isCurrencyMenuOpen = false }
                        ) {
                            AvailableCurrencies.forEach { curr ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = curr.symbol,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Text(text = "${curr.code} (${curr.name})")
                                        }
                                    },
                                    onClick = {
                                        selectedCurrency = curr.symbol
                                        isCurrencyMenuOpen = false
                                    }
                                )
                            }
                        }
                    }

                    // Reset button
                    IconButton(
                        onClick = {
                            totalCreditsText = ""
                            perCreditFeeText = ""
                            waiverPercentText = "0"
                            labFeeText = ""
                            otherFeeText = ""
                            installmentCount = 3
                            changeInstallmentCount(3)
                            Toast.makeText(context, "Calculator reset", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.RotateLeft,
                            contentDescription = "Reset",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hero / Summary Payable Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Payments,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Total Net Payable",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            if (waiverPct > 0.0) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, Color(0xFF10B981))
                                ) {
                                    Text(
                                        text = "${numberFormatter.format(waiverPct)}% Waiver Applied",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "$selectedCurrency ${numberFormatter.format(totalNetPayable)}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Brief breakdown sub-stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gross Tuition",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "$selectedCurrency${numberFormatter.format(grossTuition)}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            if (waiverSavings > 0.0) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Waiver Discount",
                                        fontSize = 11.sp,
                                        color = Color(0xFF059669)
                                    )
                                    Text(
                                        text = "-$selectedCurrency${numberFormatter.format(waiverSavings)}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF059669)
                                    )
                                }
                            }
                            if (totalAdditionalFees > 0.0) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Lab & Other Fees",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "+$selectedCurrency${numberFormatter.format(totalAdditionalFees)}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        // Due status pill if installments are toggled
                        if (isPaidList.any { it }) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Paid: $selectedCurrency${numberFormatter.format(paidTotal)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF059669)
                                )
                                Text(
                                    text = "Remaining Due: $selectedCurrency${numberFormatter.format(dueTotal)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (dueTotal > 0.0) Color(0xFFDC2626) else Color(0xFF059669)
                                )
                            }
                        }
                    }
                }
            }

            // Section 1: Course Credits & Rate
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "1. Credit & Tuition Rate",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Total credits with quick +/- buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = totalCreditsText,
                                onValueChange = { totalCreditsText = it },
                                label = { Text("Total Credits") },
                                placeholder = { Text("0") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            // Quick adjust stepper buttons
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            val current = totalCreditsText.toDoubleOrNull() ?: 0.0
                                            val c = current - 1.0
                                            totalCreditsText = if (c <= 0.0) "" else numberFormatter.format(c)
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = "Decrease credit",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            val current = totalCreditsText.toDoubleOrNull() ?: 0.0
                                            val c = current + 1.0
                                            totalCreditsText = numberFormatter.format(c)
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Increase credit",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Per credit fee input
                        OutlinedTextField(
                            value = perCreditFeeText,
                            onValueChange = { perCreditFeeText = it },
                            label = { Text("Fee Per Credit ($selectedCurrency)") },
                            placeholder = { Text("0") },
                            leadingIcon = {
                                Text(
                                    text = selectedCurrency,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Subtotal tag
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Base Tuition Subtotal:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$credits credits × $selectedCurrency${numberFormatter.format(perCreditFee)} = $selectedCurrency${numberFormatter.format(grossTuition)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Scholarship & Waiver
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Discount,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "2. Scholarship / Waiver (%)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Waiver input
                        OutlinedTextField(
                            value = waiverPercentText,
                            onValueChange = { waiverPercentText = it },
                            label = { Text("Waiver %") },
                            placeholder = { Text("0") },
                            trailingIcon = { Text("%", fontWeight = FontWeight.Bold) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Quick waiver chips in full width
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(0, 10, 20, 50, 100).forEach { pct ->
                                val isSelected = waiverPct.roundToInt() == pct
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { waiverPercentText = pct.toString() },
                                    label = {
                                        Text(
                                            text = "$pct%",
                                            fontSize = 12.sp,
                                            maxLines = 1
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF059669).copy(alpha = 0.15f),
                                        selectedLabelColor = Color(0xFF059669)
                                    )
                                )
                            }
                        }

                        // Slider for smooth adjustment
                        Slider(
                            value = waiverPct.toFloat(),
                            onValueChange = { waiverPercentText = it.roundToInt().toString() },
                            valueRange = 0f..100f,
                            steps = 19,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF059669),
                                activeTrackColor = Color(0xFF059669)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Waiver savings feedback
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF059669).copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Scholarship Discount:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF047857),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "-$selectedCurrency${numberFormatter.format(waiverSavings)} (Net: $selectedCurrency${numberFormatter.format(tuitionAfterWaiver)})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }
            }

            // Section 3: Laboratory & Other Fees (Optional)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "3. Laboratory & Other Fees (Optional)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = labFeeText,
                                onValueChange = { labFeeText = it },
                                label = { Text("Lab Fee ($selectedCurrency)") },
                                placeholder = { Text("0") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = otherFeeText,
                                onValueChange = { otherFeeText = it },
                                label = { Text("Registration / Other") },
                                placeholder = { Text("0") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = "💡 Note: Most universities apply tuition waivers to credit hours only. Lab and semester registration fees remain fixed.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section 4: Installment Distribution
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PieChart,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "4. Installment Distribution",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Validation badge
                            if (isPercentageValid) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "100%",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF059669)
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFEF4444).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${numberFormatter.format(sumPercentages)}% (Needs 100%)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDC2626),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Number of Installments selector (2 to 6)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Number of installments:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(2, 3, 4, 5, 6).forEach { count ->
                                    val isSelected = installmentCount == count
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (installmentCount != count) {
                                                changeInstallmentCount(count)
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = "$count",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                        }

                        // Presets Chips
                        val presets = remember(installmentCount) { getPresetsForCount(installmentCount) }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Distribution ratio or customize (%):",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                presets.forEach { preset ->
                                    val isSelected = selectedPresetLabel == preset.label
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedPresetLabel = preset.label
                                            if (preset.percentages.isNotEmpty()) {
                                                customPcts = preset.percentages.map { numberFormatter.format(it) }
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = preset.label,
                                                fontSize = 11.sp
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Custom percentage inputs arranged adaptively
                        val chunkedIndices = remember(installmentCount) {
                            val chunkSize = if (installmentCount <= 3) installmentCount else 2
                            (0 until installmentCount).chunked(chunkSize)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            chunkedIndices.forEach { rowIndices ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowIndices.forEach { index ->
                                        val valText = customPcts.getOrElse(index) { "" }
                                        OutlinedTextField(
                                            value = valText,
                                            onValueChange = { newVal ->
                                                selectedPresetLabel = "Custom"
                                                val updated = customPcts.toMutableList()
                                                while (updated.size <= index) updated.add("0")
                                                updated[index] = newVal
                                                customPcts = updated
                                            },
                                            label = { Text("${getOrdinalShort(index)} (%)") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 5: Dynamic Installment Result Cards
            item {
                Text(
                    text = "Installment Payment Schedule",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Dynamically render each installment card
            items(installmentCount) { index ->
                val pct = percentages.getOrElse(index) { 0.0 }
                val amount = installmentAmounts.getOrElse(index) { 0.0 }
                val isPaid = isPaidList.getOrElse(index) { false }
                val title = getOrdinalTitle(index)
                val accentColor = installmentColors[index % installmentColors.size]

                InstallmentCard(
                    number = index + 1,
                    title = title,
                    percentage = pct,
                    amount = amount,
                    currency = selectedCurrency,
                    isPaid = isPaid,
                    onTogglePaid = { paid ->
                        val updated = isPaidList.toMutableList()
                        while (updated.size <= index) updated.add(false)
                        updated[index] = paid
                        isPaidList = updated
                    },
                    accentColor = accentColor,
                    formatter = numberFormatter
                )
            }

            // Action Buttons: Copy Breakdown & Share
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val text = buildSummaryText()
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Tuition Breakdown", text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Tuition summary copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Copy")
                    }

                    Button(
                        onClick = {
                            val text = buildSummaryText()
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Tuition Fee Breakdown")
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Share")
                    }
                }
            }

            // Bottom spacer for comfortable scrolling
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InstallmentCard(
    number: Int,
    title: String,
    percentage: Double,
    amount: Double,
    currency: String,
    isPaid: Boolean,
    onTogglePaid: (Boolean) -> Unit,
    accentColor: Color,
    formatter: DecimalFormat,
    modifier: Modifier = Modifier
) {
    val cardBackground by animateColorAsState(
        targetValue = if (isPaid) {
            Color(0xFF10B981).copy(alpha = 0.08f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "InstallmentBgColor"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        border = BorderStroke(
            1.dp,
            if (isPaid) Color(0xFF10B981).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isPaid) Color(0xFF10B981) else accentColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isPaid) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Paid",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text(
                                    text = "$number",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = accentColor
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = accentColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${formatter.format(percentage)}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Paid toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isPaid) "Paid" else "Due",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPaid) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Switch(
                        checked = isPaid,
                        onCheckedChange = onTogglePaid,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF10B981)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount Payable:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$currency ${formatter.format(amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaid) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
