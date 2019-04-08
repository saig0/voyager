package io.zeebe.voyager.transformer

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.execution.WorkflowInstance
import io.zeebe.voyager.model.*

data class ExecutionContext(
	val configuration: Configuration,
	val instance: WorkflowInstance,
	val key: Long,
	val element: FlowElement
)

interface WorkflowStep {

	fun execute(context: ExecutionContext)

}

class CompleteFlowNode : WorkflowStep {

	override fun execute(context: ExecutionContext) {

		val flowNode = context.element as FlowNode

		context.configuration.auditService.event("ACTIVATED", context.instance, context.key, flowNode)
		
		flowNode.completionSteps().map { it.execute(context) }
	}
}

class TakeSequenceFlow : WorkflowStep {

	override fun execute(context: ExecutionContext) {
		
		val flowNode = context.element as FlowNode

		context.configuration.auditService.event("COMPLETED", context.instance, context.key, flowNode)
		
		val sequenceFlow = flowNode.outcomming[0]

		val next = sequenceFlow.target
		context.instance.currentState = next

		context.configuration.instanceService.updateInstance(context.instance)

		context.configuration.auditService.event("TAKEN", context.instance, context.key, sequenceFlow)

		next.activationSteps().map {
			it.execute(
				context.copy(
					element = next
				)
			)
		}
	}
}

class CreateTask : WorkflowStep {

	override fun execute(context: ExecutionContext) {

		val task = context.element as Task
		val newTask = context.configuration.taskService.create(task.type, context.instance)

		context.instance.taskRef = newTask
		context.configuration.instanceService.updateInstance(context.instance)

		context.configuration.auditService.event("ACTIVATED", context.instance, context.key, task)
	}
}

class CompleteTask : WorkflowStep {

	override fun execute(context: ExecutionContext) {

		context.configuration.taskService.remove(context.instance.taskRef!!)

		context.instance.taskRef = null
		context.configuration.instanceService.updateInstance(context.instance)
	}
}

class ConsumeToken : WorkflowStep {

	override fun execute(context: ExecutionContext) {

		context.configuration.instanceService.removeInstance(context.instance)
		context.configuration.auditService.event("COMPLETED", context.instance, context.key, context.element)

		// check if there are any active tokens
		context.configuration.auditService.event("COMPLETED", context.instance)
	}
}

