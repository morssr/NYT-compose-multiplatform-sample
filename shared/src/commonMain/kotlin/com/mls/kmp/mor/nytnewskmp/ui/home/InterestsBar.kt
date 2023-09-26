package com.mls.kmp.mor.nytnewskmp.ui.home

import CustomScrollableTabRow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.data.aricles.defaultTopics
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.library.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun InterestsBar(
    modifier: Modifier = Modifier,
    onShowTopicsSelectionDialog: () -> Unit = {}
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = stringResource(MR.strings.what_you_curious_about),
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                modifier = Modifier.padding(end = 4.dp),
                onClick = onShowTopicsSelectionDialog
            ) {
                Icon(
                    painter = painterResource(MR.images.tabler_edit_24_svg),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(MR.strings.edit_topics_content_description)
                )
            }
        }
    }
}

@Composable
fun InterestTabsRow(
    topics: List<Topics> = defaultTopics,
    modifier: Modifier = Modifier,
    currentSelectedPageIndex: Int = 0,
    onTabClick: (Int) -> Unit = {},
) {
    CustomScrollableTabRow(
        selectedTabIndex = currentSelectedPageIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        edgePadding = 8.dp,
        indicator = {},
        divider = {}
    ) {
        topics.forEachIndexed { index, topic ->
            val selected = index == currentSelectedPageIndex
            val interestString = interestEnumToStringResources(topic)

            Tab(
                modifier = Modifier.padding(horizontal = 6.dp),
                selected = selected,
                onClick = { onTabClick(index) },
            ) {
                CustomTabCell(interestString, selected)
            }
        }
    }
}

@Composable
private fun CustomTabCell(
    text: String,
    selected: Boolean,
) {
    Surface(
        modifier = Modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(30)
    ) {

        Box(
            modifier = Modifier
                .then(
                    if (selected) {
                        Modifier.background(
                            MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(30)
                            )
                    }
                ), contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun interestEnumToStringResources(interests: Topics): String {
    return when (interests) {
        Topics.HOME -> stringResource(MR.strings.home)
        Topics.POLITICS -> stringResource(MR.strings.politics)
        Topics.BOOKS -> stringResource(MR.strings.books)
        Topics.HEALTH -> stringResource(MR.strings.health)
        Topics.TECHNOLOGY -> stringResource(MR.strings.technology)
        Topics.AUTOMOBILES -> stringResource(MR.strings.automobiles)
        Topics.ARTS -> stringResource(MR.strings.arts)
        Topics.SCIENCE -> stringResource(MR.strings.science)
        Topics.FASHION -> stringResource(MR.strings.fashion)
        Topics.SPORTS -> stringResource(MR.strings.sports)
        Topics.BUSINESS -> stringResource(MR.strings.business)
        Topics.FOOD -> stringResource(MR.strings.food)
        Topics.TRAVEL -> stringResource(MR.strings.travel)
        Topics.INSIDER -> stringResource(MR.strings.insider)
        Topics.MAGAZINE -> stringResource(MR.strings.magazine)
        Topics.MOVIES -> stringResource(MR.strings.movies)
        Topics.NATIONAL -> stringResource(MR.strings.national)
        Topics.NYREGION -> stringResource(MR.strings.ny_region)
        Topics.OBITUARIES -> stringResource(MR.strings.obituaries)
        Topics.OPINION -> stringResource(MR.strings.opinion)
        Topics.REALESTATE -> stringResource(MR.strings.real_estate)
        Topics.SUNDAYREVIEW -> stringResource(MR.strings.sunday_review)
        Topics.THEATER -> stringResource(MR.strings.theater)
        Topics.TMAGAZINE -> stringResource(MR.strings.t_magazine)
        Topics.UPSHOT -> stringResource(MR.strings.upshot)
        Topics.WORLD -> stringResource(MR.strings.world)
    }
}