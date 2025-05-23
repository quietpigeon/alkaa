package com.escodro.alkaa.fake

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.escodro.appstate.AppState
import com.escodro.navigationapi.destination.TopAppBarVisibleDestinations
import com.escodro.navigationapi.destination.TopLevelDestinations
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class AppStateFake(override val navHostController: NavHostController) : AppState {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentTopDestination: Flow<TopLevel> =
        navHostController.currentBackStack
            .mapLatest { entries ->
                entries.reversed().firstNotNullOfOrNull { entry ->
                    TopLevelDestinations.firstOrNull { entry.destination.hasRoute(it::class) }
                }
            }.filterNotNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val shouldShowTopAppBar: Flow<Boolean> =
        navHostController.currentBackStackEntryFlow
            .mapLatest { navBackStackEntry ->
                val currentDestination = navBackStackEntry.destination
                TopAppBarVisibleDestinations.any { currentDestination.hasRoute(it::class) }
            }
}
