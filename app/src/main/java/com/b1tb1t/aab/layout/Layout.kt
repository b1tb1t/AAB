package com.b1tb1t.aab.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.b1tb1t.aab.ui.theme.AdbTheme


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box {
        Text(
            text = "Hello $name!",
            modifier = modifier,
        )

    }
    Text(
        text = "Hello $name!",
        modifier = modifier
    )


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdbTheme {
        Greeting("Android")
    }
}