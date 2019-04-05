package io.zeebe.voyager.inmemory

import io.zeebe.voyager.Configuration

class InMemoryConfiguration : Configuration(
        workflowService = InMemoryWorkflowService(),
        instanceService = InMemoryInstanceService()
)