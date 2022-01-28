package net.deali.composeplayground.ui.mainHome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import net.deali.composeplayground.R
import net.deali.composeplayground.components.MainHome
import net.deali.composeplayground.ui.main.MainViewModel

class MainHomeFragment : Fragment() {
    val vm by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                logNavigationBackQueue(findNavController())
                val mainItems by vm.items.observeAsState(initial = listOf())
                val isRefreshing by vm.isRefreshing.observeAsState(false)

                MainHome(
                    modifier = Modifier,
                    goodsItems = mainItems,
                    isRefreshing = isRefreshing,
                    onRefresh = { vm.refresh() },
                    onLoadMore = { vm.loadMore() },
                    goToDetail = { navController.navigate(R.id.goodsItemDetail) },
                    onBackPressed = {
                        if (navController.backQueue.size == 2) {
                            requireActivity().finish()
                        } else {
                            navController.navigateUp()
                        }
                        //                        finish()
                    }
                )
            }
        }
    }

    private fun logNavigationBackQueue(navController: NavController) {
        Log.e("kyh!!!", "navController.backQueue.size: " + navController.backQueue.size)
    }
}