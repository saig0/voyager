package io.zeebe.voyager.services

import io.zeebe.voyager.model.Workflow
import io.zeebe.voyager.execution.WorkflowInstance

interface InstanceService {
    fun newInstance(workflow: Workflow) : WorkflowInstance
    fun updateInstance(instance: WorkflowInstance)
    fun removeInstance(instance: WorkflowInstance)
    fun getInstance(key: Long) : WorkflowInstance?
}