package io.zeebe.voyager.execution

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.model.EndEvent
import io.zeebe.voyager.model.Task as BpmnTask
import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.services.InstanceService
import io.zeebe.voyager.services.TaskService

class WorkflowExecutor(configuration: Configuration) {

    private val instanceService : InstanceService = configuration.instanceService
    private val taskService: TaskService = configuration.taskService

    fun execute(workflow: Workflow) : Long {
        val newInstance = instanceService.newInstance(workflow)

        executeWorkflowInstance(newInstance)

        return newInstance.key
    }

    private fun executeWorkflowInstance(workflowInstance: WorkflowInstance) {
        val currentState = workflowInstance.currentState
        val target = currentState.outcomming[0].target
        when (target) {
            is BpmnTask -> {
                val taskType = target.type
                workflowInstance.taskRef = taskService.create(taskType, workflowInstance)
                workflowInstance.currentState = target
                instanceService.updateInstance(workflowInstance)
            }
            is EndEvent -> {
                instanceService.removeInstance(workflowInstance)
            }
        }
    }

    fun continueInstance(task: Task) {
        val instanceKey = task.instanceKey
        val workflowInstance = instanceService.getInstance(instanceKey)?:
            throw IllegalStateException("Expected to find instance with key $instanceKey, but was not found. Can't continue workflow instance.")

        if (workflowInstance.taskRef == task)
        {
            executeWorkflowInstance(workflowInstance)
        }
        else
        {
            throw IllegalArgumentException("Expected to continue workflow instance on ${workflowInstance.taskRef}, but given task reference was $task")
        }
    }

}


