package repository

import dao.HibernateDataDAO
import dao.HibernateUserDAO
import entity.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import services.DataServiceImpl
import transaction.TransactionRunner.runTrx
import java.util.*
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

    @Test
    fun `basic debts checks`() {
        val newUser = User(
            "Test",
            "McTest",
            "CLIENT",
            "email@email.com",
            "55556666"
        )

        val userDAO = HibernateUserDAO()
        val user = runTrx {
            userDAO.save(newUser)
        }

        assertEquals(user.debts, 0.0)
    }

    @Test
    fun `check if find user`() {
        val newUser = User(
            "Test",
            "McTest",
            "CLIENT",
            "email@email.com",
            "55556666"
        )

        val userDAO = HibernateUserDAO()
        val user = runTrx {
            userDAO.save(newUser)
        }

        val userFind = runTrx { userDAO.find(user.id!!) }

        assertEquals(user, userFind)
    }

    @Test
    fun `check if it returns error when not finding user`() {
        val userDAO = HibernateUserDAO()

        assertThrows<RuntimeException> {
            runTrx { userDAO.find(UUID.fromString("5f700ac8-0efe-4683-9798-6595c52d1668")) }
        }
    }

    @Test
    fun `check if the user was updated`() {
        val newUser = User(
            "Test",
            "McTest",
            "CLIENT",
            "email@email.com",
            "55556666"
        )

        val userDAO = HibernateUserDAO()
        var user = runTrx {
            userDAO.save(newUser)
        }

        user.firstName = "German"
        runTrx { userDAO.update(user) }
        val userFind = runTrx { userDAO.find(user.id!!) }

        assertEquals(userFind.firstName, "German")
    }

    @AfterEach
    fun cleanup() {
        DataServiceImpl(HibernateDataDAO()).clear()
    }
}
