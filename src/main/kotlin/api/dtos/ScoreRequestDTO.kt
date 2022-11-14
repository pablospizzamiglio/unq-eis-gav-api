package api.dtos

import java.util.*

class ScoreRequestDTO(
    val orderId: UUID?,
    val userId: UUID?,
    val score: Int?,
)
