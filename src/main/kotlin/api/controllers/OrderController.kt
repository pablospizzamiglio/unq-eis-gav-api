package api.controllers

import api.dtos.SimpleOrderDTO
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
                .check({ obj -> !obj.addressStreet.isNullOrBlank() }, "The address street was not loaded")
                .check(
                    { obj -> !obj.addressBetweenStreets.isNullOrBlank() },
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
                dates.addressStreet,
                dates.addressBetweenStreets,
                dates.city,
                dates.province,
                dates.phoneNumber,
                assistance.costPerKm,
                assistance.fixedCost,
                Status.IN_PROGRESS
            )
            val order = runTrx {
                orderDAO.save(simpleOrder)
            }
            ctx.json(order)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }
}
