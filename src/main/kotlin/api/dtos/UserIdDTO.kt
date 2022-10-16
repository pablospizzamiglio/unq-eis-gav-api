package api.dtos

import java.util.*

class UserIdDTO(
    val id: UUID
) {
    companion object {
        fun fromModel(
            id: UUID
        ): UserIdDTO {
            return UserIdDTO(id)
        }
    }
}
