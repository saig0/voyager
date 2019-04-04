package io.zeebe.voyager.inmemory

import io.zeebe.voyager.services.WorkflowService
import io.zeebe.voyager.model.Workflow

class InMemoryWorkflowService : WorkflowService {

	val workflows = mutableMapOf<String, Workflow>()

	override fun add(workflow: Workflow) {
		workflows.put(workflow.bpmnProcessId, workflow)
	}

	override fun findLatest(bpmnProcessId: String): Workflow? = workflows.get(bpmnProcessId)


}