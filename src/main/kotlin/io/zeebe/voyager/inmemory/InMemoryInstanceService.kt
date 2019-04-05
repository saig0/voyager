package io.zeebe.voyager.inmemory

import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.execution.WorkflowInstance
import io.zeebe.voyager.services.InstanceService

class InMemoryInstanceService : InstanceService
{
    private val instances: MutableMap<Long, WorkflowInstance> = mutableMapOf()
    private var keyGenerator : Long = 0

    override fun newInstance(workflow: Workflow): WorkflowInstance {
        val key = ++keyGenerator
        val instance = WorkflowInstance(workflow, key)
        instances[key] = instance
        return instance
    }

    override fun updateInstance(instance: WorkflowInstance) {
        instances[instance.key] = instance
    }

    override fun removeInstance(instance: WorkflowInstance) {
        instances.remove(instance.key)
    }

    override fun getInstance(key: Long): WorkflowInstance? = instances[key]
}