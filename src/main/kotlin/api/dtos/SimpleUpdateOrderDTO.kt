package api.dtos

import java.util.*

class SimpleUpdateOrderDTO(
    val orderId: UUID,
    val status: String,
    val password: String
)