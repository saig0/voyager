package io.zeebe.voyager.model

class Workflow(
	val bpmnProcessId: String,
	val startEvent: StartEvent
)

open class FlowElement(
	val id: String
)

open class FlowNode(id: String) : FlowElement(id = id) {

	var outcomming = mutableListOf<SequenceFlow>()
	var incomming = mutableListOf<SequenceFlow>()
}

class SequenceFlow(
	id: String,
	val source: FlowNode,
	val target: FlowNode
) : FlowElement(id = id)

class StartEvent(id: String) : FlowNode(id = id)

class EndEvent(id: String) : FlowNode(id = id)


class Task(
	id: String,
	val type: String
) : FlowNode(id = id)
				