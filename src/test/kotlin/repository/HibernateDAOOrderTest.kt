package repository

import dao.HibernateAssistanceDAO
import dao.HibernateDataDAO
import dao.HibernateOrderDAO
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
        val userOne = User(
            "Test",
            "McTest",
            "ASSISTANCE",
            "email@email.com",
            "55556666"
        )

        val assistanceOne = Assistance(Kind.LARGE, 250.0, 500.0, userOne)

        val assOne = runTrx {
            assistanceDAO.save(assistanceOne)
        }

        val order = Order(
            assOne.id!!,
            "q",
            "q",
            "q",
            "q",
            1111111111,
            assOne.costPerKm,
            assOne.fixedCost,
            Status.PENDING_APPROVAL
        )

        val orderOne = runTrx {
            orderDAO.save(order)
        }

        orderOne.status = Status.COMPLETE

        runTrx {
            orderDAO.update(orderOne)
        }

        val orderToUpdate = runTrx {
            orderDAO.find(orderOne.id!!)
        }

        Assertions.assertThat(orderToUpdate.status).isEqualTo(Status.COMPLETE)
    }


    @AfterEach
    fun cleanup() {
        DataServiceImpl(HibernateDataDAO()).clear()
    }
}
