package repository

import dao.HibernateDataDAO
import dao.HibernateUserDAO
import entity.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import services.DataServiceImpl
import transaction.TransactionRunner.runTrx
import kotlin.test.assertContains
import kotlin.test.assertEquals

class HibernateDAOUserTest {
    @Test
    fun `basic entity checks`() {
        val newUser = User(
            "Test",
            "McTest",
            "CLIENT",
            "email@email.com",
            "55556666"
        )
        val hashCodeBefore = newUser.hashCode()
        val userSet = hashSetOf(newUser)

        val userDAO = HibernateUserDAO()
        val user = runTrx {
            userDAO.save(newUser)
        }

        val hashCodeAfter = user.hashCode()

        assertEquals(newUser, user)
        assertContains(userSet, user)
        assertEquals(hashCodeBefore, hashCodeAfter)

        val recoveredUser = runTrx {
            userDAO.find(user.id!!)
        }

        assertEquals(user, recoveredUser)
    }

    @AfterEach
    fun cleanup() {
        DataServiceImpl(HibernateDataDAO()).clear()
    }
}
