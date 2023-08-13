package com.daumantas.clockutie.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daumantas.clockutie.ui.theme.ClockutieTheme
import com.daumantas.clockutie.utils.toRadians
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration.Companion.seconds

@Composable
fun Clock(currentHours: Int, currentMins: Int, currentSecs: Int) {
    var secs by remember { mutableStateOf(currentSecs) }
    var mins by remember { mutableStateOf(currentMins) }
    var hours by remember { mutableStateOf(currentHours) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            secs++
            secs %= 60
            if (secs == 0) {
                mins++
                mins %= 60
            }
            if (mins == 0 && secs == 0) {
                hours++
                hours %= 12
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val boxWithConstrainsScope = this

        val background = MaterialTheme.colorScheme.background
        val onBackground = MaterialTheme.colorScheme.onBackground

        Canvas(modifier = Modifier) {
            val center = Offset(
                x = boxWithConstrainsScope.maxWidth.toPx() / 2,
                y = boxWithConstrainsScope.maxHeight.toPx() / 2,
            )
            val radius = boxWithConstrainsScope.maxWidth.toPx() / 2 * 2 / 3
            val shadow = onBackground.copy(alpha = 0.8f)

            drawClockContainer(
                radius = radius,
                center = center,
                background = background.toArgb(),
                shadow = shadow.toArgb(),
            )
            drawTimeIndicators(radius = radius, center = center, color = onBackground)

            drawHourArrow(center = center, hours = hours, mins = mins, color = onBackground)
            drawMinutesArrow(center = center, mins = mins, secs = secs, color = onBackground)
            drawSecondsArrow(center = center, secs = secs)
        }
    }
}

private fun DrawScope.drawClockContainer(
    radius: Float,
    center: Offset,
    background: Int,
    shadow: Int,
) {
    drawContext.canvas.nativeCanvas.apply {
        drawCircle(
            center.x,
            center.y,
            radius,
            Paint().apply {
                strokeWidth = 3.dp.toPx()
                color = background
                style = Paint.Style.FILL
                setShadowLayer(150f, 0f, 0f, shadow)
            }
        )
    }
}

private fun DrawScope.drawTimeIndicators(
    radius: Float,
    center: Offset,
    color: Color,
) {
    var strokeWidth: Float
    var lineHeight: Float
    for (i in 0..360 step 6) {
        if (i % 30 == 0) {
            lineHeight = 60f
            strokeWidth = 2.dp.toPx()
        } else {
            lineHeight = 40f
            strokeWidth = Stroke.HairlineWidth
        }

        val angle = i.toFloat() * (PI / 180)
        val startX = center.x + (radius - 12f) * cos(angle).toFloat()
        val startY = center.y + (radius - 12f) * sin(angle).toFloat()
        val endX = center.x + (radius - lineHeight) * cos(angle).toFloat()
        val endY = center.y + (radius - lineHeight) * sin(angle).toFloat()
        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawHourArrow(center: Offset, hours: Int, mins: Int, color: Color) {
    val hour = (((hours + mins / 60f) * 30) - 90F).toRadians()
    val startX = center.x
    val startY = center.y
    val endX = center.x + 120f * cos(hour)
    val endY = center.y + 120f * sin(hour)
    drawLine(
        color = color,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = 7.dp.toPx(),
        cap = StrokeCap.Round,
    )
}

private fun DrawScope.drawMinutesArrow(center: Offset, mins: Int, secs: Int, color: Color) {
    val minutesAngle = (((mins + secs / 60f) * 6f) - 90f).toRadians()
    val minutesStartX = center.x
    val minutesStartY = center.y
    val minutesEndX = center.x + 200f * cos(minutesAngle)
    val minutesSEndY = center.y + 200f * sin(minutesAngle)
    drawLine(
        color = color,
        start = Offset(minutesStartX, minutesStartY),
        end = Offset(minutesEndX, minutesSEndY),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round,
    )
}

private fun DrawScope.drawSecondsArrow(center: Offset, secs: Int) {
    val secondsAngle = ((secs * 6) - 90F).toRadians()
    val secondsStartX = center.x
    val secondsStartY = center.y
    val secondsEndX = center.x + 260f * cos(secondsAngle)
    val secondsEndY = center.y + 260f * sin(secondsAngle)
    drawLine(
        color = Color.Red,
        start = Offset(secondsStartX, secondsStartY),
        end = Offset(secondsEndX, secondsEndY),
        strokeWidth = 1.dp.toPx(),
        cap = StrokeCap.Round,
    )
}

@Preview()
@Composable
fun ClockDarkPreview() {
    ClockutieTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Clock(2, 33, 21)
        }
    }
}

@Preview()
@Composable
fun ClockLightPreview() {
    ClockutieTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Clock(2, 33, 21)
        }
    }
}