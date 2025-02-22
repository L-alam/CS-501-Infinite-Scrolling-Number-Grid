package com.example.infinitescrollingnumbergrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.infinitescrollingnumbergrid.ui.theme.InfiniteScrollingNumberGridTheme
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfiniteScrollingNumberGridTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InfiniteScrollingNumberGrid()
                }
            }
        }
    }
}

@Composable
fun InfiniteScrollingNumberGrid() {
    var numbers by remember { mutableStateOf((1..30).toList()) }
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    InfiniteGridWithEffect(gridState) {
        coroutineScope.launch {
            delay(1000) // Simulate loading delay
            numbers = numbers + (numbers.size + 1..numbers.size + 30).toList()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { numbers = (1..30).toList() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Reset to 1-30")
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(numbers) { number ->
                Box(
                    modifier = Modifier
                        .padding(4.dp) // Padding around each square
                        .aspectRatio(1f) // Ensures squares
                        .background(Color.Black), // Black background
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White // White numbers
                    )
                }
            }
        }
    }
}

@Composable
fun InfiniteGridWithEffect(gridState: LazyGridState, onLoadMore: () -> Unit) {
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() &&
                    visibleItems.last().index == gridState.layoutInfo.totalItemsCount - 1) {
                    onLoadMore()
                }
            }
    }
}