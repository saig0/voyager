package io.zeebe.voyager.services

import io.zeebe.voyager.model.FlowNode
import io.zeebe.voyager.model.WorkflowInstance
import io.zeebe.voyager.model.Workflow

interface InstanceService {
    fun newInstance(workflow: Workflow) : WorkflowInstance
    fun updateInstance(key: Long, state: FlowNode)
    fun getInstance(key: Long) : WorkflowInstance?
}