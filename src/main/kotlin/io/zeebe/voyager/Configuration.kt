package io.zeebe.voyager

import io.zeebe.voyager.services.InstanceService
import io.zeebe.voyager.services.WorkflowService
import io.zeebe.voyager.services.TaskService

open class Configuration(
		val workflowService: WorkflowService,
		val instanceService: InstanceService,
		val taskService: TaskService
)