@file:OptIn(ExperimentalMaterial3Api::class)

package com.mls.kmp.mor.nytnewskmp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.data.aricles.defaultTopics
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.library.MR
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InterestsBottomSheetDialog(
    selectableTopics: List<Topics> = Topics.allTopics,
    selectedTopics: List<Topics> = defaultTopics,
    onDismiss: (updated: Boolean, updatedTopics: List<Topics>) -> Unit = { _, _ -> }
) {
    var newSelectedTopics by remember { mutableStateOf(selectedTopics) }

    ModalBottomSheet(onDismissRequest = {
        onDismiss(
            selectedTopics != newSelectedTopics,
            newSelectedTopics
        )
    }) {

        Text(
            text = stringResource(MR.strings.your_topics),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold
        )

        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            selectableTopics.forEach {

                val selected by remember(newSelectedTopics) {
                    mutableStateOf(newSelectedTopics.contains(it))
                }
                val interestName = interestEnumToStringResources(it)

                FilterChip(
                    modifier = Modifier,
                    selected = selected,
                    onClick = {

                        if (it == Topics.HOME) {
                            return@FilterChip
                        }

                        newSelectedTopics = if (selected) {
                            newSelectedTopics.filter { topic -> topic != it }
                        } else {
                            newSelectedTopics + it
                        }
                    },

                    label = { Text(interestName) },
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    }
}