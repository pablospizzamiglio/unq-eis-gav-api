package services

import dao.HibernateDataDAO
import dao.HibernateUserDAO
import entity.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserServiceTest {
    private val userServiceImpl = UserServiceImpl(HibernateUserDAO())

    @Test
    fun `firstname with special character rejected`() {
        val user = User(
            "Li" + "$" + "a",
            "Simpson",
            "CLIENT",
            "lisa.simpson@email.com",
            "1122223333"
        )

        assertThrows<RuntimeException> { userServiceImpl.save(user) }

        val users = userServiceImpl.findAll()

        assertTrue { null == users.find { user: User -> user.emailAddress == "lisa.simpson@email.com" } }
    }

    @AfterEach
    fun cleanup() {
        DataServiceImpl(HibernateDataDAO()).clear()
    }
}
