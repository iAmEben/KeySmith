package com.iameben.keysmith.ui.screen.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iameben.keysmith.util.Space
import com.iameben.keysmith.R
import com.iameben.keysmith.data.preferences.AppPreferences
import com.iameben.keysmith.ui.components.LabeledSwitch
import com.iameben.keysmith.ui.components.PasswordIndicator
import com.iameben.keysmith.ui.components.RowStrokedRounded
import com.iameben.keysmith.ui.components.enums.SwitchType
import com.iameben.keysmith.ui.theme.DeepRed
import com.iameben.keysmith.ui.theme.Gold
import com.iameben.keysmith.ui.theme.KeySmithTheme
import com.iameben.keysmith.ui.theme.Orange
import com.iameben.keysmith.ui.theme.Red
import com.iameben.keysmith.ui.theme.RusticOrange
import com.iameben.keysmith.ui.theme.YellowBrown
import java.util.prefs.Preferences


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    themeViewmodel: ThemeViewmodel = hiltViewModel(),
    switchViewmodel: SwitchViewmodel = hiltViewModel()
) {

    var sliderValue by remember { mutableFloatStateOf(12f) }
    val switchStates by switchViewmodel.switchStates.collectAsState()
    val isDarkTheme by themeViewmodel.isDarkTheme.collectAsState()
    val themeIconId = if (isDarkTheme) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
    val copyIconId = if (isDarkTheme) R.drawable.ic_copy_dark else R.drawable.ic_copy_light
    val rotationAngle by animateFloatAsState(targetValue = if (isDarkTheme) 180f else 0f)


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



            Box(modifier.fillMaxWidth()) {

                IconButton(
                    onClick = {themeViewmodel.toggleTheme()},
                    modifier = modifier
                        .wrapContentSize()
                        .padding(top = 18.dp)
                        .align(Alignment.CenterStart)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)

                ) {
                    Icon(
                        painter = painterResource(id = themeIconId),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer(rotationZ = rotationAngle)
                    )
                }


                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = "Saved Passwords",
                    modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }


        }

        Space(size = 34.dp)

        RowStrokedRounded(
            modifier = Modifier.fillMaxWidth(),

        ) {
            Text(
                text = "rgqq341Fgg@^"
            )

            Icon(
                painter = painterResource(id = copyIconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(24.dp)

            )

        }

        Space()

        if (isSystemInDarkTheme()){

            PasswordIndicator(
                dotSize = 16.dp,
                dotColor1 = DeepRed,
                dotColor2 = RusticOrange,
                dotColor3 = Gold

            )

        } else {

            PasswordIndicator(
                dotSize = 16.dp,
                dotColor1 = Red,
                dotColor2 = Orange,
                dotColor3 = YellowBrown

            )

        }


        Space(size = 34.dp)

        Text(
            text = "Password Count:",
            fontSize = 20.sp
        )

        Space()

        RowStrokedRounded(padding = 10.dp, corner = 10.dp) {

            Text(
                text = "9",
                modifier = modifier
                    .padding(start = 8.dp, end = 8.dp)
            )
        }

        Space()


        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 4f..20f,
            steps = 16,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                thumbColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .padding(vertical = 16.dp)
        )

        Space()

        SwitchType.entries.forEach { type ->
            val isChecked by remember { derivedStateOf { switchStates[type] ?: false } }
            LabeledSwitch(
                label = type.name.replace("_", " ").lowercase(),
                checked = isChecked,
                onCheckedChange = {checked ->
                    switchViewmodel.toggleSwitch(type, checked)
                }
            )
        }




        Space(size = 24.dp)

        Button(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {

            }
        ) {
            Text(
                modifier = modifier
                    .padding(8.dp),
                text = "COPY",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

    }

}

