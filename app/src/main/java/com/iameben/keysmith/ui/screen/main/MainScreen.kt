package com.iameben.keysmith.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iameben.keysmith.R
import com.iameben.keysmith.ui.components.CircularIcon
import com.iameben.keysmith.ui.components.RowStrokedRounded


@Composable
@Preview
fun MainScreen(
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircularIcon(
                R.drawable.ic_dark_mode,
                "dark mode icon",

            )

            Icon(
                Icons.Default.Menu,
                contentDescription = "light mode icon"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        RowStrokedRounded(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "rgqq341Fgg@^"
            )

            Icon(
                Icons.Default.Menu,
                contentDescription = "light mode icon"
            )

        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Password Count:",
            fontSize = 20.sp
        )

        RowStrokedRounded(
            modifier = Modifier.padding(top = 8.dp)
        ) {

            Text(
                text = "9",
                modifier = modifier
                    .padding(start = 8.dp, end = 8.dp)
            )
        }
    }

}