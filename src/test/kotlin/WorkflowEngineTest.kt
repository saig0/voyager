import io.zeebe.voyager.WorkflowEngine
import io.zeebe.voyager.inmemory.InMemoryConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
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
        assertThat(workflowEngine.findTasksByType("collect-money")).isNotEmpty
    }

    @Test
    fun `should find task by type`() {
        // given
        val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")
        workflowEngine.deploy(workflow)
        val key = workflowEngine.create("order-process")

        // when
        val tasksByType = workflowEngine.findTasksByType("collect-money")

        // then
        assertThat(tasksByType).isNotEmpty

        val firstTask = tasksByType[0]
        assertThat(firstTask.instanceKey).isEqualTo(key)
        assertThat(firstTask.type).isEqualTo("collect-money")
        assertThat(firstTask.key).isGreaterThan(0)
    }

    @Test
    fun `should complete task`() {
        // given
        val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")
        workflowEngine.deploy(workflow)
        workflowEngine.create("order-process")
        val tasksByType = workflowEngine.findTasksByType("collect-money")

        // when
        workflowEngine.completeTask(tasksByType[0])

        // then
        assertThat(workflowEngine.findTasksByType("collect-money")).isEmpty()
        assertThat(workflowEngine.findTasksByType("fetch-items")).isNotEmpty
    }

    @Test
    fun `should not complete task twice`() {
        // given
        val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")
        workflowEngine.deploy(workflow)
        workflowEngine.create("order-process")
        val tasksByType = workflowEngine.findTasksByType("collect-money")

        // when
        workflowEngine.completeTask(tasksByType[0])

        val exception = assertThrows<IllegalArgumentException>("should throw exception")
        {
            workflowEngine.completeTask(tasksByType[0])
        }

        assertThat(exception).hasMessageContaining("Expected to continue workflow instance")
    }
    @Test
    fun `should complete instance`() {
        // given
        val workflow = WorkflowEngine::class.java.classLoader.getResourceAsStream("orderProcess.bpmn")
        workflowEngine.deploy(workflow)
        val instanceKey = workflowEngine.create("order-process")

        // when
        val collectMoneyType = workflowEngine.findTasksByType("collect-money")
        workflowEngine.completeTask(collectMoneyType[0])

        val fetchItemsType = workflowEngine.findTasksByType("fetch-items")
        workflowEngine.completeTask(fetchItemsType[0])

        val shipParcelType = workflowEngine.findTasksByType("ship-parcel")
        workflowEngine.completeTask(shipParcelType[0])

        // then
        assertThat(workflowEngine.findTasksByType("collect-money")).isEmpty()
        assertThat(workflowEngine.findTasksByType("fetch-items")).isEmpty()
        assertThat(workflowEngine.findTasksByType("ship-parcel")).isEmpty()

        assertThat(workflowEngine.configuration.instanceService.getInstance(instanceKey)).isNull()
    }
}