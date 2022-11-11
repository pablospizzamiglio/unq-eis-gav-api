package services

import api.controllers.Validator
import api.dtos.UserCreateRequestDTO
import dao.HibernateUserDAO
import entity.User
import entity.UserType
import java.util.*

class UserServiceImpl(private val userDAO: HibernateUserDAO) {
    private val validator: Validator = Validator()

    fun createUser(userCreateRequest: UserCreateRequestDTO): User {
        if (validator.containsSpecialCharacter(userCreateRequest.firstName)) {
            throw RuntimeException("First Name can not contain special characters")
        }
        if (validator.containsNumber(userCreateRequest.firstName)) {
            throw RuntimeException("First Name can not contain numbers")
        }
        if (validator.containsSpecialCharacter(userCreateRequest.lastName)) {
            throw RuntimeException("Last Name can not contain special characters")
        }
        if (validator.containsNumber(userCreateRequest.lastName)) {
            throw RuntimeException("Last Name can not contain numbers")
        }
        if (!validator.isValidUserType(userCreateRequest.type)) {
            throw RuntimeException("Type of user is not valid")
        }
        if (!validator.isValidEMail(userCreateRequest.emailAddress)) {
            throw RuntimeException("E-Mail Address is not valid")
        }
        val userFound = userDAO.findAll().find { user: User -> user.emailAddress == userCreateRequest.emailAddress }

        if (userFound != null) {
            throw RuntimeException("The email is already registered")
        }
        val user = User(
            userCreateRequest.firstName,
            userCreateRequest.lastName,
            UserType.valueOf(userCreateRequest.type),
            userCreateRequest.emailAddress,
            userCreateRequest.telephoneNumber
        )
        return userDAO.save(user)
    }

    fun findByUserId(orderId: UUID): User {
        return userDAO.find(orderId)
    }

    fun findAll(): List<User> {
        return userDAO.findAll()
    }
}
