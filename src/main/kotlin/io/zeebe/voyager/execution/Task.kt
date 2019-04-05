package io.zeebe.voyager.execution

data class Task (
		val key: Long,
		val type: String,
		val instanceKey: Long
)