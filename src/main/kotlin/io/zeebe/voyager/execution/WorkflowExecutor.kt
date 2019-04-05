package io.zeebe.voyager.execution

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.model.EndEvent
import io.zeebe.voyager.model.Task
import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.services.InstanceService
import io.zeebe.voyager.services.TaskService

class WorkflowExecutor(configuration: Configuration) {

    private val instanceService : InstanceService = configuration.instanceService
    private val taskService: TaskService = configuration.taskService

    fun execute(workflow: Workflow) : Long {
        val newInstance = instanceService.newInstance(workflow)
        val currentState = newInstance.currentState


        val target = currentState.outcomming[0].target
        when(target) {
            is Task ->
            {
                val taskType = target.type
                taskService.create(taskType, newInstance)
                newInstance.currentState = target
                instanceService.updateInstance(newInstance)
            }
            is EndEvent ->
            {
                instanceService.removeInstance(newInstance)
            }
        }

        return newInstance.key
    }

}


