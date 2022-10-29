package dao

import entity.User
import entity.UserType
import org.junit.jupiter.api.*
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.test.assertContains
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateDAOUserTest {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var userDAO: HibernateUserDAO

    @BeforeAll
    fun setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("GAV")
    }

    @BeforeEach
    fun setUp() {
        entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()

        userDAO = HibernateUserDAO(entityManager)
    }

    @AfterEach
    fun tearDown() {
        entityManager.transaction.rollback()
        entityManager.close()
    }

    @Test
    fun `basic entity checks`() {
        val newUser = User(
            "Test",
            "McTest",
            UserType.CLIENT,
            "email@email.com",
            "55556666"
        )
        val hashCodeBefore = newUser.hashCode()
        val userSet = hashSetOf(newUser)

        val user = userDAO.save(newUser)

        val hashCodeAfter = user.hashCode()

        assertEquals(newUser, user)
        assertContains(userSet, user)
        assertEquals(hashCodeBefore, hashCodeAfter)

        val recoveredUser = userDAO.find(user.id!!)

        assertEquals(user, recoveredUser)
    }

    @Test
    fun `basic debts checks`() {
        val newUser = User(
            "Test",
            "McTest",
            UserType.CLIENT,
            "email@email.com",
            "55556666"
        )

        val user = userDAO.save(newUser)

        assertEquals(user.debts, 0.0)
    }

    @Test
    fun `check if find user`() {
        val newUser = User(
            "Test",
            "McTest",
            UserType.CLIENT,
            "email@email.com",
            "55556666"
        )

        val user = userDAO.save(newUser)

        val userFind = userDAO.find(user.id!!)

        assertEquals(user, userFind)
    }

    @Test
    fun `check if it returns error when not finding user`() {
        assertThrows<RuntimeException> {
            userDAO.find(UUID.fromString("5f700ac8-0efe-4683-9798-6595c52d1668"))
        }
    }

    @Test
    fun `check if the user was updated`() {
        val newUser = User(
            "Test",
            "McTest",
            UserType.CLIENT,
            "email@email.com",
            "55556666"
        )

        val user = userDAO.save(newUser)
        user.firstName = "German"
        userDAO.update(user)
        val userFind = userDAO.find(user.id!!)

        assertEquals(userFind.firstName, "German")
    }
}
