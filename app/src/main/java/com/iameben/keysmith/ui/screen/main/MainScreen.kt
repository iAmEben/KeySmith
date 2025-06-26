package com.iameben.keysmith.ui.screen.main


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iameben.keysmith.util.Space
import com.iameben.keysmith.R
import com.iameben.keysmith.ui.components.CustomSnackBarHost
import com.iameben.keysmith.ui.components.LabeledSwitch
import com.iameben.keysmith.ui.components.ModeButton
import com.iameben.keysmith.ui.components.PasswordStrengthIndicator
import com.iameben.keysmith.ui.components.RowStrokedRounded
import com.iameben.keysmith.ui.components.enums.ModeSelector
import com.iameben.keysmith.ui.components.enums.SwitchType


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    themeViewmodel: ThemeViewmodel = hiltViewModel(),
    mainScreenViewmodel: MainScreenViewmodel = hiltViewModel()
) {


    val switchStates by mainScreenViewmodel.switchStates.collectAsState()
    val sliderValue by mainScreenViewmodel.sliderValue.collectAsState()
    val selectMode by mainScreenViewmodel.selectMode.collectAsState()
    val generatedPassword by mainScreenViewmodel.generatedPassword.collectAsState()
    val passwordStrength by mainScreenViewmodel.passwordStrength.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val isDarkTheme by themeViewmodel.isDarkTheme.collectAsState()
    val themeIconId = if (isDarkTheme) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
    val copyIconId = if (isDarkTheme) R.drawable.ic_copy_dark else R.drawable.ic_copy_light
    val rotationAngle by animateFloatAsState(targetValue = if (isDarkTheme) 180f else 0f)
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackBarHostState) {
        if (snackBarHostState != mainScreenViewmodel.snackBarHostState.value){
            mainScreenViewmodel.setSnackBarHotState(snackBarHostState)
        }

    }

    Scaffold(
        snackbarHost = { CustomSnackBarHost(hostState = snackBarHostState) },
        modifier = modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(paddingValues)
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
                    text = generatedPassword,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
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


            PasswordStrengthIndicator(
                strength = passwordStrength,
                isDarkTheme = isDarkTheme
            )

            Space(size = 34.dp)

            Text(
                text = "Password Count:",
                fontSize = 20.sp
            )

            Space()

            RowStrokedRounded(padding = 10.dp, corner = 10.dp) {

                Text(
                    text = "$sliderValue",
                    modifier = modifier
                        .padding(start = 8.dp, end = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Space()


            Slider(
                value = sliderValue.toFloat(),
                onValueChange = { mainScreenViewmodel.setSliderValue(it.toInt()) },
                valueRange = 4f..21f,
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

            val modes = ModeSelector.entries.toTypedArray()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                modes.forEachIndexed { index, mode ->
                    ModeButton(
                        text = mode.name,
                        iconRes = if (mode == ModeSelector.RANDOM) R.drawable.ic_random else R.drawable.ic_smart,
                        selected = selectMode == mode,
                        onClick = { mainScreenViewmodel.setSelectMode(mode) },
                        index = index,
                        totalCount = modes.size
                    )
                }
            }


            Space()

            SwitchType.entries.forEach { type ->
                val isChecked by remember { derivedStateOf { switchStates[type] == true } }
                LabeledSwitch(
                    label = type.name.replace("_", " ").lowercase(),
                    checked = isChecked,
                    onCheckedChange = {checked ->
                        mainScreenViewmodel.toggleSwitch(type, checked)
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


}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    val mockPreferences = AppPreferences(LocalContext.current)
//    val themeViewmodel = ThemeViewmodel()
//    val mainScreenViewmodel = MainScreenViewmodel(mockPreferences)
//
//    KeySmithTheme {
//        MainScreen(
//            modifier = Modifier.fillMaxSize(),
//            themeViewmodel = themeViewmodel,
//            mainScreenViewmodel = mainScreenViewmodel
//        )
//    }
//}