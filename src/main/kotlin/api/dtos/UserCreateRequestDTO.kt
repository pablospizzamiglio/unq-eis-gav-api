package api.dtos

class UserCreateRequestDTO(
    val firstName: String,
    val lastName: String,
    val type: String,
    val emailAddress: String,
    val telephoneNumber: String
) {
    companion object {
        fun fromModel(
            firstName: String,
            lastName: String,
            type: String,
            emailAddress: String,
            telephoneNumber: String
        ): UserCreateRequestDTO {
            return UserCreateRequestDTO(firstName, lastName, type, emailAddress, telephoneNumber)
        }
    }
}
