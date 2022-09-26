package api.controllers

import api.dtos.SimpleOrderDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import entity.Order
import entity.Status
import io.javalin.http.Context
import transaction.TransactionRunner.runTrx

class OrderController(private val orderDAO: HibernateOrderDAO, private val assistanceDAO: HibernateAssistanceDAO) {

    fun generateOrder(ctx: Context) {
        val dates = ctx.bodyAsClass(SimpleOrderDTO::class.java)
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
    }
}
