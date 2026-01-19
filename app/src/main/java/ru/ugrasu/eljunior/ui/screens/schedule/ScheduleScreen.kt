package ru.ugrasu.eljunior.ui.screens.schedule

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ugrasu.eljunior.data.model.DaySchedule
import ru.ugrasu.eljunior.data.model.LessonType
import ru.ugrasu.eljunior.data.model.ScheduleItem
import ru.ugrasu.eljunior.ui.theme.AccentBlue
import ru.ugrasu.eljunior.ui.theme.BackgroundGray
import ru.ugrasu.eljunior.ui.theme.PrimaryRed
import ru.ugrasu.eljunior.ui.theme.TextPrimary
import ru.ugrasu.eljunior.ui.theme.TextSecondary
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val weekStart = remember(selectedDate) {
        selectedDate.with(DayOfWeek.MONDAY)
    }
    val weekDays = remember(weekStart) {
        (0..6).map { weekStart.plusDays(it.toLong()) }
    }

    val weekOfYear = selectedDate.get(WeekFields.of(Locale.getDefault()).weekOfYear())
    val isEvenWeek = weekOfYear % 2 == 0

    // Mock schedule data
    val scheduleItems = getMockScheduleForDate(selectedDate, isEvenWeek)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расписание",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Week navigation
            WeekNavigation(
                weekStart = weekStart,
                isEvenWeek = isEvenWeek,
                onPreviousWeek = { selectedDate = selectedDate.minusWeeks(1) },
                onNextWeek = { selectedDate = selectedDate.plusWeeks(1) }
            )

            // Day selector
            DaySelector(
                days = weekDays,
                selectedDate = selectedDate,
                onDaySelected = { selectedDate = it }
            )

            // Schedule list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (scheduleItems.isEmpty()) {
                    item {
                        EmptyScheduleCard()
                    }
                } else {
                    items(scheduleItems) { item ->
                        ScheduleCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun WeekNavigation(
    weekStart: LocalDate,
    isEvenWeek: Boolean,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    val weekEnd = weekStart.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Предыдущая неделя",
                tint = TextSecondary
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${weekStart.format(formatter)} - ${weekEnd.format(formatter)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = if (isEvenWeek) "Четная неделя" else "Нечетная неделя",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        IconButton(onClick = onNextWeek) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Следующая неделя",
                tint = TextSecondary
            )
        }
    }
}

@Composable
fun DaySelector(
    days: List<LocalDate>,
    selectedDate: LocalDate,
    onDaySelected: (LocalDate) -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EE", Locale("ru"))
    val today = LocalDate.now()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { date ->
            val isSelected = date == selectedDate
            val isToday = date == today

            Column(
                modifier = Modifier
                    .width(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            isSelected -> PrimaryRed
                            else -> Color.Transparent
                        }
                    )
                    .clickable { onDaySelected(date) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.format(dayFormatter).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            when {
                                isSelected -> Color.White.copy(alpha = 0.2f)
                                isToday -> PrimaryRed.copy(alpha = 0.1f)
                                else -> Color.Transparent
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                        color = when {
                            isSelected -> Color.White
                            isToday -> PrimaryRed
                            else -> TextPrimary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(item: ScheduleItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Time column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(50.dp)
            ) {
                Text(
                    text = item.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(2.dp)
                        .height(20.dp)
                        .background(AccentBlue.copy(alpha = 0.3f))
                )
                Text(
                    text = item.endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                // Type badge
                Box(
                    modifier = Modifier
                        .background(
                            color = when (item.type) {
                                LessonType.LECTURE -> AccentBlue.copy(alpha = 0.1f)
                                LessonType.PRACTICE -> PrimaryRed.copy(alpha = 0.1f)
                                LessonType.LABORATORY -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                                else -> BackgroundGray
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.type.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (item.type) {
                            LessonType.LECTURE -> AccentBlue
                            LessonType.PRACTICE -> PrimaryRed
                            LessonType.LABORATORY -> Color(0xFF4CAF50)
                            else -> TextSecondary
                        },
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.getLocationString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.teacher,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun EmptyScheduleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Нет занятий",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "В этот день занятия не запланированы",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Mock schedule data - in production would come from university API
 */
private fun getMockScheduleForDate(date: LocalDate, isEvenWeek: Boolean): List<ScheduleItem> {
    // Return empty for weekends
    if (date.dayOfWeek == DayOfWeek.SUNDAY) {
        return emptyList()
    }

    val baseSchedule = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> listOf(
            ScheduleItem("1", "Иностранный язык", LessonType.PRACTICE, "Иванова М.В.", "Корпус 1, Ауд. 205", "1", "205", LocalTime.of(8, 30), LocalTime.of(10, 0), date.dayOfWeek, null),
            ScheduleItem("2", "Высшая математика", LessonType.LECTURE, "Проф. Смирнов А.В.", "Корпус 2, Ауд. 301", "2", "301", LocalTime.of(10, 15), LocalTime.of(11, 45), date.dayOfWeek, null),
            ScheduleItem("3", "Программирование", LessonType.LABORATORY, "Петров И.С.", "Корпус 1, Ауд. 105", "1", "105", LocalTime.of(12, 30), LocalTime.of(14, 0), date.dayOfWeek, true)
        )
        DayOfWeek.TUESDAY -> listOf(
            ScheduleItem("4", "Физика", LessonType.LECTURE, "Доц. Кузнецов В.П.", "Корпус 2, Ауд. 201", "2", "201", LocalTime.of(8, 30), LocalTime.of(10, 0), date.dayOfWeek, null),
            ScheduleItem("5", "Физика", LessonType.LABORATORY, "Асс. Николаев Д.А.", "Корпус 2, Ауд. 115", "2", "115", LocalTime.of(10, 15), LocalTime.of(11, 45), date.dayOfWeek, null)
        )
        DayOfWeek.WEDNESDAY -> listOf(
            ScheduleItem("6", "История России", LessonType.LECTURE, "Проф. Козлова Е.А.", "Корпус 1, Ауд. 401", "1", "401", LocalTime.of(10, 15), LocalTime.of(11, 45), date.dayOfWeek, null),
            ScheduleItem("7", "Базы данных", LessonType.PRACTICE, "Сидорова Н.К.", "Корпус 1, Ауд. 107", "1", "107", LocalTime.of(12, 30), LocalTime.of(14, 0), date.dayOfWeek, null),
            ScheduleItem("8", "Философия", LessonType.SEMINAR, "Доц. Морозов А.И.", "Корпус 1, Ауд. 302", "1", "302", LocalTime.of(14, 15), LocalTime.of(15, 45), date.dayOfWeek, false)
        )
        DayOfWeek.THURSDAY -> listOf(
            ScheduleItem("9", "Высшая математика", LessonType.PRACTICE, "Смирнов А.В.", "Корпус 2, Ауд. 305", "2", "305", LocalTime.of(8, 30), LocalTime.of(10, 0), date.dayOfWeek, null),
            ScheduleItem("10", "Программирование", LessonType.LECTURE, "Проф. Петров И.С.", "Корпус 1, Ауд. 201", "1", "201", LocalTime.of(10, 15), LocalTime.of(11, 45), date.dayOfWeek, null)
        )
        DayOfWeek.FRIDAY -> listOf(
            ScheduleItem("11", "Иностранный язык", LessonType.PRACTICE, "Иванова М.В.", "Корпус 1, Ауд. 205", "1", "205", LocalTime.of(10, 15), LocalTime.of(11, 45), date.dayOfWeek, null),
            ScheduleItem("12", "Физическая культура", LessonType.PRACTICE, "Волков С.А.", "Спорткомплекс", "СК", "1", LocalTime.of(12, 30), LocalTime.of(14, 0), date.dayOfWeek, null)
        )
        DayOfWeek.SATURDAY -> listOf(
            ScheduleItem("13", "Элективные дисциплины", LessonType.LECTURE, "Разные", "Корпус 1", "1", "Разн.", LocalTime.of(10, 15), LocalTime.of(11, 45), date.dayOfWeek, true)
        )
        else -> emptyList()
    }

    return baseSchedule.filter { item ->
        item.isEvenWeek == null || item.isEvenWeek == isEvenWeek
    }
}
