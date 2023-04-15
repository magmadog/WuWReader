package com.sarbaevartur.wuwreader.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarbaevartur.wuwreader.R

@Composable
fun SettingsView() {
    LazyColumn {
        item { SettingCard(
            settingName = stringResource(id = R.string.settings_font),
            listItems = listOf("Roboto", "Times New Roman", "Sherif"),
            settingImageVector = Icons.Filled.Edit,
            settingImageDescription = stringResource(id = R.string.settings_font_description),
            onClick = { /*TODO*/ }) }

        item { SettingCard(
            settingName = stringResource(id = R.string.setting_font_size),
            listItems = (8..24).step(2).map { it.toString() },
            settingImageVector = Icons.Default.Share,
            settingImageDescription = stringResource(id = R.string.settings_font_size_description),
            onClick = { /*TODO*/ }) }

        item { SettingCard(
            settingName = stringResource(id = R.string.settings_alignment),
            listItems = listOf("left", "center", "fully"),
            settingImageVector = Icons.Default.Refresh,
            settingImageDescription = stringResource(id = R.string.settings_alignment_description),
            onClick = { /*TODO*/ }) }
    }
}

@Composable
private fun SettingCard(settingName: String, listItems: List<String>, settingImageVector: ImageVector, settingImageDescription: String, onClick: () -> Unit, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .border(border = BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(16.dp))
    ) {
        Column{
            Row{
                Image(
                    imageVector = settingImageVector,
                    contentDescription = settingImageDescription,
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .weight(2f)
                )
                Column(
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .weight(4f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = settingName,
                        style = MaterialTheme.typography.h5,
                        maxLines = 1)
                }
                Column(modifier = modifier.weight(3f)) {
                    SettingsDropDownOptionsMenu(listItems)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingsDropDownOptionsMenu(listItems: List<String>){
    val contextForToast = LocalContext.current.applicationContext

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Set") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption
                    Toast.makeText(contextForToast, selectedOption, Toast.LENGTH_SHORT).show()
                    expanded = false
                }) {
                    Text(text = selectedOption)
                }
            }
        }
    }
}

@Preview
@Composable
fun Ppreview(){
    SettingCard(
        settingName = stringResource(id = R.string.settings_font),
        listItems = listOf("roboto"),
        settingImageVector = Icons.Filled.Edit,
        settingImageDescription = stringResource(id = R.string.settings_font_description),
        onClick = { /*TODO*/ })
}