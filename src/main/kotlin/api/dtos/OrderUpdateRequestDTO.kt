package api.dtos

import java.util.*

class OrderUpdateRequestDTO(
    val orderId: UUID?,
    val status: String?,
    val kmTraveled: Int?,
    val password: String?
)
