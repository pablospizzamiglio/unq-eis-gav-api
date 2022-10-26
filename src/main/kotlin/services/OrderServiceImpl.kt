package services

import api.dtos.OrderCreateRequestDTO
import api.dtos.OrderUpdateRequestDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.Order
import entity.OrderStatus

class OrderServiceImpl(
    private val orderDAO: HibernateOrderDAO,
    private val assistanceDAO: HibernateAssistanceDAO,
    private val userDAO: HibernateUserDAO
) {
    fun createOrder(orderCreateRequest: OrderCreateRequestDTO): Order {
        val assistance = assistanceDAO.find(orderCreateRequest.assistanceId!!)
        val user = userDAO.find(orderCreateRequest.userId!!)
        val newOrder = Order(
            assistance,
            orderCreateRequest.street!!,
            orderCreateRequest.betweenStreets!!,
            orderCreateRequest.city!!,
            orderCreateRequest.province!!,
            orderCreateRequest.phoneNumber!!,
            assistance.costPerKm,
            assistance.fixedCost,
            OrderStatus.PENDING_APPROVAL,
            user,
        )
        return orderDAO.save(newOrder)
    }

    fun updateOrderStatus(orderUpdateRequest: OrderUpdateRequestDTO): Order {
        var order = orderDAO.find(orderUpdateRequest.orderId!!)
        order.status = OrderStatus.valueOf(orderUpdateRequest.status!!)
        return orderDAO.update(order)
    }
}
