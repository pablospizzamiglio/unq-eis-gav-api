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
            val dates = ctx.bodyValidator<SimpleOrderDTO>()
                .check({ obj -> !obj.assistanceId.toString().isNullOrBlank() }, "The assistant id was not loaded")
                .check({ obj -> !obj.street.isNullOrBlank() }, "The address street was not loaded")
                .check(
                    { obj -> !obj.betweenStreets.isNullOrBlank() },
                    "The address between streets was not loaded"
                )
                .check({ obj -> !obj.city.isNullOrBlank() }, "The city was not loaded")
                .check({ obj -> !obj.province.isNullOrBlank() }, "The province id was not loaded")
                .check({ obj -> !obj.phoneNumber.toString().isNullOrBlank() }, "The phonenumber was not loaded").get()
            val assistance = runTrx {
                assistanceDAO.find(dates.assistanceId)
            }
            val simpleOrder = Order(
                assistance.id!!,
                dates.street,
                dates.betweenStreets,
                dates.city,
                dates.province,
                dates.phoneNumber,
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
            val dates = ctx.bodyValidator<SimpleUpdateOrderDTO>()
                .check({ obj -> !obj.orderId.toString().isNullOrBlank() }, "The order id was not loaded")
                .check({ obj -> !obj.status.isNullOrBlank() }, "The status was not loaded")
                .check({ obj -> !obj.password.isNullOrBlank() }, "The password was not loaded")
                .check({ obj -> obj.password == "0303456" }, "Wrong password").get()
            var orderToUpdate = runTrx {
                orderDAO.find(dates.orderId)
            }
            orderToUpdate.status = enumValueOf(dates.status)
            val order = runTrx {
                orderDAO.update(orderToUpdate)
            }
            ctx.json(order)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }catch (e: java.lang.NullPointerException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
