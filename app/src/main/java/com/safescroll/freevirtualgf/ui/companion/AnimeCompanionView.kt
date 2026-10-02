package com.safescroll.freevirtualgf.ui.companion

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AnimeCompanionView(
    modifier: Modifier = Modifier,
    state: CompanionState = CompanionState()
) {
    // 1. Micro-Animation: Infinite breathing float
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breatheOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe_offset"
    )

    // 2. Micro-Animation: Random Blink Ticker
    var isRandomBlinking by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000, 6000))
            isRandomBlinking = true
            delay(150)
            isRandomBlinking = false
        }
    }

    val targetEyeScale = if (isRandomBlinking || state.isBlinking) 0.05f else 1f
    val eyeScaleY by animateFloatAsState(
        targetValue = targetEyeScale,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "blink_scale"
    )

    // Palette & Gradients
    val skinBase = Color(0xFFFFDFBA)
    val skinShadow = Color(0xFFDCA782)
    val hairBase = Color(0xFFFFD700)
    val hairShadow = Color(0xFFD4AF37)
    val jacketOrangeBase = Color(0xFFFF8C00)
    val jacketOrangeShadow = Color(0xFFCC5500)
    val jacketBlueBase = Color(0xFF1E90FF)
    val jacketBlueShadow = Color(0xFF00509E)
    val headbandBlue = Color(0xFF27408B)
    val metalLight = Color(0xFFE0E0E0)
    val metalDark = Color(0xFF9E9E9E)
    val eyeBlue = Color(0xFF00BFFF)
    val eyeDarkBlue = Color(0xFF00008B)

    // 3. Layered Box Composition
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFF80DEEA)),
                    center = Offset.Infinite,
                    radius = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = breatheOffset.dp.toPx()
                },
            contentAlignment = Alignment.Center
        ) {
            
            // Layer 1: Back Hair Layer (Detailed spikes)
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2 - 40.dp.toPx()
                
                val hairGradient = Brush.verticalGradient(
                    colors = listOf(hairBase, hairShadow),
                    startY = centerY - 150.dp.toPx(),
                    endY = centerY + 50.dp.toPx()
                )

                val path = Path().apply {
                    moveTo(centerX - 80.dp.toPx(), centerY + 50.dp.toPx())
                    // Left spikes
                    cubicTo(centerX - 130.dp.toPx(), centerY + 20.dp.toPx(), centerX - 140.dp.toPx(), centerY - 20.dp.toPx(), centerX - 120.dp.toPx(), centerY - 40.dp.toPx())
                    cubicTo(centerX - 150.dp.toPx(), centerY - 60.dp.toPx(), centerX - 130.dp.toPx(), centerY - 100.dp.toPx(), centerX - 90.dp.toPx(), centerY - 90.dp.toPx())
                    cubicTo(centerX - 110.dp.toPx(), centerY - 140.dp.toPx(), centerX - 60.dp.toPx(), centerY - 150.dp.toPx(), centerX - 40.dp.toPx(), centerY - 110.dp.toPx())
                    // Top spikes
                    cubicTo(centerX - 40.dp.toPx(), centerY - 160.dp.toPx(), centerX + 20.dp.toPx(), centerY - 170.dp.toPx(), centerX + 30.dp.toPx(), centerY - 120.dp.toPx())
                    cubicTo(centerX + 60.dp.toPx(), centerY - 160.dp.toPx(), centerX + 110.dp.toPx(), centerY - 130.dp.toPx(), centerX + 80.dp.toPx(), centerY - 90.dp.toPx())
                    // Right spikes
                    cubicTo(centerX + 130.dp.toPx(), centerY - 100.dp.toPx(), centerX + 150.dp.toPx(), centerY - 60.dp.toPx(), centerX + 110.dp.toPx(), centerY - 40.dp.toPx())
                    cubicTo(centerX + 140.dp.toPx(), centerY - 20.dp.toPx(), centerX + 130.dp.toPx(), centerY + 20.dp.toPx(), centerX + 80.dp.toPx(), centerY + 50.dp.toPx())
                    close()
                }
                drawPath(path = path, brush = hairGradient, style = Fill)
                drawPath(path = path, color = Color.Black.copy(alpha = 0.8f), style = Stroke(width = 4f))
            }

            // Layer 2: Torso/Body layer
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2 + 100.dp.toPx()
                
                val jacketGradient = Brush.verticalGradient(
                    colors = listOf(jacketOrangeBase, jacketOrangeShadow),
                    startY = centerY - 50.dp.toPx(),
                    endY = size.height
                )

                // Body
                val bodyPath = Path().apply {
                    moveTo(centerX - 140.dp.toPx(), size.height)
                    cubicTo(centerX - 120.dp.toPx(), centerY + 20.dp.toPx(), centerX - 80.dp.toPx(), centerY - 10.dp.toPx(), centerX, centerY - 20.dp.toPx())
                    cubicTo(centerX + 80.dp.toPx(), centerY - 10.dp.toPx(), centerX + 120.dp.toPx(), centerY + 20.dp.toPx(), centerX + 140.dp.toPx(), size.height)
                    close()
                }
                drawPath(path = bodyPath, brush = jacketGradient, style = Fill)
                drawPath(path = bodyPath, color = Color.Black, style = Stroke(width = 5f))

                // Collar
                val collarGradient = Brush.verticalGradient(
                    colors = listOf(jacketBlueBase, jacketBlueShadow),
                    startY = centerY - 40.dp.toPx(),
                    endY = centerY + 30.dp.toPx()
                )
                val collarPath = Path().apply {
                    moveTo(centerX - 65.dp.toPx(), centerY + 30.dp.toPx())
                    cubicTo(centerX - 75.dp.toPx(), centerY, centerX - 60.dp.toPx(), centerY - 40.dp.toPx(), centerX, centerY - 30.dp.toPx())
                    cubicTo(centerX + 60.dp.toPx(), centerY - 40.dp.toPx(), centerX + 75.dp.toPx(), centerY, centerX + 65.dp.toPx(), centerY + 30.dp.toPx())
                    close()
                }
                drawPath(path = collarPath, brush = collarGradient, style = Fill)
                drawPath(path = collarPath, color = Color.Black, style = Stroke(width = 5f))
            }

            // Layer 3: Head and neck base layer
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val headCenterY = size.height / 2 - 20.dp.toPx()
                
                // Neck Shadow & Neck
                val neckGradient = Brush.verticalGradient(
                    colors = listOf(skinShadow, skinBase),
                    startY = headCenterY + 30.dp.toPx(),
                    endY = headCenterY + 80.dp.toPx()
                )
                val neckPath = Path().apply {
                    moveTo(centerX - 22.dp.toPx(), headCenterY + 40.dp.toPx())
                    lineTo(centerX + 22.dp.toPx(), headCenterY + 40.dp.toPx())
                    lineTo(centerX + 30.dp.toPx(), headCenterY + 90.dp.toPx())
                    lineTo(centerX - 30.dp.toPx(), headCenterY + 90.dp.toPx())
                    close()
                }
                drawPath(path = neckPath, brush = neckGradient, style = Fill)
                drawPath(path = neckPath, color = Color.Black, style = Stroke(width = 4f))

                // Face (Anatomically improved chin & cheeks)
                val faceGradient = Brush.radialGradient(
                    colors = listOf(skinBase, skinShadow.copy(alpha = 0.5f)),
                    center = Offset(centerX, headCenterY - 10.dp.toPx()),
                    radius = 200f
                )
                
                val facePath = Path().apply {
                    moveTo(centerX - 65.dp.toPx(), headCenterY - 40.dp.toPx()) // Left ear top
                    // Left cheek to chin
                    cubicTo(
                        centerX - 65.dp.toPx(), headCenterY + 30.dp.toPx(),
                        centerX - 45.dp.toPx(), headCenterY + 70.dp.toPx(),
                        centerX, headCenterY + 85.dp.toPx() // Chin point
                    )
                    // Chin to right cheek
                    cubicTo(
                        centerX + 45.dp.toPx(), headCenterY + 70.dp.toPx(),
                        centerX + 65.dp.toPx(), headCenterY + 30.dp.toPx(),
                        centerX + 65.dp.toPx(), headCenterY - 40.dp.toPx() // Right ear top
                    )
                    // Top head (hidden behind headband)
                    cubicTo(
                        centerX + 60.dp.toPx(), headCenterY - 100.dp.toPx(),
                        centerX - 60.dp.toPx(), headCenterY - 100.dp.toPx(),
                        centerX - 65.dp.toPx(), headCenterY - 40.dp.toPx()
                    )
                }
                drawPath(path = facePath, brush = faceGradient, style = Fill)
                drawPath(path = facePath, color = Color.Black, style = Stroke(width = 4f))
            }

            // Layer 4: Headband layer
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val headCenterY = size.height / 2 - 20.dp.toPx()
                val bandCenterY = headCenterY - 45.dp.toPx()
                
                // Cloth Band
                val bandPath = Path().apply {
                    moveTo(centerX - 68.dp.toPx(), bandCenterY - 20.dp.toPx())
                    cubicTo(centerX - 30.dp.toPx(), bandCenterY - 30.dp.toPx(), centerX + 30.dp.toPx(), bandCenterY - 30.dp.toPx(), centerX + 68.dp.toPx(), bandCenterY - 20.dp.toPx())
                    lineTo(centerX + 70.dp.toPx(), bandCenterY + 15.dp.toPx())
                    cubicTo(centerX + 30.dp.toPx(), bandCenterY + 5.dp.toPx(), centerX - 30.dp.toPx(), bandCenterY + 5.dp.toPx(), centerX - 70.dp.toPx(), bandCenterY + 15.dp.toPx())
                    close()
                }
                drawPath(path = bandPath, color = headbandBlue, style = Fill)
                // Band shadow
                drawPath(path = bandPath, color = Color.Black.copy(alpha = 0.3f), style = Fill)
                drawPath(path = bandPath, color = Color.Black, style = Stroke(width = 4f))

                // Metal Plate with Shine
                val plateWidth = 80.dp.toPx()
                val plateHeight = 28.dp.toPx()
                val metalGradient = Brush.linearGradient(
                    colors = listOf(metalDark, metalLight, metalLight, metalDark),
                    start = Offset(centerX - plateWidth/2, bandCenterY - plateHeight/2),
                    end = Offset(centerX + plateWidth/2, bandCenterY + plateHeight/2)
                )
                
                val plateRectTopLeft = Offset(centerX - plateWidth/2, bandCenterY - plateHeight/2 - 2.dp.toPx())
                drawRoundRect(
                    brush = metalGradient,
                    topLeft = plateRectTopLeft,
                    size = Size(plateWidth, plateHeight),
                    cornerRadius = CornerRadius(12f, 12f)
                )
                
                // Reflection highlight
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.4f),
                    topLeft = Offset(plateRectTopLeft.x + 4.dp.toPx(), plateRectTopLeft.y + 2.dp.toPx()),
                    size = Size(plateWidth - 8.dp.toPx(), plateHeight / 2.5f),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                drawRoundRect(
                    color = Color.Black,
                    topLeft = plateRectTopLeft,
                    size = Size(plateWidth, plateHeight),
                    cornerRadius = CornerRadius(12f, 12f),
                    style = Stroke(width = 3f)
                )
                
                // Leaf Symbol (Engraved)
                val symbolCenter = Offset(centerX, bandCenterY - 2.dp.toPx())
                val symbolPath = Path().apply {
                    moveTo(symbolCenter.x - 8.dp.toPx(), symbolCenter.y)
                    cubicTo(symbolCenter.x - 8.dp.toPx(), symbolCenter.y - 12.dp.toPx(), symbolCenter.x + 8.dp.toPx(), symbolCenter.y - 12.dp.toPx(), symbolCenter.x + 10.dp.toPx(), symbolCenter.y - 4.dp.toPx())
                    cubicTo(symbolCenter.x + 8.dp.toPx(), symbolCenter.y + 4.dp.toPx(), symbolCenter.x, symbolCenter.y + 6.dp.toPx(), symbolCenter.x - 4.dp.toPx(), symbolCenter.y + 4.dp.toPx())
                }
                drawPath(path = symbolPath, color = Color.Black.copy(alpha = 0.7f), style = Stroke(width = 3f))
                
                // Rivets
                drawCircle(Color.DarkGray, radius = 3.dp.toPx(), center = Offset(plateRectTopLeft.x + 10.dp.toPx(), symbolCenter.y))
                drawCircle(Color.DarkGray, radius = 3.dp.toPx(), center = Offset(plateRectTopLeft.x + plateWidth - 10.dp.toPx(), symbolCenter.y))
            }

            // Layer 5: Emotion-specific eyes and eyebrows layer
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val eyeCenterY = size.height / 2 - 5.dp.toPx()
                
                val eyeWidth = 28.dp.toPx()
                val eyeHeight = 32.dp.toPx() * eyeScaleY
                val eyeOffsetX = 35.dp.toPx()

                val leftEyeCenter = Offset(centerX - eyeOffsetX, eyeCenterY)
                val rightEyeCenter = Offset(centerX + eyeOffsetX, eyeCenterY)
                
                if (eyeScaleY > 0.2f) {
                    // Sclera
                    drawOval(color = Color.White, topLeft = Offset(leftEyeCenter.x - eyeWidth/2, leftEyeCenter.y - eyeHeight/2), size = Size(eyeWidth, eyeHeight))
                    drawOval(color = Color.White, topLeft = Offset(rightEyeCenter.x - eyeWidth/2, rightEyeCenter.y - eyeHeight/2), size = Size(eyeWidth, eyeHeight))
                    
                    // Iris with gradient
                    val irisRadius = (eyeWidth / 2.2f).coerceAtLeast(1f)
                    val irisGradient = Brush.radialGradient(listOf(eyeBlue, eyeDarkBlue), center = leftEyeCenter, radius = irisRadius)
                    val rightIrisGradient = Brush.radialGradient(listOf(eyeBlue, eyeDarkBlue), center = rightEyeCenter, radius = irisRadius)
                    
                    drawCircle(brush = irisGradient, center = leftEyeCenter, radius = irisRadius)
                    drawCircle(brush = rightIrisGradient, center = rightEyeCenter, radius = irisRadius)
                    
                    // Pupil
                    drawCircle(color = Color.Black, center = leftEyeCenter, radius = irisRadius * 0.4f)
                    drawCircle(color = Color.Black, center = rightEyeCenter, radius = irisRadius * 0.4f)

                    // Eye highlights (Catchlights)
                    drawCircle(color = Color.White, center = Offset(leftEyeCenter.x - 3.dp.toPx(), leftEyeCenter.y - 4.dp.toPx()), radius = irisRadius * 0.3f)
                    drawCircle(color = Color.White, center = Offset(rightEyeCenter.x - 3.dp.toPx(), rightEyeCenter.y - 4.dp.toPx()), radius = irisRadius * 0.3f)
                }

                // Eyelashes (Thicker on top)
                val leftLash = Path().apply {
                    moveTo(leftEyeCenter.x - eyeWidth/2 - 2.dp.toPx(), leftEyeCenter.y)
                    cubicTo(leftEyeCenter.x - eyeWidth/2, leftEyeCenter.y - eyeHeight/2 - 5.dp.toPx(), leftEyeCenter.x + eyeWidth/2, leftEyeCenter.y - eyeHeight/2 - 5.dp.toPx(), leftEyeCenter.x + eyeWidth/2 + 2.dp.toPx(), leftEyeCenter.y + 2.dp.toPx())
                }
                drawPath(leftLash, Color.Black, style = Stroke(width = 6f, cap = Stroke.DefaultCap))
                
                val rightLash = Path().apply {
                    moveTo(rightEyeCenter.x - eyeWidth/2 - 2.dp.toPx(), rightEyeCenter.y + 2.dp.toPx())
                    cubicTo(rightEyeCenter.x - eyeWidth/2, rightEyeCenter.y - eyeHeight/2 - 5.dp.toPx(), rightEyeCenter.x + eyeWidth/2, rightEyeCenter.y - eyeHeight/2 - 5.dp.toPx(), rightEyeCenter.x + eyeWidth/2 + 2.dp.toPx(), rightEyeCenter.y)
                }
                drawPath(rightLash, Color.Black, style = Stroke(width = 6f, cap = Stroke.DefaultCap))
                
                // Lower eye line
                drawOval(color = Color.Black, topLeft = Offset(leftEyeCenter.x - eyeWidth/2, leftEyeCenter.y - eyeHeight/2), size = Size(eyeWidth, eyeHeight), style = Stroke(width = 2f))
                drawOval(color = Color.Black, topLeft = Offset(rightEyeCenter.x - eyeWidth/2, rightEyeCenter.y - eyeHeight/2), size = Size(eyeWidth, eyeHeight), style = Stroke(width = 2f))

                // Eyebrows based on Emotion
                val browCenterY = eyeCenterY - 25.dp.toPx()
                val browWidth = 34.dp.toPx()
                
                when (state.emotion) {
                    EmotionType.NEUTRAL -> {
                        drawLine(Color.Black, Offset(leftEyeCenter.x - browWidth/2, browCenterY), Offset(leftEyeCenter.x + browWidth/2, browCenterY + 2.dp.toPx()), strokeWidth = 5f, cap = Stroke.DefaultCap)
                        drawLine(Color.Black, Offset(rightEyeCenter.x - browWidth/2, browCenterY + 2.dp.toPx()), Offset(rightEyeCenter.x + browWidth/2, browCenterY), strokeWidth = 5f, cap = Stroke.DefaultCap)
                    }
                    EmotionType.ANGRY -> {
                        drawLine(Color.Black, Offset(leftEyeCenter.x - browWidth/2, browCenterY - 8.dp.toPx()), Offset(leftEyeCenter.x + browWidth/2 + 5.dp.toPx(), browCenterY + 12.dp.toPx()), strokeWidth = 6f)
                        drawLine(Color.Black, Offset(rightEyeCenter.x - browWidth/2 - 5.dp.toPx(), browCenterY + 12.dp.toPx()), Offset(rightEyeCenter.x + browWidth/2, browCenterY - 8.dp.toPx()), strokeWidth = 6f)
                    }
                    EmotionType.HAPPY -> {
                        val leftBrowPath = Path().apply {
                            moveTo(leftEyeCenter.x - browWidth/2, browCenterY + 5.dp.toPx())
                            quadraticTo(leftEyeCenter.x, browCenterY - 15.dp.toPx(), leftEyeCenter.x + browWidth/2, browCenterY + 5.dp.toPx())
                        }
                        drawPath(leftBrowPath, Color.Black, style = Stroke(width = 5f))
                        val rightBrowPath = Path().apply {
                            moveTo(rightEyeCenter.x - browWidth/2, browCenterY + 5.dp.toPx())
                            quadraticTo(rightEyeCenter.x, browCenterY - 15.dp.toPx(), rightEyeCenter.x + browWidth/2, browCenterY + 5.dp.toPx())
                        }
                        drawPath(rightBrowPath, Color.Black, style = Stroke(width = 5f))
                    }
                    EmotionType.SMIRK -> {
                        drawLine(Color.Black, Offset(leftEyeCenter.x - browWidth/2, browCenterY), Offset(leftEyeCenter.x + browWidth/2, browCenterY - 15.dp.toPx()), strokeWidth = 5f)
                        drawLine(Color.Black, Offset(rightEyeCenter.x - browWidth/2, browCenterY + 5.dp.toPx()), Offset(rightEyeCenter.x + browWidth/2, browCenterY + 2.dp.toPx()), strokeWidth = 5f)
                    }
                    EmotionType.SURPRISED -> {
                        drawLine(Color.Black, Offset(leftEyeCenter.x - browWidth/2, browCenterY - 15.dp.toPx()), Offset(leftEyeCenter.x + browWidth/2, browCenterY - 15.dp.toPx()), strokeWidth = 5f)
                        drawLine(Color.Black, Offset(rightEyeCenter.x - browWidth/2, browCenterY - 15.dp.toPx()), Offset(rightEyeCenter.x + browWidth/2, browCenterY - 15.dp.toPx()), strokeWidth = 5f)
                    }
                }
            }

            // Layer 6: Dynamic mouth layer
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val mouthCenterY = size.height / 2 + 45.dp.toPx()
                val mouthColor = Color(0xFF8B0000)
                val tongueColor = Color(0xFFE57373)
                
                when (state.mouthShape) {
                    MouthShape.CLOSED -> {
                        val mouthPath = Path().apply {
                            val startX = centerX - 12.dp.toPx()
                            val endX = centerX + 12.dp.toPx()
                            when (state.emotion) {
                                EmotionType.HAPPY -> {
                                    moveTo(startX, mouthCenterY - 2.dp.toPx())
                                    quadraticTo(centerX, mouthCenterY + 8.dp.toPx(), endX, mouthCenterY - 2.dp.toPx())
                                }
                                EmotionType.ANGRY -> {
                                    moveTo(startX, mouthCenterY + 5.dp.toPx())
                                    quadraticTo(centerX, mouthCenterY - 2.dp.toPx(), endX, mouthCenterY + 5.dp.toPx())
                                }
                                EmotionType.SMIRK -> {
                                    moveTo(startX, mouthCenterY)
                                    quadraticTo(centerX + 8.dp.toPx(), mouthCenterY, endX + 4.dp.toPx(), mouthCenterY - 8.dp.toPx())
                                }
                                else -> {
                                    moveTo(startX, mouthCenterY)
                                    quadraticTo(centerX, mouthCenterY + 2.dp.toPx(), endX, mouthCenterY)
                                }
                            }
                        }
                        drawPath(mouthPath, Color.Black, style = Stroke(width = 3f, cap = Stroke.DefaultCap))
                    }
                    MouthShape.HALF_OPEN -> {
                        val path = Path().apply {
                            moveTo(centerX - 10.dp.toPx(), mouthCenterY - 2.dp.toPx())
                            cubicTo(centerX - 5.dp.toPx(), mouthCenterY + 10.dp.toPx(), centerX + 5.dp.toPx(), mouthCenterY + 10.dp.toPx(), centerX + 10.dp.toPx(), mouthCenterY - 2.dp.toPx())
                            cubicTo(centerX + 5.dp.toPx(), mouthCenterY - 4.dp.toPx(), centerX - 5.dp.toPx(), mouthCenterY - 4.dp.toPx(), centerX - 10.dp.toPx(), mouthCenterY - 2.dp.toPx())
                            close()
                        }
                        drawPath(path, mouthColor, style = Fill)
                        drawPath(path, Color.Black, style = Stroke(width = 3f))
                    }
                    MouthShape.WIDE_OPEN -> {
                        val wideMouthPath = Path().apply {
                            moveTo(centerX - 16.dp.toPx(), mouthCenterY - 2.dp.toPx())
                            cubicTo(centerX - 12.dp.toPx(), mouthCenterY + 35.dp.toPx(), centerX + 12.dp.toPx(), mouthCenterY + 35.dp.toPx(), centerX + 16.dp.toPx(), mouthCenterY - 2.dp.toPx())
                            lineTo(centerX - 16.dp.toPx(), mouthCenterY - 2.dp.toPx())
                            close()
                        }
                        drawPath(wideMouthPath, mouthColor, style = Fill)
                        
                        // Tongue
                        val tonguePath = Path().apply {
                            moveTo(centerX - 10.dp.toPx(), mouthCenterY + 18.dp.toPx())
                            cubicTo(centerX - 5.dp.toPx(), mouthCenterY + 10.dp.toPx(), centerX + 5.dp.toPx(), mouthCenterY + 10.dp.toPx(), centerX + 10.dp.toPx(), mouthCenterY + 18.dp.toPx())
                            cubicTo(centerX + 12.dp.toPx(), mouthCenterY + 28.dp.toPx(), centerX - 12.dp.toPx(), mouthCenterY + 28.dp.toPx(), centerX - 10.dp.toPx(), mouthCenterY + 18.dp.toPx())
                        }
                        drawPath(tonguePath, tongueColor, style = Fill)
                        drawPath(wideMouthPath, Color.Black, style = Stroke(width = 3f))
                        
                        // Teeth
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(centerX - 14.dp.toPx(), mouthCenterY - 2.dp.toPx()),
                            size = Size(28.dp.toPx(), 6.dp.toPx())
                        )
                        drawLine(Color.Black, Offset(centerX - 14.dp.toPx(), mouthCenterY + 4.dp.toPx()), Offset(centerX + 14.dp.toPx(), mouthCenterY + 4.dp.toPx()), strokeWidth = 2f)
                    }
                }
            }

            // Layer 7: Front hair / bangs layer
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2 - 20.dp.toPx()
                val bandCenterY = centerY - 45.dp.toPx()
                
                val hairGradient = Brush.verticalGradient(
                    colors = listOf(hairBase, hairShadow),
                    startY = bandCenterY - 40.dp.toPx(),
                    endY = centerY + 30.dp.toPx()
                )

                val path = Path().apply {
                    // Left bang
                    moveTo(centerX - 80.dp.toPx(), bandCenterY - 10.dp.toPx())
                    cubicTo(centerX - 95.dp.toPx(), centerY + 10.dp.toPx(), centerX - 70.dp.toPx(), centerY + 40.dp.toPx(), centerX - 65.dp.toPx(), centerY + 50.dp.toPx())
                    cubicTo(centerX - 60.dp.toPx(), centerY + 20.dp.toPx(), centerX - 40.dp.toPx(), bandCenterY, centerX - 50.dp.toPx(), bandCenterY - 10.dp.toPx())
                    
                    // Middle-left bang
                    cubicTo(centerX - 50.dp.toPx(), centerY, centerX - 40.dp.toPx(), centerY + 30.dp.toPx(), centerX - 30.dp.toPx(), centerY + 40.dp.toPx())
                    cubicTo(centerX - 25.dp.toPx(), centerY + 10.dp.toPx(), centerX - 10.dp.toPx(), bandCenterY - 5.dp.toPx(), centerX - 15.dp.toPx(), bandCenterY - 15.dp.toPx())

                    // Center bang
                    cubicTo(centerX - 10.dp.toPx(), centerY + 10.dp.toPx(), centerX, centerY + 35.dp.toPx(), centerX + 5.dp.toPx(), centerY + 30.dp.toPx())
                    cubicTo(centerX + 5.dp.toPx(), centerY + 5.dp.toPx(), centerX + 20.dp.toPx(), bandCenterY - 5.dp.toPx(), centerX + 15.dp.toPx(), bandCenterY - 15.dp.toPx())

                    // Middle-right bang
                    cubicTo(centerX + 25.dp.toPx(), centerY, centerX + 35.dp.toPx(), centerY + 30.dp.toPx(), centerX + 45.dp.toPx(), centerY + 35.dp.toPx())
                    cubicTo(centerX + 40.dp.toPx(), centerY + 10.dp.toPx(), centerX + 50.dp.toPx(), bandCenterY, centerX + 45.dp.toPx(), bandCenterY - 10.dp.toPx())

                    // Right bang
                    cubicTo(centerX + 60.dp.toPx(), centerY + 20.dp.toPx(), centerX + 80.dp.toPx(), centerY + 40.dp.toPx(), centerX + 75.dp.toPx(), centerY + 50.dp.toPx())
                    cubicTo(centerX + 85.dp.toPx(), centerY + 10.dp.toPx(), centerX + 70.dp.toPx(), bandCenterY - 10.dp.toPx(), centerX + 80.dp.toPx(), bandCenterY - 20.dp.toPx())
                    
                    // Top curve
                    cubicTo(centerX + 40.dp.toPx(), bandCenterY - 50.dp.toPx(), centerX - 40.dp.toPx(), bandCenterY - 50.dp.toPx(), centerX - 80.dp.toPx(), bandCenterY - 10.dp.toPx())
                }
                // Hair shadow for depth
                drawPath(path = path, color = Color.Black.copy(alpha = 0.2f), style = Fill)
                drawPath(path = path, brush = hairGradient, style = Fill)
                drawPath(path = path, color = Color.Black, style = Stroke(width = 4f))
            }
        }
    }
}
