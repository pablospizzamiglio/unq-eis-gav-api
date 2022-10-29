package services

import api.controllers.Validator
import api.dtos.UserCreateRequestDTO
import dao.HibernateUserDAO
import entity.User
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
        if (!validator.isValidEMail(userCreateRequest.emailAddress)) {
            throw RuntimeException("Is not a valid E-mail adress format")
        }
        if (!validator.isValidUserType(userCreateRequest.type)) {
            throw RuntimeException("Type of user is not valid")
        }
        val user = User(
            userCreateRequest.firstName,
            userCreateRequest.lastName,
            userCreateRequest.type,
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
