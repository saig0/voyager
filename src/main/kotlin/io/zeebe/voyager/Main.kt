package io.zeebe.voyager

import io.zeebe.voyager.inmemory.InMemoryConfiguration
import io.zeebe.voyager.services.AuditService
import io.zeebe.voyager.execution.WorkflowInstance
import io.zeebe.voyager.model.FlowNode
import io.zeebe.voyager.inmemory.InMemoryAuditService

fun main() {

	println("Hello Voyager")

	val auditService = InMemoryAuditService()
	val workflowEngine = WorkflowEngine(configuration = InMemoryConfiguration(
			auditService = auditService
	))

	val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")

	workflowEngine.deploy(workflow)

	workflowEngine.create("order-process")

	workflowEngine.findTasksByType("collect-money").map { task ->
		workflowEngine.completeTask(task)
	}

	workflowEngine.findTasksByType("fetch-items").map { task ->
		workflowEngine.completeTask(task)
	}

	workflowEngine.findTasksByType("ship-parcel").map { task ->
		workflowEngine.completeTask(task)
	}

	auditService.events().map { event -> println("> $event") }
}