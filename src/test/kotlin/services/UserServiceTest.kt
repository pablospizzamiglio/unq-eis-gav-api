package services

import dao.HibernateUserDAO
import entity.User
import org.junit.jupiter.api.*
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var userDAO: HibernateUserDAO
    private lateinit var userService: UserServiceImpl


    @BeforeAll
    fun setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("GAV")
    }

    @BeforeEach
    fun setUp() {
        entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()

        userDAO = HibernateUserDAO(entityManager)
        userService = UserServiceImpl(userDAO)
    }

    @AfterEach
    fun tearDown() {
        entityManager.transaction.rollback()
        entityManager.close()
    }

    @Test
    fun `firstname with special character rejected`() {
        val user = User(
            "Li" + "$" + "a",
            "Simpson",
            "CLIENT",
            "lisa.simpson@email.com",
            "1122223333"
        )

        assertThrows<RuntimeException> { userService.save(user) }

        val users = userService.findAll()

        assertTrue { null == users.find { user: User -> user.emailAddress == "lisa.simpson@email.com" } }
    }
}