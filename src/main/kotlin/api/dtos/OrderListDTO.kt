package api.dtos

import entity.Order

class OrderListDTO(val result: List<OrderDTO>) {
    companion object {
        fun fromModel(orders: List<Order>): OrderListDTO {
            val ordersDTOs = orders.map {
                OrderDTO.fromModel(it)
            }
            return OrderListDTO(ordersDTOs)
        }
    }
}
