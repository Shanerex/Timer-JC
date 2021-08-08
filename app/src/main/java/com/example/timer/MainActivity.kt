package com.example.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.ui.theme.TimerTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0x101010),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Timer(
                        totalTime = 30000L,
                        handleColor = Color.Yellow,
                        activeBarColor = Color.Yellow,
                        inactiveBarColor = Color.DarkGray,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    activeBarColor: Color,
    inactiveBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var value by remember {
        mutableStateOf(initialValue)
    }

    var currTime by remember {
        mutableStateOf(totalTime)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = currTime, key2 = isTimerRunning) {
        if(currTime > 0 && isTimerRunning) {
            delay(100L)
            currTime -= 100L
            value = currTime / totalTime.toFloat()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Square)//cap = StrokeCap.Round
            )

            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Square)//cap = StrokeCap.Round
            )

            val center = Offset(size.width / 2f, size.height / 2f)
            val angle = (250f * value + 145f) * (PI / 180f) .toFloat()
            val r = size.width / 2f
            val base = cos(angle) * r
            val adj = sin(angle) * r

            drawPoints(
                listOf(Offset(center.x + base, center.y + adj)),
                PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }
        Text(
            text = (currTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Button(
            onClick = {
                      if(currTime <= 0L) {
                          currTime = totalTime
                          isTimerRunning = false
                          value = initialValue
                      } else {
                          isTimerRunning = !isTimerRunning
                      }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if(!isTimerRunning || currTime <= 0L) Color.Yellow else Color.Red
            )
        ) {
            Text(
                text = if (isTimerRunning && currTime > 0L) "Stop"
                else if (!isTimerRunning && currTime >= 0L) "Start"
                else "Reset"
            )
        }
    }
}

