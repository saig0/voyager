package io.zeebe.voyager

import io.zeebe.voyager.services.WorkflowService

open class Configuration(
		val workflowService: WorkflowService
)