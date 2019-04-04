package io.zeebe.voyager

import io.zeebe.voyager.inmemory.InMemoryConfiguration

fun main() {

	println("Hello Voyager")

	val workflowEngine = WorkflowEngine(configuration = InMemoryConfiguration())

	val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")
	
	workflowEngine.deploy(workflow)
	
	workflowEngine.create("order-process")

}