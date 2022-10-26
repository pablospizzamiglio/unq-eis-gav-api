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
