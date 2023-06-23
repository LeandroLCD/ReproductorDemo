package com.leandrolcd.reproductordemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.leandrolcd.reproductordemo.ui.menu.MenuScreen
import com.leandrolcd.reproductordemo.ui.theme.ReproductorDemoTheme

class MainActivity : ComponentActivity() {

    lateinit var reproExoPlayer: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //para xml
        if(setupExoPlayer(getString(R.string.uri_principal))){
            //pintas el reproductor
            }else{
            //error en pantalla
        }
        setContent {
            ReproductorDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //para compose
                    MenuScreen()
                   //VideoPlayer(uri = getString(R.string.uri_principal))
                }
            }
        }
    }
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun  setupExoPlayer(uri: String):Boolean {
        var resp = true
        val context = this
        reproExoPlayer = ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )

                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

                setMediaSource(source)
                prepare()
            }
        reproExoPlayer.playWhenReady = true

        reproExoPlayer.addListener(object : Player.Listener {
            @SuppressLint("SuspiciousIndentation")
            override fun onPlayerError(error: PlaybackException) {
                val currentUri = reproExoPlayer.currentMediaItem?.playbackProperties?.uri.toString()
                if (currentUri == context.getString(R.string.uri_principal)){
                    reproExoPlayer.apply {
                        val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                            context,
                            defaultDataSourceFactory
                        )

                        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(context.getString(R.string.uri_respaldo)))

                        setMediaSource(source)
                        prepare()
                    }
                }else{
                    Log.d("Analisis", "onPlayerError: false")
                    Toast.makeText(context, "Actualmente presentamos fallas tecnicas", Toast.LENGTH_LONG).show()
                    resp = false
                }

            }
        })
        return resp
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(uri: String) {
    val context = LocalContext.current
    val icon = remember {
        mutableStateOf(Icons.Default.Pause)
    }
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )

                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

                setMediaSource(source)
                prepare()
            }


    }

    exoPlayer.playWhenReady = true
   // exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    //exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    exoPlayer.addListener(object : Player.Listener {
        @SuppressLint("SuspiciousIndentation")
        override fun onPlayerError(error: PlaybackException) {
            val currentUri = exoPlayer.currentMediaItem?.playbackProperties?.uri.toString()
                if (currentUri == context.getString(R.string.uri_principal)){
                    exoPlayer.apply {
                        val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                            context,
                            defaultDataSourceFactory
                        )

                        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(context.getString(R.string.uri_respaldo)))

                        setMediaSource(source)
                        prepare()
                    }
                }else{
                    Log.d("Analisis", "onPlayerError: false")
                    Toast.makeText(context, "Actualmente presentamos fallas tecnicas", Toast.LENGTH_LONG).show()
                }

        }
    })

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                 hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }

//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Button(onClick = {
//            if (exoPlayer.isPlaying) {
//                exoPlayer.pause()
//                icon.value = Icons.Default.PlayArrow
//            }else{
//                exoPlayer.play()
//                icon.value = Icons.Default.Pause
//            }
//        }) {
//           Icon(imageVector = icon.value, contentDescription = "Play", tint = Color.Red)
//        }
//    }
}