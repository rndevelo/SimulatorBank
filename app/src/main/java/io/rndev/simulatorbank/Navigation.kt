package io.rndev.simulatorbank

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.rndev.presentation.AccountsScreen
import io.rndev.presentation.AuthScreen
import kotlinx.serialization.Serializable

@Serializable
data object Auth : NavKey

@Serializable
data object Accounts : NavKey

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
                        onNavAccount = {
                            backStack.clear()
                            backStack.add(Accounts)
                        },
                    )
                }
                entry<Accounts> {
                    AccountsScreen()
                }
            },
    )
}
