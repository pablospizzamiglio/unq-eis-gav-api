package api.controllers

import api.dtos.OrderCreateRequestDTO
import api.dtos.OrderUpdateRequestDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entityManager
import io.javalin.core.validation.ValidationException
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import services.OrderServiceImpl
import java.util.*

class OrderController {
    fun createOrder(ctx: Context) {
        try {
            val orderCreateRequest = ctx.bodyValidator<OrderCreateRequestDTO>()
                .check({ obj -> obj.assistanceId != null }, "Assistant Id can not be empty")
                .check({ obj -> !obj.street.isNullOrBlank() }, "Street can not be empty")
                .check({ obj -> !obj.betweenStreets.isNullOrBlank() }, "Between streets can not be empty")
                .check({ obj -> !obj.city.isNullOrBlank() }, "City can not be empty")
                .check({ obj -> !obj.province.isNullOrBlank() }, "Province can not be empty")
                .check({ obj -> !obj.phoneNumber.isNullOrBlank() }, "Phone Number can not be empty")
                .check({ obj -> obj.userId != null }, "User Id can not be empty")
                .get()

            val assistanceDAO = HibernateAssistanceDAO(ctx.entityManager)
            val userDAO = HibernateUserDAO(ctx.entityManager)
            val orderDAO = HibernateOrderDAO(ctx.entityManager)
            val orderService = OrderServiceImpl(orderDAO, assistanceDAO, userDAO)

            ctx.entityManager.transaction.begin()
            val order = orderService.createOrder(orderCreateRequest)
            ctx.entityManager.transaction.commit()

            ctx.json(order)
        } catch (e: ValidationException) {
            ctx.entityManager.transaction.rollback()
            throw e
        } catch (e: Exception) {
            ctx.entityManager.transaction.rollback()
            throw BadRequestResponse(e.message!!)
        }
    }

    fun updateOrder(ctx: Context) {
        try {
            val orderUpdateRequest = ctx.bodyValidator<OrderUpdateRequestDTO>()
                .check({ obj -> obj.orderId != null }, "Order Id can not be empty")
                .check({ obj -> !obj.status.isNullOrBlank() }, "Status can not be empty")
                .check({ obj -> !obj.password.isNullOrBlank() && obj.password == "0303456" }, "Password is incorrect")
                .get()

            val assistanceDAO = HibernateAssistanceDAO(ctx.entityManager)
            val userDAO = HibernateUserDAO(ctx.entityManager)
            val orderDAO = HibernateOrderDAO(ctx.entityManager)
            val orderService = OrderServiceImpl(orderDAO, assistanceDAO, userDAO)

            ctx.entityManager.transaction.begin()
            val order = orderService.updateOrderStatus(orderUpdateRequest)
            ctx.entityManager.transaction.commit()

            ctx.json(order)
        } catch (e: ValidationException) {
            ctx.entityManager.transaction.rollback()
            throw e
        } catch (e: Exception) {
            ctx.entityManager.transaction.rollback()
            throw BadRequestResponse(e.message!!)
        }
    }

    fun findOrder(ctx: Context) {
        try {
            val id = ctx.pathParam("id")
            val orderId = UUID.fromString(id)

            val assistanceDAO = HibernateAssistanceDAO(ctx.entityManager)
            val userDAO = HibernateUserDAO(ctx.entityManager)
            val orderDAO = HibernateOrderDAO(ctx.entityManager)
            val orderService = OrderServiceImpl(orderDAO, assistanceDAO, userDAO)

            val order = orderService.findOrder(orderId)
            ctx.json(order)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }
}
