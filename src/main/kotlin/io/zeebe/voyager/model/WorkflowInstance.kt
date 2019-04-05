package io.zeebe.voyager.model

data class WorkflowInstance(val workflow: Workflow, val key: Long) {
    var currentState: FlowNode = workflow.startEvent
}