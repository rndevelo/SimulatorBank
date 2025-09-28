package io.rndev.simulatorbank

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.rndev.detail.presentation.DetailScreen
import io.rndev.detail.presentation.DetailViewModel
import io.rndev.presentation.AccountsScreen
import io.rndev.presentation.AuthScreen
import kotlinx.serialization.Serializable

@Serializable
data object Auth : NavKey

@Serializable
data object Accounts : NavKey

@Serializable
data class Detail(val accountId: String) : NavKey

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(Auth)

    NavDisplay(
        backStack = backStack,
        entryDecorators =
            listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                entry<Auth> {
                    AuthScreen(
                        onLoginSuccessNavigation = {
                            backStack.clear()
                            backStack.add(Accounts)
                        },
                    )
                }
                entry<Accounts> {
                    AccountsScreen(
                        onAccountClick = { accountId ->
                            backStack.add(Detail(accountId))
                        }
                    )
                }
                entry<Detail> { key ->
                    val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key.accountId)
                        }
                    )
                    DetailScreen(
                        viewModel = viewModel,
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
            },
    )
}
