package repository

import dao.HibernateUserDAO
//import entity.Address
import entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import transaction.TransactionRunner.runTrx

class UserRepositoryTests {
    @Test
    fun `basic entity checks`() {
        val u = User(
            "Test",
            "McTest",
            "CLIENT",
            "email@email.com",
            "55556666"
        )
        val hashCodeBefore = u.hashCode()
        val userSet = hashSetOf(u)

        val userDAO = HibernateUserDAO()

        val user = runTrx {
            userDAO.save(u)
        }

        val hashCodeAfter = user.hashCode()

        assertThat(user).isEqualTo(u)
        assertThat(userSet).contains(u)
        assertThat(hashCodeAfter).isEqualTo(hashCodeBefore)
    }
}
