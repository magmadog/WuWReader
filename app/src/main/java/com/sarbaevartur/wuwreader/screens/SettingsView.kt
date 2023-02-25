package com.sarbaevartur.wuwreader.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarbaevartur.wuwreader.R

@Composable
fun SettingsView() {
    LazyColumn {
        item { SettingCard(
            settingName = stringResource(id = R.string.settings_font),
            settingValue = "roboto",
            settingImageVector = Icons.Filled.Edit,
            settingImageDescription = stringResource(id = R.string.settings_font_description),
            onClick = { /*TODO*/ }) }
    }

}

@Composable
fun SettingCard(settingName: String, settingValue: String, settingImageVector: ImageVector, settingImageDescription: String, onClick: () -> Unit, modifier: Modifier = Modifier){

    Column {
        Row(
            modifier = modifier
                .clickable(onClick = onClick)
                .clip(shape = RoundedCornerShape(8.dp))
        ) {
            Image(
                settingImageVector,
                contentDescription = settingImageDescription,
                modifier = modifier.weight(1f)
            )
            Column(
                modifier = modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = settingName,
                    style = MaterialTheme.typography.h5,
                    maxLines = 2)
            }
            Text(
                text = settingValue, modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun Ppreview(){
    SettingCard(
        settingName = stringResource(id = R.string.settings_font),
        settingValue = "roboto",
        settingImageVector = Icons.Filled.Edit,
        settingImageDescription = stringResource(id = R.string.settings_font_description),
        onClick = { /*TODO*/ })
}