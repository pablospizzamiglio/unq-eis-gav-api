package api.dtos

import entity.User
import java.util.*

class AssistanceSimpleDTO(
    val id: UUID,
    val kind: String,
    val detail: String,
    val costPerKm: Double,
    val assistant: User
) {
    companion object {
        fun fromModel(
            id: UUID?,
            kind: String,
            detail: String,
            costPerKm: Double,
            assistant: User
        ): AssistanceSimpleDTO {
            return AssistanceSimpleDTO(id!!, kind, detail, costPerKm, assistant)
        }
    }
}