package com.example.httplogger

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A composable function to create a custom tab
 *
 * @param modifier: Modifier for styling the tab
 * @param tabTitles: Pass the titles that need to be displayed on the tab (its a list)
 * @param onTabSelected: A callback function that is called a tab is selected
 * @param indicator: An optional composable function to pass a custom indicator
 * @param tabModifier: A lambda function that takes three parameters:
 * Boolean: Indicates whether the tab is selected.
 * Modifier: The existing Modifier for the tab.
 * RoundedCornerShape: The shape to be applied to the tab.
 * @param tabContent: A lambda function that allows for change of color of the inside of tab
 *

 */


@Composable
private fun ZindigiTab(
    tabTitles: List<String>,
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit, //callback function
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = {},
    tabModifier: @Composable (Boolean, Modifier, RoundedCornerShape) -> Modifier = { _, m, _ -> m },
    tabContent: @Composable (String, Boolean) -> Unit = { title, selected ->
        Text(
            text = title,
            color = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            indicator = indicator,
            divider = {},  // Empty divider composable
            modifier = Modifier
                .fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                val shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                    tabTitles.size - 1 -> RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        topStart = 0.dp,
                        bottomStart = 0.dp
                    )
                    else -> RoundedCornerShape(0.dp)
                }

                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        onTabSelected(index)
                    },
                    text = {
                        tabContent(title, selectedTabIndex == index)
                    },
                    modifier = tabModifier(selectedTabIndex == index, Modifier, shape)
                        .fillMaxHeight()
                )
            }
        }
    }
}

/**
 * A composable function to create a custom indicator
 *
 * @param tabPositions: : A list of positions for each tab, providing information about their layout and dimensions.
 * @param selectedTabIndex: The index of the currently selected tab
 */

@Composable
fun CustomIndicator(tabPositions: List<TabPosition>, selectedTabIndex: Int) {
    Box(
        Modifier
            .tabIndicatorOffset(tabPositions[selectedTabIndex])
            .height(4.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

/**
 * A composable wrapper function to create a custom tab with an indicator
 * @param content: : Composable function can be called within the body of the parent function to render the content it defines.
 *
 */


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZindigiTabWithIndicator(
    modifier: Modifier = Modifier,
    tabTitles: List<String>,
    pagerState: PagerState,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    content: @Composable () -> Unit = {}
) {
    ZindigiTab(
        tabTitles = tabTitles,
        modifier = modifier,
        onTabSelected = { index ->
            onTabSelected(index)
        },
        indicator = { tabPositions ->
            CustomIndicator(tabPositions = tabPositions, selectedTabIndex = selectedTabIndex)
        },
        tabContent = { title, selected ->
            Text(
                text = title,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )

    content()
}

//@OptIn(ExperimentalFoundationApi::class)
//@Preview
//@Composable
//fun PreviewZindigiTabWithIndicator() {
//    HttpLoggerTheme {
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFF1F8E9)) // Apply background to the preview
//        ) {
//            var selectedTabIndex by remember { mutableStateOf(0) }
//            var PagerState by remember { mutableStateOf(0) }
//
//
//            ZindigiTabWithIndicator(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth(),
//                tabTitles = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4"),
//                content = {},
//                selectedTabIndex = selectedTabIndex,
//                onTabSelected = {},
//                pagerState = PagerState
//
//            )
//        }
//    }
//}