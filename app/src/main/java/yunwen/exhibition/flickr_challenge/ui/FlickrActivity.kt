package yunwen.exhibition.flickr_challenge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import yunwen.exhibition.flickr_challenge.FlickrViewModel
import yunwen.exhibition.flickr_challenge.ui.theme.FlickrChallengeTheme

@AndroidEntryPoint
class FlickrActivity : ComponentActivity() {

    private val viewModel: FlickrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickrChallengeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "search") {
                    composable("search") {
                        SearchScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("detail/{index}") { backStackEntry ->
                        val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
                        val items = viewModel.items.collectAsStateWithLifecycle()
                        DetailScreen(
                            item = items.value[index ?: 0],
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
