package api.controllers

import api.dtos.SimpleOrderDTO
import api.dtos.SimpleUpdateOrderDTO
import entity.Order
import entity.Status
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import services.AssistanceServiceImpl
import services.OrderServiceImpl
import services.UserServiceImpl

class OrderController(
    private val orderService: OrderServiceImpl,
    private val assistanceService: AssistanceServiceImpl,
    private val userService: UserServiceImpl
) {

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
            val user = userService.find(newOrderRequest.userId)
            val assistance = assistanceService.find(newOrderRequest.assistanceId)
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
            val order = orderService.save(newOrder)
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
            var order = orderService.find(orderToUpdate.orderId)
            order.status = enumValueOf(orderToUpdate.status)
            val updatedOrder = orderService.update(order)
            ctx.json(updatedOrder)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        } catch (e: java.lang.NullPointerException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
