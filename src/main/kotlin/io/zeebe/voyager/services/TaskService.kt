package io.zeebe.voyager.services

import io.zeebe.voyager.execution.WorkflowInstance
import io.zeebe.voyager.execution.Task

interface TaskService {
	
	fun create(type: String, instance: WorkflowInstance): Task
	
	fun findByType(type: String): List<Task>
	
	fun remove(task: Task)
	
}