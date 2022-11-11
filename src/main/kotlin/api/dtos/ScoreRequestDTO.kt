package api.dtos

import java.util.*

class ScoreRequestDTO(
    val orderId: UUID,
    val userId: UUID,
    val score: Int,
) {
    companion object {
        fun fromModel(
            orderId: UUID,
            userId: UUID,
            score: Int
        ): ScoreRequestDTO {
            return ScoreRequestDTO(
                orderId,
                userId,
                score
            )
        }
    }
}
