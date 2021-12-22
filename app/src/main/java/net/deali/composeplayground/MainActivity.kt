package net.deali.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.deali.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                val items by vm.items.observeAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarCoroutineScope = rememberCoroutineScope()

                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        ItemList(items!!, Modifier.weight(1f)) {
                            snackbarCoroutineScope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("${it}번째 아이템 입니다.")
                            }
                        }

                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

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
fun ItemList(items: List<Item>, modifier: Modifier, showSnackBar: (String) -> Unit) {
    LazyColumn(
        modifier.fillMaxWidth()
    ) {
        items(items) { item ->
            Row(Modifier
                .height(50.dp)
                .clickable {
                    showSnackBar.invoke(item.title)
                }) {
                Text(
                    item.title,
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
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
        ItemList(
            items = listOf(
                Item("1"),
                Item("2"),
                Item("3"),
                Item("4")
            ),
            Modifier
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