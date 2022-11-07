package api.dtos

import entity.Assistance
import entity.Order
import entity.OrderStatus
import entity.User
import java.util.*

class OrderDTO(
    val id: UUID,
    val assistance: Assistance,
    var street: String,
    var city: String,
    var province: String,
    var phoneNumber: String,
    var status: OrderStatus,
    val user: User
) {
    companion object {
        fun fromModel(order: Order): OrderDTO {
            return OrderDTO(
                order.id!!,
                order.assistance,
                order.street,
                order.city,
                order.province,
                order.phoneNumber,
                order.status,
                order.user
            )
        }
    }
}
