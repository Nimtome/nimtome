package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nimtome.app.model.DndClass

@ExperimentalMaterialApi
@Composable
fun ClassSelector(
    modifier: Modifier,
    allClasses: List<DndClass>,
    onClassChange: (DndClass) -> Unit,
    starterClass: DndClass
) {
    var expandedState by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(starterClass) }

    ExposedDropdownMenuBox(
        expanded = expandedState,
        onExpandedChange = {
            expandedState = !expandedState
        },
        modifier = modifier

    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.legibleName,
            onValueChange = {},
            label = { Text("Class") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expandedState
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expandedState,
            onDismissRequest = {
                expandedState = false
            },
        ) {

            allClasses.forEach {
                DropdownMenuItem(
                    onClick = {
                        onClassChange(it)
                        selectedOption = it
                        expandedState = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = it.legibleName)
                }
            }
        }
    }
}
