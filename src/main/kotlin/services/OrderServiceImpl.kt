package services

import api.controllers.Validator
import api.dtos.OrderCreateRequestDTO
import api.dtos.OrderUpdateRequestDTO
import api.dtos.ScoreRequestDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.Order
import entity.OrderStatus
import entity.UserType
import java.util.*

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
        if (validator.containsSpecialCharacter(orderCreateRequest.city) ||
            validator.containsNumber(orderCreateRequest.city)
        ) {
            throw RuntimeException("City can not contain special characters or numbers")
        }
        if (validator.containsSpecialCharacter(orderCreateRequest.province) ||
            validator.containsNumber(orderCreateRequest.province)
        ) {
            throw RuntimeException("Province can not contain special characters or numbers")
        }
        if (!validator.isAllNumbers(orderCreateRequest.phoneNumber) ||
            orderCreateRequest.phoneNumber?.length != 10
        ) {
            throw RuntimeException("Phone Number must be all numbers and 10 digits long")
        }

        val assistance = assistanceDAO.find(orderCreateRequest.assistanceId!!)
        val user = userDAO.find(orderCreateRequest.userId!!)

        if (user.type != UserType.CLIENT) {
            throw RuntimeException("User is not a customer")
        }

        val newOrder = Order(
            assistance,
            user,
            OrderStatus.PENDING_APPROVAL,
            orderCreateRequest.street!!,
            orderCreateRequest.betweenStreets!!,
            orderCreateRequest.city!!,
            orderCreateRequest.province!!,
            orderCreateRequest.phoneNumber,
            assistance.costPerKm,
            assistance.fixedCost,
            assistance.cancellationCost
        )
        return orderDAO.save(newOrder)
    }

    fun updateOrderStatus(orderUpdateRequest: OrderUpdateRequestDTO): Order {
        val order = orderDAO.find(orderUpdateRequest.orderId!!)

        val newStatus = OrderStatus.valueOf(orderUpdateRequest.status!!)

        if (newStatus == order.status) {
            throw RuntimeException("Order can not be updated because its status is already ${order.status}")
        }
        if (order.status in setOf(OrderStatus.CANCELLED, OrderStatus.COMPLETED)) {
            throw RuntimeException("Order can not be updated from status ${order.status} to ${orderUpdateRequest.status}")
        }
        if (newStatus == OrderStatus.COMPLETED && order.status == OrderStatus.PENDING_APPROVAL) {
            throw RuntimeException("Order can not be updated from status ${order.status} to ${orderUpdateRequest.status}")
        }
        if (newStatus == OrderStatus.PENDING_APPROVAL && order.status != OrderStatus.PENDING_APPROVAL) {
            throw RuntimeException("Order can not be updated from status ${order.status} to ${orderUpdateRequest.status}")
        }
        if (newStatus == OrderStatus.COMPLETED && (orderUpdateRequest.kmTraveled == null || orderUpdateRequest.kmTraveled <= 0)) {
            throw RuntimeException("Traveled kilometers can not be zero or a negative number")
        }

        if (order.status == OrderStatus.IN_PROGRESS && newStatus == OrderStatus.COMPLETED) {
            order.kmTraveled = orderUpdateRequest.kmTraveled
        } else if (newStatus == OrderStatus.CANCELLED) {
            val assistance = assistanceDAO.find(order.assistance.id!!)
            val user = userDAO.find(order.user.id!!)
            order.cancellationCost = assistance.cancellationCost
            user.debts += assistance.cancellationCost
        }

        order.status = newStatus

        return orderDAO.update(order)
    }

    fun findOrder(orderId: UUID): Order {
        return orderDAO.find(orderId)
    }

    fun findAll(status: String? = null): List<Order> {
        val orders = if (status.isNullOrBlank()) {
            orderDAO.findAll()
        } else {
            val orderStatus = status.split(",").map { OrderStatus.valueOf(it) }
            orderDAO.findAllByStatus(orderStatus)
        }
        return orders
    }

    fun addScore(orderScoreRequest: ScoreRequestDTO): Any {
        val order = orderDAO.find(orderScoreRequest.orderId!!)
        if (order.score > 0) {
            throw RuntimeException("The order ${orderScoreRequest.orderId} has already been scored")
        }
        if (order.user.id!! != orderScoreRequest.userId) {
            throw RuntimeException("The order ${orderScoreRequest.orderId} does not correspond to the user ${orderScoreRequest.userId}")
        }
        if (orderScoreRequest.score < 1 || orderScoreRequest.score > 5) {
            throw RuntimeException("The score ${orderScoreRequest.score} must be greater than or equal to 1 or less than or equal to 5")
        }
        order.score = orderScoreRequest.score
        return orderDAO.update(order)
    }
}
