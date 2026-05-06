package com.tarekbohdima.bakingapp.ui.stepplayer

import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.tarekbohdima.bakingapp.domain.model.Step

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepPlayerScreen(
    onBack: () -> Unit,
    viewModel: StepPlayerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (uiState as? StepPlayerUiState.Success)?.currentStep?.shortDescription ?: ""
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val state = uiState) {
                is StepPlayerUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is StepPlayerUiState.Success -> {
                    Column(Modifier.fillMaxSize()) {
                        StepPlayerContent(
                            step = state.currentStep,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = state.currentStep.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                        )
                        NavigationRow(
                            hasPrevious = state.hasPrevious,
                            hasNext = state.hasNext,
                            onPrevious = viewModel::goToPrevious,
                            onNext = { viewModel.goToNext(state.steps.size) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepPlayerContent(step: Step?, modifier: Modifier = Modifier) {
    when {
        step == null -> Box(modifier.aspectRatio(16f / 9f))
        step.videoURL.isNotBlank() -> VideoPlayer(videoUrl = step.videoURL, modifier = modifier)
        step.thumbnailURL.isNotBlank() -> AsyncImage(
            model = step.thumbnailURL,
            contentDescription = step.shortDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier.aspectRatio(16f / 9f),
        )
        else -> Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.aspectRatio(16f / 9f),
        ) {
            Text("No media for this step", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun VideoPlayer(videoUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val player = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(player) {
        onDispose { player.release() }
    }
    AndroidView(
        factory = {
            PlayerView(it).apply {
                this.player = player
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            }
        },
        update = { it.player = player },
        modifier = modifier.aspectRatio(16f / 9f),
    )
}

@Composable
private fun NavigationRow(
    hasPrevious: Boolean,
    hasNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onPrevious, enabled = hasPrevious) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous step")
        }
        IconButton(onClick = onNext, enabled = hasNext) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next step")
        }
    }
}
