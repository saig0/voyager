package io.zeebe.voyager.inmemory

import io.zeebe.voyager.services.TaskService
import io.zeebe.voyager.execution.Task
import io.zeebe.voyager.execution.WorkflowInstance

class InMemoryTaskService : TaskService {

    private var keyGenerator: Long = 1
    private val tasks = mutableMapOf<Long, Task>()
    private val tasksByType = mutableMapOf<String, MutableList<Task>>()

    override fun create(type: String, instance: WorkflowInstance): Task {

        val task = Task(
                key = keyGenerator++,
                type = type
        )

        tasks[task.key] = task
        val taskList = tasksByType.getOrDefault(type, mutableListOf())
        taskList.add(task)
        tasksByType[type] = taskList

        return task
    }


    override fun findByType(type: String): List<Task> = tasksByType[type]?.toList().orEmpty()

    override fun remove(task: Task) {
        tasks.remove(task.key)
        tasksByType[task.type]?.remove(task)
    }

}