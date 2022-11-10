package dao

import entity.Assistance
import entity.AssistanceKind
import entity.User
import entity.UserType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateDAOAssistantTest {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var assistanceDAO: HibernateAssistanceDAO
    private lateinit var userOne: User
    private lateinit var userTwo: User
    private lateinit var assistanceOne: Assistance
    private lateinit var assistanceTwo: Assistance

    @BeforeAll
    fun setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("GAV")
    }

    @BeforeEach
    fun setUp() {
        entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()

        assistanceDAO = HibernateAssistanceDAO(entityManager)

        userOne = User(
            "Test",
            "McTest",
            UserType.ASSISTANCE,
            "email@email.com",
            "55556666"
        )
        userTwo = User(
            "German",
            "McRonalds",
            UserType.ASSISTANCE,
            "german@email.com",
            "0303456664"
        )

        assistanceOne = Assistance(AssistanceKind.LARGE, userOne, 250.0, 500.0, 150.0)
        assistanceTwo = Assistance(AssistanceKind.SMALL, userTwo, 325.0, 425.0, 150.0)
    }

    @AfterEach
    fun tearDown() {
        entityManager.transaction.rollback()
        entityManager.close()
    }

    @Test
    fun `basic assistance checks`() {
        val newAssistanceOne = assistanceDAO.save(assistanceOne)

        assertEquals(assistanceOne.user, newAssistanceOne.user)
        assertEquals(assistanceOne.kind, newAssistanceOne.kind)
        assertEquals(assistanceOne.costPerKm, newAssistanceOne.costPerKm)
        assertEquals(assistanceOne.fixedCost, newAssistanceOne.fixedCost)
        assertEquals(assistanceOne.cancellationCost, newAssistanceOne.cancellationCost)
    }

    @Test
    fun `filter by kind returns an empty list`() {
        val assistanceRecords = assistanceDAO.findAllByKind(AssistanceKind.SMALL.toString())

        assertTrue { assistanceRecords.isEmpty() }
    }

    @Test
    fun `filter by kind returns a non-empty list`() {
        val newAssistanceOne = assistanceDAO.save(assistanceOne)
        val newAssistanceTwo = assistanceDAO.save(assistanceTwo)

        val a = AssistanceKind.SMALL.toString()

        val assistanceRecords = assistanceDAO.findAllByKind(AssistanceKind.SMALL.toString())

        assertContentEquals(listOf(newAssistanceTwo), assistanceRecords)
    }
}
