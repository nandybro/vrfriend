package com.safescroll.freevirtualgf.ui.companion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

// ---- Confirmed morph target names from this model (Face_Blendshape.Fcl_* convention) ----
private const val MORPH_HAPPY     = "Face_Blendshape.Fcl_ALL_Joy"
private const val MORPH_ANGRY     = "Face_Blendshape.Fcl_ALL_Angry"
private const val MORPH_SAD       = "Face_Blendshape.Fcl_ALL_Sorrow"
private const val MORPH_SURPRISED = "Face_Blendshape.Fcl_ALL_Surprised"
private const val MORPH_NEUTRAL   = "Face_Blendshape.Fcl_ALL_Neutral"
private const val MORPH_FUN       = "Face_Blendshape.Fcl_ALL_Fun"
private const val MORPH_MTH_A     = "Face_Blendshape.Fcl_MTH_A"   // mouth "A" for speech
private const val MORPH_MTH_I     = "Face_Blendshape.Fcl_MTH_I"   // mouth "I"
private const val MORPH_MTH_O     = "Face_Blendshape.Fcl_MTH_O"   // mouth "O"
private const val MORPH_BLINK     = "Face_Blendshape.Fcl_EYE_Close"

// All "ALL_" expression morphs that should be reset when switching expressions
private val ALL_EXPRESSIONS = listOf(
    MORPH_HAPPY, MORPH_ANGRY, MORPH_SAD, MORPH_SURPRISED, MORPH_NEUTRAL, MORPH_FUN
)

@Composable
fun VRMCompanionScreen(modifier: Modifier = Modifier) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    val cameraNode = rememberCameraNode(engine) {
        position = Position(x = 0f, y = 1.0f, z = 2.5f)
        lookAt(Position(x = 0f, y = 0.9f, z = 0f))
    }

    var modelNode by remember { mutableStateOf<ModelNode?>(null) }
    var isModelReady by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F7FA), Color(0xFF80DEEA))
    )

    // Auto-blink loop
    LaunchedEffect(isModelReady) {
        if (!isModelReady) return@LaunchedEffect
        while (true) {
            delay((2000L..5000L).random()) // blink every 2-5 seconds
            setMorph(modelNode, MORPH_BLINK, 1f)
            delay(150L)
            setMorph(modelNode, MORPH_BLINK, 0f)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SceneView(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                modelLoader = modelLoader,
                cameraNode = cameraNode,
            ) {
                val instance = rememberModelInstance(
                    modelLoader = modelLoader,
                    assetFileLocation = "model.vrm"
                )

                instance?.let { mi ->
                    LaunchedEffect(mi) { isModelReady = true }
                    ModelNode(
                        modelInstance = mi,
                        scaleToUnits = 1.8f,
                        centerOrigin = Position(x = 0f, y = -1f, z = 0f),
                        apply = { modelNode = this }
                    )
                }
            }

            if (!isModelReady) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF00796B)
                )
            }
        }

        // ---- Control Panel ----
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isModelReady) "Interact with 3D Companion" else "Loading Model...",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))

                // Row 1: Emotions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EmotionButton("😊 Happy", isModelReady) {
                        setExpression(modelNode, MORPH_HAPPY)
                    }
                    EmotionButton("😠 Angry", isModelReady) {
                        setExpression(modelNode, MORPH_ANGRY)
                    }
                    EmotionButton("😢 Sad", isModelReady) {
                        setExpression(modelNode, MORPH_SAD)
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Row 2: More emotions + speak
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EmotionButton("😲 Surprised", isModelReady) {
                        setExpression(modelNode, MORPH_SURPRISED)
                    }
                    EmotionButton("😐 Neutral", isModelReady) {
                        clearExpression(modelNode)
                    }
                    EmotionButton("💬 Speak", isModelReady) {
                        coroutineScope.launch { speakAnimation(modelNode) }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionButton(label: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.wrapContentWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

// ---- Morph target manipulation ----

/** Set a single morph target by exact name to a given weight (0f..1f). */
private fun setMorph(node: ModelNode?, name: String, weight: Float) {
    node ?: return
    node.renderableNodes.forEach { rn ->
        val names = rn.morphTargetNames
        val idx = names.indexOf(name)
        if (idx >= 0) {
            val current = FloatArray(names.size)
            current[idx] = weight
            rn.setMorphWeights(current)
        }
    }
}

/** Clear all expressions, then apply one. */
private fun setExpression(node: ModelNode?, expressionMorph: String) {
    node ?: return
    // First: zero all expression morphs on all renderable nodes
    node.renderableNodes.forEach { rn ->
        val names = rn.morphTargetNames
        if (names.isEmpty()) return@forEach
        val weights = FloatArray(names.size)
        // Zero out all expression slots
        ALL_EXPRESSIONS.forEach { expr ->
            val idx = names.indexOf(expr)
            if (idx >= 0) weights[idx] = 0f
        }
        rn.setMorphWeights(weights)
    }
    // Then: set the desired expression
    node.renderableNodes.forEach { rn ->
        val names = rn.morphTargetNames
        val idx = names.indexOf(expressionMorph)
        if (idx >= 0) {
            val weights = FloatArray(names.size)
            weights[idx] = 1.0f
            rn.setMorphWeights(weights)
        }
    }
}

/** Reset all expressions to neutral (zero all expression morphs). */
private fun clearExpression(node: ModelNode?) {
    node ?: return
    node.renderableNodes.forEach { rn ->
        val names = rn.morphTargetNames
        if (names.isEmpty()) return@forEach
        val weights = FloatArray(names.size)
        ALL_EXPRESSIONS.forEach { expr ->
            val idx = names.indexOf(expr)
            if (idx >= 0) weights[idx] = 0f
        }
        rn.setMorphWeights(weights)
    }
}

/** Animate mouth open/close for ~3 seconds to simulate speech. */
private suspend fun speakAnimation(node: ModelNode?) {
    node ?: return
    val durationMs = 3000L
    val stepMs = 80L
    val steps = (durationMs / stepMs).toInt()

    repeat(steps) { i ->
        val t = i * stepMs / 1000f
        val v = ((sin(t * 15.0) + 1.0) / 2.0).toFloat()

        node.renderableNodes.forEach { rn ->
            val names = rn.morphTargetNames
            if (names.isEmpty()) return@forEach
            val weights = FloatArray(names.size)

            val aIdx = names.indexOf(MORPH_MTH_A)
            val iIdx = names.indexOf(MORPH_MTH_I)
            val oIdx = names.indexOf(MORPH_MTH_O)
            if (aIdx >= 0) weights[aIdx] = v * 0.8f
            if (iIdx >= 0) weights[iIdx] = (1f - v) * 0.3f
            if (oIdx >= 0) weights[oIdx] = (sin(t * 7.0).toFloat().coerceAtLeast(0f)) * 0.4f
            rn.setMorphWeights(weights)
        }
        delay(stepMs)
    }

    // Close mouth after speaking
    node.renderableNodes.forEach { rn ->
        val names = rn.morphTargetNames
        if (names.isNotEmpty()) rn.setMorphWeights(FloatArray(names.size))
    }
}
