package services

import dao.HibernateOrderDAO
import entity.Order
import java.util.*

class OrderServiceImpl(private val orderDAO: HibernateOrderDAO) {

    fun save(order: Order): Order {
        return orderDAO.save(order)
    }

    fun update(order: Order) {
        orderDAO.update(order)
    }

    fun find(orderId: UUID): Order {
        return orderDAO.find(orderId)
    }
}
