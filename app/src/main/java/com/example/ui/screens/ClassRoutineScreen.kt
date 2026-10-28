package com.example.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RoutineItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class RoutineDay(
    val dayOfWeek: Int,   // 6 = Sat, 7 = Sun, 1 = Mon, 2 = Tue, 3 = Wed, 4 = Thu, 5 = Fri
    val fullName: String, // "Saturday"
    val shortName: String // "Sat"
)

val ROUTINE_DAYS = listOf(
    RoutineDay(dayOfWeek = 6, fullName = "Saturday", shortName = "Sat"),
    RoutineDay(dayOfWeek = 7, fullName = "Sunday", shortName = "Sun"),
    RoutineDay(dayOfWeek = 1, fullName = "Monday", shortName = "Mon"),
    RoutineDay(dayOfWeek = 2, fullName = "Tuesday", shortName = "Tue"),
    RoutineDay(dayOfWeek = 3, fullName = "Wednesday", shortName = "Wed"),
    RoutineDay(dayOfWeek = 4, fullName = "Thursday", shortName = "Thu"),
    RoutineDay(dayOfWeek = 5, fullName = "Friday", shortName = "Fri")
)

fun getRoutineDateString(dayIndex: Int): String {
    val cal = Calendar.getInstance()
    val calDay = cal.get(Calendar.DAY_OF_WEEK)
    val currentOffsetFromSaturday = when (calDay) {
        Calendar.SATURDAY -> 0
        Calendar.SUNDAY -> 1
        Calendar.MONDAY -> 2
        Calendar.TUESDAY -> 3
        Calendar.WEDNESDAY -> 4
        Calendar.THURSDAY -> 5
        Calendar.FRIDAY -> 6
        else -> 0
    }
    cal.add(Calendar.DAY_OF_YEAR, dayIndex - currentOffsetFromSaturday)
    return SimpleDateFormat("MMM d", Locale.US).format(cal.time)
}

fun getRoutineFullDateString(dayIndex: Int): String {
    val cal = Calendar.getInstance()
    val calDay = cal.get(Calendar.DAY_OF_WEEK)
    val currentOffsetFromSaturday = when (calDay) {
        Calendar.SATURDAY -> 0
        Calendar.SUNDAY -> 1
        Calendar.MONDAY -> 2
        Calendar.TUESDAY -> 3
        Calendar.WEDNESDAY -> 4
        Calendar.THURSDAY -> 5
        Calendar.FRIDAY -> 6
        else -> 0
    }
    cal.add(Calendar.DAY_OF_YEAR, dayIndex - currentOffsetFromSaturday)
    return SimpleDateFormat("EEEE, MMMM d", Locale.US).format(cal.time)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassRoutineScreen(
    routineItems: List<RoutineItem>,
    onAddRoutineItem: (dayOfWeek: Int, courseName: String, startTime: String, endTime: String, room: String?) -> Unit,
    onUpdateRoutineItem: (RoutineItem) -> Unit,
    onDeleteRoutineItem: (RoutineItem) -> Unit,
    onBack: () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Default to current day of week (Saturday=6, Sunday=7, Monday=1, etc.)
    val todayDayOfWeek = remember {
        val calDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        when (calDay) {
            Calendar.SATURDAY -> 6
            Calendar.SUNDAY -> 7
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> 6
        }
    }

    val todayIndex = remember(todayDayOfWeek) {
        val idx = ROUTINE_DAYS.indexOfFirst { it.dayOfWeek == todayDayOfWeek }
        if (idx >= 0) idx else 0
    }

    val pagerState = rememberPagerState(initialPage = todayIndex) { ROUTINE_DAYS.size }
    val coroutineScope = rememberCoroutineScope()
    var isWeeklyView by remember { mutableStateOf(false) }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<RoutineItem?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = bottomBar,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Class Routine",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("routine_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Quick View Mode Switcher
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = !isWeeklyView,
                            onClick = { isWeeklyView = false },
                            label = { Text("Daily") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarViewDay,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        FilterChip(
                            selected = isWeeklyView,
                            onClick = { isWeeklyView = true },
                            label = { Text("Weekly") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarViewWeek,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                modifier = Modifier.testTag("add_routine_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Class")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isWeeklyView) {
                // DAILY VIEW MODE WITH SWIPEABLE HORIZONTAL PAGER (SWIPING CHANGES DAY & DATE)
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    ROUTINE_DAYS.forEachIndexed { index, rDay ->
                        val count = routineItems.count { it.dayOfWeek == rDay.dayOfWeek }
                        val isSelected = pagerState.currentPage == index
                        val isToday = index == todayIndex
                        val dateLabel = getRoutineDateString(index)

                        Tab(
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (count > 0) "${rDay.shortName} ($count)" else rDay.shortName,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = dateLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 11.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                            },
                            modifier = Modifier.testTag("tab_day_${rDay.dayOfWeek}")
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val rDay = ROUTINE_DAYS[page]
                    val dayItems = remember(routineItems, rDay.dayOfWeek) {
                        routineItems.filter { it.dayOfWeek == rDay.dayOfWeek }
                            .sortedBy { it.startTimeMinutes }
                    }
                    val fullDateStr = getRoutineFullDateString(page)
                    val isToday = page == todayIndex

                    if (dayItems.isEmpty()) {
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
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "No Classes on ${rDay.fullName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isToday) "Today • $fullDateStr" else fullDateStr,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = "Tap '+' to add course timetable slots with start time, end time, and room number.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = fullDateStr,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isToday) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = "Today",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            items(dayItems, key = { it.id }) { item ->
                                RoutineItemCard(
                                    item = item,
                                    onEdit = {
                                        editingItem = item
                                        showAddEditDialog = true
                                    },
                                    onDelete = { onDeleteRoutineItem(item) }
                                )
                            }
                        }
                    }
                }
            } else {
                // WEEKLY VIEW MODE
                WeeklyRoutineView(
                    routineDays = ROUTINE_DAYS,
                    routineItems = routineItems,
                    todayDayIndex = todayIndex,
                    onSelectDay = { dayIdx ->
                        isWeeklyView = false
                        coroutineScope.launch {
                            pagerState.scrollToPage(dayIdx)
                        }
                    },
                    onEditItem = { item ->
                        editingItem = item
                        showAddEditDialog = true
                    },
                    onDeleteItem = onDeleteRoutineItem,
                    onAddClassForDay = { dayIdx ->
                        coroutineScope.launch {
                            pagerState.scrollToPage(dayIdx)
                        }
                        editingItem = null
                        showAddEditDialog = true
                    }
                )
            }
        }
    }

    if (showAddEditDialog) {
        AddEditRoutineDialog(
            existing = editingItem,
            defaultDay = ROUTINE_DAYS[pagerState.currentPage].dayOfWeek,
            onDismiss = { showAddEditDialog = false },
            onSave = { dayOfWeek, course, start, end, room ->
                if (editingItem != null) {
                    onUpdateRoutineItem(
                        editingItem!!.copy(
                            dayOfWeek = dayOfWeek,
                            courseName = course,
                            startTime = start,
                            endTime = end,
                            roomNumber = room
                        )
                    )
                } else {
                    onAddRoutineItem(dayOfWeek, course, start, end, room)
                }
                showAddEditDialog = false
            }
        )
    }
}

@Composable
fun WeeklyRoutineView(
    routineDays: List<RoutineDay>,
    routineItems: List<RoutineItem>,
    todayDayIndex: Int,
    onSelectDay: (Int) -> Unit,
    onEditItem: (RoutineItem) -> Unit,
    onDeleteItem: (RoutineItem) -> Unit,
    onAddClassForDay: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalClasses = routineItems.size
    val activeDaysCount = routineDays.count { rDay -> routineItems.any { it.dayOfWeek == rDay.dayOfWeek } }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Weekly Summary Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Weekly Schedule Overview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$totalClasses total classes across $activeDaysCount active days",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarViewWeek,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        // Each Day of Week Card
        items(routineDays.size) { index ->
            val routineDay = routineDays[index]
            val dayNumber = routineDay.dayOfWeek
            val dayName = routineDay.fullName
            val dateStr = getRoutineDateString(index)
            val classesForDay = routineItems.filter { it.dayOfWeek == dayNumber }
                .sortedBy { it.startTimeMinutes }
            val isToday = index == todayDayIndex
            val hasClasses = classesForDay.isNotEmpty()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("weekly_day_card_$dayNumber"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isToday) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = if (isToday) {
                    androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.primary
                    )
                } else {
                    androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header Row of Day
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column {
                                Text(
                                    text = dayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = dateStr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isToday) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "Today",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (hasClasses) "${classesForDay.size} classes" else "No class",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (hasClasses) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            IconButton(
                                onClick = { onAddClassForDay(index) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add class for $dayName",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = { onSelectDay(index) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "View $dayName",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    if (hasClasses) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 1.dp
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            classesForDay.forEach { classItem ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Text(
                                                text = classItem.courseName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AccessTime,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(13.dp),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                    Text(
                                                        text = "${classItem.startTime} - ${classItem.endTime}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                                if (!classItem.roomNumber.isNullOrBlank()) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.LocationOn,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(12.dp),
                                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                        Text(
                                                            text = classItem.roomNumber,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        Row {
                                            IconButton(
                                                onClick = { onEditItem(classItem) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            IconButton(
                                                onClick = { onDeleteItem(classItem) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Empty Day placeholder
                        Text(
                            text = "No scheduled lectures or labs.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoutineItemCard(
    item: RoutineItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("routine_card_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = item.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${item.startTime} - ${item.endTime}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (!item.roomNumber.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = item.roomNumber,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.size(28.dp).testTag("routine_menu_${item.id}")
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddEditRoutineDialog(
    existing: RoutineItem?,
    defaultDay: Int,
    onDismiss: () -> Unit,
    onSave: (dayOfWeek: Int, course: String, startTime: String, endTime: String, room: String?) -> Unit
) {
    var courseName by remember { mutableStateOf(existing?.courseName ?: "") }
    var startTime by remember { mutableStateOf(existing?.startTime ?: "09:00 AM") }
    var endTime by remember { mutableStateOf(existing?.endTime ?: "10:30 AM") }
    var roomNumber by remember { mutableStateOf(existing?.roomNumber ?: "") }
    var selectedDay by remember { mutableIntStateOf(existing?.dayOfWeek ?: defaultDay) }

    val context = LocalContext.current

    val startTimePickerDialog = remember(context, startTime) {
        val timeCal = Calendar.getInstance()
        try {
            val parsed = SimpleDateFormat("hh:mm a", Locale.US).parse(startTime.trim())
            if (parsed != null) timeCal.time = parsed
        } catch (_: Exception) {}

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val chosenCal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                startTime = SimpleDateFormat("hh:mm a", Locale.US).format(chosenCal.time)
            },
            timeCal.get(Calendar.HOUR_OF_DAY),
            timeCal.get(Calendar.MINUTE),
            false
        )
    }

    val endTimePickerDialog = remember(context, endTime) {
        val timeCal = Calendar.getInstance()
        try {
            val parsed = SimpleDateFormat("hh:mm a", Locale.US).parse(endTime.trim())
            if (parsed != null) timeCal.time = parsed
        } catch (_: Exception) {}

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val chosenCal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                endTime = SimpleDateFormat("hh:mm a", Locale.US).format(chosenCal.time)
            },
            timeCal.get(Calendar.HOUR_OF_DAY),
            timeCal.get(Calendar.MINUTE),
            false
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existing != null) "Edit Routine Class" else "Add Class to Routine",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Day selector chips
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val currentDayName = ROUTINE_DAYS.find { it.dayOfWeek == selectedDay }?.fullName ?: "Select Day"
                    Text(
                        text = "Day: $currentDayName",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ROUTINE_DAYS) { rDay ->
                            FilterChip(
                                selected = selectedDay == rDay.dayOfWeek,
                                onClick = { selectedDay = rDay.dayOfWeek },
                                label = { Text(rDay.shortName) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = { Text("Course Name *") },
                    placeholder = { Text("e.g. Data Structures") },
                    modifier = Modifier.fillMaxWidth().testTag("routine_course_input"),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Start Time interactive picker
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Start Time *") },
                            trailingIcon = {
                                IconButton(onClick = { startTimePickerDialog.show() }) {
                                    Icon(Icons.Default.AccessTime, contentDescription = "Select Start Time", tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { startTimePickerDialog.show() }
                        )
                    }

                    // End Time interactive picker
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = endTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("End Time *") },
                            trailingIcon = {
                                IconButton(onClick = { endTimePickerDialog.show() }) {
                                    Icon(Icons.Default.AccessTime, contentDescription = "Select End Time", tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { endTimePickerDialog.show() }
                        )
                    }
                }

                OutlinedTextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    label = { Text("Room / Hall (Optional)") },
                    placeholder = { Text("e.g. Room 402 or Lab 3") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (courseName.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank()) {
                        onSave(
                            selectedDay,
                            courseName.trim(),
                            startTime.trim(),
                            endTime.trim(),
                            roomNumber.ifBlank { null }
                        )
                    }
                },
                enabled = courseName.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank(),
                modifier = Modifier.testTag("save_routine_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
