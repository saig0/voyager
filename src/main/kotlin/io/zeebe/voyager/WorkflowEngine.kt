package io.zeebe.voyager

import java.io.InputStream
import io.zeebe.voyager.transformer.WorkflowTransformer
import io.zeebe.model.bpmn.Bpmn
import io.zeebe.voyager.execution.WorkflowExecutor

class WorkflowEngine(val configuration: Configuration) {

	val executor = WorkflowExecutor(configuration)

	fun deploy(stream: InputStream) {

		val model = Bpmn.readModelFromStream(stream)

		val workflows = WorkflowTransformer.transform(model)

		workflows.forEach(configuration.workflowService::add)
	}

	fun create(bpmnProcessId: String): Long {

		val workflow = configuration.workflowService.findLatest(bpmnProcessId)
			?: throw IllegalArgumentException("no workflow found with BPMN process id '${bpmnProcessId}'")

		println("Create instance of '${bpmnProcessId}' ...")

		return executor.execute(workflow)
	}

}