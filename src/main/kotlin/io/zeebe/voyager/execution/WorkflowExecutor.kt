package io.zeebe.voyager.execution

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.model.EndEvent
import io.zeebe.voyager.model.Task as BpmnTask
import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.services.InstanceService
import io.zeebe.voyager.services.TaskService
import io.zeebe.voyager.transformer.ExecutionContext

class WorkflowExecutor(val configuration: Configuration) {

	private val instanceService: InstanceService = configuration.instanceService
	private val taskService: TaskService = configuration.taskService
	private val auditService = configuration.auditService

	fun execute(workflow: Workflow): Long {
		val newInstance = instanceService.newInstance(workflow)

		auditService.event("CREATED", newInstance)

		executeWorkflowInstance(newInstance)

		return newInstance.key
	}

	private fun executeWorkflowInstance(workflowInstance: WorkflowInstance) {
		val currentState = workflowInstance.currentState

		val context = ExecutionContext(
			configuration = configuration,
			instance = workflowInstance,
			key = workflowInstance.key,
			element = currentState
		)

		currentState.activationSteps().map { it.execute(context) }
	}

	fun continueInstance(task: Task) {
		val instanceKey = task.instanceKey
		val workflowInstance = instanceService.getInstance(instanceKey)
			?: throw IllegalStateException("Expected to find instance with key $instanceKey, but was not found. Can't continue workflow instance.")

		if (workflowInstance.taskRef == task) {

			val currentState = workflowInstance.currentState

			val context = ExecutionContext(
				configuration = configuration,
				instance = workflowInstance,
				key = workflowInstance.key,
				element = currentState
			)

			currentState.completionSteps().map { it.execute(context) }

		} else {
			throw IllegalArgumentException("Expected to continue workflow instance on ${workflowInstance.taskRef}, but given task reference was $task")
		}
	}

}


