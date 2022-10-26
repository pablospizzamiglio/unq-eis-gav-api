package api.dtos

import java.util.*

class OrderCreateRequestDTO(
    val assistanceId: UUID?,
    val street: String?,
    val betweenStreets: String?,
    val city: String?,
    val province: String?,
    val phoneNumber: String?,
    val userId: UUID?,
)
