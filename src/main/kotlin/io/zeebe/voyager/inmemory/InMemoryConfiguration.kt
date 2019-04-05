package io.zeebe.voyager.inmemory

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.services.WorkflowService
import io.zeebe.voyager.services.InstanceService
import io.zeebe.voyager.services.TaskService
import io.zeebe.voyager.services.AuditService

class InMemoryConfiguration(
	workflowService: WorkflowService = InMemoryWorkflowService(),
	instanceService: InstanceService = InMemoryInstanceService(),
	taskService: TaskService = InMemoryTaskService(),
	auditService: AuditService = InMemoryAuditService()
) : Configuration(
	workflowService,
	instanceService,
	taskService,
	auditService
)