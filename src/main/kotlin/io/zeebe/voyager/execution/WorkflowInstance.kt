package io.zeebe.voyager.execution

import io.zeebe.voyager.model.FlowNode
import io.zeebe.voyager.model.Workflow

data class WorkflowInstance(val workflow: Workflow, val key: Long) {
    var currentState: FlowNode = workflow.startEvent
}