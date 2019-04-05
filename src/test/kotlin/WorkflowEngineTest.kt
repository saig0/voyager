import io.zeebe.voyager.WorkflowEngine
import io.zeebe.voyager.inmemory.InMemoryConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkflowEngineTest {

    private val workflowEngine = WorkflowEngine(configuration = InMemoryConfiguration())

    @Test
    fun `should deploy workflow`() {
        // given
        val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")

        // when
        workflowEngine.deploy(workflow)

        // then
    }

    @Test
    fun `should throw an exception on missing workflow`() {
        // expected
        val exception = assertThrows<IllegalArgumentException> ("Should throw an exception") {
            workflowEngine.create("order-process")
        }
        assertEquals("no workflow found with BPMN process id 'order-process'", exception.message)
    }

    @Test
    fun `should create this fu* workflow`() {
        // given
        val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")
        workflowEngine.deploy(workflow)

        // when
        val key = workflowEngine.create("order-process")

        // then
        assertThat(key).isGreaterThan(0)
        assertThat(workflowEngine.configuration.taskService.findByType("collect-money")).isNotEmpty
    }
}