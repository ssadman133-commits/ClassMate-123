package com.example.ui.screens

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SemesterRecord
import com.example.util.DateFormatter
import com.example.util.GradeScale
import java.util.Locale

data class CourseEntry(
    var id: Long = System.currentTimeMillis(),
    var name: String = "",
    var credit: String = "",
    var grade: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CgpaCalculatorScreen(
    gradeScale: GradeScale,
    isGradeScaleConfigured: Boolean = true,
    onSaveGradeScale: (GradeScale) -> Unit = {},
    onDismissGradeScaleSetup: () -> Unit = {},
    semesterHistory: List<SemesterRecord>,
    onSaveSemester: (name: String, gpa: Double, credits: Double) -> Unit,
    onDeleteSemester: (SemesterRecord) -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val calculatorListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // One-time grade scale setup dialog on first entry, or when user opens customize
    var showInitialGradeSetup by remember(isGradeScaleConfigured) {
        mutableStateOf(!isGradeScaleConfigured)
    }

    if (showInitialGradeSetup) {
        InitialGradeScaleSetupDialog(
            currentScale = gradeScale,
            isInitialSetup = !isGradeScaleConfigured,
            onDismiss = {
                showInitialGradeSetup = false
                if (!isGradeScaleConfigured) {
                    onDismissGradeScaleSetup()
                }
            },
            onSave = { newScale ->
                onSaveGradeScale(newScale)
                showInitialGradeSetup = false
            }
        )
    }

    // Course entries list - fresh clean state starting at 0
    val courses = remember {
        mutableStateListOf(
            CourseEntry(id = 1, name = "", credit = "", grade = ""),
            CourseEntry(id = 2, name = "", credit = "", grade = ""),
            CourseEntry(id = 3, name = "", credit = "", grade = "")
        )
    }

    val addNewCourse: () -> Unit = {
        courses.add(
            CourseEntry(
                id = System.currentTimeMillis(),
                name = "",
                credit = "",
                grade = ""
            )
        )
        coroutineScope.launch {
            delay(50)
            // Scroll directly down to the new course and bottom button
            val targetIndex = (courses.size - 1) + 3 + 1
            calculatorListState.animateScrollToItem(targetIndex)
        }
    }

    // Cumulative CGPA inputs
    var prevCgpaText by remember { mutableStateOf("") }
    var prevCreditsText by remember { mutableStateOf("") }

    // Save Semester Dialog
    var showSaveDialog by remember { mutableStateOf(false) }
    var semesterNameInput by remember { mutableStateOf("Semester 1") }

    // Current Semester GPA calculation (only calculated when credit and grade are provided)
    val semesterStats by remember(gradeScale) {
        derivedStateOf {
            var totalCredits = 0.0
            var totalPoints = 0.0
            for (c in courses) {
                val cr = c.credit.toDoubleOrNull() ?: 0.0
                if (c.grade.isNotBlank() && cr > 0.0) {
                    val pt = gradeScale.getPointForGrade(c.grade)
                    totalCredits += cr
                    totalPoints += (cr * pt)
                }
            }
            val gpa = if (totalCredits > 0.0) totalPoints / totalCredits else 0.0
            Pair(gpa, totalCredits)
        }
    }

    // Cumulative CGPA calculation
    val cumulativeCgpa by remember(semesterStats, prevCgpaText, prevCreditsText) {
        derivedStateOf {
            val prevCgpa = prevCgpaText.toDoubleOrNull() ?: 0.0
            val prevCredits = prevCreditsText.toDoubleOrNull() ?: 0.0
            val curGpa = semesterStats.first
            val curCredits = semesterStats.second

            val combinedCredits = prevCredits + curCredits
            if (combinedCredits > 0.0) {
                val totalPoints = (prevCgpa * prevCredits) + (curGpa * curCredits)
                totalPoints / combinedCredits
            } else {
                0.0
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CGPA Calculator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("cgpa_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showInitialGradeSetup = true },
                        modifier = Modifier.testTag("cgpa_customize_scale_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Customize Grade Scale",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Calculator", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = null) },
                    modifier = Modifier.testTag("tab_cgpa_calculator")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("History (${semesterHistory.size})", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    modifier = Modifier.testTag("tab_cgpa_history")
                )
            }

            if (selectedTab == 0) {
                LazyColumn(
                    state = calculatorListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Result Summary Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .testTag("cgpa_result_summary_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Semester GPA",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = if (semesterStats.second > 0.0) String.format(Locale.US, "%.2f", semesterStats.first) else "0.00",
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Semester Credits",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = if (semesterStats.second > 0.0) String.format(Locale.US, "%.1f", semesterStats.second) else "0.0",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                                if (prevCreditsText.isNotBlank()) {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Updated Cumulative CGPA",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = String.format(Locale.US, "%.2f", cumulativeCgpa),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            semesterNameInput = "Semester ${semesterHistory.size + 1}"
                                            showSaveDialog = true
                                        },
                                        enabled = semesterStats.second > 0.0,
                                        modifier = Modifier.testTag("save_semester_result_button")
                                    ) {
                                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Semester")
                                    }
                                }
                            }
                        }
                    }


                    // Optional Previous CGPA Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Cumulative CGPA (Optional)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = prevCgpaText,
                                        onValueChange = { prevCgpaText = it },
                                        label = { Text("Prev CGPA") },
                                        placeholder = { Text("e.g. 3.65") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("prev_cgpa_input"),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = prevCreditsText,
                                        onValueChange = { prevCreditsText = it },
                                        label = { Text("Prev Credits") },
                                        placeholder = { Text("e.g. 45.0") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("prev_credits_input"),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }

                    // Courses Section Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Courses (${courses.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (courses.size > 3) {
                                    Text(
                                        text = "Scroll down for all courses",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (courses.any { it.name.isNotBlank() || it.credit.isNotBlank() || it.grade.isNotBlank() } || prevCgpaText.isNotBlank() || prevCreditsText.isNotBlank()) {
                                    TextButton(
                                        onClick = {
                                            courses.clear()
                                            courses.add(CourseEntry(id = 1, name = "", credit = "", grade = ""))
                                            courses.add(CourseEntry(id = 2, name = "", credit = "", grade = ""))
                                            courses.add(CourseEntry(id = 3, name = "", credit = "", grade = ""))
                                            prevCgpaText = ""
                                            prevCreditsText = ""
                                        },
                                        modifier = Modifier.testTag("clear_cgpa_calculator_button")
                                    ) {
                                        Text("Reset")
                                    }
                                }

                                OutlinedButton(
                                    onClick = addNewCourse,
                                    modifier = Modifier.testTag("add_course_row_button")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Course")
                                }
                            }
                        }
                    }

                    // Course Rows
                    itemsIndexed(courses, key = { _, item -> item.id }) { index, course ->
                        CourseRowCard(
                            course = course,
                            index = index,
                            onUpdate = { updated ->
                                courses[index] = updated
                            },
                            onDelete = {
                                if (courses.size > 1) {
                                    courses.removeAt(index)
                                }
                            },
                            canDelete = courses.size > 1
                        )
                    }

                    // Bottom Add Course Button for immediate visibility and convenience
                    item {
                        OutlinedButton(
                            onClick = addNewCourse,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("add_course_bottom_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Another Course (Course ${courses.size + 1})", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            } else {
                // Semester History Tab
                if (semesterHistory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "No Saved Semesters",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Calculate your semester GPA in the Calculator tab and tap 'Save Semester' to preserve your academic history locally.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(semesterHistory, key = { _, item -> item.id }) { _, record ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("semester_history_item_${record.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = record.semesterName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${String.format(Locale.US, "%.1f", record.totalCredits)} credits • ${DateFormatter.formatDate(record.createdAt)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = "GPA ${String.format(Locale.US, "%.2f", record.gpa)}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = { onDeleteSemester(record) },
                                            modifier = Modifier.testTag("delete_semester_${record.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "Delete semester",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Semester Result", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter a label for this semester result:")
                    OutlinedTextField(
                        value = semesterNameInput,
                        onValueChange = { semesterNameInput = it },
                        label = { Text("Semester Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_semester_name_input"),
                        singleLine = true
                    )
                    Text(
                        text = "GPA: ${String.format(Locale.US, "%.2f", semesterStats.first)} • Credits: ${String.format(Locale.US, "%.1f", semesterStats.second)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (semesterNameInput.isNotBlank()) {
                            onSaveSemester(
                                semesterNameInput.trim(),
                                semesterStats.first,
                                semesterStats.second
                            )
                            showSaveDialog = false
                        }
                    },
                    modifier = Modifier.testTag("confirm_save_semester_button")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseRowCard(
    course: CourseEntry,
    index: Int = 0,
    onUpdate: (CourseEntry) -> Unit,
    onDelete: () -> Unit,
    canDelete: Boolean
) {
    val gradeOptions = listOf("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F")
    var gradeDropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Course Name
            OutlinedTextField(
                value = course.name,
                onValueChange = { onUpdate(course.copy(name = it)) },
                placeholder = { Text("Course ${index + 1}") },
                modifier = Modifier.weight(1.5f),
                singleLine = true
            )

            // Credit
            OutlinedTextField(
                value = course.credit,
                onValueChange = { onUpdate(course.copy(credit = it)) },
                label = { Text("Cr", maxLines = 1) },
                placeholder = { Text("3.0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.width(72.dp),
                singleLine = true
            )

            // Grade Dropdown
            ExposedDropdownMenuBox(
                expanded = gradeDropdownExpanded,
                onExpandedChange = { gradeDropdownExpanded = !gradeDropdownExpanded },
                modifier = Modifier.width(96.dp)
            ) {
                OutlinedTextField(
                    value = course.grade,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gr", maxLines = 1) },
                    placeholder = { Text("—") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeDropdownExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = gradeDropdownExpanded,
                    onDismissRequest = { gradeDropdownExpanded = false }
                ) {
                    gradeOptions.forEach { grade ->
                        DropdownMenuItem(
                            text = { Text(grade, fontWeight = FontWeight.Bold) },
                            onClick = {
                                onUpdate(
                                    course.copy(
                                        grade = grade,
                                        credit = if (course.credit.isBlank()) "3.0" else course.credit
                                    )
                                )
                                gradeDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Delete action
            if (canDelete) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove course",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun InitialGradeScaleSetupDialog(
    currentScale: GradeScale,
    isInitialSetup: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (GradeScale) -> Unit
) {
    var aPlus by remember(currentScale) { mutableStateOf(currentScale.aPlus.toString()) }
    var a by remember(currentScale) { mutableStateOf(currentScale.a.toString()) }
    var aMinus by remember(currentScale) { mutableStateOf(currentScale.aMinus.toString()) }
    var bPlus by remember(currentScale) { mutableStateOf(currentScale.bPlus.toString()) }
    var b by remember(currentScale) { mutableStateOf(currentScale.b.toString()) }
    var bMinus by remember(currentScale) { mutableStateOf(currentScale.bMinus.toString()) }
    var cPlus by remember(currentScale) { mutableStateOf(currentScale.cPlus.toString()) }
    var c by remember(currentScale) { mutableStateOf(currentScale.c.toString()) }
    var d by remember(currentScale) { mutableStateOf(currentScale.d.toString()) }
    var f by remember(currentScale) { mutableStateOf(currentScale.f.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isInitialSetup) Icons.Default.School else Icons.Default.Tune,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = if (isInitialSetup) "University Grade Setup" else "Customize Grade Scale",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Adjust grade points for your university",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = if (isInitialSetup) {
                            "Set point values according to your university requirement. This is saved for future calculations and can be customized anytime from the top bar icon or Settings."
                        } else {
                            "Update grade points according to your university requirement. Your GPA and CGPA calculations will update automatically based on these values."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = aPlus,
                            onValueChange = { aPlus = it },
                            label = { Text("A+") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        OutlinedTextField(
                            value = a,
                            onValueChange = { a = it },
                            label = { Text("A") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = aMinus,
                            onValueChange = { aMinus = it },
                            label = { Text("A-") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        OutlinedTextField(
                            value = bPlus,
                            onValueChange = { bPlus = it },
                            label = { Text("B+") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = b,
                            onValueChange = { b = it },
                            label = { Text("B") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        OutlinedTextField(
                            value = bMinus,
                            onValueChange = { bMinus = it },
                            label = { Text("B-") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = cPlus,
                            onValueChange = { cPlus = it },
                            label = { Text("C+") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        OutlinedTextField(
                            value = c,
                            onValueChange = { c = it },
                            label = { Text("C") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = d,
                            onValueChange = { d = it },
                            label = { Text("D") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        OutlinedTextField(
                            value = f,
                            onValueChange = { f = it },
                            label = { Text("F") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val scale = GradeScale(
                        aPlus = aPlus.toDoubleOrNull() ?: 4.0,
                        a = a.toDoubleOrNull() ?: 3.75,
                        aMinus = aMinus.toDoubleOrNull() ?: 3.50,
                        bPlus = bPlus.toDoubleOrNull() ?: 3.25,
                        b = b.toDoubleOrNull() ?: 3.00,
                        bMinus = bMinus.toDoubleOrNull() ?: 2.75,
                        cPlus = cPlus.toDoubleOrNull() ?: 2.50,
                        c = c.toDoubleOrNull() ?: 2.25,
                        d = d.toDoubleOrNull() ?: 2.00,
                        f = f.toDoubleOrNull() ?: 0.00
                    )
                    onSave(scale)
                },
                modifier = Modifier.testTag("initial_save_grade_scale_button")
            ) {
                Text(if (isInitialSetup) "Save & Continue" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("initial_skip_grade_scale_button")
            ) {
                Text(if (isInitialSetup) "Use Default (4.0)" else "Cancel")
            }
        }
    )
}
