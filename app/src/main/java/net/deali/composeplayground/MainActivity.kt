package net.deali.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import net.deali.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                val items by vm.items.observeAsState(initial = listOf())
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarCoroutineScope = rememberCoroutineScope()
                val isRefreshing by vm.isRefreshing.observeAsState(false)
                SwipeRefresh(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { vm.refresh() },
                ) {
                    Column {
                        MainItems(items, Modifier.weight(1f), onLoadMore = {
                            vm.loadMore()
                        }) {
                            snackbarCoroutineScope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("아이템 : ${it}")
                            }
                        }

                        SnackbarHost(hostState = snackbarHostState)

                        BottomButtons(addItem = {
                            vm.addItem()
                        }, deleteItem = {
                            vm.deleteItem()
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MainItems(items: List<Item>, modifier: Modifier, onLoadMore: () -> Unit, showSnackBar: (String) -> Unit) {
    val listState = rememberLazyListState()

    LazyColumn(modifier.fillMaxWidth(), state = listState) {
        items(items) { item ->
            MainItem(item = item, showSnackBar = showSnackBar)
        }
    }

    InfiniteListHandler(listState = listState) {
        onLoadMore()
    }
}

@Composable
fun MainItem(item: Item, showSnackBar: (String) -> Unit) {
    ConstraintLayout(Modifier
        .height(100.dp)
        .clickable {
            showSnackBar.invoke(item.title)
        }) {

        val (image, title, content) = createRefs()

        if (item.content != null) {
            createVerticalChain(title, content, chainStyle = ChainStyle.Spread)
        }

        Image(
            painter = rememberImagePainter(
                data = item.imageUrl
            ),
            contentDescription = "android logo",
            modifier = Modifier
                .size(50.dp)
                .constrainAs(image) {
                    top.linkTo(anchor = parent.top)
                    bottom.linkTo(anchor = parent.bottom)
                    start.linkTo(anchor = parent.start, margin = 16.dp)
                }
        )

        Text(
            item.title,
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .constrainAs(title) {
                    start.linkTo(anchor = image.end, margin = 16.dp)
                    top.linkTo(anchor = parent.top)
                    bottom.linkTo(anchor = if (item.content == null) {
                        parent.bottom
                    } else {
                        content.top
                    })
                }
        )
        item.content?.let {
            Text(
                item.content,
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .constrainAs(content) {
                        start.linkTo(anchor = image.end, margin = 16.dp)
                        top.linkTo(anchor = title.bottom)
                        bottom.linkTo(anchor = parent.bottom)
                    }
            )
        }

    }

    Divider(color = Color.LightGray, thickness = 1.dp)
}


@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .filter {
                it
            }.collect {
                onLoadMore()
            }
    }
}

@Composable
fun BottomButtons(addItem: () -> Unit, deleteItem: () -> Unit) {
    Row {
        TextButton(
            onClick = { deleteItem.invoke() },
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(6.dp)
                .background(Color.LightGray)
                .weight(1f)
        ) {
            Text(stringResource(R.string.delete_last_item), color = Color.Black)
        }

        TextButton(
            onClick = { addItem.invoke() },
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(6.dp)
                .background(Color.LightGray)
                .weight(1f)
        ) {
            Text(stringResource(R.string.add_last_item), color = Color.Black)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePlaygroundTheme {
        MainItems(
            items = listOf(
                Item("1"),
                Item("2"),
                Item("3"),
                Item("4")
            ),
            modifier = Modifier,
            onLoadMore = {

            }
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomsPreview() {
    ComposePlaygroundTheme {
        BottomButtons(addItem = {}, deleteItem = {})
    }
}

@Preview(showBackground = true)
@Composable
fun MainItemPreView() {
    ComposePlaygroundTheme {
        MainItem(item = Item(title = "테스트", content = null, imageUrl = ""), showSnackBar = {})
    }
}