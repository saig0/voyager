package io.zeebe.voyager.inmemory

import io.zeebe.voyager.model.FlowNode
import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.model.WorkflowInstance
import io.zeebe.voyager.services.InstanceService

class InMemoryInstanceService : InstanceService
{
    val instances: MutableList<WorkflowInstance> = mutableListOf()

    override fun newInstance(workflow: Workflow): WorkflowInstance {
        val key = instances.size as Long;
        val instance = WorkflowInstance(workflow, key)
        instances.add(instance)
        return instance
    }

    override fun updateInstance(key: Long, state: FlowNode) {
        instances.get(key as Int).currentState = state
    }

    override fun getInstance(key: Long): WorkflowInstance? {
        return instances.get(key as Int)
    }
}