package api.dtos

import entity.User
import java.util.*

class AssistanceSimpleDTO(
    val id: UUID,
    val kind: String,
    val fixedCost: Double,
    val costPerKm: Double,
    val assistant: User
) {
    companion object {
        fun fromModel(
            id: UUID?,
            kind: String,
            fixedCost: Double,
            costPerKm: Double,
            assistant: User
        ): AssistanceSimpleDTO {
            return AssistanceSimpleDTO(id!!, kind, fixedCost, costPerKm, assistant)
        }
    }
}