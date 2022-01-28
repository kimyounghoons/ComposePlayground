package net.deali.composeplayground.ui.addGoodsItem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import net.deali.composeplayground.R

class AddGoodsItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = remember { findNavController() }
                logNavigationBackQueue(navController)

                Column {
                    Text(text = "홈으로",
                        Modifier
                            .size(200.dp)
                            .background(Color.Yellow)
                            .clickable {
                                navController.popBackStack(R.id.home, false)
                            })
                }
            }
        }
    }

    private fun logNavigationBackQueue(navController: NavController) {
        Log.e("kyh!!!", "navController.backQueue.size: " + navController.backQueue.size)
    }
}