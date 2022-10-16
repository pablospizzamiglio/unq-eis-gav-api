package api.dtos

class UserDTO(
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
        ): UserDTO {
            return UserDTO(firstName, lastName, type, emailAddress, telephoneNumber)
        }
    }
}
