package net.deali.composeplayground.components

import androidx.activity.compose.BackHandler
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
import net.deali.composeplayground.models.GoodsItem
import net.deali.composeplayground.R
import net.deali.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun MainHome(modifier : Modifier, goodsItems : List<GoodsItem>, isRefreshing: Boolean, onRefresh: ()->Unit, onLoadMore: ()->Unit, onBackPressed: () -> Unit, goToDetail: (GoodsItem) ->Unit) {
    BackHandler(enabled = true, onBack = onBackPressed)

    SwipeRefresh(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
    ) {
        Column {
            MainItems(goodsItems, Modifier.weight(1f), onLoadMore = onLoadMore, goToDetail = goToDetail)
        }
    }
}

@Composable
fun MainItems(goodsItems: List<GoodsItem>, modifier: Modifier, onLoadMore: () -> Unit, goToDetail: (GoodsItem) -> Unit) {
    val listState = rememberLazyListState()

    LazyColumn(modifier.fillMaxWidth(), state = listState) {
        items(goodsItems) { item ->
            MainItem(goodsItem = item,goToDetail)
        }
    }

    InfiniteListHandler(listState = listState) {
        onLoadMore()
    }
}

@Composable
fun MainItem(goodsItem: GoodsItem, goToDetail: (GoodsItem) -> Unit) {
    ConstraintLayout(Modifier
        .height(100.dp)
        .clickable {
            goToDetail.invoke(goodsItem)
        }) {

        val (image, title, content, divider) = createRefs()

        Image(
            painter = rememberImagePainter(
                data = goodsItem.imageUrl
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
            goodsItem.name,
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .constrainAs(title) {
                    start.linkTo(anchor = image.end, margin = 16.dp)
                    top.linkTo(anchor = parent.top)
                    bottom.linkTo(anchor = if (goodsItem.price == null) {
                        parent.bottom
                    } else {
                        content.top
                    })
                }
        )

        goodsItem.price?.let {
            createVerticalChain(title, content, chainStyle = ChainStyle.Spread)
            Text(
                goodsItem.price,
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

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                })
    }
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePlaygroundTheme {
        MainItems(
            goodsItems = listOf(
                GoodsItem("1"),
                GoodsItem("2"),
                GoodsItem("3"),
                GoodsItem("4")
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
fun MainItemPreView() {
    ComposePlaygroundTheme {
        MainItem(goodsItem = GoodsItem(name = "테스트", price = null, imageUrl = ""), goToDetail = {})
    }
}