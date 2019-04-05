package io.zeebe.voyager.inmemory

import io.zeebe.voyager.services.AuditService
import io.zeebe.voyager.execution.WorkflowInstance
import io.zeebe.voyager.model.FlowNode
import io.zeebe.voyager.model.FlowElement

class InMemoryAuditService : AuditService {

	interface AuditEvent
	data class WorkflowInstanceAuditEvent(val intent: String, val instance: WorkflowInstance) : AuditEvent
	data class ElementInstanceAuditEvent(
		val intent: String,
		val instance: WorkflowInstance,
		val key: Long,
		val element: FlowElement
	) : AuditEvent

	private val events = mutableListOf<AuditEvent>()

	fun events(): List<AuditEvent> = events.toList()

	override fun event(intent: String, instance: WorkflowInstance) {
		events.add(
			WorkflowInstanceAuditEvent(
				intent = intent,
				instance = instance
			)
		)
	}

	override fun event(intent: String, instance: WorkflowInstance, key: Long, element: FlowElement) {
		events.add(
			ElementInstanceAuditEvent(
				intent = intent,
				instance = instance,
				key = key,
				element = element
			)
		)
	}

}