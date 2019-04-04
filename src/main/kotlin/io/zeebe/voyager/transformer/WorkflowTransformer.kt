package io.zeebe.voyager.transformer

import io.zeebe.voyager.model.Workflow
import io.zeebe.model.bpmn.BpmnModelInstance
import io.zeebe.model.bpmn.instance.Process

object WorkflowTransformer {
	
	fun transform(model: BpmnModelInstance): List<Workflow> {
		val processes = model.getModelElementsByType(Process::class.java)
		
		val workflows = processes.map { process ->
 			val bpmnProcessId = process.getId()
			
			Workflow(bpmnProcessId = bpmnProcessId)
		}
		
		return workflows;
	}
	
}