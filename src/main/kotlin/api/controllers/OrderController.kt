package api.controllers

import api.dtos.SimpleOrderDTO
import api.dtos.SimpleUpdateOrderDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import entity.Order
import entity.Status
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import transaction.TransactionRunner.runTrx

class OrderController(private val orderDAO: HibernateOrderDAO, private val assistanceDAO: HibernateAssistanceDAO) {

    fun generateOrder(ctx: Context) {
        try {
            val newOrder = ctx.bodyValidator<SimpleOrderDTO>()
                .check({ obj -> !obj.assistanceId.toString().isNullOrBlank() }, "The assistant id was not loaded")
                .check({ obj -> !obj.street.isNullOrBlank() }, "The address street was not loaded")
                .check(
                    { obj -> !obj.betweenStreets.isNullOrBlank() },
                    "The address between streets was not loaded"
                )
                .check({ obj -> !obj.city.isNullOrBlank() }, "The city was not loaded")
                .check({ obj -> !obj.province.isNullOrBlank() }, "The province id was not loaded")
                .check({ obj -> !obj.phoneNumber.isNullOrBlank() }, "The phone number was not loaded").get()
            val assistance = runTrx {
                assistanceDAO.find(newOrder.assistanceId)
            }
            val simpleOrder = Order(
                assistance,
                newOrder.street,
                newOrder.betweenStreets,
                newOrder.city,
                newOrder.province,
                newOrder.phoneNumber,
                assistance.costPerKm,
                assistance.fixedCost,
                Status.PENDING_APPROVAL
            )
            val order = runTrx {
                orderDAO.save(simpleOrder)
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
