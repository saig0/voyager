package io.zeebe.voyager.execution

import io.zeebe.voyager.Configuration
import io.zeebe.voyager.model.EndEvent
import io.zeebe.voyager.model.Task
import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.services.InstanceService

class WorkflowExecutor(configuration: Configuration) {

    private val instanceService : InstanceService = configuration.instanceService

    fun execute(workflow: Workflow) : Long {
        val newInstance = instanceService.newInstance(workflow)
        val currentState = newInstance.currentState


        val target = currentState.outcomming[0].target
        when(target) {
            is Task ->
            {
                newInstance.currentState = target
                instanceService.updateInstance(newInstance)
            }
            is EndEvent ->
            {
                instanceService.removeInstance(newInstance)
            }
        }

        return newInstance.key
    }

}


