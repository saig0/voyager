package io.zeebe.voyager.services

import io.zeebe.voyager.model.Workflow

interface WorkflowService {
	
	fun add(workflow: Workflow)
	
	fun findLatest(bpmnProcessId: String): Workflow?
	
}