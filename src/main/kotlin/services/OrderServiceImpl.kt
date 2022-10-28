package services

import api.controllers.Validator
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
    private val validator = Validator()
    fun createOrder(orderCreateRequest: OrderCreateRequestDTO): Order {
        if (validator.containsSpecialCharacter(orderCreateRequest.street)) {
            throw RuntimeException("Street can not contain special characters")
        }
        if (validator.containsSpecialCharacter(orderCreateRequest.betweenStreets)) {
            throw RuntimeException("Between streets can not contain special characters")
        }
        if (validator.containsSpecialCharacter(orderCreateRequest.city) || validator.containsNumbers(orderCreateRequest.city)) {
            throw RuntimeException("City can not contain special characters or numbers")
        }
        if (validator.containsSpecialCharacter(orderCreateRequest.province) || validator.containsNumbers(orderCreateRequest.province)) {
            throw RuntimeException("Province can not contain special characters or numbers")
        }
        if (!validator.isAllNumbers(orderCreateRequest.phoneNumber) || orderCreateRequest.phoneNumber?.length != 10) {
            throw RuntimeException("Phone Number must be all numbers and 10 digits long")
        }

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
        val order = orderDAO.find(orderUpdateRequest.orderId!!)
        order.status = OrderStatus.valueOf(orderUpdateRequest.status!!)
        return orderDAO.update(order)
    }
}
