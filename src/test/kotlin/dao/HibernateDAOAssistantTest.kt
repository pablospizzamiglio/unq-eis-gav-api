package dao

import entity.Assistance
import entity.Kind
import entity.User
import entity.UserType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateDAOAssistantTest {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var assistanceDAO: HibernateAssistanceDAO

    @BeforeAll
    fun setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("GAV")
    }

    @BeforeEach
    fun setUp() {
        entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()

        assistanceDAO = HibernateAssistanceDAO(entityManager)
    }

    @AfterEach
    fun tearDown() {
        entityManager.transaction.rollback()
        entityManager.close()
    }

    @Test
    fun `basic assistance checks`() {
        val userOne = User(
            "Test",
            "McTest",
            UserType.ASSISTANCE,
            "email@email.com",
            "55556666"
        )
        val userTwo = User(
            "German",
            "McRonalds",
            UserType.ASSISTANCE,
            "german@email.com",
            "0303456664"
        )

        val assistanceOne = Assistance(Kind.LARGE, 250.0, 500.0, userOne)
        val assistanceTwo = Assistance(Kind.MEDIUM, 325.0, 425.0, userTwo)

        val assOne = assistanceDAO.save(assistanceOne)
        val assTwo = assistanceDAO.save(assistanceTwo)

        Assertions.assertThat(userOne).isEqualTo(assOne.user)
        Assertions.assertThat(userTwo).isEqualTo(assTwo.user)
    }

    @Test
    fun `wizard filtering by kind no data`() {
        val assistanceRecords = assistanceDAO.findAllByKind("SMALL")

        Assertions.assertThat(assistanceRecords).isEmpty()
    }

    @Test
    fun `wizard filtering by kind with data`() {
        val userOne = User(
            "Test",
            "McTest",
            UserType.ASSISTANCE,
            "email@email.com",
            "55556666"
        )
        val userTwo = User(
            "German",
            "McRonalds",
            UserType.ASSISTANCE,
            "german@email.com",
            "0303456664"
        )

        val assistanceOne = Assistance(Kind.LARGE, 250.0, 500.0, userOne)
        val assistanceTwo = Assistance(Kind.SMALL, 325.0, 425.0, userTwo)

        assistanceDAO.save(assistanceOne)
        val assTwo = assistanceDAO.save(assistanceTwo)
        val result = assistanceDAO.findAllByKind("SMALL")

        Assertions.assertThat(result).contains(assTwo)
    }
}
