package io.zeebe.voyager.services

import io.zeebe.voyager.execution.WorkflowInstance
import io.zeebe.voyager.model.FlowElement

interface AuditService {
		
	fun event(intent: String, instance: WorkflowInstance)
	
	fun event(intent: String, instance: WorkflowInstance, key: Long, element: FlowElement)
	
}