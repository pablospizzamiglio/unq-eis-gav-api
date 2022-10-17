package repository

import dao.HibernateAssistanceDAO
import dao.HibernateDataDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import services.DataServiceImpl
import transaction.TransactionRunner.runTrx

class HibernateDAOOrderTest {
    @Test
    fun `basic order update`() {
        val assistanceDAO = HibernateAssistanceDAO()
        val orderDAO = HibernateOrderDAO()
        val userDAO = HibernateUserDAO()

        val newUser = User(
            "Test",
            "McTest",
            "ASSISTANCE",
            "email@email.com",
            "55556666"
        )

        val persistedUser = runTrx {
            userDAO.save(newUser)
        }

        val newAssistance = Assistance(Kind.LARGE, 250.0, 500.0, persistedUser)

        val persistedAssistance = runTrx {
            assistanceDAO.save(newAssistance)
        }

        val order = Order(
            persistedAssistance,
            "q",
            "q",
            "q",
            "q",
            "1111111111",
            persistedAssistance.costPerKm,
            persistedAssistance.fixedCost,
            Status.PENDING_APPROVAL,
            persistedUser
        )

        val persistedOrder = runTrx {
            orderDAO.save(order)
        }

        persistedOrder.status = Status.COMPLETE

        runTrx {
            orderDAO.update(persistedOrder)
        }

        val updatedOrder = runTrx {
            orderDAO.find(persistedOrder.id!!)
        }

        Assertions.assertThat(updatedOrder.status).isEqualTo(Status.COMPLETE)
    }


    @AfterEach
    fun cleanup() {
        DataServiceImpl(HibernateDataDAO()).clear()
    }
}
