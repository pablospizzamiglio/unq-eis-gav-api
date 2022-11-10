package api.dtos

import entity.Assistance
import entity.User
import java.util.*

class AssistanceSimpleDTO(
    val id: UUID,
    val assistant: User,
    val kind: String,
    val fixedCost: Double,
    val costPerKm: Double,
    val cancellationCost: Double,
) {
    companion object {
        fun fromModel(assistance: Assistance): AssistanceSimpleDTO {
            return AssistanceSimpleDTO(
                assistance.id!!,
                assistance.user,
                assistance.kind.toString(),
                assistance.fixedCost,
                assistance.costPerKm,
                assistance.cancellationCost
            )
        }
    }
}
