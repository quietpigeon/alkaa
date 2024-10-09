package com.escodro.task.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.destination.HomeDestination
import com.escodro.navigation.destination.TasksDestination
import com.escodro.navigation.event.Event
import com.escodro.navigation.event.TaskEvent
import com.escodro.navigation.provider.NavGraph
import com.escodro.task.presentation.add.AddTaskBottomSheet
import com.escodro.task.presentation.detail.main.TaskDetailScreen
import com.escodro.task.presentation.list.TaskListSection

internal class TaskNavGraph : NavGraph {

    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.TaskList> {
            TaskListSection(
                onItemClick = { id -> navEventController.sendEvent(TaskEvent.OnTaskClick(id)) },
                onAddClick = { navEventController.sendEvent(TaskEvent.OnNewTaskClick) },
            )
        }

        composable<TasksDestination.TaskDetail> { navEntry ->
            val route: TasksDestination.TaskDetail = navEntry.toRoute()
            TaskDetailScreen(
                taskId = route.taskId,
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }

        dialog<TasksDestination.AddTaskBottomSheet> {
            AddTaskBottomSheet(
                onHideBottomSheet = { navEventController.sendEvent(Event.OnBack) },
            )
        }
    }
}
