package io.zeebe.voyager.model

import io.zeebe.voyager.transformer.*

class Workflow(
	val bpmnProcessId: String,
	val startEvent: StartEvent
)

abstract class FlowElement(
	val id: String
) 

open class FlowNode(id: String) : FlowElement(id = id) {

	var outcomming = mutableListOf<SequenceFlow>()
	var incomming = mutableListOf<SequenceFlow>()
	
	open fun activationSteps(): List<WorkflowStep> = emptyList()
	open fun completionSteps(): List<WorkflowStep> = emptyList()
}

class SequenceFlow(
	id: String,
	val source: FlowNode,
	val target: FlowNode
) : FlowElement(id = id)

class StartEvent(id: String) : FlowNode(id = id) {
	
	override fun activationSteps(): List<WorkflowStep> = listOf(CompleteFlowNode())
	override fun completionSteps(): List<WorkflowStep> = listOf(TakeSequenceFlow())
	
}

class EndEvent(id: String) : FlowNode(id = id) {
	
	override fun activationSteps(): List<WorkflowStep> = listOf(CompleteFlowNode())
	override fun completionSteps() = listOf(ConsumeToken())
	
}


class Task(
	id: String,
	val type: String
) : FlowNode(id = id) {
	
	override fun activationSteps() = listOf(CreateTask())
	
	override fun completionSteps() = listOf(
			CompleteTask(),
			TakeSequenceFlow()
	)
}
				