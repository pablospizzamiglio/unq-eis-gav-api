package api.dtos

import java.util.*

class SimpleOrderDTO(
    val assistanceId: UUID,
    val addressStreet: String,
    val addressBetweenStreets: String,
    val city: String,
    val province: String,
    val phoneNumber: Int
)