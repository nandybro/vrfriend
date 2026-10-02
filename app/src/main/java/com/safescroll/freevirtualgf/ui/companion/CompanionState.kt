package com.safescroll.freevirtualgf.ui.companion

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class EmotionType {
    NEUTRAL, ANGRY, HAPPY, SMIRK, SURPRISED
}

enum class MouthShape {
    CLOSED, HALF_OPEN, WIDE_OPEN
}

data class CompanionState(
    val emotion: EmotionType = EmotionType.NEUTRAL,
    val mouthShape: MouthShape = MouthShape.CLOSED,
    val isBlinking: Boolean = false
)

/**
 * Helper coroutine function to calculate speech duration and loop through
 * random mouth shapes to simulate real-time lip-sync, while keeping
 * the selected emotion expression locked.
 */
suspend fun MutableState<CompanionState>.playSpeechAnimation(text: String) {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return

    // Approximate 70ms per character for simple lip-sync
    val speechDurationMs = trimmed.length * 70L 
    val endTime = System.currentTimeMillis() + speechDurationMs
    
    while (System.currentTimeMillis() < endTime) {
        val nextShape = if (Random.nextBoolean()) MouthShape.HALF_OPEN else MouthShape.WIDE_OPEN
        this.value = this.value.copy(mouthShape = nextShape)
        // Wait between 100ms to 250ms per mouth shape transition
        delay(Random.nextLong(100, 250)) 
    }
    
    // Always return to closed state after speaking
    this.value = this.value.copy(mouthShape = MouthShape.CLOSED)
}
