package services

import dao.HibernateOrderDAO
import entity.Order
import transaction.TransactionRunner.runTrx
import java.util.*

class OrderServiceImpl(
    private val orderDAO: HibernateOrderDAO
) {

    fun save(order: Order): Order {
        return runTrx {
            orderDAO.save(order)
        }
    }

    fun update(order: Order) {
        return runTrx {
            orderDAO.update(order)
        }
    }

    fun find(orderId: UUID): Order {
        return runTrx {
            orderDAO.find(orderId)
        }
    }
}
