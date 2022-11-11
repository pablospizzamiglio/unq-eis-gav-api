package services

import api.dtos.UserCreateRequestDTO
import dao.HibernateUserDAO
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
    fun `create user`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bello",
            "CLIENT",
            "luisbello@email.com",
            "5555555555"
        )

        userService.createUser(user)
        val users = userService.findAll()

        assertTrue { users.size == 1 }
    }

    @Test
    fun `firstname with special character throws an exception`() {
        val user = UserCreateRequestDTO(
            "Li\$a",
            "Simpson",
            "CLIENT",
            "lisa.simpson@email.com",
            "1122223333"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `firstname with numbers throws an exception`() {
        val user = UserCreateRequestDTO(
            "Lu1s",
            "Bello",
            "CLIENT",
            "luisbello@email.com",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `lastname with special character throws an exception`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bell++o",
            "CLIENT",
            "luisbello@email.com",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `lastname with numbers throws an exception`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bell05",
            "CLIENT",
            "luisbello@email.com",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `email with special character before the @ throws an exception`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bello",
            "CLIENT",
            "luis@bello@email.com",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `email without com throws an exception`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bello",
            "CLIENT",
            "luisbello@email",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `email without @ throws an exception`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bello",
            "CLIENT",
            "luisbello.email.com",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `user with invalid type throws an exception`() {
        val user = UserCreateRequestDTO(
            "Luis",
            "Bello",
            "CLIE",
            "luisbello.email.com",
            "5555555555"
        )

        assertThrows<RuntimeException> { userService.createUser(user) }

        val users = userService.findAll()

        assertTrue { users.isEmpty() }
    }

    @Test
    fun `Registration with email already registered throws an exception`() {
        val user = UserCreateRequestDTO(
            "Lisa",
            "Simpsons",
            "CLIENT",
            "lisa.simpson@email.com",
            "1122223333"
        )

        userService.createUser(user)
        val users = userService.findAll()

        assertThrows<RuntimeException> { userService.createUser(user) }
        assertTrue { users.size == 1 }
    }


}
