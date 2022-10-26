package api.dtos

import java.util.*

class OrderUpdateRequestDTO(
    val orderId: UUID?,
    val status: String?,
    val password: String?
)
