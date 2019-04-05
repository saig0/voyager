package io.zeebe.voyager.transformer

import io.zeebe.model.bpmn.BpmnModelInstance
import io.zeebe.model.bpmn.instance.Process as BpmnProcess
import io.zeebe.model.bpmn.instance.StartEvent as BpmnStartEvent
import io.zeebe.model.bpmn.instance.ServiceTask as BpmnServiceTask
import io.zeebe.model.bpmn.instance.EndEvent as BpmnEndEvent
import io.zeebe.model.bpmn.instance.FlowNode as BpmnFlowNode
import io.zeebe.model.bpmn.instance.SequenceFlow as BpmnSequenceFlow
import io.zeebe.voyager.model.*

object WorkflowTransformer {

	fun transform(model: BpmnModelInstance): List<Workflow> {
		val processes = model.getModelElementsByType(BpmnProcess::class.java)

		val workflows = processes.map { process ->
			val bpmnProcessId = process.getId()

			val flowNodes = process.getChildElementsByType(BpmnFlowNode::class.java).map { flowNode ->
				when (flowNode) {
					is BpmnStartEvent -> StartEvent(id = flowNode.id)
					is BpmnServiceTask -> Task(id = flowNode.id, type = "???")
					is BpmnEndEvent -> EndEvent(id = flowNode.id)
					else -> FlowNode(id = flowNode.id)
				}
			}

			val flowNodesById = flowNodes.associateBy { it.id }

			process.getChildElementsByType(BpmnSequenceFlow::class.java).map { sequenceFlow ->
				val source = flowNodesById.get(sequenceFlow.source.id)!!
				val target = flowNodesById.get(sequenceFlow.target.id)!!

				val flow = SequenceFlow(
					id = sequenceFlow.id,
					source = source,
					target = target
				)

				source.outcomming.add(flow)
				target.incomming.add(flow)
			}

			val startEvent = flowNodes.filterIsInstance(StartEvent::class.java).toList().first()

			Workflow(
				bpmnProcessId = bpmnProcessId,
				startEvent = startEvent
			)
		}

		return workflows;
	}

}