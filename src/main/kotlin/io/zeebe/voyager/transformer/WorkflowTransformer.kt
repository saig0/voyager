package io.zeebe.voyager.transformer

import io.zeebe.voyager.model.Workflow
import io.zeebe.model.bpmn.BpmnModelInstance
import io.zeebe.model.bpmn.instance.Process as BpmnProcess
import io.zeebe.model.bpmn.instance.StartEvent as BpmnStartEvent
import io.zeebe.voyager.model.StartEvent

object WorkflowTransformer {

	fun transform(model: BpmnModelInstance): List<Workflow> {
		val processes = model.getModelElementsByType(BpmnProcess::class.java)

		val workflows = processes.map { process ->
			val bpmnProcessId = process.getId()

			val startEvents = process.getChildElementsByType(BpmnStartEvent::class.java)

			val startEvent = startEvents.toList().get(0)

			Workflow(
				bpmnProcessId = bpmnProcessId,
				startEvent = StartEvent(
					id = startEvent.getId(),
					outcomming = emptyList()
				)
			)
		}

		return workflows;
	}

}