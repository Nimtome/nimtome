package com.flyinpancake.dndspells.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.model.DndClass

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