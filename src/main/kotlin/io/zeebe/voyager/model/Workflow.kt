package io.zeebe.voyager.model

class Workflow(
	val bpmnProcessId: String,
	val startEvent: StartEvent
)

open class FlowNode(
	val id: String,
	val incomming: List<SequenceFlow>,
	val outcomming: List<SequenceFlow>
)

class SequenceFlow(
	val id: String,
	val source: FlowNode,
	val target: FlowNode
)

class StartEvent(
	id: String,
	outcomming: List<SequenceFlow>
) : FlowNode(
	id = id,
	incomming = emptyList(),
	outcomming = outcomming
)

class Task(
	id: String,
	incomming: List<SequenceFlow>,
	outcomming: List<SequenceFlow>
) : FlowNode(
	id = id,
	incomming = incomming,
	outcomming = outcomming
)
				