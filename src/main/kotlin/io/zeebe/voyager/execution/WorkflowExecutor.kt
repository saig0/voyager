package io.zeebe.voyager.execution

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.services.InstanceService

class WorkflowExecutor(val configuration: Configuration) {

    val instanceService : InstanceService = configuration.instanceService

    fun execute(workflow: Workflow) : Long {
        val newInstance = instanceService.newInstance(workflow)



        return newInstance.key
    }

}


