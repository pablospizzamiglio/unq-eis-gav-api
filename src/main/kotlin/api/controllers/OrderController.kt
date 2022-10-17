package api.controllers

import api.dtos.SimpleOrderDTO
import api.dtos.SimpleUpdateOrderDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.Order
import entity.Status
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import transaction.TransactionRunner.runTrx

class OrderController(private val orderDAO: HibernateOrderDAO, private val assistanceDAO: HibernateAssistanceDAO, private val userDAO: HibernateUserDAO) {

    fun generateOrder(ctx: Context) {
        try {
            val newOrderRequest = ctx.bodyValidator<SimpleOrderDTO>()
                .check({ obj -> obj.assistanceId != null }, "The assistant id was not loaded")
                .check({ obj -> !obj.street.isNullOrBlank() }, "The address street was not loaded")
                .check(
                    { obj -> !obj.betweenStreets.isNullOrBlank() },
                    "The address between streets was not loaded"
                )
                .check({ obj -> !obj.city.isNullOrBlank() }, "The city was not loaded")
                .check({ obj -> !obj.province.isNullOrBlank() }, "The province id was not loaded")
                .check({ obj -> !obj.phoneNumber.isNullOrBlank() }, "The phone number was not loaded")
                .check({ obj -> obj.userId != null }, "The user id was not loaded")
                .get()
            val order = runTrx {
                val assistance = assistanceDAO.find(newOrderRequest.assistanceId)
                val user = userDAO.find(newOrderRequest.userId)
                val newOrder = Order(
                    assistance,
                    newOrderRequest.street,
                    newOrderRequest.betweenStreets,
                    newOrderRequest.city,
                    newOrderRequest.province,
                    newOrderRequest.phoneNumber,
                    assistance.costPerKm,
                    assistance.fixedCost,
                    Status.PENDING_APPROVAL,
                    user,
                )
                orderDAO.save(newOrder)
            }
            ctx.json(order)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

    fun updateOrder(ctx: Context) {
        try {
            val orderToUpdate = ctx.bodyValidator<SimpleUpdateOrderDTO>()
                .check({ obj -> !obj.orderId.toString().isNullOrBlank() }, "The order id was not loaded")
                .check({ obj -> !obj.status.isNullOrBlank() }, "The status was not loaded")
                .check({ obj -> !obj.password.isNullOrBlank() }, "The password was not loaded")
                .check({ obj -> obj.password == "0303456" }, "Wrong password").get()
            var order = runTrx {
                orderDAO.find(orderToUpdate.orderId)
            }
            order.status = enumValueOf(orderToUpdate.status)
            val updatedOrder = runTrx {
                orderDAO.update(order)
            }
            ctx.json(updatedOrder)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        } catch (e: java.lang.NullPointerException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
